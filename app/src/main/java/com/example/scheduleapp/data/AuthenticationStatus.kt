package com.example.scheduleapp.data

sealed class AuthenticationStatus {

    object Success : AuthenticationStatus()

    data class Error(val message: String) : AuthenticationStatus()

    object Progress: AuthenticationStatus()

}
