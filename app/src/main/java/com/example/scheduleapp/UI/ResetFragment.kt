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
import com.example.scheduleapp.databinding.FragmentResetBinding
import com.example.scheduleapp.viewmodels.OuterViewModel
import com.google.android.material.textfield.TextInputEditText
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class ResetFragment : Fragment() {
    private val viewModel: OuterViewModel by activityViewModels()
    private lateinit var binding: FragmentResetBinding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        binding = FragmentResetBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.loginButton.setOnClickListener {
            view.findNavController()
                .navigate(ResetFragmentDirections.actionResetFragmentToLoginFragment())
        }

        binding.userEmail.addTextChangedListener(getBlankStringsChecker(binding.userEmail))

        binding.resetButton.setOnClickListener {
            sendResetMessage()
        }
    }

    fun setButtonVisibility() {
        if (binding.progressBar.visibility == View.GONE) {
            binding.resetButton.isEnabled = !binding.userEmail.text.toString().isBlank()
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

    fun sendResetMessage() {
        binding.progressBar.visibility = View.VISIBLE
        binding.resetButton.isEnabled = false

        viewModel.sendResetMessage(binding.userEmail.text.toString()).addOnCompleteListener{reset->
            binding.progressBar.visibility = View.GONE
            setButtonVisibility()

            if (reset.isSuccessful()) {
                Toast.makeText(activity, "Reset message sent successfully.", Toast.LENGTH_SHORT).show()
                Log.d("TAG", "Successful send")
            } else {
                Toast.makeText(activity, "Failed to send the reset message: ${reset.exception!!.message.toString()}", Toast.LENGTH_LONG).show()
                Log.d("TAG", reset.exception!!.message.toString())
            }
        }
    }

    companion object {
        fun newInstance() =
            ResetFragment()
    }
}