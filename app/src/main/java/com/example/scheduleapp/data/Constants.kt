package com.example.scheduleapp.data

object Constants {
    const val APP_MIN_PASSWORD_LENGTH = 8
    const val APP_PREFERENCES = "APP_PREFERENCES"
    const val APP_PREFERENCES_STAY = "APP_PREFERENCES_STAY_BOOL"
    const val APP_PREFERENCES_PUSHES = "APP_PREFERENCES_PUSHES_BOOL"
    const val APP_PREFERENCES_GROUP = "APP_PREFERENCES_GROUP"
    const val APP_PREFERENCES_GROUP_REGISTER = "APP_PREFERENCES_GROUP_REGISTER"
    const val APP_BD_PATHS_BASE = "FlatScheduleDetailed"
    const val APP_BD_PATHS_BASE_PARAMETERS = "${APP_BD_PATHS_BASE}/BaseParameters"
    const val APP_BD_PATHS_SCHEDULE_BASE = "${APP_BD_PATHS_BASE}/BaseSchedules"
    const val APP_BD_PATHS_SCHEDULE_CURRENT = "${APP_BD_PATHS_BASE}/CurrentSchedules"
    val APP_CALENDER_DAY_OF_WEEK = listOf("вс", "пн", "вт", "ср", "чт", "пт", "сб")
}
