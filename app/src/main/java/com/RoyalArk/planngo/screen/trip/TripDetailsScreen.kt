package com.RoyalArk.planngo.screen.trip

import androidx.activity.compose.BackHandler
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.selection.selectable
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.KeyboardArrowDown
import androidx.compose.material.icons.filled.KeyboardArrowLeft
import androidx.compose.material.icons.filled.KeyboardArrowUp
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Checkbox
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Divider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.RadioButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.ScrollableTabRow
import androidx.compose.material3.Tab
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateMapOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.scale
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.semantics.Role
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import coil.compose.AsyncImage
import com.RoyalArk.planngo.R
import com.RoyalArk.planngo.Routes
import com.RoyalArk.planngo.data.model.Activity
import com.RoyalArk.planngo.data.model.Expense
import com.RoyalArk.planngo.data.model.User
import com.RoyalArk.planngo.viewmodel.BudgetViewModel
import com.RoyalArk.planngo.viewmodel.ItineraryViewModel
import com.RoyalArk.planngo.viewmodel.TripDetailsViewModel

@Composable
fun TripDetailsScreen(tripId: String, navController: NavController) {
    val tabTitles = listOf("Overview", "Itinerary", "Budget", "Chat", "Gallery", "Map")
    var selectedTabIndex by remember { mutableStateOf(0) }

    Scaffold { padding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
                .padding(16.dp)
        ) {
            IconButton(
                onClick = {
                    navController.popBackStack()
                },
                modifier = Modifier
                    .size(40.dp)
                    .clip(CircleShape)
                    .background(MaterialTheme.colorScheme.secondary)
                    .scale(.75f)
            ) {
                Icon(
                    Icons.Filled.KeyboardArrowLeft,
                    contentDescription = "Keyboard Arrow Left button",
                    modifier = Modifier.size(30.dp),
                    tint = MaterialTheme.colorScheme.onSecondary
                )
            }

            Spacer(modifier = Modifier.height(15.dp))

            ScrollableTabRow(
                selectedTabIndex = selectedTabIndex,
                edgePadding = 8.dp
            ) {
                tabTitles.forEachIndexed { index, title ->
                    Tab(
                        selected = selectedTabIndex == index,
                        onClick = { selectedTabIndex = index },
                        text = { Text(title) }
                    )
                }
            }

            when (selectedTabIndex) {
                0 -> OverviewTab(tripId)
                1 -> ItineraryTab(tripId)
                2 -> BudgetTab(tripId)
                3 -> ChatTab(tripId)
                4 -> GalleryTab(tripId)
                5 -> MapTab(tripId)
            }
        }
    }
}


@Composable
fun OverviewTab(tripId: String, tripDetailsViewModel: TripDetailsViewModel = viewModel()) {
// TODO: Fetch trip details and show: title, destination, date, members, invite link
//    Text("Overview Content for trip: $tripId")

    val trip by tripDetailsViewModel.trip
    val members = tripDetailsViewModel.members

    LaunchedEffect(tripId) {
        tripDetailsViewModel.loadTrip(tripId)
    }


    trip?.let { trip ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            // Cover Image
            if (trip.coverImageUrl == "") {
                Icon(
                    painter = painterResource(R.drawable.user_group),
                    contentDescription = "Group icon",
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(180.dp),
                )
            } else {
                AsyncImage(
                    model = trip.coverImageUrl,
                    contentDescription = "Cover Image",
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(180.dp),
                    contentScale = ContentScale.Crop
                )
            }

            Text(text = trip.title, style = MaterialTheme.typography.headlineSmall)
            Text(text = "Destination: ${trip.destination}")
            Text(text = "Dates: ${trip.startDate} - ${trip.endDate}")

            Divider()

            Text("Invite Link:")
            Text(text = trip.inviteLink, color = MaterialTheme.colorScheme.primary)

            Divider()

            Text("Members:")
            members.forEach { user ->
                MemberItem(user)
            }
        }
    } ?: run {
        Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
            CircularProgressIndicator()
        }
    }

}

@Composable
fun MemberItem(user: User) {
    Row(
        verticalAlignment = Alignment.CenterVertically,
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 6.dp)
    ) {
        AsyncImage(
            model = user.profileImageUrl,
            contentDescription = "Profile Photo",
            modifier = Modifier
                .size(42.dp)
                .clip(CircleShape),
            contentScale = ContentScale.Crop
        )
        Spacer(modifier = Modifier.width(12.dp))
        Text(text = "${user.firstname} ${user.lastname}")
    }
}


