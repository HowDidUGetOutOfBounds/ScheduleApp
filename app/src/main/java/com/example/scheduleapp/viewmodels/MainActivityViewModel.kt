package com.example.scheduleapp.viewmodels

import android.content.Context
import android.content.SharedPreferences
import android.util.Log
import androidx.lifecycle.ViewModel
import com.example.scheduleapp.R
import com.example.scheduleapp.data.GroupArray
import com.example.scheduleapp.models.FirebaseImplementation
import com.google.android.gms.tasks.Task
import com.google.firebase.auth.AuthResult
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.database.FirebaseDatabase
import com.google.gson.Gson
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class MainActivityViewModel @Inject constructor(
    private val rImplementation: FirebaseImplementation,
    private val sPreferences: SharedPreferences
) : ViewModel() {
    private val APP_GROUP_LIST = ArrayList<String>()
    private val APP_MIN_PASSWORD_LENGTH = 8

    init {
        Log.d("TAG", "Created a view model for the outer app segment successfully.")
        UpdateGroups()
    }

    fun getMinPasswordLength(): Int {
        return APP_MIN_PASSWORD_LENGTH
    }

    fun UpdateGroups() {
        rImplementation.downloadDB().addOnCompleteListener { task ->
            if (task.isSuccessful) {
                Log.d("TAG", "Successfully downloaded the data from the database:")
                Log.d("TAG", task.result.value.toString())

                try {
                    var groups = Gson().fromJson(task.result.value.toString(), GroupArray::class.java).GroupList
                    APP_GROUP_LIST.clear()
                    groups.forEach { group ->
                        APP_GROUP_LIST.add(group.groupname!!)
                    }
                    Log.d("TAG", "Successfully read and converted the data:")
                    Log.d("TAG", groups.toString())
                } catch (e: Exception) {
                    Log.d("TAG", "Failed to convert the data: ${e.message}")
                }
            } else {
                Log.d("TAG", "Failed to download the data from the database.")
            }
        }
    }

    fun getGroupList(): ArrayList<String> {
        return APP_GROUP_LIST
    }

    fun getCurrentUser(): FirebaseUser? {
        return rImplementation.getCurrentUser()
    }

    fun signIn(email: String, password: String, newAccount: Boolean): Task<AuthResult> {
        return rImplementation.signIn(email, password, newAccount)
    }

    fun signOut() {
        rImplementation.signOut()
    }

    fun sendResetMessage(email: String): Task<Void> {
        return rImplementation.sendResetMessage(email)
    }

    fun editPreferences(): SharedPreferences.Editor {
        return sPreferences.edit()
    }

    fun <T> getPreference(preference: String, defValue: T): T {
        if (preference.contains("_BOOL")) {
            return (sPreferences.getBoolean(preference, (defValue as Boolean)) as T)
        } else {
            return (sPreferences.getString(preference, (defValue as String)) as T)
        }
    }
}