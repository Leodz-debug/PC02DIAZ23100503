package com.example.pc02diaz23100503.data.remote

import com.example.pc02diaz23100503.data.model.ExchangeRateResponse
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Path

interface ExchangeRateApi {
    @GET("v6/{apiKey}/pair/{from}/{to}/{amount}")
    suspend fun getExchangeRate(
        @Path("apiKey") apiKey: String,
        @Path("from") from: String,
        @Path("to") to: String,
        @Path("amount") amount: Double
    ): Response<ExchangeRateResponse>
}
