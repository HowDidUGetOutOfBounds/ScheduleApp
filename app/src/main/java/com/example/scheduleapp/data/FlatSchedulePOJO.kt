package com.example.scheduleapp.data

import com.google.gson.annotations.SerializedName


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