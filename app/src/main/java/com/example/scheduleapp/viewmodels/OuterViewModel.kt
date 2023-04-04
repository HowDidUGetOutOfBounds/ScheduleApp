package com.example.scheduleapp.viewmodels

import android.content.Context
import android.content.SharedPreferences
import android.util.Log
import androidx.lifecycle.ViewModel
import com.example.scheduleapp.R
import com.example.scheduleapp.data.GroupArray
import com.google.android.gms.tasks.Task
import com.google.firebase.auth.AuthResult
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.GenericTypeIndicator
import com.google.gson.Gson
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class OuterViewModel @Inject constructor(
    private val mContext: Context
) : ViewModel() {
    private lateinit var fAuth: FirebaseAuth
    private lateinit var fDatabase: FirebaseDatabase
    private lateinit var sPreferences: SharedPreferences

    private val APP_GROUP_LIST = ArrayList<String>()
    private val APP_MIN_PASSWORD_LENGTH = 8

    init {
        Log.d("TAG", "Created a view model for the outer app segment successfully.")
        InitializeParameters()
        UpdateGroups()
    }

    fun InitializeParameters() {
        fAuth = FirebaseAuth.getInstance()
        fDatabase = FirebaseDatabase.getInstance()
        sPreferences = mContext.getSharedPreferences(mContext.resources.getString(R.string.app_name), Context.MODE_PRIVATE)
    }

    fun UpdateGroups() {
        fDatabase.getReference("").get().addOnCompleteListener { task ->
            if (task.isSuccessful) {
                Log.d("TAG", "Successfully downloaded the data from the database.")

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

    fun getMinPasswordLength(): Int {
        return APP_MIN_PASSWORD_LENGTH
    }

    fun getGroupList(): ArrayList<String> {
        return APP_GROUP_LIST
    }

    fun signUp(email: String, password: String): Task<AuthResult> {
        return fAuth.createUserWithEmailAndPassword(email, password)
    }

    fun signIn(email: String, password: String): Task<AuthResult> {
        return fAuth.signInWithEmailAndPassword(email, password)
    }

    fun sendResetMessage(email: String): Task<Void> {
        return fAuth.sendPasswordResetEmail(email)
    }

    fun signOut() {
        fAuth.signOut()
    }

    fun checkIfUserIsNull(): Boolean {
        return (fAuth.currentUser == null)
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