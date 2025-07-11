package com.RoyalArk.planngo.screen.profile

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.navigation.NavController
import com.RoyalArk.planngo.Routes
import com.RoyalArk.planngo.ui.view.BottomNavBar

@Composable
fun ProfileScreen(navController: NavController){
    Scaffold (
        bottomBar = { BottomNavBar(navController, Routes.ProfileScreen) }
    ){ padding->
        Column (
            modifier = Modifier.fillMaxSize()
                .padding(padding)
        ){

        }
    }
}