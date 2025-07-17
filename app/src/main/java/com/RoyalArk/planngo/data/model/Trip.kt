package com.RoyalArk.planngo.data.model

import kotlinx.serialization.Serializable

@Serializable
data class Trip(
    val id: String = "",
    val title: String = "",
    val destination: String = "",
    val startDate: String = "",
    val endDate: String = "",
    val creatorId: String = "",
    val coverImageUrl: String = "",
    val members: List<String> = emptyList(),
    val inviteLink: String = ""
)

@Serializable
data class Activity(
    val id: String = "",
    val time: String = "",
    val location: String = "",
    val description: String = "",
    val assignedMembers: List<String> = emptyList()
)

@Serializable
data class ItineraryDay(
    val date: String = "", // e.g., "2025-07-12"
    val activities: List<Activity> = emptyList()
)

@Serializable
data class Expense(
    val id: String = "",
    val title: String = "",
    val amount: Double = 0.0,
    val paidBy: String = "",
    val sharedWith: List<String> = emptyList(),
    val timestamp: Long = System.currentTimeMillis()
)