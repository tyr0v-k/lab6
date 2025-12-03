package com.example.lab1

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.lab1.databinding.FragmentHomeBinding
import com.example.lab1.network.KtorNetwork
import com.example.lab1.network.KtorNetworkApi
import kotlinx.coroutines.launch
import java.util.Calendar


class HomeFragment : Fragment() {
    private lateinit var binding: FragmentHomeBinding
    private lateinit var itemAdapter: ItemAdapter
    private var _ktorApi: KtorNetworkApi? = null
    private val ktorApi get() = _ktorApi!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        super.onCreateView(inflater, container, savedInstanceState)
        _ktorApi = KtorNetwork()
        binding = FragmentHomeBinding.inflate(layoutInflater)

        ViewCompat.setOnApplyWindowInsetsListener(binding.main) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        binding.let { binding ->
            binding.recyclerView.layoutManager = LinearLayoutManager(requireContext())

            try{
                lifecycleScope.launch{
                    val itemsLists = ktorApi.getReleases()
                    itemAdapter = ItemAdapter(itemsLists)
                    binding.recyclerView.adapter=itemAdapter
                }
            }
            catch (e: Exception){
                Toast.makeText(requireContext(), "Please, connect to the Internet", Toast.LENGTH_SHORT).show()
            }
        }
        Log.d("HOME_LOGGER","Home fragment started at " + Calendar.getInstance().time)
        binding.iconButton.setOnClickListener {
            Log.d("HOME_LOGGER","Home fragment finished at " + Calendar.getInstance().time)
            findNavController().navigate(HomeFragmentDirections.actionHomeFragmentToSettingsFragment())
        }
        return binding.root
    }
    }