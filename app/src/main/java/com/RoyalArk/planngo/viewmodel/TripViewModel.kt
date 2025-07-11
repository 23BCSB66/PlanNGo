package com.RoyalArk.planngo.viewmodel

import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import com.RoyalArk.planngo.data.model.Trip
import com.google.firebase.firestore.FirebaseFirestore
import androidx.compose.runtime.State

class TripViewModel : ViewModel() {
    private val firestore = FirebaseFirestore.getInstance()
    private val tripsCollection = firestore.collection("Trips")

    private val _tripCreationState = mutableStateOf<String?>(null)
    val tripCreationState: State<String?> = _tripCreationState

    private val _tripJoinState = mutableStateOf<String?>(null)
    val tripJoinState: State<String?> = _tripJoinState

    fun createTrip(trip: Trip) {
        val docRef = tripsCollection.document()
        val tripWithId = trip.copy(id = docRef.id)

        docRef.set(tripWithId)
            .addOnSuccessListener {
                _tripCreationState.value = tripWithId.id
            }
            .addOnFailureListener {
                _tripCreationState.value = null
            }
    }

    fun joinTrip(tripCode: String) {
        tripsCollection.document(tripCode).get()
            .addOnSuccessListener { document ->
                if (document.exists()) {
                    _tripJoinState.value = tripCode
                } else {
                    _tripJoinState.value = null
                }
            }
            .addOnFailureListener {
                _tripJoinState.value = null
            }
    }

    fun resetState() {
        _tripCreationState.value = null
        _tripJoinState.value = null
    }

}