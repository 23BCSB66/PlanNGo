package com.RoyalArk.planngo.data.model

import kotlinx.serialization.Serializable

@Serializable
data class User(
    val id: String = "",
    val firstname: String = "",
    val lastname: String = "",
    val email: String = "",
    val gender: String = "",
    val agerange: String = "",
    val image: String = "",
)
