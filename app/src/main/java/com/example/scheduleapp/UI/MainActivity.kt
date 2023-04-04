package com.example.scheduleapp.UI

import android.os.Bundle
import android.util.Log
import android.view.Menu
import android.view.MenuItem
import android.widget.Toast
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.navigation.findNavController
import com.example.scheduleapp.R
import com.example.scheduleapp.databinding.ActivityMainBinding
import com.example.scheduleapp.viewmodels.OuterViewModel
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : AppCompatActivity() {
    private val viewModel: OuterViewModel by viewModels()
    private lateinit var binding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.main_menu, menu)
        return super.onCreateOptionsMenu(menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.settings -> {
                if (viewModel.checkIfUserIsNull()) {
                    Toast.makeText(this, "You aren't signed in yet.", Toast.LENGTH_SHORT).show()
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


    override fun onDestroy() {
        if (!viewModel.getPreference(resources.getString(R.string.app_preferences_stay), false)) {
            if (!viewModel.checkIfUserIsNull()) {
                viewModel.signOut()
            }
        }
        super.onDestroy()
    }
}