package com.example.scheduleapp.viewmodels

import android.content.Context
import android.content.SharedPreferences
import android.util.Log
import androidx.lifecycle.ViewModel
import com.example.scheduleapp.R
import com.example.scheduleapp.data.Group
import com.example.scheduleapp.data.GroupArray
import com.google.android.gms.tasks.Task
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.FirebaseDatabase
import com.google.gson.Gson
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class ScheduleFragmentViewModel @Inject constructor(val mContext: Context, var mDatabase: FirebaseDatabase) : ViewModel() {
    private lateinit var APP_GROUP_ARRAY: ArrayList<Group>
    private lateinit var mPreferences: SharedPreferences
    init {
        InitializeParameters()
        getAll()
    }

    fun InitializeParameters() {
        mDatabase = FirebaseDatabase.getInstance()
        mPreferences = mContext.getSharedPreferences(mContext.resources.getString(R.string.app_name), Context.MODE_PRIVATE)
    }
    fun getAll(): Task<DataSnapshot> {
        return mDatabase.getReference("").get()
    }

    fun fillAll(value: String) {
        APP_GROUP_ARRAY = Gson().fromJson(
            value, GroupArray::class.java
        ).GroupList
        Log.d("TAG_Schedule", "fun getAll ${APP_GROUP_ARRAY[0]}")
    }

    fun getGroup(): Group? {
        for (i in 0 until APP_GROUP_ARRAY.size) {
            if (APP_GROUP_ARRAY[i].groupname == mPreferences.getString(mContext.getString(R.string.app_preferences_group), null)) {
                return APP_GROUP_ARRAY[i]
            }
        }
        return Group()
    }
}