package com.RoyalArk.planngo.data.model

import com.google.firebase.Timestamp

data class ChatMessage(
    val text: String = "",
    val senderId: String = "",
    val timestamp: Timestamp = Timestamp.now()
)