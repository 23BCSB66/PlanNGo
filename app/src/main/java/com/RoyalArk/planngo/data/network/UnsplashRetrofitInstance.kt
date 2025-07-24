package com.RoyalArk.planngo.data.network

import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

object UnsplashRetrofitInstance {
    private const val BASE_URL = "https://api.unsplash.com/"

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

    val api: UnsplashApi = retrofit.create(UnsplashApi::class.java)
}
