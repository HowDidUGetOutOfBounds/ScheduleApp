package com.example.scheduleapp.UI

import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.activityViewModels
import androidx.navigation.findNavController
import com.example.scheduleapp.R
import com.example.scheduleapp.databinding.FragmentLoginBinding
import com.example.scheduleapp.viewmodels.OuterViewModel
import com.google.android.material.textfield.TextInputEditText
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class LoginFragment : Fragment() {
    private val viewModel: OuterViewModel by activityViewModels()
    private lateinit var binding: FragmentLoginBinding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        binding = FragmentLoginBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        if (!viewModel.checkIfUserIsNull()) {
            view.findNavController()
                .navigate(LoginFragmentDirections.actionLoginFragmentToFragmentContainer())
        }

        binding.stayCheck.isChecked = viewModel.getPreference(resources.getString(R.string.app_preferences_stay), false)
        binding.stayCheck.setOnCheckedChangeListener { buttonView, isChecked ->
            viewModel.editPreferences()
                .putBoolean(resources.getString(R.string.app_preferences_stay), isChecked)
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

        binding.userEmail.addTextChangedListener(getBlankStringsChecker(binding.userEmail))
        binding.userPassword.addTextChangedListener(getBlankStringsChecker(binding.userPassword))

        binding.loginButton.setOnClickListener {
            signIn()
        }
    }

    fun setButtonVisibility() {
        if (binding.progressBar.visibility == View.GONE) {
            binding.loginButton.isEnabled =
                !(binding.userEmail.text.toString().isBlank() || binding.userPassword.text.toString().isBlank())
                        && binding.userPassword.text.toString().count() >= viewModel.getMinPasswordLength()
        }
    }

    fun getBlankStringsChecker(textInput: TextInputEditText): TextWatcher {
        return object: TextWatcher {
            override fun afterTextChanged(s: Editable) {
                if (textInput.text.toString().replace(" ", "") == textInput.text.toString()) {
                    setButtonVisibility()
                } else {
                    textInput.setText(textInput.text.toString().replace(" ", ""))
                }
            }
            override fun beforeTextChanged(s: CharSequence, start: Int, count: Int, after: Int) {}
            override fun onTextChanged(s: CharSequence, start: Int, before: Int, count: Int) {}
        }
    }

    fun signIn() {
        binding.progressBar.visibility = View.VISIBLE
        binding.loginButton.isEnabled = false
        viewModel.signIn(binding.userEmail.text.toString(), binding.userPassword.text.toString(), false).addOnCompleteListener{login->
            binding.progressBar.visibility = View.GONE
            setButtonVisibility()

            if (login.isSuccessful()) {
                Toast.makeText(activity, "Logged in successfully", Toast.LENGTH_SHORT).show()
                Log.d("TAG", "Successful login")
                requireView().findNavController()
                    .navigate(LoginFragmentDirections.actionLoginFragmentToFragmentContainer())
            } else {
                Toast.makeText(activity, "Failed to log in: ${login.exception!!.message.toString()}", Toast.LENGTH_LONG).show()
                Log.d("TAG", login.exception!!.message.toString())
            }
        }
    }

    companion object {
        fun newInstance() =
            LoginFragment()
    }
}