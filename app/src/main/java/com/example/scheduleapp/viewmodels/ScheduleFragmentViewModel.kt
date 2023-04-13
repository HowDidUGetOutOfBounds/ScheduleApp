package com.example.scheduleapp.viewmodels

import android.content.SharedPreferences
import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.scheduleapp.data.*
import com.example.scheduleapp.models.FirebaseImplementation
import com.example.scheduleapp.models.FirebaseRepository
import com.google.gson.Gson
import dagger.hilt.android.lifecycle.HiltViewModel
import java.text.FieldPosition
import java.util.Calendar
import javax.inject.Inject
import kotlin.collections.ArrayList

@HiltViewModel
class ScheduleFragmentViewModel @Inject constructor(
    private val rImplementation: FirebaseRepository, private val sPreferences: SharedPreferences
) : ViewModel() {
    //var appGroupArray: ArrayList<Group> = arrayListOf()
    val downloadStatus: MutableLiveData<DownloadStatus> = MutableLiveData()

    init {
        getAll()
    }

    fun getDayWithOffset(index: Int): Date? {
        var position = index - 2
        val c = Calendar.getInstance()

        if (position != 0) {
            c.add(Calendar.DATE, position)
        }

        val year = c.get(Calendar.YEAR)
        val month = c.get(Calendar.MONTH) + 1
        val day = c.get(Calendar.DAY_OF_MONTH)

        return Date(year, month, day)
    }

    fun checkDate(position: Int, currentGroup: Group): Day? {
        val currentDate = getDayWithOffset(position)
        Log.d("TAGSchedule", "Current position: ${position}")
        Log.d("TAGSchedule", "Current Schedule ${currentGroup}")
        Log.d("TAGSchedule", "Current date: $currentDate")
        for (item in currentGroup.schedule) {
            Log.d("TAGSchedule", "Database date ${item.date} ")
            if (currentDate == item.date) {
                return item
            }
        }
        return null
    }

    fun getGroup(arrayListGroup: ArrayList<Group>): Group? {
        val groupName =
            sPreferences.getString(Constants.APP_PREFERENCES_GROUP + "_" + rImplementation.getCurrentUser()!!.email.toString(), "1K9291")
        for(item in arrayListGroup){
            if (groupName==item.groupname){
                return item
            }
        }
        return null
    }

    fun getAll() {
        downloadStatus.value = DownloadStatus.Progress
        try {
            rImplementation.downloadDB().addOnCompleteListener { text ->
                var appGroupArray = Gson().fromJson(
                    text.result.value.toString(), GroupArray::class.java
                ).GroupList
                downloadStatus.value = DownloadStatus.Success(appGroupArray)
            }
        } catch (e: Exception) {
            downloadStatus.value = DownloadStatus.Error(e.message.toString())
        }
    }
}
