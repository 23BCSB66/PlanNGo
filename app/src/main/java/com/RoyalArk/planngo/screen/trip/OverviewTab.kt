package com.RoyalArk.planngo.screen.trip

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
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Divider
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import coil.compose.AsyncImage
import com.RoyalArk.planngo.R
import com.RoyalArk.planngo.data.model.User
import com.RoyalArk.planngo.viewmodel.TripDetailsViewModel

@Composable
fun OverviewTab(tripId: String, navController: NavController, tripDetailsViewModel: TripDetailsViewModel = viewModel()) {
// TODO: Fetch trip details and show: title, destination, date, members, invite link
//    Text("Overview Content for trip: $tripId")

    val trip by tripDetailsViewModel.trip
    val members = tripDetailsViewModel.members

    LaunchedEffect(tripId) {
        tripDetailsViewModel.loadTrip(tripId)
    }


    trip?.let { trip ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            // Cover Image
            if (trip.coverImageUrl == "") {
                Icon(
                    painter = painterResource(R.drawable.user_group),
                    contentDescription = "Group icon",
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(180.dp),
                )
            } else {
                AsyncImage(
                    model = trip.coverImageUrl,
                    contentDescription = "Cover Image",
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(180.dp),
                    contentScale = ContentScale.Crop
                )
            }

            Text(text = trip.title, style = MaterialTheme.typography.headlineSmall)
            Text(text = "Destination: ${trip.destination}")
            Text(text = "Dates: ${trip.startDate} - ${trip.endDate}")

            Divider()

            Text("Invite Link:")
            Text(text = trip.inviteLink, color = MaterialTheme.colorScheme.primary)

            Divider()

            Text("Members:")
            members.forEach { user ->
                MemberItem(user)
            }
        }
    } ?: run {
        Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
            CircularProgressIndicator()
        }
    }

}


@Composable
fun MemberItem(user: User) {
    Row(
        verticalAlignment = Alignment.CenterVertically,
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 6.dp)
    ) {
        AsyncImage(
            model = user.profileImageUrl,
            contentDescription = "Profile Photo",
            modifier = Modifier
                .size(42.dp)
                .clip(CircleShape),
            contentScale = ContentScale.Crop
        )
        Spacer(modifier = Modifier.width(12.dp))
        Text(text = "${user.firstname} ${user.lastname}")
    }
}