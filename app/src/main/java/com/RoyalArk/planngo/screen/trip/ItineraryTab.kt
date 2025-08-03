package com.RoyalArk.planngo.screen.trip

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
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.KeyboardArrowDown
import androidx.compose.material.icons.filled.KeyboardArrowUp
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateMapOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import coil.compose.AsyncImage
import com.RoyalArk.planngo.data.model.Activity
import com.RoyalArk.planngo.data.model.User
import com.RoyalArk.planngo.viewmodel.ItineraryViewModel
import com.RoyalArk.planngo.viewmodel.TripDetailsViewModel
import kotlin.collections.set

@Composable
fun ItineraryTab(
    tripId: String,
    navController: NavController,
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