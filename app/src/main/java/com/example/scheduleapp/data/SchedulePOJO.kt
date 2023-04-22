package com.example.scheduleapp.data

import com.google.gson.annotations.SerializedName


data class GroupArray (
    //var GroupList : ArrayList<Group> = arrayListOf(),
    var FlatSchedule : FlatSchedule? = FlatSchedule()
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
*/

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
