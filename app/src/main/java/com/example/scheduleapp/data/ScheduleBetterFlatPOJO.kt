package com.example.scheduleapp.data

import android.util.Log

data class FlatScheduleDetailed (
    var cabinetList    : ArrayList<Data_IntString>    = arrayListOf(),
    var groupList      : ArrayList<Data_IntString>      = arrayListOf(),
    var lessonList     : ArrayList<Data_IntString>     = arrayListOf(),
    var teacherList    : ArrayList<Data_IntString>    = arrayListOf(),
    var dayList        : ArrayList<Data_IntDate>        = arrayListOf(),
    var scheduleDay   : ArrayList<Data_IntArrayofInt>   = arrayListOf(),
    var scheduleGroup : ArrayList<Data_IntArrayofInt> = arrayListOf(),
    var scheduleLesson : ArrayList<Data_IntIntIntArrayofInt> = arrayListOf(),
    var cabinetLesson  : ArrayList<Data_IntIntIntArrayofInt>  = arrayListOf(),
    var teacherLesson  : ArrayList<Data_IntIntIntArrayofInt>  = arrayListOf()
)

data class Data_IntIntIntArrayofInt (
    var scheduleId : Int? = null,
    var lessonNum  : Int? = null,
    var subGroups  : ArrayList<Int> = arrayListOf(),
    var specialId  : Int? = null
)

data class ScheduleDetailed (
    var lessonNum  : Int?    = null,

    var discipline1 : String? = null,
    var cabinet1    : String?    = null,
    var teacher1    : String? = null,

    var discipline2 : String? = null,
    var cabinet2    : String?    = null,
    var teacher2    : String? = null,
)