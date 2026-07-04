package com.example.pc02diaz23100503.data.model

import com.google.firebase.Timestamp

data class ConversionRecord(
    val id: String = "",
    val email: String = "",
    val uid: String = "",
    val amount: Double = 0.0,
    val from: String = "",
    val to: String = "",
    val rate: Double = 0.0,
    val result: Double = 0.0,
    val createdAt: Timestamp? = null
)
