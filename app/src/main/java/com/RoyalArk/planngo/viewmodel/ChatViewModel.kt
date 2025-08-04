package com.RoyalArk.planngo.viewmodel

import androidx.compose.runtime.mutableStateListOf
import androidx.lifecycle.ViewModel
import com.RoyalArk.planngo.data.model.ChatMessage
import com.google.firebase.Firebase
import com.google.firebase.Timestamp
import com.google.firebase.firestore.firestore

class ChatViewModel : ViewModel() {
    private val db = Firebase.firestore

    private val _messages = mutableStateListOf<ChatMessage>()
    val messages: List<ChatMessage> = _messages

    fun listenForMessages(tripId: String) {
        db.collection("trips")
            .document(tripId)
            .collection("messages")
            .orderBy("timestamp")
            .addSnapshotListener { snapshot, _ ->
                if (snapshot != null) {
                    _messages.clear()
                    for (doc in snapshot.documents) {
                        doc.toObject(ChatMessage::class.java)?.let { _messages.add(it) }
                    }
                }
            }
    }

    fun sendMessage(tripId: String, text: String, senderId: String) {
        val message = ChatMessage(text = text, senderId = senderId, timestamp = Timestamp.now())
        db.collection("trips")
            .document(tripId)
            .collection("messages")
            .add(message)
    }
}
