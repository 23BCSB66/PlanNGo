package com.RoyalArk.planngo.screen.trip

import android.widget.Toast
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
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.DateRange
import androidx.compose.material.icons.filled.KeyboardArrowLeft
import androidx.compose.material3.Button
import androidx.compose.material3.DatePicker
import androidx.compose.material3.DatePickerDialog
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.rememberDatePickerState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.scale
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import com.RoyalArk.planngo.R
import com.RoyalArk.planngo.Routes
import com.RoyalArk.planngo.data.model.Trip
import com.RoyalArk.planngo.viewmodel.TripViewModel
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale


@Composable
fun NewTripScreen(
    navController: NavController,
    tripViewModel: TripViewModel = viewModel()
) {
    val selectedTab = rememberSaveable { mutableStateOf(TripTab.CREATE) }
    val context = LocalContext.current
    val tripCreatedId = tripViewModel.tripCreationState.value
    val tripJoinedId = tripViewModel.tripJoinState.value

    LaunchedEffect(tripCreatedId) {
        tripCreatedId?.let {
            navController.navigate(Routes.TripDetailsScreen + "/$it") {
                popUpTo(Routes.NewTripScreen) {
                    inclusive = true
                }
            }
            tripViewModel.resetState()
        }
    }

    LaunchedEffect(tripJoinedId) {
        tripJoinedId?.let {
            navController.navigate(Routes.TripDetailsScreen + "/$it") {
                popUpTo(Routes.NewTripScreen) {
                    inclusive = true
                }
            }
            tripViewModel.resetState()
        }
    }

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

            // Tab Switcher
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .clip(RoundedCornerShape(50))
                    .background(MaterialTheme.colorScheme.secondary)
            ) {
                TripTab.entries.forEach { tab ->
                    val selected = selectedTab.value == tab
                    Box(
                        modifier = Modifier
                            .weight(1f)
                            .clip(RoundedCornerShape(50))
                            .background(if (selected) MaterialTheme.colorScheme.primary else Color.Transparent)
                            .clickable { selectedTab.value = tab }
                            .padding(vertical = 12.dp),
                        contentAlignment = Alignment.Center
                    ) {
                        Text(
                            text = tab.label,
                            color = if (selected) MaterialTheme.colorScheme.onPrimary else MaterialTheme.colorScheme.onSurfaceVariant,
                            style = MaterialTheme.typography.labelLarge,
                            fontWeight = FontWeight.SemiBold
                        )
                    }
                }
            }

            Spacer(modifier = Modifier.height(24.dp))

            when (selectedTab.value) {
                TripTab.CREATE -> CreateTripForm { trip -> tripViewModel.createTrip(trip) }
                TripTab.JOIN -> JoinTripForm { code -> tripViewModel.joinTrip(code) }
            }
        }
    }
}

