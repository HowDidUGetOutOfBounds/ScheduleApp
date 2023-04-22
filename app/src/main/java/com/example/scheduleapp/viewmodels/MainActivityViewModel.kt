package com.example.scheduleapp.viewmodels

import android.content.Context
import android.content.SharedPreferences
import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.scheduleapp.R
import com.example.scheduleapp.data.*
import com.example.scheduleapp.models.FirebaseImplementation
import com.example.scheduleapp.models.FirebaseRepository
import com.google.android.gms.tasks.Task
import com.google.firebase.auth.AuthResult
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.database.FirebaseDatabase
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import dagger.hilt.android.lifecycle.HiltViewModel
import java.util.*
import javax.inject.Inject
import kotlin.collections.ArrayList

@HiltViewModel
class MainActivityViewModel @Inject constructor(
    private val rImplementation: FirebaseRepository,
    private val sPreferences: SharedPreferences
) : ViewModel() {
    var downloadState: MutableLiveData<DownloadStatus> = MutableLiveData()
    var authState: MutableLiveData<AuthenticationStatus> = MutableLiveData()
    private var groupList = arrayListOf<Data_IntString>()
    private var flatSchedule = FlatSchedule()

    init {
        Log.d("TAG", "Created a view model for the outer app segment successfully.")
    }

    fun downloadGroupList() {
        downloadState.value = DownloadStatus.Progress
        rImplementation.downloadDBReference("FlatSchedule/groupList").addOnCompleteListener { task ->
            if (task.isSuccessful) {
                Log.d("TAG", "Successfully downloaded specific data from the database:")
                Log.d("TAG", task.result.value.toString())

                try {
                    groupList = Gson().fromJson(task.result.value.toString(), object : TypeToken<ArrayList<Data_IntString>>() {}.type)
                    downloadState.value = DownloadStatus.SuccessLocal(groupList)

                    Log.d("TAG", "Successfully read and converted specific data:")
                    Log.d("TAG", groupList.toString())
                } catch (e: Exception) {
                    downloadState.value = DownloadStatus.Error(e.message.toString())
                    Log.d("TAG", "Failed to convert specific data: ${e.message}")
                }
            } else {
                downloadState.value = DownloadStatus.Error("Failed to download the data from the database.")
                Log.d("TAG", "Failed to download specific data from the database.")
            }
        }
    }

    fun downloadSchedule() {
        downloadState.value = DownloadStatus.Progress
        rImplementation.downloadDB().addOnCompleteListener { task ->
            if (task.isSuccessful) {
                Log.d("TAG", "Successfully downloaded the data from the database:")
                Log.d("TAG", task.result.value.toString())

                try {
                    flatSchedule = Gson().fromJson(task.result.value.toString(), GroupArray::class.java).FlatSchedule!!
                    downloadState.value = DownloadStatus.SuccessGlobal(flatSchedule)

                    Log.d("TAG", "Successfully read and converted the data:")
                    Log.d("TAG", flatSchedule.toString())
                } catch (e: Exception) {
                    downloadState.value = DownloadStatus.Error(e.message.toString())
                    Log.d("TAG", "Failed to convert the data: ${e.message}")
                }
            } else {
                downloadState.value = DownloadStatus.Error("Failed to download the data from the database.")
                Log.d("TAG", "Failed to download the data from the database.")
            }
        }
    }

    fun getSchedule(): FlatSchedule {
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
        var position = index - 7
        val c = Calendar.getInstance()

        if (position != 0) {
            c.add(Calendar.DATE, position)
        }

        val weekDay=Constants.APP_PREFERENCE_DAY_OF_WEEK[c.get(Calendar.DAY_OF_WEEK)-1]
        val day = c.get(Calendar.DAY_OF_MONTH)

        return "$weekDay${System.getProperty("line.separator")}$day"
    }



    fun getCurrentUser(): FirebaseUser? {
        return rImplementation.getCurrentUser()
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

    fun editPreferences(): SharedPreferences.Editor {
        return sPreferences.edit()
    }

    fun <T> getPreference(preference: String, defValue: T): T {
        if (preference.contains("_BOOL")) {
            return (sPreferences.getBoolean(preference, (defValue as Boolean)) as T)
        } else {
            return (sPreferences.getString(preference, (defValue as String)) as T)
        }
    }
}