package com.example.scheduleapp.UI

import android.content.Context
import android.content.SharedPreferences
import android.os.Bundle
import android.util.Log
import android.view.Menu
import android.view.MenuItem
import android.widget.Toast
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.LiveData
import androidx.navigation.findNavController
import com.example.scheduleapp.R
import com.example.scheduleapp.adapters.MainScreenAdapter
import com.example.scheduleapp.data.GroupArray
import com.example.scheduleapp.viewmodels.MainActivityViewModel
import com.example.scheduleapp.databinding.ActivityMainBinding
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.FirebaseDatabase
import com.google.gson.Gson
import com.google.gson.annotations.SerializedName
import dagger.hilt.android.AndroidEntryPoint
import retrofit2.converter.gson.GsonConverterFactory

@AndroidEntryPoint
class MainActivity : AppCompatActivity() {
    val APP_GROUP_LIST = ArrayList<String>()
    val APP_MIN_PASSWORD_LENGTH = 8
    val mDatabase = FirebaseDatabase.getInstance()
    val mAuth = FirebaseAuth.getInstance()

    lateinit var mPreferences: SharedPreferences
    private lateinit var binding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        mPreferences = this.getSharedPreferences("APP_PREFERENCES", Context.MODE_PRIVATE)

        mDatabase.getReference("").get().addOnCompleteListener { task ->
            if (task.isSuccessful) {
                Log.d("TAG", "Successfully downloaded the data from the database.")

                try {
                    var groups = Gson().fromJson(task.result.value.toString(), GroupArray::class.java).GroupList
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