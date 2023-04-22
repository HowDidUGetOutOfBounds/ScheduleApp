package com.example.scheduleapp.data

sealed class DownloadStatus {

    data class SuccessLocal(val result: ArrayList<Data_IntString>) : DownloadStatus()

    data class SuccessGlobal(val result: FlatSchedule) : DownloadStatus()

    data class Error(val message: String) : DownloadStatus()

    object Progress: DownloadStatus()

}
