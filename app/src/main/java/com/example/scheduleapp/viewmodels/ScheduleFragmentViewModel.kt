package com.example.scheduleapp.viewmodels


import android.content.SharedPreferences
import android.util.Log
import androidx.lifecycle.ViewModel
import com.example.scheduleapp.data.*
import com.example.scheduleapp.data.Date
import com.example.scheduleapp.models.FirebaseRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import java.util.*
import javax.inject.Inject

@HiltViewModel
class ScheduleFragmentViewModel @Inject constructor(
    private val rImplementation: FirebaseRepository, private val sPreferences: SharedPreferences
) : ViewModel() {


    fun getDayToTab( index: Int): String {
        var position = index - 7
        val c = Calendar.getInstance()

        if (position != 0) {
            c.add(Calendar.DATE, position)
        }

        val weekDay=Constants.APP_PREFERENCE_DAY_OF_WEEK[c.get(Calendar.DAY_OF_WEEK)-1]
        val day = c.get(Calendar.DAY_OF_MONTH)

        return "$weekDay${System.getProperty("line.separator")}$day"
    }

    fun getDayWithOffset( index: Int): Date {
        var position = index - 7
        val c = Calendar.getInstance()

        if (position != 0) {
            c.add(Calendar.DATE, position)
        }

        val year = c.get(Calendar.YEAR)
        val month = c.get(Calendar.MONTH) + 1
        val day = c.get(Calendar.DAY_OF_MONTH)
        return Date(year, month, day)
    }

    fun getDayId(schedule: FlatSchedule, index: Int): Int? {
        val date = getDayWithOffset(index)
        for (item in schedule.dayList) {
            if (date == item.date) {
                return item.id
            }
        }
        return null
    }

    fun getGroupId(schedule: FlatSchedule): Int? {
        if (rImplementation.getCurrentUser() == null) {
            return null
        }
        val groupName = sPreferences.getString(
            Constants.APP_PREFERENCES_GROUP + "_" + rImplementation.getCurrentUser()!!.email.toString(), null
        )
        for (item in schedule.groupList) {
            if (groupName == item.title) {
                return item.id
            }
        }
        return null
    }

    fun getScheduleByGroupAndDay(groupId: Int, dayId: Int, schedule: FlatSchedule): ArrayList<Schedule>? {
        var result: ArrayList<Schedule> = arrayListOf()
        for (i in 1..7) {
            result.add(Schedule(pair = i, "", "", ""))
        }

        var scheduleId: Int? = null

        var firstScheduleArray = getById(dayId, schedule.scheduleDay)
        var secondScheduleArray = getById(groupId, schedule.scheduleGroup)

        if (firstScheduleArray == null || secondScheduleArray == null) {
            Log.d("TAG", "getScheduleByGroupAndDay: One of the schedule arrays is missing!")
            return null
        }

        if (firstScheduleArray.scheduleId.size < secondScheduleArray.scheduleId.size) {
            for (item in firstScheduleArray.scheduleId) {
                if (secondScheduleArray.scheduleId.contains(item)) {
                    scheduleId = item
                    break
                }
            }
        } else {
            for (item in secondScheduleArray.scheduleId) {
                if (firstScheduleArray.scheduleId.contains(item)) {
                    scheduleId = item
                    break
                }
            }
        }

        if (scheduleId == null) {
            return null
        }

        for (item in schedule.cabinetPair) {
            if (item.scheduleId == scheduleId) {
                result[item.pairNum!!-1].cabinet = getById(item.specialId!!, schedule.cabinetList)!!.title
            }
        }
        for (item in schedule.schedulePair) {
            if (item.scheduleId == scheduleId) {
                result[item.pairNum!!-1].discipline = getById(item.specialId!!, schedule.pairList)!!.title
            }
        }
        for (item in schedule.teacherPair) {
            if (item.scheduleId == scheduleId) {
                result[item.pairNum!!-1].teacher = getById(item.specialId!!, schedule.teacherList)!!.title
            }
        }
        return result
    }
    private fun getById(id: Int, array: ArrayList<Data_IntString>): Data_IntString? {
        for (item in array) {
            if (item.id == id) {
                return item
            }
        }
        return null
    }
    private fun getById(id: Int, array: ArrayList<Data_IntArrayofInt>): Data_IntArrayofInt? {
        for (item in array) {
            if (item.specialId == id) {
                return item
            }
        }
        return null
    }

    /*
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
*/
}
