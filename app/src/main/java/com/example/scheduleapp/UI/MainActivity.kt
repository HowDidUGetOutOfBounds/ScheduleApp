package com.example.scheduleapp.UI

import android.content.Context
import android.content.SharedPreferences
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.widget.Toast
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.activityViewModels
import androidx.navigation.findNavController
import com.example.scheduleapp.R
import com.example.scheduleapp.adapters.MainScreenAdapter
import com.example.scheduleapp.viewmodels.MainActivityViewModel
import com.example.scheduleapp.databinding.ActivityMainBinding
import com.google.firebase.auth.FirebaseAuth
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : AppCompatActivity() {
    val APP_MIN_PASSWORD_LENGTH = 8
    val mAuth = FirebaseAuth.getInstance()

    lateinit var mPreferences: SharedPreferences
    private lateinit var binding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        mPreferences = this.getSharedPreferences("APP_PREFERENCES", Context.MODE_PRIVATE)
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.main_menu, menu)
        return super.onCreateOptionsMenu(menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.settings -> {
                if (mAuth.currentUser != null) {
                    binding.container.findNavController().navigate(R.id.settingsFragment)
                } else {
                    Toast.makeText(this, "You aren't signed in yet.", Toast.LENGTH_SHORT).show()
                }
                return true
            }
        }
        return super.onOptionsItemSelected(item)
    }


    override fun onDestroy() {
        if (!mPreferences.getBoolean("APP_PREFERENCES_STAY", false)) {
            if (mAuth.currentUser != null) {
                mAuth.signOut()
            }
        }
        super.onDestroy()
    }
}