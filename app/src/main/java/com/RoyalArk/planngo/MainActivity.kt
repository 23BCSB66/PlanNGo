package com.RoyalArk.planngo

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Surface
import androidx.compose.ui.Modifier
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.RoyalArk.planngo.screen.auth.CreateAccountScreen
import com.RoyalArk.planngo.screen.auth.SignInScreen
import com.RoyalArk.planngo.screen.auth.WelcomeScreen
import com.RoyalArk.planngo.screen.home.HomeScreen
import com.RoyalArk.planngo.screen.profile.ProfileScreen
import com.RoyalArk.planngo.screen.reminder.ReminderScreen
import com.RoyalArk.planngo.screen.trip.NewTripScreen
import com.RoyalArk.planngo.screen.trip.TripDetailsScreen
import com.RoyalArk.planngo.screen.trip.TripScreen
import com.RoyalArk.planngo.ui.theme.PlanNGoTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            PlanNGoTheme {
                Surface(modifier = Modifier.fillMaxSize()) {
                    val navController = rememberNavController()
                    NavHost(
                        navController = navController,
                        startDestination = Routes.WelcomeScreen,
                        builder = {
                            composable(Routes.WelcomeScreen) {
                                WelcomeScreen(navController)
                            }
                            composable(Routes.SignInScreen) {
                                SignInScreen(navController)
                            }
                            composable(Routes.CreateAccountScreen) {
                                CreateAccountScreen(navController)
                            }

                            composable(Routes.HomeScreen) {
                                HomeScreen(navController)
                            }
                            composable(Routes.TripScreen) {
                                TripScreen(navController)
                            }
                            composable(Routes.ProfileScreen) {
                                ProfileScreen(navController)
                            }
                            composable(Routes.NewTripScreen) {
                                NewTripScreen(navController)
                            }
                            composable(Routes.ReminderScreen) {
                                ReminderScreen(navController)
                            }
                            composable(Routes.TripDetailsScreen + "/{id}") { backStackEntry ->
                                val id = backStackEntry.arguments?.getString("id") ?: return@composable
                                TripDetailsScreen(id,navController)
                            }
                        }
                    )
                }
            }
        }
    }
}


object Routes {
    var WelcomeScreen = "WelcomeScreen"
    var SignInScreen = "SignInScreen"
    var CreateAccountScreen = "CreateAccountScreen"
    var HomeScreen = "HomeScreen"
    var ProfileScreen = "ProfileScreen"
    var TripScreen = "TripScreen"
    var ReminderScreen = "ReminderScreen"
    var NewTripScreen = "NewTripScreen"
    var TripDetailsScreen = "TripDetailsScreen"
}