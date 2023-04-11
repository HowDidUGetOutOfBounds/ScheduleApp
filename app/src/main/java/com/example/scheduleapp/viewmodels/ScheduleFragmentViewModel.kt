package com.example.scheduleapp.viewmodels

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.scheduleapp.data.Date
import com.example.scheduleapp.data.Group
import com.example.scheduleapp.data.GroupArray
import com.example.scheduleapp.models.FirebaseImplementation
import com.google.gson.Gson
import dagger.hilt.android.lifecycle.HiltViewModel
import java.text.FieldPosition
import java.util.Calendar
import javax.inject.Inject
import kotlin.collections.ArrayList

@HiltViewModel
class ScheduleFragmentViewModel @Inject constructor(
    private val rImplementation: FirebaseImplementation
) : ViewModel() {
    var appGroupArray: MutableLiveData<ArrayList<Group>> = MutableLiveData(arrayListOf())


    init {
        getAll()

    }

    fun getDayWithOffset(index: Int): Date? {
        var position = index - 1
        val c = Calendar.getInstance()
        if (position != 0) {
            c.add(Calendar.DATE, position)
        }


        val year = c.get(Calendar.YEAR)
        val month = c.get(Calendar.MONTH)
        val day = c.get(Calendar.DAY_OF_MONTH)
        return Date(year, month, day)
    }


    fun getAll() {
        //Added progress from DonwaldStatus
        try {
            //added successful from DonwaldStatus
            rImplementation.downloadDB().addOnCompleteListener { text ->
                appGroupArray.value = Gson().fromJson(
                    text.result.value.toString(), GroupArray::class.java
                ).GroupList
            }
        } catch (e: Exception) {
            //added error from DonwaldStatus
        }
    }
}