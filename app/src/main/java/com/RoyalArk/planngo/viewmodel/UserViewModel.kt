package com.RoyalArk.planngo.viewmodel

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.RoyalArk.planngo.data.model.User
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore

class UserViewModel : ViewModel() {
    private val db = FirebaseFirestore.getInstance()
    private val usersCollection = db.collection("users")
    private val auth = FirebaseAuth.getInstance()

    private val _user = MutableLiveData<User?>()
    val user: LiveData<User?> get() = _user

    private val currentUserId: String?
        get() = auth.currentUser?.uid

    // ─── Create User ───────────────────────────────────────────────────────────────
    fun createUserData(
        id: String,
        fname: String,
        lname: String,
        email: String,
        onResult: (Boolean, String?) -> Unit
    ) {
        if (id.isBlank()) {
            onResult(false, "User ID missing")
            return
        }

        val user = User(
            id = id,
            firstname = fname,
            lastname = lname,
            email = email,
        )

        usersCollection.document(id).set(user)
            .addOnSuccessListener {
                Log.d("UserViewModel", "✅ User created: $user")
                onResult(true, null)
            }
            .addOnFailureListener { error ->
                Log.e("UserViewModel", "❌ Error creating user: ${error.message}")
                onResult(false, error.message)
            }
    }

}