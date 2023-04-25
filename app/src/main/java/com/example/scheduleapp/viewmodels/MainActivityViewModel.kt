package com.example.scheduleapp.viewmodels

import android.content.SharedPreferences
import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.scheduleapp.adapters.MainScreenAdapter.Companion.PAGE_COUNT
import com.example.scheduleapp.utils.Utils.createUnsuccessfulTask
import com.example.scheduleapp.data.*
import com.example.scheduleapp.models.FirebaseRepository
import com.google.android.gms.tasks.OnCompleteListener
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.database.DataSnapshot
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.*
import java.util.*
import javax.inject.Inject
import kotlin.collections.ArrayList

@HiltViewModel
class MainActivityViewModel @Inject constructor(
    private val rImplementation: FirebaseRepository,
    private val sPreferences: SharedPreferences
) : ViewModel() {
    var authState: MutableLiveData<AuthenticationStatus> = MutableLiveData()
    var groupsDownloadState: MutableLiveData<DownloadStatus<ArrayList<Data_IntString>>> = MutableLiveData()
    var scheduleDownloadState: MutableLiveData<DownloadStatus<FlatScheduleDetailed>> = MutableLiveData()

    private var groupList = arrayListOf<Data_IntString>()
    private var flatSchedule = FlatScheduleDetailed()

    private lateinit var timer: Timer
    private lateinit var listenerToRemove: OnCompleteListener<DataSnapshot>

    init {
        Log.d("TAG", "Created a view model for the outer app segment successfully.")
    }

    fun downloadGroupList() {
        groupsDownloadState.value = DownloadStatus.Progress
        setTimeout(5000L)

        listenerToRemove = OnCompleteListener<DataSnapshot> { task ->
            if (task.isSuccessful) {
                timer.cancel()
                Log.d("TAG", "Successfully downloaded specific data from the database:")
                Log.d("TAG", task.result.value.toString())

                try {
                    groupList = Gson().fromJson(
                        task.result.value.toString(),
                        object : TypeToken<ArrayList<Data_IntString>>() {}.type
                    )
                    groupsDownloadState.value = DownloadStatus.Success(groupList)

                    Log.d("TAG", "Successfully read and converted specific data:")
                    Log.d("TAG", groupList.toString())
                } catch (e: Exception) {
                    groupsDownloadState.value = DownloadStatus.Error(e.message.toString())
                    Log.d("TAG", "Failed to convert specific data: ${e.message}")
                }
            } else {
                groupsDownloadState.value = DownloadStatus.Error("Failed to download the data from the database.")
                Log.d("TAG", "Failed to download specific data from the database.")
            }
        }

        rImplementation.downloadDBReference(Constants.APP_BD_PATHS_GROUP_LIST)
            .addOnCompleteListener(listenerToRemove)
    }

    fun downloadSchedule() {
        scheduleDownloadState.value = DownloadStatus.Progress
        setTimeout(5000L)

        listenerToRemove = OnCompleteListener<DataSnapshot> { task ->
            if (task.isSuccessful) {
                timer.cancel()
                Log.d("TAG", "Successfully downloaded the data from the database:")
                Log.d("TAG", task.result.value.toString())

                try {
                    flatSchedule = Gson().fromJson(
                        task.result.value.toString(),
                        GroupArray::class.java
                    ).FlatScheduleDetailed!!
                    scheduleDownloadState.value = DownloadStatus.Success(flatSchedule)

                    Log.d("TAG", "Successfully read and converted the data:")
                    Log.d("TAG", flatSchedule.toString())
                } catch (e: Exception) {
                    scheduleDownloadState.value = DownloadStatus.Error(e.message.toString())
                    Log.d("TAG", "Failed to convert the data: ${e.message}")
                }
            } else {
                scheduleDownloadState.value = DownloadStatus.Error("Connection or network error.")
                Log.d("TAG", "Failed to download the data from the database.")
            }
        }
        rImplementation.downloadDB()
            .addOnCompleteListener(listenerToRemove)
    }

    private fun setTimeout(time: Long) {
        timer = Timer()
        val timerTask = object : TimerTask() {
            override fun run() {
                MainScope().launch {
                    listenerToRemove.onComplete(
                        createUnsuccessfulTask()
                    )
                }
            }
        }
        timer.schedule(timerTask, time)
    }

    fun getSchedule(): FlatScheduleDetailed {
        return flatSchedule
    }

    fun getGroupNames(): ArrayList<String> {
        val groupNames = arrayListOf<String>()
        for (item in groupList) {
            groupNames.add(item.title!!)
        }
        return groupNames
    }

    fun getDayToTab(index: Int): String {
        val position = index - PAGE_COUNT/2
        val c = Calendar.getInstance()

        if (position != 0) {
            c.add(Calendar.DATE, position)
        }

        val weekDay = Constants.APP_CALENDER_DAY_OF_WEEK[c.get(Calendar.DAY_OF_WEEK) - 1]
        val day = c.get(Calendar.DAY_OF_MONTH)

        return "$weekDay${System.getProperty("line.separator")}$day"
    }

    fun isUserSingedIn(): Boolean {
        if (rImplementation.getCurrentUser() != null) {
            return true
        }
        return false
    }

    fun getUserEmail(): String? {
        if (!isUserSingedIn()) {
            return null
        }
        return rImplementation.getCurrentUser()!!.email
    }

    fun signIn(email: String, password: String, newAccount: Boolean) {
        authState.value = AuthenticationStatus.Progress
        rImplementation.signIn(email, password, newAccount).addOnCompleteListener { task ->
            if (task.isSuccessful) {
                authState.value = AuthenticationStatus.Success
            } else {
                authState.value = AuthenticationStatus.Error(task.exception!!.message.toString())
            }
        }
    }

    fun signOut() {
        authState = MutableLiveData()
        rImplementation.signOut()
    }

    fun sendResetMessage(email: String) {
        authState.value = AuthenticationStatus.Progress
        rImplementation.sendResetMessage(email).addOnCompleteListener { task ->
            if (task.isSuccessful) {
                authState.value = AuthenticationStatus.Success
            } else {
                authState.value = AuthenticationStatus.Error(task.exception!!.message.toString())
            }
        }
    }

    fun <T> editPreferences(preference: String, value: T) {
        val sEdit: SharedPreferences.Editor
        if (preference.contains("_BOOL")) {
            sEdit = sPreferences.edit().putBoolean(preference, (value as Boolean))
        } else {
            sEdit = sPreferences.edit().putString(preference, (value as String))
        }
        sEdit.apply()
    }

    fun <T> getPreference(preference: String, defValue: T): T {
        if (preference.contains("_BOOL")) {
            return (sPreferences.getBoolean(preference, (defValue as Boolean)) as T)
        } else {
            return (sPreferences.getString(preference, (defValue as String)) as T)
        }
    }
}