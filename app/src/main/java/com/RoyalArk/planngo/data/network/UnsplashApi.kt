package com.RoyalArk.planngo.data.network

import com.RoyalArk.planngo.data.model.UnsplashResponse
import retrofit2.http.GET
import retrofit2.http.Query

interface UnsplashApi {
    @GET("search/photos")
    suspend fun searchPhotos(
        @Query("query") query: String,
        @Query("client_id") clientId: String,
        @Query("per_page") perPage: Int = 20
    ): UnsplashResponse
}
