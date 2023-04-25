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

    fun getDayWithOffset(index: Int): Date {
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

    fun getGroup(): String? {
        if (rImplementation.getCurrentUser() == null) {
            return null
        }
        val groupName = sPreferences.getString(
            Constants.APP_PREFERENCES_GROUP + "_" + rImplementation.getCurrentUser()!!.email.toString(), null
        )
        return groupName
    }

    fun getDayId(dayList: ArrayList<Data_IntDate>, index: Int): Int? {
        val date = getDayWithOffset(index)
        for (item in dayList) {
            if (date == item.date) {
                return item.id
            }
        }
        return null
    }

    fun getGroupId(groupList: ArrayList<Data_IntString>, groupName: String?): Int? {
        if (groupName == null) {
            return null
        }
        for (item in groupList) {
            if (groupName == item.title) {
                return item.id
            }
        }
        return null
    }

    fun getScheduleByGroupAndDay(groupId: Int, dayId: Int, schedule: FlatSchedule): ArrayList<Schedule>? {
        var resArray: ArrayList<Schedule> = arrayListOf()
        for (i in 1..7) {
            resArray.add(Schedule(pair = i, "", "", ""))
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
                resArray[item.pairNum!!-1].cabinet = getById(item.specialId!!, schedule.cabinetList)!!.title
            }
        }
        for (item in schedule.schedulePair) {
            if (item.scheduleId == scheduleId) {
                resArray[item.pairNum!!-1].discipline = getById(item.specialId!!, schedule.pairList)!!.title
            }
        }
        for (item in schedule.teacherPair) {
            if (item.scheduleId == scheduleId) {
                resArray[item.pairNum!!-1].teacher = getById(item.specialId!!, schedule.teacherList)!!.title
            }
        }

        for (item in resArray) {

        }
        return resArray
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













    fun getScheduleByGroupAndDayDetailed(groupId: Int, dayId: Int, schedule: FlatScheduleDetailed): ArrayList<ScheduleDetailed>? {
        var result: ArrayList<ScheduleDetailed> = arrayListOf()
        for (i in 1..14) {
            result.add(ScheduleDetailed(lessonNum = i, "", "", "", "", "", ""))
        }

        var scheduleId: Int? = null

        var firstScheduleArray = getById(dayId, schedule.scheduleDay)
        var secondScheduleArray = getById(groupId, schedule.scheduleGroup)

        if (firstScheduleArray == null || secondScheduleArray == null) {
            Log.d("TAG", "getScheduleByGroupAndDay: One of the schedule arrays is missing!")
            return result
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
            return result
        }

        for (item in schedule.cabinetLesson) {
            if (item.scheduleId == scheduleId) {
                if (item.subGroups.contains(1)) {
                    result[(item.lessonNum!!-1)].cabinet1 = getById(item.specialId!!, schedule.cabinetList)!!.title
                }
                if (item.subGroups.contains(2)) {
                    result[(item.lessonNum!!-1)].cabinet2 = getById(item.specialId!!, schedule.cabinetList)!!.title
                }
            }
        }
        for (item in schedule.scheduleLesson) {
            if (item.scheduleId == scheduleId) {
                if (item.subGroups.contains(1)) {
                    result[item.lessonNum!!-1].discipline1 = getById(item.specialId!!, schedule.lessonList)!!.title
                }
                if (item.subGroups.contains(2)) {
                    result[item.lessonNum!!-1].discipline2 = getById(item.specialId!!, schedule.lessonList)!!.title
                }
            }
        }
        for (item in schedule.teacherLesson) {
            if (item.scheduleId == scheduleId) {
                if (item.subGroups.contains(1)) {
                    result[item.lessonNum!!-1].teacher1 = getById(item.specialId!!, schedule.teacherList)!!.title
                }
                if (item.subGroups.contains(2)) {
                    result[item.lessonNum!!-1].teacher2 = getById(item.specialId!!, schedule.teacherList)!!.title
                }
            }
        }


        return result
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
