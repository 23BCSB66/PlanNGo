package com.RoyalArk.planngo.data.model

import kotlinx.serialization.Serializable

@Serializable
data class Trip(
    val id: String = "",
    val title: String = "",
    val destination: String = "",
    val startDate: String = "",
    val endDate: String = "",
    val members: List<String> = listOf()
)