package com.example.scheduleapp.data

import com.google.gson.annotations.SerializedName


data class GroupArray (
    @SerializedName("GroupList" ) var GroupList : ArrayList<Group> = arrayListOf()
)


data class Group (
    @SerializedName("groupname" ) var groupname : String?   = null,
    @SerializedName("schedule"  ) var schedule  : Schedule? = Schedule()
)

data class Schedule (
    @SerializedName("fake_arg" ) var fakeArg : String? = null
)
