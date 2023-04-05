package com.example.scheduleapp.data

import com.google.gson.annotations.SerializedName


data class GroupArray (
    @SerializedName("GroupList" ) var GroupList : ArrayList<Group> = arrayListOf()
)

data class Group (
    @SerializedName("groupname" ) var groupname : String?   = null,
    @SerializedName("schedule"  ) var schedule  : ArrayList<Day> = arrayListOf()
)

data class Day (
    @SerializedName("date" ) var date : Date?   = Date(),
    @SerializedName("dayschedule" ) var dayschedule : ArrayList<Schedule> = arrayListOf()
)

data class Date (
    @SerializedName("year" ) var year : Int?   = null,
    @SerializedName("month" ) var month : Int?   = null,
    @SerializedName("day" ) var day : Int?   = null
)

data class Schedule (
    @SerializedName("pair"       ) var pair       : Int?    = null,
    @SerializedName("discipline" ) var discipline : String? = null,
    @SerializedName("cabinet"    ) var cabinet    : String?    = null,
    @SerializedName("teacher"    ) var teacher    : String? = null
)
