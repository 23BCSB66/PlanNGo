package com.RoyalArk.planngo.data.model

import kotlinx.serialization.Serializable

@Serializable
data class User(
    val uid: String = "",
    val firstname: String = "",
    val lastname: String = "",
    val email: String = "",
    val gender: String = "",
    val phoneNumber : String = "",
    val profileImageUrl: String? = null,
    val joinedTrips: List<String> = emptyList(),
    val pendingInvites: List<String> = emptyList(),
    val createdAt: Long = System.currentTimeMillis()
)


@Serializable
data class ReminderItem(
    val message: String = "",
    val timestamp: Long = System.currentTimeMillis(),
    val seen: Boolean = false
)
