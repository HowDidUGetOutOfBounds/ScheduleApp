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
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.navigation.findNavController
import com.example.scheduleapp.R
import com.example.scheduleapp.databinding.FragmentRegistrationBinding
import com.example.scheduleapp.viewmodels.OuterViewModel
import com.google.android.material.textfield.TextInputEditText
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class RegistrationFragment : Fragment() {
    private val viewModel: OuterViewModel by activityViewModels()
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

        binding.stayCheck.isChecked = viewModel.getPreference(resources.getString(R.string.app_preferences_stay), false)
        binding.stayCheck.setOnCheckedChangeListener { buttonView, isChecked ->
            viewModel.editPreferences()
                .putBoolean(resources.getString(R.string.app_preferences_stay), isChecked)
                .apply()
        }

        binding.selectGroupSpinner.adapter = ArrayAdapter((activity as MainActivity), android.R.layout.simple_spinner_item, viewModel.getGroupList()).also { adapter ->
            adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        }
        for (i in 0 until binding.selectGroupSpinner.adapter.count) {
            if (binding.selectGroupSpinner.getItemAtPosition(i).toString() == viewModel.getPreference(resources.getString(R.string.app_preferences_group_register), "")) {
                binding.selectGroupSpinner.setSelection(i)
                break
            }
        }
        binding.selectGroupSpinner.onItemSelectedListener = object: AdapterView.OnItemSelectedListener {
            override fun onItemSelected(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {
                viewModel.editPreferences()
                    .putString(resources.getString(R.string.app_preferences_group_register), parent?.getItemAtPosition(position).toString())
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
    }

    fun setButtonVisibility() {
        if (binding.progressBar.visibility == View.GONE) {
            binding.registerButton.isEnabled =
                !(binding.userEmail.text.toString().isBlank() || binding.userPassword1.text.toString().isBlank() || binding.userPassword2.text.toString().isBlank())
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

    fun signUp() {
        if (binding.userPassword1.text.toString().count() < viewModel.getMinPasswordLength()) {
            Toast.makeText(activity, "Your password should be at least 8 characters long", Toast.LENGTH_SHORT).show()
        } else if (!binding.userPassword1.text.toString().equals(binding.userPassword2.text.toString())) {
            Toast.makeText(activity, "Your passwords don't match. Please confirm your password.", Toast.LENGTH_SHORT).show()
        } else {
            binding.progressBar.visibility = View.VISIBLE
            binding.registerButton.isEnabled = false

            viewModel.signIn(binding.userEmail.text.toString(), binding.userPassword1.text.toString(), true).addOnCompleteListener{registration->
                binding.progressBar.visibility = View.GONE
                setButtonVisibility()

                if (registration.isSuccessful()) {
                    Toast.makeText(activity, "Registered successfully.", Toast.LENGTH_SHORT).show()
                    Log.d("TAG", "Successful registration")

                    viewModel.editPreferences()
                        .putString(resources.getString(R.string.app_preferences_group), viewModel.getPreference(resources.getString(R.string.app_preferences_group_register), ""))
                        .putString(resources.getString(R.string.app_preferences_group_register), null)
                        .apply()
                    requireView().findNavController()
                        .navigate(RegistrationFragmentDirections.actionRegistrationFragmentToLoginFragment())
                } else {
                    Toast.makeText(activity, "Registration failure: ${registration.exception!!.message.toString()}", Toast.LENGTH_LONG).show()
                    Log.d("TAG", registration.exception!!.message.toString())
                }
            }
        }
    }

    companion object {
        fun newInstance() =
            RegistrationFragment()
    }
}