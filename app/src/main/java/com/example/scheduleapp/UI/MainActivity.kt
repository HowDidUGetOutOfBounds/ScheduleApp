package com.example.scheduleapp.UI

import android.Manifest
import android.app.AlarmManager
import android.content.Context
import android.content.pm.PackageManager
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.util.Log
import android.view.Menu
import android.view.MenuItem
import android.widget.Toast
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.navigation.findNavController
import com.example.scheduleapp.R
import com.example.scheduleapp.data.Constants.APP_PREFERENCES_PUSHES
import com.example.scheduleapp.data.Constants.APP_PREFERENCES_SCHEDULE_VERSION
import com.example.scheduleapp.data.Constants.APP_PREFERENCES_STAY
import com.example.scheduleapp.data.Constants.APP_TOAST_NOT_SIGNED_IN
import com.example.scheduleapp.databinding.ActivityMainBinding
import com.example.scheduleapp.viewmodels.MainActivityViewModel
import dagger.hilt.android.AndroidEntryPoint

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
            (ActivityCompat.checkSelfPermission(this, Manifest.permission.POST_NOTIFICATIONS) == PackageManager.PERMISSION_GRANTED) && viewModel.getPreference(APP_PREFERENCES_PUSHES, false)
        )

        if (viewModel.getPreference(APP_PREFERENCES_PUSHES, false)) {
            viewModel.setNotification(this, getSystemService(Context.ALARM_SERVICE) as AlarmManager)
        }

    }


    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.main_menu, menu)
        return super.onCreateOptionsMenu(menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        if (!viewModel.isUserSingedIn()) {
            Toast.makeText(this, APP_TOAST_NOT_SIGNED_IN, Toast.LENGTH_SHORT).show()
            return false
        } else {
            val currentFragment = supportFragmentManager.primaryNavigationFragment!!.childFragmentManager.fragments.last()
            when (item.itemId) {
                R.id.settings -> {
                    if (currentFragment::class.java == SettingsFragment::class.java) {
                        Log.d("TAG", "Refusing to go to settings.")
                        return false
                    } else {
                        binding.container.findNavController().navigate(R.id.settingsFragment)
                        return true
                    }
                }

                R.id.reloadButton -> {
                    if (currentFragment::class.java != FragmentContainer::class.java) {
                        Log.d("TAG", "Refusing to reload fragment container.")
                        return false
                    } else {
                        (currentFragment as FragmentContainer).reloadSchedule()
                        return true
                    }
                }
            }
        }
        return super.onOptionsItemSelected(item)
    }

    companion object {
        const val REQUEST_CODE_LOC_NOTIFICATION_MAIN_THREAD = 1
        const val REQUEST_CODE_LOC_NOTIFICATION_PERMISSION = 2
        const val REQUEST_CODE_LOC_NOTIFICATION_ID = 4
    }
}