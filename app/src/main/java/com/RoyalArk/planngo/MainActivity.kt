package com.RoyalArk.planngo

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Surface
import androidx.compose.ui.Modifier
import androidx.core.splashscreen.SplashScreen.Companion.installSplashScreen
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.RoyalArk.planngo.screen.auth.CreateAccountScreen
import com.RoyalArk.planngo.screen.auth.SignInScreen
import com.RoyalArk.planngo.screen.auth.WelcomeScreen
import com.RoyalArk.planngo.screen.home.HomeScreen
import com.RoyalArk.planngo.screen.home.PlaceDetailsScreen
import com.RoyalArk.planngo.screen.profile.EditProfileScreen
import com.RoyalArk.planngo.screen.profile.ProfileScreen
import com.RoyalArk.planngo.screen.reminder.ReminderScreen
import com.RoyalArk.planngo.screen.trip.AddMemberTab
import com.RoyalArk.planngo.screen.trip.BudgetTab
import com.RoyalArk.planngo.screen.trip.ChatTab
import com.RoyalArk.planngo.screen.trip.GalleryTab
import com.RoyalArk.planngo.screen.trip.ItineraryTab
import com.RoyalArk.planngo.screen.trip.MapTab
import com.RoyalArk.planngo.screen.trip.NewTripScreen
import com.RoyalArk.planngo.screen.trip.OverviewTab
import com.RoyalArk.planngo.screen.trip.TripDetailsScreen
import com.RoyalArk.planngo.screen.trip.TripScreen
import com.RoyalArk.planngo.ui.theme.PlanNGoTheme

const val PLACE_DETAILS_ROUTE = "place_details"
class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        installSplashScreen()
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
                                val id =
                                    backStackEntry.arguments?.getString("id") ?: return@composable
                                TripDetailsScreen(id, navController)
                            }

                            composable(Routes.EditProfileScreen) {
                                EditProfileScreen(navController)
                            }

                            composable(Routes.PlaceDetailsScreen + "/{id}") { backStackEntry ->
                                val id =
                                    backStackEntry.arguments?.getString("id") ?: return@composable
                                PlaceDetailsScreen(id, navController)
                            }

                            composable("${Routes.Overview}/{tripId}") {
                                val tripId = it.arguments?.getString("tripId") ?: return@composable
                                OverviewTab(tripId, navController)
                            }

                            composable("${Routes.AddMember}/{tripId}") {
                                val tripId = it.arguments?.getString("tripId") ?: return@composable
                                AddMemberTab(tripId, navController)
                            }

                            composable("${Routes.Itinerary}/{tripId}") {
                                val tripId = it.arguments?.getString("tripId") ?: return@composable
                                ItineraryTab(tripId, navController)
                            }

                            composable("${Routes.Budget}/{tripId}") {
                                val tripId = it.arguments?.getString("tripId") ?: return@composable
                                BudgetTab(tripId, navController)
                            }

                            composable("${Routes.Gallery}/{tripId}") {
                                val tripId = it.arguments?.getString("tripId") ?: return@composable
                                GalleryTab(tripId, navController)
                            }

                            composable("${Routes.Map}/{tripId}") {
                                val tripId = it.arguments?.getString("tripId") ?: return@composable
                                MapTab(tripId, navController)
                            }
                            composable("$PLACE_DETAILS_ROUTE/{id}") { backStackEntry ->
                                val id = backStackEntry.arguments?.getString("id") ?: return@composable
                                PlaceDetailsScreen(id = id, navController = navController)
                            }

                            composable("${Routes.Chat}/{tripId}") {
                                val tripId = it.arguments?.getString("tripId") ?: return@composable
                                ChatTab(tripId, navController)
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
    var PlaceDetailsScreen = "PlaceDetailsScreen"

    var TripScreen = "TripScreen"
    var NewTripScreen = "NewTripScreen"
    var ReminderScreen = "ReminderScreen"

    var ProfileScreen = "ProfileScreen"
    var EditProfileScreen = "EditProfileScreen"

    var TripDetailsScreen = "TripDetailsScreen"
    var Overview = "$TripDetailsScreen/overview"
    var AddMember = "$TripDetailsScreen/add_member"
    var Itinerary = "$TripDetailsScreen/itinerary"
    var Budget = "$TripDetailsScreen/budget"
    var Gallery = "$TripDetailsScreen/gallery"
    var Map = "$TripDetailsScreen/map"
    var Chat = "$TripDetailsScreen/chat"
}