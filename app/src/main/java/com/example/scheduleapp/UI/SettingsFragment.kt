package com.example.scheduleapp.UI

import android.content.SharedPreferences
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import android.widget.ArrayAdapter
import androidx.appcompat.widget.SwitchCompat
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.navigation.findNavController
import androidx.navigation.fragment.findNavController
import com.example.scheduleapp.R
import com.example.scheduleapp.databinding.FragmentLoginBinding
import com.example.scheduleapp.databinding.FragmentSettingsBinding
import com.example.scheduleapp.viewmodels.MainActivityViewModel
import com.google.firebase.auth.FirebaseAuth
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class SettingsFragment : Fragment() {
    private val exampleViewModel: MainActivityViewModel by activityViewModels()

    lateinit var mAuth: FirebaseAuth
    lateinit var mPreferences: SharedPreferences
    private lateinit var binding: FragmentSettingsBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        mAuth = (activity as MainActivity).mAuth
        mPreferences = (activity as MainActivity).mPreferences

        // Inflate the layout for this fragment
        binding = FragmentSettingsBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.enablePushesCheckBox.setOnCheckedChangeListener(){v, checked ->
            if(checked)
            {
                findNavController().navigate(R.id.loginFragment)
            }
        }

        binding.selectGroupSpinner.adapter = ArrayAdapter((activity as MainActivity), android.R.layout.simple_spinner_item, (activity as MainActivity).APP_GROUP_LIST).also { adapter ->
            adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        }
        for (i in 0 until binding.selectGroupSpinner.adapter.count) {
            if (binding.selectGroupSpinner.getItemAtPosition(i).toString() == mPreferences.getString("APP_PREFERENCES_GROUP", "")) {
                binding.selectGroupSpinner.setSelection(i)
                break
            }
        }
        binding.selectGroupSpinner.onItemSelectedListener = object: AdapterView.OnItemSelectedListener {
            override fun onItemSelected(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {
                mPreferences.edit()
                    .putString("APP_PREFERENCES_GROUP", parent?.getItemAtPosition(position).toString())
                    .apply()
                (activity as MainActivity).title = mPreferences.getString("APP_PREFERENCES_GROUP", resources.getString(R.string.app_name))
            }
            override fun onNothingSelected(parent: AdapterView<*>?) {}
        }

        binding.logoutTrigger.setOnClickListener {
            mAuth.signOut()
            mPreferences.edit()
                .putBoolean("APP_PREFERENCES_STAY", false)
                .putString("APP_PREFERENCES_GROUP", null)
                .apply()
            (activity as MainActivity).title = resources.getString(R.string.app_name)

            view.findNavController()
                .navigate(SettingsFragmentDirections.actionSettingsFragmentToLoginFragment())
        }
    }

    companion object {
        fun newInstance() =
            SettingsFragment()
    }
}