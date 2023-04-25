package com.example.scheduleapp.data

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

data class Data_IntString (
    var id    : Int?    = null,
    var title : String? = null
)

data class Data_IntDate (
    var date : Date? = Date(),
    var id   : Int?  = null
)

data class Data_IntArrayofInt (
    var specialId     : Int?           = null,
    var scheduleId : ArrayList<Int> = arrayListOf()
)

data class Data_IntIntIntArrayofInt (
    var scheduleId : Int? = null,
    var lessonNum  : Int? = null,
    var subGroups  : ArrayList<Int> = arrayListOf(),
    var specialId  : Int? = null
)