package com.example.scheduleapp.data

sealed class DownloadStatus {

    data class Success(val result: FlatSchedule) : DownloadStatus()

    data class Error(val message: String) : DownloadStatus()

    object Progress: DownloadStatus()

}
