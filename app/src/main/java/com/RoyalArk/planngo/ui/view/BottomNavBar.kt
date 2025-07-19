package com.RoyalArk.planngo.ui.view

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.material3.Divider
import androidx.compose.material.icons.filled.Add
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.FloatingActionButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.ui.Alignment
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.clipToBounds
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.RoyalArk.planngo.R
import com.RoyalArk.planngo.Routes

@Composable
fun BottomNavBar(navController: NavController, selectedItem: String) {
    Box(
        modifier = Modifier
            .fillMaxWidth()
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .navigationBarsPadding()
                .clipToBounds()
        ) {
            Divider(color = MaterialTheme.colorScheme.onBackground)
            Spacer(Modifier.height(10.dp))
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 10.dp)
                    .background(MaterialTheme.colorScheme.background),
                horizontalArrangement = Arrangement.SpaceAround,
                verticalAlignment = Alignment.CenterVertically
            ) {
                BottomMenuIcon(
                    "Home",
                    icon = painterResource(R.drawable.home),
                    contentDesc = "Home",
                    route = Routes.HomeScreen,
                    selectedItem = selectedItem,
                    navController = navController
                )
                BottomMenuIcon(
                    "Trips",
                    icon = painterResource(R.drawable.trip),
                    contentDesc = "Trips",
                    route = Routes.TripScreen,
                    selectedItem = selectedItem,
                    navController = navController
                )

                Spacer(modifier = Modifier.width(48.dp)) // Spacer for FAB

                BottomMenuIcon(
                    "Reminder",
                    icon = painterResource(R.drawable.reminder),
                    contentDesc = "Reminder",
                    route = Routes.ReminderScreen,
                    selectedItem = selectedItem,
                    navController = navController
                )
                BottomMenuIcon(
                    "Profile",
                    icon = painterResource(R.drawable.person),
                    contentDesc = "Profile",
                    route = Routes.ProfileScreen,
                    selectedItem = selectedItem,
                    navController = navController
                )
            }
        }

        // Center Floating Action Button
        Box(
            modifier = Modifier
                .align(Alignment.BottomCenter)
                .navigationBarsPadding()
                .offset(y = (-24).dp)
                .clip(CircleShape)
        ) {
            FloatingActionButton(
                onClick = {
                    navController.navigate(Routes.NewTripScreen)
                },
                modifier = Modifier
                    .size(60.dp),
                containerColor = MaterialTheme.colorScheme.primary,
                elevation = FloatingActionButtonDefaults.elevation(0.dp),
                shape = CircleShape
            ) {
                Icon(
                    imageVector = Icons.Default.Add,
                    contentDescription = "Add",
                    tint = MaterialTheme.colorScheme.onPrimary,
                    modifier = Modifier.size(30.dp)
                )
            }
        }
    }
}


@Composable
fun BottomMenuIcon(
    label: String,
    icon: Painter,
    contentDesc: String,
    route: String,
    selectedItem: String,
    navController: NavController
) {
    val isSelected = selectedItem == route

    Column(
        modifier = Modifier
            .clickable(enabled = !isSelected) {
                navController.navigate(route) {
                    launchSingleTop = true
                    restoreState = true
                    popUpTo(navController.graph.startDestinationId) {
                        saveState = true
                    }
                }
            }
            .width(70.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Icon(
            painter = icon,
            contentDescription = contentDesc,
            modifier = Modifier.size(20.dp),
            tint = if (isSelected) MaterialTheme.colorScheme.primary
            else MaterialTheme.colorScheme.onBackground
        )
        Text(
            text = label,
            fontSize = 10.sp,
            color = if (isSelected) MaterialTheme.colorScheme.primary
            else MaterialTheme.colorScheme.onBackground,
            maxLines = 1
        )
    }
}

