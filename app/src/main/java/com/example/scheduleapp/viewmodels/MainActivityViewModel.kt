package com.example.scheduleapp.viewmodels

import android.app.AlarmManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.scheduleapp.UI.MainActivity.Companion.REQUEST_CODE_LOC_NOTIFICATION_ID
import com.example.scheduleapp.adapters.MainScreenAdapter.Companion.PAGE_COUNT
import com.example.scheduleapp.data.*
import com.example.scheduleapp.data.Constants.APP_BD_PATHS_BASE_PARAMETERS
import com.example.scheduleapp.data.Constants.APP_BD_PATHS_SCHEDULE_CURRENT
import com.example.scheduleapp.data.Constants.APP_CALENDER_DAY_OF_WEEK
import com.example.scheduleapp.data.Constants.APP_TOAST_WEAK_CONNECTION
import com.example.scheduleapp.models.FirebaseRepository
import com.google.android.gms.tasks.OnCompleteListener
import com.google.firebase.database.DataSnapshot
import com.google.gson.Gson
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
    var paramsDownloadState: MutableLiveData<DownloadStatus<FlatScheduleParameters>> = MutableLiveData()
    var scheduleDownloadState: MutableLiveData<DownloadStatus<FlatScheduleDetailed>> = MutableLiveData()

    private var flatScheduleParameters = FlatScheduleParameters()
    private var flatScheduleDetailed = FlatScheduleDetailed()

    private lateinit var timer: Timer
    private lateinit var listenerToRemove: OnCompleteListener<DataSnapshot>

    init {
        Log.d("TAG", "Created a view model for the outer app segment successfully.")
    }

    fun resetAuthState() {
        authState = MutableLiveData()
    }

    fun resetDownloadState(onlyParams: Boolean) {
        if (onlyParams) {
            paramsDownloadState = MutableLiveData()
        } else {
            scheduleDownloadState = MutableLiveData()
        }
    }

    fun downloadParameters() {
        paramsDownloadState.value = DownloadStatus.Progress
        setTimeout(5000L, true)

        listenerToRemove = getDownloadListener(true)
        rImplementation.downloadByReference(APP_BD_PATHS_BASE_PARAMETERS)
            .addOnCompleteListener(listenerToRemove)
    }

    fun downloadSchedule() {
        scheduleDownloadState.value = DownloadStatus.Progress
        setTimeout(8000L, false)

        listenerToRemove = getDownloadListener(false)
        rImplementation.downloadByReference(APP_BD_PATHS_SCHEDULE_CURRENT)
            .addOnCompleteListener(listenerToRemove)
    }

    private fun getDownloadListener(onlyParams: Boolean): OnCompleteListener<DataSnapshot> {
        val listener = OnCompleteListener<DataSnapshot> { task ->
            if (task.isSuccessful) {
                timer.cancel()
                Log.d("TAG", "Successfully downloaded data from the database:")
                Log.d("TAG", task.result.value.toString())

                try {
                    if (onlyParams) {
                        flatScheduleParameters = Gson().fromJson(
                            task.result.value.toString(),
                            FlatScheduleParameters::class.java
                        )
                        paramsDownloadState.value = DownloadStatus.Success(flatScheduleParameters)
                    } else {
                        flatScheduleDetailed = Gson().fromJson(
                            task.result.value.toString(),
                            FlatScheduleDetailed::class.java
                        )
                        scheduleDownloadState.value = DownloadStatus.Success(flatScheduleDetailed)
                    }
                    Log.d("TAG", "Successfully read and converted the data.")
                } catch (e: Exception) {
                    if (onlyParams) {
                        paramsDownloadState.value = DownloadStatus.Error(e.message.toString())
                    } else {
                        scheduleDownloadState.value = DownloadStatus.Error(e.message.toString())
                    }
                    Log.d("TAG", "Failed to convert the data: ${e.message}")
                }
            } else {
                if (onlyParams) {
                    paramsDownloadState.value = DownloadStatus.Error("Connection or network error.")
                } else {
                    scheduleDownloadState.value = DownloadStatus.Error("Connection or network error.")
                }
                Log.d("TAG", "Failed to download data from the database.")
            }
        }
        return listener
    }

    private fun setTimeout(time: Long, onlyParams: Boolean) {
        timer = Timer()
        val timerTask = object : TimerTask() {
            override fun run() {
                MainScope().launch {
                    if (onlyParams) {
                        paramsDownloadState.value =
                            DownloadStatus.WeakProgress(APP_TOAST_WEAK_CONNECTION)
                    } else {
                        scheduleDownloadState.value =
                            DownloadStatus.WeakProgress(APP_TOAST_WEAK_CONNECTION)
                    }
                }
            }
        }
        timer.schedule(timerTask, time)
    }

    fun getSchedule(): FlatScheduleDetailed {
        return flatScheduleDetailed
    }

    fun getParameters(): FlatScheduleParameters {
        return flatScheduleParameters
    }

    fun getGroupNames(): ArrayList<String> {
        val groupNames = arrayListOf<String>()
        for (item in flatScheduleParameters.groupList) {
            groupNames.add(item.title!!)
        }
        return groupNames
    }

    fun getDayToTab(index: Int): String {
        val position = index - PAGE_COUNT / 2
        val c = Calendar.getInstance()

        if (position != 0) {
            c.add(Calendar.DATE, position)
        }

        val weekDay = APP_CALENDER_DAY_OF_WEEK[c.get(Calendar.DAY_OF_WEEK) - 1]
        var day = c.get(Calendar.DAY_OF_MONTH).toString()
        if (day.length < 2) {
            day = "0$day"
        }

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


    fun setNotification(
        context: Context?,
        alarmManager: AlarmManager
    ) {
        Log.d("Not_Debugger", "Notification set")
        val alarmIntent: PendingIntent = Intent(context, AlarmReceiver::class.java).let { intent ->
            PendingIntent.getBroadcast(
                context, REQUEST_CODE_LOC_NOTIFICATION_ID, intent, PendingIntent.FLAG_UPDATE_CURRENT + PendingIntent.FLAG_MUTABLE
            )
        }
        alarmManager.setRepeating(
            AlarmManager.RTC_WAKEUP,  Calendar.getInstance().timeInMillis,60000L, alarmIntent
        ) // 180000L
    }


    fun cancelNotification(context: Context?, alarmManager: AlarmManager){
        Log.d("Not_Debugger", "Notification cancelled")
        val alarmIntent = Intent(context, AlarmReceiver::class.java).let { intent ->
            PendingIntent.getBroadcast(
                context, REQUEST_CODE_LOC_NOTIFICATION_ID, intent, PendingIntent.FLAG_UPDATE_CURRENT + PendingIntent.FLAG_MUTABLE
            )
        }
        alarmManager.cancel(alarmIntent)
    }
}