package com.RoyalArk.planngo.viewmodel

import androidx.compose.runtime.mutableStateMapOf
import androidx.lifecycle.ViewModel
import com.RoyalArk.planngo.data.model.Activity
import com.google.firebase.firestore.FirebaseFirestore

class ItineraryViewModel : ViewModel() {
    private val db = FirebaseFirestore.getInstance()

    private val _itinerary = mutableStateMapOf<String, List<Activity>>() // date -> list
    val itinerary: Map<String, List<Activity>> get() = _itinerary

    fun fetchItinerary(tripId: String) {
        db.collection("trips").document(tripId).collection("itinerary")
            .get()
            .addOnSuccessListener { dayDocs ->
                dayDocs.forEach { dayDoc ->
                    val date = dayDoc.id
                    db.collection("trips").document(tripId)
                        .collection("itinerary").document(date)
                        .collection("activities").get()
                        .addOnSuccessListener { activityDocs ->
                            val activities = activityDocs.mapNotNull { it.toObject(Activity::class.java).copy(id = it.id) }
                            _itinerary[date] = activities
                        }
                }
            }
    }

    fun addActivity(tripId: String, date: String, activity: Activity) {
        val docRef = db.collection("trips").document(tripId)
            .collection("itinerary").document(date)
            .collection("activities").document()
        val newActivity = activity.copy(id = docRef.id)
        docRef.set(newActivity)
    }

}