package com.RoyalArk.planngo.screen.home

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.RoyalArk.planngo.Routes
import com.RoyalArk.planngo.ui.view.BottomNavBar

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HomeScreen(navController: NavController) {
    val trips = remember {
        mutableStateOf(
            listOf("Goa Trip", "Manali Tour", "Europe Backpacking")
        )
    }

    Scaffold(
        bottomBar = { BottomNavBar(navController,Routes.HomeScreen) },
        floatingActionButton = {
            FloatingActionButton(
                onClick = {}
            ) {
                Text("+")
            }
        }
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .padding(innerPadding)
                .fillMaxSize()
                .padding(16.dp)
        ) {
            // 🔹 Custom Header
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Column {
                    Text("Hi,!", fontSize = 24.sp)
                    Text("Plan your next trip easily.")
                }

                Button(onClick = {
                    navController.navigate(Routes.WelcomeScreen) {
                        popUpTo(Routes.HomeScreen) { inclusive = true }
                    }
                }) {
                    Text("Logout")
                }
            }

            Spacer(modifier = Modifier.height(24.dp))

            Text(
                text = "Your Trips",
                style = MaterialTheme.typography.titleLarge
            )

            Spacer(modifier = Modifier.height(8.dp))

            LazyColumn {
                items(trips.value) { trip ->
                    Card(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(vertical = 6.dp),
                        colors = CardDefaults.cardColors(
                            containerColor = MaterialTheme.colorScheme.surfaceVariant
                        )
                    ) {
                        Text(
                            text = trip,
                            modifier = Modifier.padding(16.dp),
                            style = MaterialTheme.typography.bodyLarge
                        )
                    }
                }
            }
        }
    }
}
