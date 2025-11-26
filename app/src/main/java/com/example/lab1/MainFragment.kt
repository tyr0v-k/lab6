package com.example.lab1

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.fragment.app.Fragment
import androidx.lifecycle.asLiveData
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import com.example.lab1.databinding.FragmentLoginBinding
import com.example.lab1.databinding.FragmentMainBinding
import com.example.lab1.databinding.FragmentRegisterBinding
import com.example.lab1.ui.login.LoginFragment
import kotlinx.coroutines.launch
import java.util.Calendar

class MainFragment : Fragment() {

    private lateinit var binding: FragmentMainBinding
    private lateinit var userManager: UserManager


    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        super.onCreateView(inflater, container, savedInstanceState)
        binding = FragmentMainBinding.inflate(layoutInflater)
        ViewCompat.setOnApplyWindowInsetsListener(binding.main) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        lifecycleScope.launch{
            userManager = UserManager(requireContext())
            Log.d("MAIN_LOGGER","Logged as " + userManager.getUser())
            if(userManager.getUser() != null && userManager.getUser() != ""){
                findNavController().navigate(MainFragmentDirections.actionMainFragmentToHomeFragment())
            }
        }


        binding.getStartedWOLoginButton.setOnClickListener {
            Log.d("MAIN_LOGGER","Main fragment finished at " + Calendar.getInstance().time)
            findNavController().navigate(MainFragmentDirections.actionMainFragmentToHomeFragment())
        }

        binding.getStartedButton.setOnClickListener {
            Log.d("MAIN_LOGGER","Main fragment finished at " + Calendar.getInstance().time)
            findNavController().navigate(MainFragmentDirections.actionMainFragmentToLoginFragment())
        }

        Log.d("MAIN_LOGGER","Main fragment created at " + Calendar.getInstance().time)
        return binding.root

    }
}