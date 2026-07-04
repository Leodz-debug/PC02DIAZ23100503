package com.example.pc02diaz23100503.data.model

import com.google.gson.annotations.SerializedName

data class ExchangeRateResponse(
    val result: String,
    @SerializedName("base_code") val baseCode: String,
    @SerializedName("target_code") val targetCode: String,
    @SerializedName("conversion_rate") val conversionRate: Double,
    @SerializedName("conversion_result") val conversionResult: Double
)