@Composable
fun ItineraryTab(
    tripId: String,
    itineraryViewModel: ItineraryViewModel = viewModel(),
    tripDetailsViewModel: TripDetailsViewModel = viewModel()
) {
// TODO: Daily planner with activities
//    Text("Itinerary Planner for trip: $tripId")

    val itinerary = itineraryViewModel.itinerary
    val members = tripDetailsViewModel.members
    val expandedDays = remember { mutableStateMapOf<String, Boolean>() }

    var showAddDialog by remember { mutableStateOf(false) }
    var selectedDate by remember { mutableStateOf("") }

    LaunchedEffect(tripId) {
        tripDetailsViewModel.loadTrip(tripId)
        itineraryViewModel.fetchItinerary(tripId)
    }

    Box(modifier = Modifier.fillMaxSize()) {
        LazyColumn(modifier = Modifier.fillMaxSize()) {
            itinerary.toSortedMap().forEach { (date, activities) ->
                item {
                    Card(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(8.dp),
                        elevation = CardDefaults.cardElevation(4.dp)
                    ) {
                        Column {
                            Row(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .clickable {
                                        expandedDays[date] = !(expandedDays[date] ?: false)
                                    }
                                    .padding(16.dp),
                                horizontalArrangement = Arrangement.SpaceBetween
                            ) {
                                Text(text = date, style = MaterialTheme.typography.titleMedium)
                                Icon(
                                    imageVector = if (expandedDays[date] == true) Icons.Default.KeyboardArrowUp else Icons.Default.KeyboardArrowDown,
                                    contentDescription = null
                                )
                            }

                            if (expandedDays[date] == true) {
                                activities.forEach { activity ->
                                    ActivityCard(activity, members)
                                }

                                TextButton(
                                    onClick = {
                                        selectedDate = date
                                        showAddDialog = true
                                    },
                                    modifier = Modifier.align(Alignment.End)
                                ) {
                                    Icon(Icons.Default.Add, contentDescription = null)
                                    Text("Add Activity")
                                }
                            }
                        }
                    }
                }
            }
        }

        if (showAddDialog) {
            AddActivityDialog(
                date = selectedDate,
                onDismiss = { showAddDialog = false },
                onAdd = { newActivity ->
                    itineraryViewModel.addActivity(tripId, selectedDate, newActivity)
                    showAddDialog = false
                }
            )
        }
    }

}


@Composable
fun ActivityCard(activity: Activity, members: List<User>) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp, vertical = 4.dp),
        elevation = CardDefaults.cardElevation(2.dp)
    ) {
        Column(modifier = Modifier.padding(12.dp)) {
            Text("Time: ${activity.time}", style = MaterialTheme.typography.bodyMedium)
            Text("Location: ${activity.location}")
            Text("Description: ${activity.description}")
            Spacer(modifier = Modifier.height(8.dp))
            Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                activity.assignedMembers.mapNotNull { id -> members.find { it.uid == id } }
                    .forEach { user ->
                        AsyncImage(
                            model = user.profileImageUrl,
                            contentDescription = user.firstname,
                            modifier = Modifier
                                .size(32.dp)
                                .clip(CircleShape)
                        )
                    }
            }
        }
    }
}

@Composable
fun AddActivityDialog(
    date: String,
    onDismiss: () -> Unit,
    onAdd: (Activity) -> Unit
) {
    var time by remember { mutableStateOf("") }
    var location by remember { mutableStateOf("") }
    var description by remember { mutableStateOf("") }
    AlertDialog(
        onDismissRequest = onDismiss,
        confirmButton = {
            TextButton(onClick = {
                val activity = Activity(
                    time = time,
                    location = location,
                    description = description
                )
                onAdd(activity)
            }) {
                Text("Add")
            }
        },
        dismissButton = {
            TextButton(onClick = onDismiss) {
                Text("Cancel")
            }
        },
        title = { Text("Add Activity for $date") },
        text = {
            Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                OutlinedTextField(
                    value = time,
                    onValueChange = { time = it },
                    label = { Text("Time") })
                OutlinedTextField(
                    value = location,
                    onValueChange = { location = it },
                    label = { Text("Location") })
                OutlinedTextField(
                    value = description,
                    onValueChange = { description = it },
                    label = { Text("Description") })
            }
        }
    )
}


@Composable
fun BudgetTab(
    tripId: String,
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


@Composable
fun ChatTab(tripId: String) {
// TODO: Realtime group chat
    Text("Chat Room for trip: $tripId")
}

@Composable
fun GalleryTab(tripId: String) {
// TODO: Image/video upload & view
    Text("Gallery for trip: $tripId")
}

@Composable
fun MapTab(tripId: String) {
// TODO: Show itinerary locations on map
    Text("Map View for trip: $tripId")
}

