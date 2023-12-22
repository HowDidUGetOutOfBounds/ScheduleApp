package com.example.scheduleapp.data

object Constants {
    const val APP_MIN_PASSWORD_LENGTH = 8

    const val APP_PREFERENCES = "APP_PREFERENCES"
    const val APP_PREFERENCES_STAY = "APP_PREFERENCES_STAY_BOOL"
    const val APP_PREFERENCES_PUSHES = "APP_PREFERENCES_PUSHES_BOOL"
    const val APP_PREFERENCES_GROUP = "APP_PREFERENCES_GROUP"
    const val APP_PREFERENCES_GROUP_REGISTER = "APP_PREFERENCES_GROUP_REGISTER"

    const val APP_BD_PATHS_BASE = "FlatSchedule"
    const val APP_BD_PATHS_BASE_PARAMETERS = "${APP_BD_PATHS_BASE}/BaseParameters"
    const val APP_BD_PATHS_SCHEDULE_BASE = "${APP_BD_PATHS_BASE}/BaseSchedules"
    const val APP_BD_PATHS_SCHEDULE_CURRENT = "${APP_BD_PATHS_BASE}/CurrentSchedules"

    const val APP_TOAST_WEAK_CONNECTION = "Looks like there are some problems with connection..."
    const val APP_TOAST_NOT_SIGNED_IN = "You aren't signed in yet."
    const val APP_TOAST_LOGIN_FAILED = "Failed to log in"
    const val APP_TOAST_LOGIN_SUCCESS = "Logged in successfully"
    const val APP_TOAST_SIGNUP_FAILED = "Failed to sign up"
    const val APP_TOAST_SIGNUP_SUCCESS = "Registered successfully"
    const val APP_TOAST_RESET_SEND_FAILED = "Failed to send the reset message"
    const val APP_TOAST_RESET_SEND_SUCCESS = "Reset message sent successfully"
    const val APP_TOAST_PASSWORD_TOO_SHORT = "Your password should be at least $APP_MIN_PASSWORD_LENGTH characters long."
    const val APP_TOAST_PASSWORDS_DONT_MATCH = "Your passwords don't match. Please confirm your password."
    const val APP_TOAST_DATA_DOWNLOAD_FAILED = "Failed to download the data"
    const val APP_TOAST_SCHEDULE_DOWNLOAD_FAILED = "Failed to download the schedules"

    val APP_CALENDER_DAY_OF_WEEK = listOf("вс", "пн", "вт", "ср", "чт", "пт", "сб")
}
