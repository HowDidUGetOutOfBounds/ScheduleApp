package com.example.scheduleapp.UI

import android.content.SharedPreferences
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
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

        binding.logoutTrigger.setOnClickListener {
            mAuth.signOut()
            mPreferences.edit()
                .putBoolean("APP_PREFERENCES_STAY", false)
                .apply()
            view.findNavController()
                .navigate(SettingsFragmentDirections.actionSettingsFragmentToLoginFragment())
        }
    }

    companion object {
        fun newInstance() =
            SettingsFragment()
    }
}