package com.RoyalArk.planngo.data.model

import kotlinx.serialization.Serializable

@Serializable
data class PlaceRecommendation(
    val name: String,
    val description: String,
    val imageUrl: String
)

