package com.example.lab1.data

import kotlinx.serialization.Serializable

@Serializable
data class Item(
    val status: String? = null,
    val format: String? = null,
    val catno: String? = null,
    val thumb: String? = null,
    val resource_url: String? = null,
    val title: String? = null,
    val id: Int? = null,
    val year: Int? = null,
    val artist: String? = null
)