package com.example.scheduleapp.data

import dagger.hilt.android.AndroidEntryPoint

data class ScheduleTest(
    val pair: Int = 0, val discipline: String = "", val cabinet: Int = 0, val teacher: String = ""
)

