package com.RoyalArk.planngo.screen.trip

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
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
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.FilterChip
import androidx.compose.material3.FilterChipDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import coil.compose.AsyncImage
import com.RoyalArk.planngo.R
import com.RoyalArk.planngo.Routes
import com.RoyalArk.planngo.data.model.Trip
import com.RoyalArk.planngo.ui.view.BottomNavBar
import com.RoyalArk.planngo.viewmodel.TripViewModel
import kotlinx.coroutines.delay
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

@Composable
fun TripScreen(
    navController: NavController,
    tripViewModel: TripViewModel = viewModel()
) {
    val created = tripViewModel.createdTrips
    val joined = tripViewModel.joinedTrips

    val currentUser = tripViewModel.currentUserId.value
    val allTrips = (created + joined).distinctBy { it.id }

    var selectedFilter by remember { mutableStateOf(TripFilterType.ALL) }
    var searchQuery by remember { mutableStateOf("") }
    var debouncedQuery by remember { mutableStateOf("") }

    LaunchedEffect(Unit) {
        tripViewModel.fetchTrips()
    }

    // Debounce effect for search
    LaunchedEffect(searchQuery) {
        delay(300L)
        debouncedQuery = searchQuery
    }

    // Lambda to handle trip click and navigate
    val onTripClick: (Trip) -> Unit = { trip ->
        navController.navigate(Routes.TripDetailsScreen + "/${trip.id}")
    }


    val filteredTrips = allTrips.filter { trip ->
        val matchesFilter = when (selectedFilter) {
            TripFilterType.ALL -> true
            TripFilterType.CREATED -> trip.creatorId == currentUser
            TripFilterType.JOINED -> trip.creatorId != currentUser
            TripFilterType.UPCOMING -> try {
                val sdf = SimpleDateFormat("dd-MM-yyyy", Locale.getDefault())
                val date = sdf.parse(trip.endDate)
                date?.after(Date()) ?: false
            } catch (e: Exception) {
                false
            }

            TripFilterType.PAST -> try {
                val sdf = SimpleDateFormat("dd-MM-yyyy", Locale.getDefault())
                val date = sdf.parse(trip.endDate)
                date?.before(Date()) ?: false
            } catch (e: Exception) {
                false
            }
        }

        val matchesSearch = trip.title.contains(debouncedQuery, ignoreCase = true)

        matchesFilter && matchesSearch
    }

    Scaffold(
        bottomBar = { BottomNavBar(navController, Routes.TripScreen) }
    ) { padding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
                .padding(top = 16.dp)
        ) {
            //Search Bar
            OutlinedTextField(
                value = searchQuery,
                onValueChange = { searchQuery = it },
                placeholder = { Text("Search trips...") },
                leadingIcon = { Icon(Icons.Default.Search, contentDescription = "Search") },
                trailingIcon = {
                    if (searchQuery.isNotEmpty()) {
                        IconButton(onClick = { searchQuery = "" }) {
                            Icon(Icons.Default.Close, contentDescription = "Clear")
                        }
                    }
                },
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp, vertical = 8.dp),
                shape = RoundedCornerShape(10.dp),
                singleLine = true
            )

            // Filter Row
            LazyRow(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 15.dp, vertical = 10.dp),
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                items(TripFilterType.entries.toTypedArray()) { filter ->
                    FilterChip(
                        selected = filter == selectedFilter,
                        onClick = { selectedFilter = filter },
                        label = { Text(filter.label) },
                        colors = FilterChipDefaults.filterChipColors(
                            selectedContainerColor = MaterialTheme.colorScheme.primary,
                            selectedLabelColor = MaterialTheme.colorScheme.onPrimary
                        )
                    )
                }
            }

            LazyColumn(modifier = Modifier.fillMaxSize()) {
                if (filteredTrips.isEmpty()) {
                    item {
                        Text(
                            "No trips found for selected filter.",
                            modifier = Modifier.padding(16.dp)
                        )
                    }
                } else {
                    items(filteredTrips) { trip ->
                        TripCard(trip = trip, onClick = onTripClick)
                    }
                }
            }
        }
    }
}

enum class TripFilterType(val label: String) {
    ALL("All Trips"),
    CREATED("Created"),
    JOINED("Joined"),
    UPCOMING("Upcoming"),
    PAST("Previous")
}

@Composable
fun TripCard(trip: Trip, onClick: (Trip) -> Unit) {
    Card(
        onClick = { onClick(trip) },
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 15.dp, vertical = 3.dp),
    ) {
        Row(
            modifier = Modifier
                .padding(12.dp)
                .fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically
        ) {
            if (trip.coverImageUrl.isEmpty()) {
                Icon(
                    painter = painterResource(R.drawable.user_group),
                    contentDescription = "Group icon",
                    modifier = Modifier
                        .size(55.dp)
                        .clip(CircleShape)
                        .background(MaterialTheme.colorScheme.primary.copy(alpha = 0.2f))
                        .padding(10.dp),
//                    tint = MaterialTheme.colorScheme.primary,
                    tint = Color.Unspecified
                )
            } else {
                AsyncImage(
                    model = trip.coverImageUrl,
                    contentDescription = "Cover Image",
                    modifier = Modifier
                        .size(55.dp)
                        .clip(CircleShape),
                    contentScale = ContentScale.Crop
                )
            }

            Spacer(Modifier.width(12.dp))

            Column() {
                Text(
                    trip.title.replaceFirstChar { it.uppercase() },
                    fontSize = 20.sp,
                    fontWeight = FontWeight.SemiBold
                )
                Text(
                    "${trip.startDate} to ${trip.endDate}",
                    fontSize = 12.sp
                )
            }
        }
    }
}