package com.example.scheduleapp.data

import dagger.hilt.android.AndroidEntryPoint
import kotlin.contracts.Returns

class TestScheduleData {

    fun loadSchedule(): List<ScheduleTest> {
        return listOf(
            ScheduleTest(1, "ОАИП", 213, "Батура"),
            ScheduleTest(2, "ИКГ", 101, "Дукмасова"),
            ScheduleTest(3, "Математическое моделирование", 213, "Батура"),
            ScheduleTest(4, "Английский язык", 420, "Кузнецова"),
            ScheduleTest(5),
            ScheduleTest(6),
            ScheduleTest(7)
        )
    }
}