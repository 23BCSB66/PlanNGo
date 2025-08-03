package com.RoyalArk.planngo.screen.trip

import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.navigation.NavController

@Composable
fun ChatTab(tripId: String, navController: NavController) {
// TODO: Realtime group chat
    Text("Chat Room for trip: $tripId")
}