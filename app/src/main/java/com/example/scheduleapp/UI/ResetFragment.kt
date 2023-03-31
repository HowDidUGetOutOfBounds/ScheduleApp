package com.example.scheduleapp.UI

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
import com.example.scheduleapp.databinding.FragmentResetBinding
import com.google.android.material.textfield.TextInputEditText
import com.google.firebase.BuildConfig
import com.google.firebase.auth.FirebaseAuth

class ResetFragment : Fragment() {
    lateinit var mAuth: FirebaseAuth
    private lateinit var binding: FragmentResetBinding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        mAuth = (activity as MainActivity).mAuth

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
            binding.progressBar.visibility = View.VISIBLE
            binding.resetButton.isEnabled = false

            mAuth.sendPasswordResetEmail(binding.userEmail.text.toString()).addOnCompleteListener{reset->
                binding.progressBar.visibility = View.GONE
                setButtonVisibility()

                if (reset.isSuccessful()) {
                    Toast.makeText(activity, "Reset message sent successfully.", Toast.LENGTH_SHORT).show()
                    Log.d("TAG", "Successful send")
                } else {
                    Toast.makeText(activity, "Failed to send the reset message: ${reset.exception!!.message.toString()}", Toast.LENGTH_SHORT).show()
                    Log.d("TAG", reset.exception!!.message.toString())
                }
            }
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

    fun setButtonVisibility() {
        if (binding.progressBar.visibility == View.GONE) {
            binding.resetButton.isEnabled = !binding.userEmail.text.toString().isBlank()
        }
    }

    companion object {
        fun newInstance() =
            ResetFragment()
    }
}