package com.example.scheduleapp.data

import com.google.gson.annotations.SerializedName


data class FlatSchedule (
    @SerializedName("cabinetList"   ) var cabinetList   : ArrayList<Data_IntString>   = arrayListOf(),
    @SerializedName("groupList"     ) var groupList     : ArrayList<Data_IntString>     = arrayListOf(),
    @SerializedName("pairList"      ) var pairList      : ArrayList<Data_IntString>      = arrayListOf(),
    @SerializedName("teacherList"   ) var teacherList   : ArrayList<Data_IntString>   = arrayListOf(),
    @SerializedName("dayList"       ) var dayList       : ArrayList<Data_IntDate>       = arrayListOf(),
    @SerializedName("scheduleDay"   ) var scheduleDay   : ArrayList<Data_IntArrayofInt>   = arrayListOf(),
    @SerializedName("scheduleGroup" ) var scheduleGroup : ArrayList<Data_IntArrayofInt> = arrayListOf(),
    @SerializedName("cabinetPair"   ) var cabinetPair   : ArrayList<Data_IntIntInt>   = arrayListOf(),
    @SerializedName("schedulePair"  ) var schedulePair  : ArrayList<Data_IntIntInt>  = arrayListOf(),
    @SerializedName("teacherPair"   ) var teacherPair   : ArrayList<Data_IntIntInt>   = arrayListOf()
)

data class Data_IntString (
    @SerializedName("id"    ) var id    : Int?    = null,
    @SerializedName("title" ) var title : String? = null
)

data class Data_IntDate (
    @SerializedName("date" ) var date : Date? = Date(),
    @SerializedName("id"   ) var id   : Int?  = null
)

data class Data_IntArrayofInt (
    @SerializedName("specialId"     ) var specialId     : Int?           = null,
    @SerializedName("scheduleId" ) var scheduleId : ArrayList<Int> = arrayListOf()
)

data class Data_IntIntInt (
    @SerializedName("scheduleId"     ) var scheduleId     : Int? = null,
    @SerializedName("pairNum"    ) var pairNum    : Int? = null,
    @SerializedName("specialId" ) var specialId : Int? = null
)