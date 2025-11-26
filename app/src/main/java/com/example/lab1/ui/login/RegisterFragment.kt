package com.example.lab1.ui.login

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import android.widget.Toast
import androidx.core.os.bundleOf
import androidx.fragment.app.Fragment
import androidx.fragment.app.setFragmentResult
import androidx.navigation.fragment.findNavController
import com.example.lab1.R
import com.example.lab1.data.User
import com.example.lab1.databinding.FragmentRegisterBinding
import java.util.Calendar

class RegisterFragment : Fragment() {

    private lateinit var binding: FragmentRegisterBinding
    lateinit var name : EditText
    lateinit var username : EditText
    lateinit var password : EditText


    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        super.onCreateView(inflater, container, savedInstanceState)
        binding = FragmentRegisterBinding.inflate(layoutInflater)
        name = binding.nameReg
        username = binding.usernameReg
        password = binding.passwordReg
        val register = binding.registerButton

        register.setOnClickListener {
            Log.d("REGISTER_LOGGER","Click to register")
            Log.d("REGISTER_LOGGER","Return to login fragment at " + Calendar.getInstance().time)
            returnToActivity()
        }

        Log.d("REGISTER_LOGGER","Register fragment created at " + Calendar.getInstance().time)
        return binding.root

    }

    private fun returnToActivity(){
        if (name.text.isNullOrBlank()) {
            Toast.makeText(requireContext(), "Enter name", Toast.LENGTH_SHORT).show()
            return
        }

        if (username.text.isNullOrBlank()) {
            Toast.makeText(requireContext(), "Enter username", Toast.LENGTH_SHORT).show()
            return
        }

        if (password.text.isNullOrBlank()) {
            Toast.makeText(requireContext(), "Enter password", Toast.LENGTH_SHORT).show()
            return
        }
        val user = User(name.text.toString(),username.text.toString(),password.text.toString())
        Log.d("REGISTER_LOGGER","Successfully registered" + user.username + " " + user.password)
        val action = RegisterFragmentDirections.actionRegisterFragmentToLoginFragment(user, username.text.toString())
        findNavController().navigate(action)
    }

}