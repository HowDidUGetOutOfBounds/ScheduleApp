package com.example.scheduleapp.UI

import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.EditText
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.navigation.findNavController
import com.example.scheduleapp.R
import com.example.scheduleapp.data.AuthenticationStatus
import com.example.scheduleapp.data.Constants
import com.example.scheduleapp.data.Data_IntString
import com.example.scheduleapp.databinding.FragmentRegistrationBinding
import com.example.scheduleapp.viewmodels.MainActivityViewModel
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class RegistrationFragment : Fragment() {
    private val viewModel: MainActivityViewModel by activityViewModels()
    private lateinit var binding: FragmentRegistrationBinding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        binding = FragmentRegistrationBinding.inflate(layoutInflater)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.selectGroupSpinner.adapter = ArrayAdapter((activity as MainActivity), R.layout.spinner_item, viewModel.getGroupNames()).also { adapter ->
            adapter.setDropDownViewResource(R.layout.spinner_dropdown_item)
        }
        for (i in 0 until binding.selectGroupSpinner.adapter.count) {
            if (binding.selectGroupSpinner.getItemAtPosition(i).toString() == viewModel.getPreference(Constants.APP_PREFERENCES_GROUP_REGISTER, "")) {
                binding.selectGroupSpinner.setSelection(i)
                break
            }
        }
        binding.selectGroupSpinner.onItemSelectedListener = object: AdapterView.OnItemSelectedListener {
            override fun onItemSelected(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {
                viewModel.editPreferences()
                    .putString(Constants.APP_PREFERENCES_GROUP_REGISTER, parent?.getItemAtPosition(position).toString())
                    .apply()
            }
            override fun onNothingSelected(parent: AdapterView<*>?) {}
        }

        binding.loginButton.setOnClickListener {
            view.findNavController()
                .navigate(RegistrationFragmentDirections.actionRegistrationFragmentToLoginFragment())
        }

        binding.userEmail.addTextChangedListener(getBlankStringsChecker(binding.userEmail))
        binding.userPassword1.addTextChangedListener(getBlankStringsChecker(binding.userPassword1))
        binding.userPassword2.addTextChangedListener(getBlankStringsChecker(binding.userPassword2))

        binding.registerButton.setOnClickListener {
            signUp()
        }

        initObservers()
    }

    fun setButtonVisibility() {
        if (binding.progressBar.visibility == View.GONE) {
            binding.registerButton.isEnabled =
                !(binding.userEmail.text.toString().isBlank() || binding.userPassword1.text.toString().isBlank() || binding.userPassword2.text.toString().isBlank())
        }
    }

    fun getBlankStringsChecker(textInput: EditText): TextWatcher {
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

    fun signUp() {
        if (binding.userPassword1.text.toString().count() < Constants.APP_MIN_PASSWORD_LENGTH) {
            Toast.makeText(activity, "Your password should be at least ${Constants.APP_MIN_PASSWORD_LENGTH} characters long", Toast.LENGTH_SHORT).show()
        } else if (!binding.userPassword1.text.toString().equals(binding.userPassword2.text.toString())) {
            Toast.makeText(activity, "Your passwords don't match. Please confirm your password.", Toast.LENGTH_SHORT).show()
        } else {
            viewModel.signIn(binding.userEmail.text.toString(), binding.userPassword1.text.toString(), true)
        }
    }

    fun initObservers() {
        viewModel.authState.observe(viewLifecycleOwner) {authStatus->
            when (authStatus) {
                is AuthenticationStatus.Success -> {
                    setButtonVisibility()
                    binding.progressBar.visibility = View.GONE
                    Toast.makeText(activity, "Registered successfully.", Toast.LENGTH_SHORT).show()
                    Log.d("TAG", "Successful registration")

                    viewModel.editPreferences()
                        .putString(Constants.APP_PREFERENCES_GROUP + "_" + binding.userEmail.text.toString(), viewModel.getPreference(Constants.APP_PREFERENCES_GROUP_REGISTER, ""))
                        .putString(Constants.APP_PREFERENCES_GROUP_REGISTER, null)
                        .apply()
                    requireView().findNavController()
                        .navigate(RegistrationFragmentDirections.actionRegistrationFragmentToLoginFragment())
                }
                is AuthenticationStatus.Error -> {
                    setButtonVisibility()
                    binding.progressBar.visibility = View.GONE
                    Toast.makeText(activity, "Failed to sign up: ${authStatus.message}", Toast.LENGTH_LONG).show()
                    Log.d("TAG", authStatus.message)
                }
                is AuthenticationStatus.Progress -> {
                    binding.loginButton.isEnabled = false
                    binding.progressBar.visibility = View.VISIBLE
                }
                else -> {}
            }
        }
    }
}