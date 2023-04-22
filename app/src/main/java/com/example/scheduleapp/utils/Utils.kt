package com.example.scheduleapp.utils

import android.app.Activity
import com.google.android.gms.tasks.OnFailureListener
import com.google.android.gms.tasks.OnSuccessListener
import com.google.android.gms.tasks.Task
import com.google.firebase.database.DataSnapshot
import java.util.concurrent.Executor

object Utils {

    /**
     * As firebase doesn't give us error on timeout and just keeps retrying,
     * we just generate our own Unsuccessful Event.
     * Note that no other function is called except for isSuccessful()
     */
    fun createUnsuccessfulTask(): Task<DataSnapshot> {
        return object : Task<DataSnapshot>() {
            override fun addOnFailureListener(p0: OnFailureListener): Task<DataSnapshot> {
                TODO("Not yet implemented")
            }

            override fun addOnFailureListener(
                p0: Activity,
                p1: OnFailureListener
            ): Task<DataSnapshot> {
                TODO("Not yet implemented")
            }

            override fun addOnFailureListener(
                p0: Executor,
                p1: OnFailureListener
            ): Task<DataSnapshot> {
                TODO("Not yet implemented")
            }

            override fun getException(): java.lang.Exception? {
                TODO("Not yet implemented")
            }

            override fun getResult(): DataSnapshot {
                TODO("Not yet implemented")
            }

            override fun <X : Throwable?> getResult(p0: Class<X>): DataSnapshot {
                TODO("Not yet implemented")
            }

            override fun isCanceled(): Boolean {
                TODO("Not yet implemented")
            }

            override fun isComplete(): Boolean {
                TODO("Not yet implemented")
            }

            override fun addOnSuccessListener(
                p0: Executor,
                p1: OnSuccessListener<in DataSnapshot>
            ): Task<DataSnapshot> {
                TODO("Not yet implemented")
            }

            override fun addOnSuccessListener(
                p0: Activity,
                p1: OnSuccessListener<in DataSnapshot>
            ): Task<DataSnapshot> {
                TODO("Not yet implemented")
            }

            override fun addOnSuccessListener(p0: OnSuccessListener<in DataSnapshot>): Task<DataSnapshot> {
                TODO("Not yet implemented")
            }

            override fun isSuccessful(): Boolean {
                return false
            }

        }
    }
}