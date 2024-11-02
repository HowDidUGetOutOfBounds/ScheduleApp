package com.example.scheduleapp.viewmodels

import android.Manifest
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Context.NOTIFICATION_SERVICE
import android.content.Intent
import android.content.SharedPreferences
import android.content.pm.PackageManager
import android.os.Build
import android.util.Log
import androidx.annotation.RequiresApi
import androidx.core.app.ActivityCompat
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import com.example.scheduleapp.R
import com.example.scheduleapp.UI.MainActivity
import com.example.scheduleapp.UI.MainActivity.Companion.REQUEST_CODE_LOC_NOTIFICATION_MAIN_THREAD
import com.example.scheduleapp.data.Constants
import com.example.scheduleapp.data.Constants.APP_BD_PATHS_SCHEDULE_VERSION
import com.example.scheduleapp.data.Constants.APP_KEY_CHANNEL_ID
import com.example.scheduleapp.data.Constants.APP_PREFERENCES_SCHEDULE_VERSION
import com.google.android.gms.tasks.OnCompleteListener
import com.google.android.gms.tasks.Task
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.FirebaseDatabase
import kotlinx.coroutines.MainScope
import kotlinx.coroutines.launch
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale
import java.util.Timer
import java.util.TimerTask

class AlarmReceiver : BroadcastReceiver() {
    private lateinit var fDatabase: FirebaseDatabase
    private lateinit var sPreferences: SharedPreferences

    override fun onReceive(context: Context, intent: Intent) {
        Log.d("Not_Debugger", "Receiver transaction began.")

        fDatabase = FirebaseDatabase.getInstance()
        sPreferences = context.getSharedPreferences(Constants.APP_PREFERENCES, Context.MODE_PRIVATE)

        var currVersion: Long

        val mainTask = downloadByReference(APP_BD_PATHS_SCHEDULE_VERSION)
        var listener: OnCompleteListener<DataSnapshot>?
        val timer = setTimeout(5000L) {
            Log.d("Not_Debugger", "Ran out of time to download.")
            listener = null
        }

        listener = OnCompleteListener { task ->
            if (task.isSuccessful) {
                Log.d("Not_Debugger", "Download successful.")
                timer.cancel()
                currVersion = task.result.value.toString().toLong()
                if (currVersion > sPreferences.getLong(APP_PREFERENCES_SCHEDULE_VERSION, 0)) {
                    Log.d("Not_Debugger", "Sending notification.")
                    sPreferences.edit().putLong(APP_PREFERENCES_SCHEDULE_VERSION, currVersion).apply()
                    sendNotification(context)
                }
            }
        }
        mainTask.addOnCompleteListener(listener!!)
    }

    private fun downloadByReference(reference: String): Task<DataSnapshot> {
        return fDatabase.getReference(reference).get()
    }

    private fun setTimeout(time: Long, timerFunction: ()->Unit): Timer {
        val timer = Timer()
        val timerTask = object : TimerTask() {
            override fun run() {
                MainScope().launch {
                    timerFunction()
                }
            }
        }
        timer.schedule(timerTask, time)
        return timer
    }

    fun sendNotification(context:Context){
        val name: CharSequence = "MyNotification"
        val description = "My notification channel description"
        val importance = NotificationManager.IMPORTANCE_HIGH
        val notificationChannel = NotificationChannel(APP_KEY_CHANNEL_ID, name, importance)
        notificationChannel.description = description
        val notificationManager =
            context.getSystemService(NOTIFICATION_SERVICE) as NotificationManager
        notificationManager.createNotificationChannel(notificationChannel)

        val date = Date()
        val notificationId = SimpleDateFormat("ddHHmmss", Locale.US).format(date).toInt()

        val mainIntent = Intent(context, MainActivity::class.java)
        mainIntent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
        val mainPendingIntent = PendingIntent.getActivity(
            context,
            REQUEST_CODE_LOC_NOTIFICATION_MAIN_THREAD,
            mainIntent,
            PendingIntent.FLAG_MUTABLE
        )

        val notificationBuilder = NotificationCompat.Builder(context, APP_KEY_CHANNEL_ID)
        notificationBuilder.setSmallIcon(R.mipmap.ic_launcher)
        notificationBuilder.setContentTitle("Расписание")
        notificationBuilder.setContentText("Обновите расписание, новая версия вышла.")
        notificationBuilder.priority = NotificationCompat.PRIORITY_HIGH

        notificationBuilder.setAutoCancel(true)
        notificationBuilder.setContentIntent(mainPendingIntent)

        val notificationManagerCompat = NotificationManagerCompat.from(context)

        if (ActivityCompat.checkSelfPermission(
                context, Manifest.permission.POST_NOTIFICATIONS
            ) != PackageManager.PERMISSION_GRANTED
        ) {
            return
        }
        notificationManagerCompat.notify(notificationId, notificationBuilder.build())
    }
}