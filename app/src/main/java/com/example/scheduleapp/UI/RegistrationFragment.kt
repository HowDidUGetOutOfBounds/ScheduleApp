package com.example.scheduleapp.UI

import android.content.Context
import android.content.SharedPreferences
import android.os.Bundle
import android.text.Editable
import android.text.SpannableStringBuilder
import android.text.TextWatcher
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.navigation.findNavController
import com.example.scheduleapp.BuildConfig
import com.example.scheduleapp.databinding.FragmentRegistrationBinding
import com.google.firebase.auth.FirebaseAuth

class RegistrationFragment : Fragment() {
    lateinit var mAuth: FirebaseAuth
    lateinit var mPreferences: SharedPreferences
    private lateinit var binding: FragmentRegistrationBinding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        mAuth = (activity as MainActivity).mAuth
        mPreferences = (activity as MainActivity).mPreferences

        // Inflate the layout for this fragment
        binding = FragmentRegistrationBinding.inflate(layoutInflater)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.stayCheck.isChecked = mPreferences.getBoolean("APP_PREFERENCES_STAY", false)
        binding.stayCheck.setOnCheckedChangeListener { buttonView, isChecked ->
            mPreferences.edit()
                .putBoolean("APP_PREFERENCES_STAY", isChecked)
                .apply()
        }

        binding.loginButton.setOnClickListener {
            view.findNavController()
                .navigate(RegistrationFragmentDirections.actionRegistrationFragmentToLoginFragment())
        }

        binding.userEmail.addTextChangedListener(getBlankStringsChecker())
        binding.userPassword1.addTextChangedListener(getBlankStringsChecker())
        binding.userPassword2.addTextChangedListener(getBlankStringsChecker())

        binding.registerButton.setOnClickListener {
            if (binding.userPassword1.text.toString().count() < (activity as MainActivity).APP_MIN_PASSWORD_LENGTH) {
                Toast.makeText(activity, "Your password should be at least 8 characters long", Toast.LENGTH_SHORT).show()
            } else if (!binding.userPassword1.text.toString().equals(binding.userPassword2.text.toString())) {
                Toast.makeText(activity, "Your passwords don't match. Please confirm your password.", Toast.LENGTH_SHORT).show()
            } else {
                binding.progressBar.visibility = View.VISIBLE
                binding.registerButton.isEnabled = false

                mAuth.createUserWithEmailAndPassword(binding.userEmail.text.toString(), binding.userPassword1.text.toString()).addOnCompleteListener{registration->
                    binding.progressBar.visibility = View.GONE
                    setButtonVisibility()

                    if (registration.isSuccessful()) {
                        Toast.makeText(activity, "Registration success.", Toast.LENGTH_SHORT).show()
                        Log.d("TAG", "Successful registration")
                        view.findNavController()
                            .navigate(RegistrationFragmentDirections.actionRegistrationFragmentToLoginFragment())
                    } else {
                        Toast.makeText(activity, "Registration failure.", Toast.LENGTH_SHORT).show()
                        Log.d("TAG", registration.exception!!.message.toString())
                    }
                }
            }
        }
    }

    fun getBlankStringsChecker(): TextWatcher {
        return object: TextWatcher {
            override fun afterTextChanged(s: Editable) { setButtonVisibility() }
            override fun beforeTextChanged(s: CharSequence, start: Int, count: Int, after: Int) {}
            override fun onTextChanged(s: CharSequence, start: Int, before: Int, count: Int) {}
        }
    }

    fun setButtonVisibility() {
        if (binding.progressBar.visibility == View.GONE) {
            binding.registerButton.isEnabled =
                !arrayListOf(binding.userEmail.text.toString().trim(), binding.userPassword1.text.toString().trim(), binding.userPassword2.text.toString().trim()).contains("")
        }
    }

    companion object {
        fun newInstance() =
            RegistrationFragment()
    }
}