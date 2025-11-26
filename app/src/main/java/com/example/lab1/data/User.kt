package com.example.lab1.data

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class User(val name: String, val username: String, val password: String) : Parcelable

