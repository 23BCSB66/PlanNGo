package com.RoyalArk.planngo.screen.trip

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.selection.selectable
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.Checkbox
import androidx.compose.material3.Divider
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.RadioButton
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.semantics.Role
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import com.RoyalArk.planngo.data.model.Expense
import com.RoyalArk.planngo.data.model.User
import com.RoyalArk.planngo.viewmodel.BudgetViewModel
import com.RoyalArk.planngo.viewmodel.TripDetailsViewModel
import kotlin.collections.component1
import kotlin.collections.component2
import kotlin.collections.forEach

@Composable
fun BudgetTab(
    tripId: String,
    navController: NavController,
    budgetViewModel: BudgetViewModel = viewModel(),
    tripDetailsViewModel: TripDetailsViewModel = viewModel()
) {
// TODO: List of expenses, split summary
//    Text("Budget Tracker for trip: $tripId")

    val expenses = budgetViewModel.expenses
    val members = tripDetailsViewModel.members
    var showDialog by remember { mutableStateOf(false) }

    LaunchedEffect(tripId) {
        tripDetailsViewModel.loadTrip(tripId)
        budgetViewModel.fetchExpenses(tripId)
    }

    val total = expenses.sumOf { it.amount }

    val perPersonTotal = remember(expenses) {
        val contributions = mutableMapOf<String, Double>()

        expenses.forEach { exp ->
            exp.sharedWith.forEach { uid ->
                val share = exp.amount / exp.sharedWith.size
                contributions[uid] = (contributions[uid] ?: 0.0) + share
            }
        }
        contributions
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(12.dp)
    ) {
        Text("Total: ₹${"%.2f".format(total)}", style = MaterialTheme.typography.titleMedium)

        Spacer(modifier = Modifier.height(8.dp))
        Text("Per Person:", style = MaterialTheme.typography.labelLarge)
        perPersonTotal.forEach { (uid, amount) ->
            val user = members.find { it.uid == uid }
            Text("${user?.firstname ?: "User"}: ₹${"%.2f".format(amount)}")
        }

        Spacer(modifier = Modifier.height(16.dp))

        LazyColumn {
            items(expenses) { exp ->
                val payer = members.find { it.uid == exp.paidBy }
                Card(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(vertical = 4.dp)
                ) {
                    Column(modifier = Modifier.padding(12.dp)) {
                        Text(exp.title, style = MaterialTheme.typography.bodyLarge)
                        Text("Amount: ₹${"%.2f".format(exp.amount)}")
                        Text("Paid by: ${payer?.firstname ?: exp.paidBy}")
                        Text(
                            "Shared with: ${
                                exp.sharedWith.mapNotNull { uid -> members.find { it.uid == uid }?.firstname }
                                    .joinToString()
                            }"
                        )
                    }
                }
            }
        }

        Spacer(modifier = Modifier.height(16.dp))
        Button(onClick = { showDialog = true }, modifier = Modifier.fillMaxWidth()) {
            Text("Add Expense")
        }
    }

    if (showDialog) {
        AddExpenseDialog(
            members = members,
            onDismiss = { showDialog = false },
            onAdd = { expense ->
                budgetViewModel.addExpense(tripId, expense)
                showDialog = false
            }
        )
    }

}


@Composable
fun AddExpenseDialog(
    members: List<User>,
    onDismiss: () -> Unit,
    onAdd: (Expense) -> Unit
) {
    var title by remember { mutableStateOf("") }
    var amount by remember { mutableStateOf("") }
    var paidBy by remember { mutableStateOf("") }
    val sharedWith = remember { mutableStateListOf<String>() }
    AlertDialog(
        onDismissRequest = onDismiss,
        confirmButton = {
            TextButton(onClick = {
                val expense = Expense(
                    title = title,
                    amount = amount.toDoubleOrNull() ?: 0.0,
                    paidBy = paidBy,
                    sharedWith = sharedWith.toList()
                )
                onAdd(expense)
            }) {
                Text("Add")
            }
        },
        dismissButton = {
            TextButton(onClick = onDismiss) {
                Text("Cancel")
            }
        },
        title = { Text("Add Expense") },
        text = {
            Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                OutlinedTextField(
                    value = title,
                    onValueChange = { title = it },
                    label = { Text("Title") })
                OutlinedTextField(
                    value = amount,
                    onValueChange = { amount = it },
                    label = { Text("Amount") },
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number)
                )
                Text("Who Paid:")
                members.forEach { user ->
                    Row(
                        Modifier
                            .fillMaxWidth()
                            .selectable(
                                selected = paidBy == user.uid,
                                onClick = { paidBy = user.uid },
                                role = Role.RadioButton
                            ),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        RadioButton(selected = paidBy == user.uid, onClick = null)
                        Text("${user.firstname} ${user.lastname}")
                    }
                }
                Divider()
                Text("Shared With:")
                members.forEach { user ->
                    Row(
                        Modifier.fillMaxWidth(),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Checkbox(
                            checked = sharedWith.contains(user.uid),
                            onCheckedChange = {
                                if (it) sharedWith.add(user.uid)
                                else sharedWith.remove(user.uid)
                            }
                        )
                        Text("${user.firstname} ${user.lastname}")
                    }
                }
            }
        }
    )
}