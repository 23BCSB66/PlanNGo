package com.RoyalArk.planngo.screen.reminder

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.NavController
import com.RoyalArk.planngo.Routes
import com.RoyalArk.planngo.ui.view.BottomNavBar

@Composable
fun ReminderScreen(navController: NavController){
    Scaffold (
        bottomBar = { BottomNavBar(navController,Routes.ReminderScreen) }
    ){ padding->
        Column (
            modifier = Modifier.fillMaxSize()
                .padding(padding)
        ){

        }
    }
}