package com.RoyalArk.planngo.data.network

import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import kotlin.getValue
import kotlin.jvm.java

object NominatimRetrofitInstance {
    private const val BASE_URL = "https://nominatim.openstreetmap.org/"

    private val client by lazy {
        OkHttpClient.Builder().build()
    }

    private val retrofit by lazy {
        Retrofit.Builder()
            .baseUrl(BASE_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .client(client)
            .build()
    }

    val api: NominatimApi = retrofit.create(NominatimApi::class.java)
}