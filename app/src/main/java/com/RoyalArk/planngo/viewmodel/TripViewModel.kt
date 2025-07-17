package com.RoyalArk.planngo.viewmodel

import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import com.RoyalArk.planngo.data.model.Trip
import com.google.firebase.firestore.FirebaseFirestore
import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateListOf
import com.google.firebase.auth.FirebaseAuth

class TripViewModel : ViewModel() {
    private val firestore = FirebaseFirestore.getInstance()
    private val auth = FirebaseAuth.getInstance()
    private val tripsCollection = firestore.collection("Trips")

    private val _tripCreationState = mutableStateOf<String?>(null)
    val tripCreationState: State<String?> = _tripCreationState

    private val _tripJoinState = mutableStateOf<String?>(null)
    val tripJoinState: State<String?> = _tripJoinState

    private val _currentUserId = mutableStateOf<String?>(null)
    val currentUserId: State<String?> get() = _currentUserId

    init {
        _currentUserId.value = auth.currentUser?.uid
    }

    fun createTrip(trip: Trip) {
        val currentUserId = FirebaseAuth.getInstance().currentUser?.uid ?: return
        val docRef = tripsCollection.document()
        val tripWithId = trip.copy(
            id = docRef.id,
            creatorId = currentUserId,
            members = listOf(currentUserId)
        )

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


    private val _createdTrips = mutableStateListOf<Trip>()
    val createdTrips: List<Trip> get() = _createdTrips

    private val _joinedTrips = mutableStateListOf<Trip>()
    val joinedTrips: List<Trip> get() = _joinedTrips

    fun fetchTrips() {
        val uid = auth.currentUser?.uid ?: return

        // Created trips
        firestore.collection("Trips")
            .whereEqualTo("creatorId", uid)
            .addSnapshotListener { snapshot, _ ->
                _createdTrips.clear()
                snapshot?.forEach {
                    it.toObject(Trip::class.java)?.let { trip -> _createdTrips.add(trip.copy(id = it.id)) }
                }
            }

        // Joined trips
        firestore.collection("Trips")
            .whereArrayContains("members", uid)
            .addSnapshotListener { snapshot, _ ->
                _joinedTrips.clear()
                snapshot?.forEach {
                    val trip = it.toObject(Trip::class.java)
                    if (trip.creatorId != uid) {
                        _joinedTrips.add(trip.copy(id = it.id))
                    }
                }
            }
    }


}