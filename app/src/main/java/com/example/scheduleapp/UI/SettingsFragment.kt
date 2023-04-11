package com.example.scheduleapp.UI

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import android.widget.ArrayAdapter
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.navigation.findNavController
import com.example.scheduleapp.R
import com.example.scheduleapp.data.Constants
import com.example.scheduleapp.databinding.FragmentSettingsBinding
import com.example.scheduleapp.viewmodels.MainActivityViewModel
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class SettingsFragment : Fragment() {
    private val viewModel: MainActivityViewModel by activityViewModels()
    private lateinit var binding: FragmentSettingsBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        binding = FragmentSettingsBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.enablePushesCheckBox.isChecked = viewModel.getPreference(Constants.app_preferences_pushes, false)
        binding.staySignedInCheckBox.isChecked = viewModel.getPreference(Constants.app_preferences_stay, false)
        binding.enablePushesCheckBox.setOnCheckedChangeListener(){v, checked ->
            viewModel.editPreferences()
                .putBoolean(Constants.app_preferences_pushes, checked)
                .apply()
        }
        binding.staySignedInCheckBox.setOnCheckedChangeListener(){v, checked ->
            viewModel.editPreferences()
                .putBoolean(Constants.app_preferences_stay, checked)
                .apply()
        }

        binding.selectGroupSpinner.adapter = ArrayAdapter((activity as MainActivity), R.layout.spinner_item, viewModel.getGroupList()).also { adapter ->
            adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        }
        for (i in 0 until binding.selectGroupSpinner.adapter.count) {
            if (binding.selectGroupSpinner.getItemAtPosition(i).toString() == viewModel.getPreference(getGroupPreferencesId(), "")) {
                binding.selectGroupSpinner.setSelection(i)
                break
            }
        }
        binding.selectGroupSpinner.onItemSelectedListener = object: AdapterView.OnItemSelectedListener {
            override fun onItemSelected(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {
                viewModel.editPreferences()
                    .putString(getGroupPreferencesId(), parent?.getItemAtPosition(position).toString())
                    .apply()
                (activity as MainActivity).title = viewModel.getPreference(getGroupPreferencesId(), resources.getString(R.string.app_name))
            }
            override fun onNothingSelected(parent: AdapterView<*>?) {}
        }

        binding.logoutTrigger.setOnClickListener {
            logOut()
        }
    }

    fun logOut() {
        viewModel.signOut()
        viewModel.editPreferences()
            .putBoolean(Constants.app_preferences_stay, false)
            .apply()
        (activity as MainActivity).title = resources.getString(R.string.app_name)

        requireView().findNavController()
            .navigate(SettingsFragmentDirections.actionSettingsFragmentToLoginFragment())
    }

    fun getGroupPreferencesId(): String {
        return Constants.app_preferences_group+"_"+viewModel.getCurrentUser()!!.email.toString()
    }

    companion object {
        fun newInstance() =
            SettingsFragment()
    }
}