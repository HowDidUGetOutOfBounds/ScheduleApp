package com.example.scheduleapp.UI

import android.content.Context
import android.content.SharedPreferences
import android.content.pm.ApplicationInfo
import android.content.pm.PackageManager
import android.os.Bundle
import android.text.Editable
import android.text.SpannableStringBuilder
import android.text.TextWatcher
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import android.widget.Toast
import androidx.navigation.findNavController
import androidx.navigation.fragment.findNavController
import com.example.scheduleapp.R
import com.example.scheduleapp.databinding.FragmentLoginBinding
import com.google.firebase.BuildConfig
import com.google.firebase.auth.FirebaseAuth

class LoginFragment : Fragment() {
    lateinit var mAuth: FirebaseAuth
    lateinit var mPreferences: SharedPreferences
    private lateinit var binding: FragmentLoginBinding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        mAuth = (activity as MainActivity).mAuth
        mPreferences = (activity as MainActivity).mPreferences

        // Inflate the layout for this fragment
        binding = FragmentLoginBinding.inflate(inflater, container, false)
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

        binding.registerButton.setOnClickListener {
            view.findNavController()
                .navigate(LoginFragmentDirections.actionLoginFragmentToRegistrationFragment())
        }
        binding.forgotButton.setOnClickListener {
            view.findNavController()
                .navigate(LoginFragmentDirections.actionLoginFragmentToResetFragment())
        }

        binding.userEmail.addTextChangedListener(getBlankStringsChecker())
        binding.userPassword.addTextChangedListener(getBlankStringsChecker())

        binding.loginButton.setOnClickListener {
            binding.progressBar.visibility = View.VISIBLE
            binding.loginButton.isEnabled = false
            mAuth.signInWithEmailAndPassword(binding.userEmail.text.toString(), binding.userPassword.text.toString()).addOnCompleteListener{login->
                binding.progressBar.visibility = View.GONE
                setButtonVisibility()

                if (login.isSuccessful()) {
                    Toast.makeText(activity, "Logged in successfully", Toast.LENGTH_SHORT).show()
                    Log.d("TAG", "Successful login")
                    view.findNavController()
                        .navigate(LoginFragmentDirections.actionLoginFragmentToFragmentContainer())
                } else {
                    Toast.makeText(activity, "Failed to log in: ${login.exception!!.message.toString()}", Toast.LENGTH_SHORT).show()
                    Log.d("TAG", login.exception!!.message.toString())
                }
            }
        }
    }

    override fun onStart() {
        super.onStart()

        if (mAuth.currentUser != null) {
            if (mPreferences.getBoolean("APP_PREFERENCES_STAY", false)) {
                view?.findNavController()!!
                    .navigate(LoginFragmentDirections.actionLoginFragmentToFragmentContainer())
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
            binding.loginButton.isEnabled =
                !(binding.userEmail.text.toString().isBlank() || binding.userPassword.text.toString().isBlank())
                        && binding.userPassword.text.toString().count() >= (activity as MainActivity).APP_MIN_PASSWORD_LENGTH
        }
    }

    companion object {
        fun newInstance() =
            LoginFragment()
    }
}