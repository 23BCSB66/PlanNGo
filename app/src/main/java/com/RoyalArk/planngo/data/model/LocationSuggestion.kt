package com.RoyalArk.planngo.data.model

import kotlinx.serialization.Serializable

@Serializable
data class LocationSuggestion(
    val display_name: String,
    val lat: String,
    val lon: String
)
