package com.RoyalArk.planngo.viewmodel

import androidx.compose.runtime.mutableStateListOf
import androidx.lifecycle.ViewModel
import com.RoyalArk.planngo.data.model.Expense
import com.google.firebase.firestore.FirebaseFirestore

class BudgetViewModel : ViewModel() {
    private val db = FirebaseFirestore.getInstance()

    private val _expenses = mutableStateListOf<Expense>()
    val expenses: List<Expense> get() = _expenses

    fun fetchExpenses(tripId: String) {
        db.collection("trips").document(tripId).collection("expenses")
            .orderBy("timestamp")
            .addSnapshotListener { snapshot, _ ->
                if (snapshot != null) {
                    _expenses.clear()
                    _expenses.addAll(snapshot.documents.mapNotNull { it.toObject(Expense::class.java)?.copy(id = it.id) })
                }
            }
    }

    fun addExpense(tripId: String, expense: Expense) {
        val ref = db.collection("trips").document(tripId).collection("expenses").document()
        val newExpense = expense.copy(id = ref.id)
        ref.set(newExpense)
    }

}