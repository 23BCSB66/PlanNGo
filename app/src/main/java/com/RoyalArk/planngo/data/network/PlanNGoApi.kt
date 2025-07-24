package com.RoyalArk.planngo.data.network

import com.RoyalArk.planngo.data.model.LocationSuggestion
import retrofit2.http.GET
import retrofit2.http.Query

interface PlanNGoApi {
    @GET("search")
    suspend fun getSuggestions(
        @Query("q") query: String,
        @Query("format") format: String = "json",
        @Query("limit") limit: Int = 5
    ): List<LocationSuggestion>
}