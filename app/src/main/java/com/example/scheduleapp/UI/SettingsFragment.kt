package com.example.scheduleapp.UI

import android.Manifest
import android.app.AlarmManager
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.Toast
import androidx.core.app.ActivityCompat
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.navigation.findNavController
import com.example.scheduleapp.R
import com.example.scheduleapp.data.Constants.APP_LINK_CONTACT_US
import com.example.scheduleapp.data.Constants.APP_PREFERENCES_GROUP
import com.example.scheduleapp.data.Constants.APP_PREFERENCES_PUSHES
import com.example.scheduleapp.data.Constants.APP_PREFERENCES_STAY
import com.example.scheduleapp.data.Constants.APP_TOAST_NOTIFICATIONS_NOT_ALLOWED
import com.example.scheduleapp.databinding.FragmentSettingsBinding
import com.example.scheduleapp.viewmodels.MainActivityViewModel
import dagger.hilt.android.AndroidEntryPoint


@AndroidEntryPoint
class SettingsFragment : Fragment() {
    private val viewModel: MainActivityViewModel by activityViewModels()
    private lateinit var binding: FragmentSettingsBinding
    private lateinit var alarmManager: AlarmManager

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

        alarmManager = requireActivity().getSystemService(Context.ALARM_SERVICE) as AlarmManager

        binding.enablePushesCheckBox.isChecked =
            viewModel.getPreference(APP_PREFERENCES_PUSHES, false)
        binding.staySignedInCheckBox.isChecked =
            viewModel.getPreference(APP_PREFERENCES_STAY, false)
        binding.enablePushesCheckBox.setOnCheckedChangeListener { v, checked ->
            if (checked && !viewModel.getPreference(APP_PREFERENCES_PUSHES, false)) {
                viewModel.setNotification(context, alarmManager)
            } else if (!checked && viewModel.getPreference(APP_PREFERENCES_PUSHES, false)) {
                viewModel.cancelNotification(context, alarmManager)
            }
            viewModel.editPreferences(APP_PREFERENCES_PUSHES, checked)
        }
        binding.staySignedInCheckBox.setOnCheckedChangeListener() { v, checked ->
            viewModel.editPreferences(APP_PREFERENCES_STAY, checked)
        }

        if (ActivityCompat.checkSelfPermission(requireActivity(), Manifest.permission.POST_NOTIFICATIONS) != PackageManager.PERMISSION_GRANTED) {
            Toast.makeText(requireActivity(), APP_TOAST_NOTIFICATIONS_NOT_ALLOWED, Toast.LENGTH_LONG).show()
            binding.enablePushesCheckBox.isEnabled = false
        }

        binding.selectGroupSpinner.adapter = ArrayAdapter(
            (activity as MainActivity),
            R.layout.spinner_item,
            viewModel.getGroupNames()
        ).also { adapter ->
            adapter.setDropDownViewResource(R.layout.spinner_dropdown_item)
        }
        for (i in 0 until binding.selectGroupSpinner.adapter.count) {
            if (binding.selectGroupSpinner.getItemAtPosition(i).toString() == viewModel.getPreference(getGroupPreferencesId(), "")) {
                binding.selectGroupSpinner.setSelection(i)
                break
            }
        }
        binding.selectGroupSpinner.onItemSelectedListener =
            object : AdapterView.OnItemSelectedListener {
                override fun onItemSelected(
                    parent: AdapterView<*>?,
                    view: View?,
                    position: Int,
                    id: Long
                ) {
                    viewModel.editPreferences(getGroupPreferencesId(), parent?.getItemAtPosition(position).toString())
                    (activity as MainActivity).title = viewModel.getPreference(
                        getGroupPreferencesId(),
                        resources.getString(R.string.app_name)
                    )
                }

                override fun onNothingSelected(parent: AdapterView<*>?) {}
            }

        binding.logoutTrigger.setOnClickListener {
            logOut()
        }

        binding.contactUsTextView.setOnClickListener {
            var url = APP_LINK_CONTACT_US
            if (!url.startsWith("http://") && !url.startsWith("https://")) {
                url = "http://$url"
            }
            val browserIntent = Intent(Intent.ACTION_VIEW, Uri.parse(url))
            startActivity(browserIntent)
        }
    }

    private fun logOut() {
        viewModel.signOut()
        viewModel.editPreferences(APP_PREFERENCES_STAY, false)
        (activity as MainActivity).title = resources.getString(R.string.app_name)

        requireView().findNavController()
            .navigate(SettingsFragmentDirections.actionSettingsFragmentToLoginFragment())
    }

    private fun getGroupPreferencesId(): String {
        return APP_PREFERENCES_GROUP + "_" + viewModel.getUserEmail()
    }

}