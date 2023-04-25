package com.example.scheduleapp.data


data class GroupArray (
    //var GroupList : ArrayList<Group> = arrayListOf(),
    var FlatSchedule : FlatSchedule? = FlatSchedule(),
    var FlatScheduleDetailed : FlatScheduleDetailed? = FlatScheduleDetailed()
)

data class FlatSchedule (
    var cabinetList   : ArrayList<Data_IntString>   = arrayListOf(),
    var groupList     : ArrayList<Data_IntString>     = arrayListOf(),
    var pairList      : ArrayList<Data_IntString>      = arrayListOf(),
    var teacherList   : ArrayList<Data_IntString>   = arrayListOf(),
    var dayList       : ArrayList<Data_IntDate>       = arrayListOf(),
    var scheduleDay   : ArrayList<Data_IntArrayofInt>   = arrayListOf(),
    var scheduleGroup : ArrayList<Data_IntArrayofInt> = arrayListOf(),
    var cabinetPair   : ArrayList<Data_IntIntInt>   = arrayListOf(),
    var schedulePair  : ArrayList<Data_IntIntInt>  = arrayListOf(),
    var teacherPair   : ArrayList<Data_IntIntInt>   = arrayListOf()
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

data class Data_IntIntInt (
    var scheduleId     : Int? = null,
    var pairNum    : Int? = null,
    var specialId : Int? = null
)


/*
data class Group (
    var groupname : String?   = null,
    var schedule  : ArrayList<Day> = arrayListOf()
)

data class Day (
    var date : Date?   = Date(),
    var dayschedule : ArrayList<Schedule> = arrayListOf()
)

data class Date (
    var year : Int?   = null,
    var month : Int?   = null,
    var day : Int?   = null
)

data class Schedule (
    var pair       : Int?    = null,
    var discipline : String? = null,
    var cabinet    : String?    = null,
    var teacher    : String? = null
)
*/