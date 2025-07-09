package com.RoyalArk.planngo.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.firestore.FirebaseFirestore

class AuthViewModel : ViewModel() {
    private val auth: FirebaseAuth = FirebaseAuth.getInstance()
    private val firestore = FirebaseFirestore.getInstance()

    private val _authState = MutableLiveData<AuthState>()
    val authState: LiveData<AuthState> = _authState

    init {
        checkAuthState()
    }

    fun checkAuthState() {
        val currentUser = auth.currentUser

        if (currentUser == null) {
            _authState.value = AuthState.Unauthenticated
            return
        }

        val uid = currentUser.uid

        firestore.collection("users")
            .document(uid)
            .get()
            .addOnSuccessListener { document ->
                _authState.value = if (document.exists()) {
                    AuthState.Authenticated
                } else {
                    AuthState.Unauthenticated
                }
            }
            .addOnFailureListener {
                _authState.value = AuthState.Error("Error checking user in Firestore")
            }
    }

    fun signIn(email: String, password: String) {
        if (email.isBlank() || password.isBlank()) {
            _authState.value = AuthState.Error("Email or Password can't be empty")
            return
        }

        _authState.value = AuthState.Loading

        auth.signInWithEmailAndPassword(email, password)
            .addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    _authState.value = AuthState.Authenticated
                } else {
                    _authState.value =
                        AuthState.Error(task.exception?.localizedMessage ?: "Something went wrong")
                }
            }
    }

    fun createAccount(
        email: String,
        password: String,
        onAccountCreated: (FirebaseUser?) -> Unit
    ) {
        if (email.isBlank() || password.isBlank()) {
            _authState.value = AuthState.Error("Email or Password can't be empty")
            return
        }

        _authState.value = AuthState.Loading

        auth.createUserWithEmailAndPassword(email, password)
            .addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    val user = auth.currentUser
                    _authState.value = AuthState.Authenticated
                    onAccountCreated(user)
                } else {
                    _authState.value = AuthState.Error(
                        task.exception?.message ?: "Something went wrong"
                    )
                    onAccountCreated(null)
                }
            }
    }

    fun logout() {
        auth.signOut()
        _authState.value = AuthState.Unauthenticated
    }
}

sealed class AuthState {
    object Authenticated : AuthState()
    object Unauthenticated : AuthState()
    object Loading : AuthState()
    data class Error(val message: String) : AuthState()
}