enum class TripTab(val label: String) {
    CREATE("Create Trip"),
    JOIN("Join Trip")
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CreateTripForm(onCreate: (Trip) -> Unit) {
    var title by remember { mutableStateOf("") }
    var destination by remember { mutableStateOf("") }
    var startDate by remember { mutableStateOf("") }
    var endDate by remember { mutableStateOf("") }

    var datePickerController by remember { mutableStateOf(false) }
    var isStartDatePicker by remember { mutableStateOf(true) }
    val dateState = rememberDatePickerState()
    val context = LocalContext.current

    if (datePickerController) {
        DatePickerDialog(
            onDismissRequest = { datePickerController = false },
            confirmButton = {
                TextButton(
                    onClick = {
                        if (isStartDatePicker) {
                            startDate = dateState.selectedDateMillis?.let { formatDate(it) } ?: ""
                        } else {
                            endDate = dateState.selectedDateMillis?.let { formatDate(it) } ?: ""
                        }
                        datePickerController = false
                    }
                ) {
                    Text("Confirm")
                }
            },
            dismissButton = {
                TextButton(
                    onClick = { datePickerController = false }
                ) {
                    Text("Cancel")
                }
            }
        ) {
            DatePicker(state = dateState)
        }
    }

    Column(
        verticalArrangement = Arrangement.spacedBy(12.dp),
        modifier = Modifier.fillMaxWidth()
    ) {
        OutlinedTextField(
            value = title,
            onValueChange = { title = it },
            label = { Text("Trip Title") },
            modifier = Modifier.fillMaxWidth()
        )

        OutlinedTextField(
            value = destination,
            onValueChange = { destination = it },
            label = { Text("Destination") },
            modifier = Modifier.fillMaxWidth()
        )

        // Start Date
        OutlinedTextField(
            value = startDate,
            onValueChange = {},
            label = { Text("Start Date") },
            readOnly = true,
            modifier = Modifier
                .fillMaxWidth()
                .clickable {
                    isStartDatePicker = true
                    datePickerController = true
                },
            trailingIcon = {
                Icon(
                    painter = painterResource(R.drawable.datepicker),
                    contentDescription = "Date Picker",
                    modifier = Modifier
                        .size(25.dp)
                        .clickable {
                            isStartDatePicker = true
                            datePickerController = true
                        }
                )
            }
        )

        // End Date
        OutlinedTextField(
            value = endDate,
            onValueChange = {},
            label = { Text("End Date") },
            readOnly = true,
            modifier = Modifier
                .fillMaxWidth()
                .clickable {
                    isStartDatePicker = false
                    datePickerController = true
                },
            trailingIcon = {
                Icon(
                    painter = painterResource(R.drawable.datepicker),
                    contentDescription = "Date Picker",
                    modifier = Modifier
                        .size(25.dp)
                        .clickable {
                            isStartDatePicker = false
                            datePickerController = true
                        }
                )
            }
        )


        Button(
            onClick = {
                val dateFormat = SimpleDateFormat("dd-MM-yyyy", Locale.getDefault())
                try {
                    val start = dateFormat.parse(startDate)
                    val end = dateFormat.parse(endDate)

                    if (start == null || end == null) {
                        Toast.makeText(context, "Invalid date format", Toast.LENGTH_SHORT).show()
                        return@Button
                    }

                    if (start.after(end)) {
                        Toast.makeText(
                            context,
                            "Start date must be before end date",
                            Toast.LENGTH_SHORT
                        ).show()
                        return@Button
                    }

                    val trip = Trip(
                        title = title,
                        destination = destination,
                        startDate = startDate,
                        endDate = endDate
                    )
                    onCreate(trip)

                } catch (e: Exception) {
                    Toast.makeText(context, "Error parsing date", Toast.LENGTH_SHORT).show()
                }
            },
            modifier = Modifier.fillMaxWidth(),
            enabled = title.isNotBlank() &&
                    destination.isNotBlank() &&
                    startDate.isNotBlank() &&
                    endDate.isNotBlank()
        ) {
            Text("Create Trip")
        }
    }
}

// Helper function to format the date
fun formatDate(millis: Long): String {
    val dateFormat = SimpleDateFormat("dd-MM-yyyy", Locale.getDefault())
    return dateFormat.format(Date(millis))
}


@Composable
fun JoinTripForm(onJoin: (String) -> Unit) {
    var tripCode by remember { mutableStateOf("") }

    Column(
        verticalArrangement = Arrangement.spacedBy(12.dp),
        modifier = Modifier.fillMaxWidth()
    ) {
        OutlinedTextField(
            value = tripCode,
            onValueChange = { tripCode = it },
            label = { Text("Trip Code") },
            modifier = Modifier.fillMaxWidth()
        )

        Button(
            onClick = { onJoin(tripCode) },
            modifier = Modifier.fillMaxWidth(),
            enabled = tripCode.isNotBlank()
        ) {
            Text("Join Trip")
        }
    }
}