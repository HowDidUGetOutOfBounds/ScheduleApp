package com.example.scheduleapp.data

sealed class DownloadStatus {

    data class Success(val result: ArrayList<Group>) : DownloadStatus()

    data class Error(val message: String) : DownloadStatus()

    object Progress: DownloadStatus()

}
