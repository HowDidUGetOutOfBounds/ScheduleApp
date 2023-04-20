package com.example.scheduleapp.UI

import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import android.widget.Toast
import androidx.fragment.app.activityViewModels
import androidx.navigation.findNavController
import com.example.scheduleapp.data.AuthenticationStatus
import com.example.scheduleapp.data.Constants
import com.example.scheduleapp.data.DownloadStatus
import com.example.scheduleapp.databinding.FragmentLoginBinding
import com.example.scheduleapp.viewmodels.MainActivityViewModel
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class LoginFragment : Fragment() {
    private val viewModel: MainActivityViewModel by activityViewModels()
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

        if (viewModel.getCurrentUser() != null) {
            if (!viewModel.getPreference(Constants.APP_PREFERENCES_STAY, false)) {
                viewModel.signOut()
            } else {
                view.findNavController()
                    .navigate(LoginFragmentDirections.actionLoginFragmentToFragmentContainer())
            }
        }

        initDownloadObservers()
    }

    fun setButtonVisibility() {
        if (binding.progressBar.visibility == View.GONE) {
            binding.loginButton.isEnabled =
                !(binding.userEmail.text.toString().isBlank() || binding.userPassword.text.toString().isBlank())
                        && binding.userPassword.text.toString().count() >= Constants.APP_MIN_PASSWORD_LENGTH
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

    fun initDownloadObservers() {
        viewModel.downloadState.observe(viewLifecycleOwner) { downloadStatus ->
            when (downloadStatus) {
                is DownloadStatus.Progress -> {
                    binding.progressBar.visibility = View.VISIBLE
                }
                is DownloadStatus.Error -> {
                    binding.progressBar.visibility = View.GONE
                    Toast.makeText(
                        activity,
                        "Failed to connect to DB: ${downloadStatus.message}",
                        Toast.LENGTH_LONG
                    ).show()
                }
                is DownloadStatus.Success -> {
                    binding.progressBar.visibility = View.GONE
                    InitializeView()
                    initAuthObservers()
                }
            }
        }
    }

    fun initAuthObservers() {
        viewModel.authState.observe(viewLifecycleOwner) {authStatus->
            when (authStatus) {
                is AuthenticationStatus.Success -> {
                    setButtonVisibility()
                    binding.progressBar.visibility = View.GONE
                    Toast.makeText(activity, "Logged in successfully", Toast.LENGTH_SHORT).show()
                    Log.d("TAG", "Successful login")
                    requireView().findNavController()
                        .navigate(LoginFragmentDirections.actionLoginFragmentToFragmentContainer())
                }
                is AuthenticationStatus.Error -> {
                    setButtonVisibility()
                    binding.progressBar.visibility = View.GONE
                    Toast.makeText(activity, "Failed to log in: ${authStatus.message}", Toast.LENGTH_LONG).show()
                    Log.d("TAG", authStatus.message)
                }
                is AuthenticationStatus.Progress -> {
                    binding.loginButton.isEnabled = false
                    binding.progressBar.visibility = View.VISIBLE
                }
            }
        }
    }

    fun InitializeView() {
        binding.stayCheck.isEnabled = true
        binding.stayCheck.isChecked = viewModel.getPreference(Constants.APP_PREFERENCES_STAY, false)
        binding.stayCheck.setOnCheckedChangeListener { buttonView, isChecked ->
            viewModel.editPreferences()
                .putBoolean(Constants.APP_PREFERENCES_STAY, isChecked)
                .apply()
        }

        binding.registerButton.setOnClickListener {
            requireView().findNavController()
                .navigate(LoginFragmentDirections.actionLoginFragmentToRegistrationFragment())
        }
        binding.forgotButton.setOnClickListener {
            requireView().findNavController()
                .navigate(LoginFragmentDirections.actionLoginFragmentToResetFragment())
        }

        binding.userEmail.isEnabled = true
        binding.userEmail.addTextChangedListener(getBlankStringsChecker(binding.userEmail))
        binding.userPassword.isEnabled = true
        binding.userPassword.addTextChangedListener(getBlankStringsChecker(binding.userPassword))

        binding.loginButton.setOnClickListener {
            viewModel.signIn(binding.userEmail.text.toString(), binding.userPassword.text.toString(), false)
        }
    }

    companion object {
        fun newInstance() =
            LoginFragment()
    }
}