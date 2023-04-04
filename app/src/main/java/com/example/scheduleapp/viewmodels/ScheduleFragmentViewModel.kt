package com.example.scheduleapp.viewmodels

import android.util.Log
import androidx.lifecycle.ViewModel
import com.example.scheduleapp.data.GroupArray
import com.google.firebase.database.FirebaseDatabase
import com.google.gson.Gson
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class ScheduleFragmentViewModel @Inject constructor(val mDatabase: FirebaseDatabase) : ViewModel() {
    fun getAll(){
        mDatabase.getReference("").get().addOnCompleteListener { task ->
            val groups = Gson().fromJson(
                task.result.value.toString(), GroupArray::class.java
            ).GroupList
            Log.d("TAG_Schedule", "fun getAll ${groups[0]}")
        }
    }
}