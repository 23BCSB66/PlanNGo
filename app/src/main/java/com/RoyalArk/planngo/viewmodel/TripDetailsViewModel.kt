package com.RoyalArk.planngo.viewmodel

import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import com.RoyalArk.planngo.data.model.Trip
import com.RoyalArk.planngo.data.model.User
import com.google.firebase.firestore.FirebaseFirestore

class TripDetailsViewModel : ViewModel() {
    private val firestore = FirebaseFirestore.getInstance()
    private val tripsCollection = firestore.collection("Trips")

    private val _trip = mutableStateOf<Trip?>(null)
    val trip: State<Trip?> = _trip

    fun loadTrip(tripId: String) {
        tripsCollection.document(tripId).get()
            .addOnSuccessListener { document ->
                document.toObject(Trip::class.java)?.let {
                    _trip.value = it.copy(id = document.id)
                    loadTripMembers(it.members)
                }
            }
    }

    private val _members = mutableStateListOf<User>()
    val members: List<User> get() = _members

    fun loadTripMembers(memberIds: List<String>) {
        _members.clear()
        memberIds.forEach { uid ->
            firestore.collection("users").document(uid).get()
                .addOnSuccessListener { snapshot ->
                    snapshot.toObject(User::class.java)?.let { _members.add(it) }
                }
        }
    }
}