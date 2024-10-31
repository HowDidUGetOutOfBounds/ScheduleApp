package com.example.scheduleapp.UI

import android.annotation.SuppressLint
import android.app.AlarmManager
import android.app.Notification
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Build
import android.os.Bundle
import android.util.Log
import android.view.Menu
import android.view.MenuItem
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContract
import androidx.activity.result.contract.ActivityResultContracts
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import androidx.navigation.findNavController
import com.example.scheduleapp.R
import com.example.scheduleapp.data.Constants.APP_PREFERENCES_PUSHES
import com.example.scheduleapp.data.Constants.APP_PREFERENCES_STAY
import com.example.scheduleapp.data.Constants.APP_TOAST_NOT_SIGNED_IN
import com.example.scheduleapp.databinding.ActivityMainBinding
import com.example.scheduleapp.viewmodels.AlarmReceiver
import com.example.scheduleapp.viewmodels.MainActivityViewModel
import dagger.hilt.android.AndroidEntryPoint
import java.util.Calendar

@AndroidEntryPoint
class MainActivity : AppCompatActivity() {
    private val viewModel: MainActivityViewModel by viewModels()
    private lateinit var binding: ActivityMainBinding


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        supportActionBar!!.setBackgroundDrawable(ColorDrawable(Color.parseColor("#05080D")))

        viewModel.editPreferences(
            APP_PREFERENCES_STAY,
            viewModel.getPreference(APP_PREFERENCES_STAY, true)
        )
        viewModel.editPreferences(
            APP_PREFERENCES_PUSHES,
            viewModel.getPreference(APP_PREFERENCES_PUSHES, false)
        )

    }


    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.main_menu, menu)
        return super.onCreateOptionsMenu(menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.settings -> {
                if (!viewModel.isUserSingedIn()) {
                    Toast.makeText(this, APP_TOAST_NOT_SIGNED_IN, Toast.LENGTH_SHORT).show()
                    return false
                } else if (supportFragmentManager.primaryNavigationFragment!!.childFragmentManager.fragments.last()::class.java == SettingsFragment::class.java) {
                    Log.d("TAG", "Refusing to go to settings.")
                    return false
                } else {
                    binding.container.findNavController().navigate(R.id.settingsFragment)
                    return true
                }
            }
        }
        return super.onOptionsItemSelected(item)
    }
}