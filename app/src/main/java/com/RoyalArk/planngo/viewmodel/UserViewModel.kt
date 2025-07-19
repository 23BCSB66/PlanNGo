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
            uid = id,
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


    fun fetchCurrentUser() {
        val uid = currentUserId ?: return
        usersCollection.document(uid)
            .get()
            .addOnSuccessListener { document ->
                if (document.exists()) {
                    _user.value = document.toObject(User::class.java)
                    Log.d("UserViewModel", "✅ Fetched user: ${_user.value}")
                } else {
                    Log.w("UserViewModel", "⚠️ No user found with uid $uid")
                    _user.value = null
                }
            }
            .addOnFailureListener { error ->
                Log.e("UserViewModel", "❌ Failed to fetch user: ${error.message}")
                _user.value = null
            }
    }


    fun updateUser(
        firstname: String,
        lastname: String,
        phoneNumber: String,
        gender: String,
        onResult: (Boolean, String?) -> Unit
    ) {
        val uid = currentUserId
        if (uid == null) {
            onResult(false, "User ID missing")
            return
        }

        val updates = mapOf(
            "firstname" to firstname,
            "lastname" to lastname,
            "phoneNumber" to phoneNumber,
            "gender" to gender
        )

        usersCollection.document(uid)
            .update(updates)
            .addOnSuccessListener {
                Log.d("UserViewModel", "✅ User fields updated: $updates")
                fetchCurrentUser() // Optional: refresh LiveData
                onResult(true, null)
            }
            .addOnFailureListener { error ->
                Log.e("UserViewModel", "❌ Failed to update user: ${error.message}")
                onResult(false, error.message)
            }
    }

}