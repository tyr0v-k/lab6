package com.example.lab1

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.lab1.data.Item
import com.example.lab1.databinding.ItemBinding

class ItemAdapter(private val items: List<Item>?) : RecyclerView.Adapter<ItemAdapter.HomeViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): HomeViewHolder {
        val binding = ItemBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return HomeViewHolder(binding)
    }

    override fun onBindViewHolder(holder: HomeViewHolder, position: Int) {
        items?.let { holder.bind(it[position]) }
    }

    override fun getItemCount(): Int = items!!.size

    class HomeViewHolder(private val binding: ItemBinding) : RecyclerView.ViewHolder(binding.root) {
        fun bind(item: Item) {
            binding.itemName.text = item.title
            binding.description.text = "Artist: " + item.artist + "\nFormat: " + item.format +"\nCat. Number: " + item.catno
            binding.date.text = item.year.toString()
        }
    }
}