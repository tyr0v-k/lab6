package com.example.lab1

import android.content.Context
import android.os.Bundle
import android.os.Environment
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatDelegate
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import com.example.lab1.data.Item
import com.example.lab1.databinding.FragmentSettingsBinding
import com.example.lab1.network.KtorNetwork
import com.example.lab1.network.KtorNetworkApi
import com.google.android.material.switchmaterial.SwitchMaterial
import kotlinx.coroutines.launch
import java.io.File
import java.io.FileOutputStream
import java.io.FileWriter
import java.io.IOException
import java.util.Calendar

class SettingsFragment : Fragment() {
    private lateinit var binding: FragmentSettingsBinding
    private lateinit var switch: SwitchMaterial
    private lateinit var userManager: UserManager

    private var _ktorApi: KtorNetworkApi? = null
    private val ktorApi get() = _ktorApi!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        super.onCreateView(inflater, container, savedInstanceState)
        binding = FragmentSettingsBinding.inflate(layoutInflater)
        _ktorApi = KtorNetwork()

        ViewCompat.setOnApplyWindowInsetsListener(binding.main) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        lifecycleScope.launch {
            userManager = UserManager(requireContext())
            Log.d("SETTINGS_LOGGER", "Logged as " + userManager.getUser())
            binding.textView3.text = userManager.getUser()
        }
        binding.button.setOnClickListener {
            lifecycleScope.launch{
                userManager.logOut()
                Log.d("SETTINGS_LOGGER","Logged out at " + Calendar.getInstance().time)
                findNavController().navigate(SettingsFragmentDirections.actionSettingsFragmentToLoginFragment())
            }

        }

        val sharedPreferences = requireActivity().getPreferences(Context.MODE_PRIVATE)
        val editor = sharedPreferences.edit()
        val isDarkModeOn = sharedPreferences.getBoolean("isDarkModeOn", false)
        switch = binding.darkModeSwitch
        if (isDarkModeOn) {
            switch.isChecked = true
        } else {
            switch.isChecked = false
        }
        switch.setOnClickListener {
            if (isDarkModeOn) {
                AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO)
                editor.putBoolean("isDarkModeOn", false)
                editor.apply()
            } else {
                AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES)
                editor.putBoolean("isDarkModeOn", true)
                editor.apply()
            }
        }
        Log.d("SETTINGS_LOGGER","Settings fragment started at " + Calendar.getInstance().time)
        binding.iconButton.setOnClickListener {
            Log.d("SETTINGS_LOGGER","Settings fragment finished at " + Calendar.getInstance().time)
            findNavController().navigate(SettingsFragmentDirections.actionSettingsFragmentToHomeFragment())
        }


        if(File(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS), sharedPreferences.getString("backUpFileName", "backup")+".txt").exists()){
            binding.buttonDelete.setEnabled(true)
            binding.buttonDelete.setVisibility(View.VISIBLE)
        }
        if(File(context?.filesDir, sharedPreferences.getString("backUpFileName", "backup")+".txt").exists()){
            binding.buttonRestore.setEnabled(true)
            binding.buttonRestore.setVisibility(View.VISIBLE)
        }

        binding.buttonBackUp.setOnClickListener {
            val fileOld = File(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS), "${sharedPreferences.getString("backUpFileName", "backup")}.txt")
            val fileRestore = File(context?.filesDir, "${sharedPreferences.getString("backUpFileName", "backup")}.txt")
            if(fileOld.exists()){
                fileOld.delete()
            }
            if(fileRestore.exists()){
                fileRestore.delete()
                binding.buttonRestore.setEnabled(false)
                binding.buttonRestore.setVisibility(View.INVISIBLE)
            }
            if (binding.fileName.text.toString() != null && binding.fileName.text.toString() != ""){
                editor.putString("backUpFileName", binding.fileName.text.toString())
                editor.apply()
            }
            lifecycleScope.launch{
                val itemsLists = ktorApi.getReleases()
                backUpToFile(itemsLists)
            }
            binding.buttonDelete.setEnabled(true)
            binding.buttonDelete.setVisibility(View.VISIBLE)
        }

        binding.buttonDelete.setOnClickListener {
            val file = File(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS), "${sharedPreferences.getString("backUpFileName", "backup")}.txt")
            Log.d("SETTINGS_LOGGER","Trying to access " + file.toURI() + " "+ Calendar.getInstance().time)
            if (file.exists()){
                try {
                    val fileNew = File(context?.filesDir, "${sharedPreferences.getString("backUpFileName", "backup")}.txt")
                    val fileWriter = FileOutputStream(fileNew, false)
                    fileWriter?.write(file.readBytes())
                    fileWriter?.close()
                    file.delete()
                    binding.buttonDelete.setEnabled(false)
                    binding.buttonDelete.setVisibility(View.INVISIBLE)
                    binding.buttonRestore.setEnabled(true)
                    binding.buttonRestore.setVisibility(View.VISIBLE)
                } catch (e: IOException) {
                    e.printStackTrace()
                }
            }
            else{
                Log.d("SETTINGS_LOGGER","File not exists" + Calendar.getInstance().time)
            }
        }

        binding.buttonRestore.setOnClickListener {
            val file = File(context?.filesDir, "${sharedPreferences.getString("backUpFileName", "backup")}.txt")
            if (file.exists()){
                try {
                    val fileNew = File(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS), sharedPreferences.getString("backUpFileName", "backup")+".txt")
                    val fileWriter = FileOutputStream(fileNew, false)
                    fileWriter?.write(file.readBytes())
                    fileWriter?.close()
                    file.delete()
                    binding.buttonRestore.setEnabled(false)
                    binding.buttonRestore.setVisibility(View.INVISIBLE)
                    binding.buttonDelete.setEnabled(true)
                    binding.buttonDelete.setVisibility(View.VISIBLE)
                } catch (e: IOException) {
                    e.printStackTrace()
                }
            }
            else{
                Log.d("SETTINGS_LOGGER","File not exists" + Calendar.getInstance().time)
            }
        }

        return binding.root
    }

    fun backUpToFile(objectsList: List<Item>?){
        val sharedPreferences = requireActivity().getPreferences(Context.MODE_PRIVATE)
            val file = File(
                Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS),
                sharedPreferences.getString("backUpFileName", "backup")+".txt"
            )
            try {
                val fileWriter = FileWriter(file, false)
                objectsList?.forEach {
                    fileWriter.write("${it.format}, ${it.title}, ${it.artist}, ${it.catno}, ${it.id}, ${it.resource_url}, ${it.status}, ${it.thumb}, ${it.year}\n")
                }
                fileWriter.close()
            } catch (e: IOException) {
                e.printStackTrace()
            }
        }
    }