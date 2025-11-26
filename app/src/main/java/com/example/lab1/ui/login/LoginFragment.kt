package com.example.lab1.ui.login

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.fragment.app.Fragment
import androidx.lifecycle.asLiveData
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.example.lab1.UserManager
import com.example.lab1.databinding.FragmentLoginBinding
import kotlinx.coroutines.launch
import java.util.Calendar
import kotlin.getValue

class LoginFragment : Fragment() {
    private lateinit var binding: FragmentLoginBinding

    lateinit var initUsername : String
    lateinit var initPassword : String
    lateinit var username : EditText
    lateinit var password : EditText

    private lateinit var userManager: UserManager

    private val args: LoginFragmentArgs by navArgs()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View?{
        super.onCreateView(inflater, container, savedInstanceState)

        binding = FragmentLoginBinding.inflate(layoutInflater)
        username = binding.usernameLog!!
        password = binding.passwordLog!!
        userManager = UserManager(requireContext())

        if(args.user != null){
            initUsername = args.user?.username.toString()
            initPassword = args.user?.password.toString()
            Log.d("LOGIN_LOGGER","username = " + initUsername + ", password = " + initPassword)
            binding.loginButton?.setEnabled(true)
            Toast.makeText(requireContext(), "Mail sent on " + args.email, Toast.LENGTH_LONG).show()
            Log.d("LOGIN_LOGGER","On fragment result notification was shown at " + Calendar.getInstance().time)
        }

        binding.loginButton?.setOnClickListener {
            Log.d("LOGIN_LOGGER","Click to login")
            lifecycleScope.launch {
                login()
            }
        }

        binding.goRegButton?.setOnClickListener {
            Log.d("LOGIN_LOGGER","Go to register page")
            findNavController().navigate(LoginFragmentDirections.actionLoginFragmentToRegisterFragment())
        }

        Log.d("LOGIN_LOGGER","Login fragment started at " + Calendar.getInstance().time)
        return binding.root

    }

    private suspend fun login(){
        if (username.text.isNullOrBlank() || !username.text.toString().equals(initUsername)) {
            Toast.makeText(requireContext(), "Enter valid username", Toast.LENGTH_SHORT).show()
            return
        }

        if (password.text.isNullOrBlank() || !password.text.toString().equals(initPassword)) {
            Toast.makeText(requireContext(), "Enter valid password", Toast.LENGTH_SHORT).show()
            return
        }
        userManager.storeUser(username.text.toString())
        Log.d("LOGIN_LOGGER","Successfully logged in as " + userManager.getUser())
        findNavController().navigate(LoginFragmentDirections.actionLoginFragmentToHomeFragment())
    }
}