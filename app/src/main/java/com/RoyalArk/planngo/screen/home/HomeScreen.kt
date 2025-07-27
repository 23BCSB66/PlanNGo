package com.RoyalArk.planngo.screen.home

import android.widget.Toast
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.interaction.collectIsFocusedAsState
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowDropDown
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Place
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import coil.compose.AsyncImage
import com.RoyalArk.planngo.R
import com.RoyalArk.planngo.Routes
import com.RoyalArk.planngo.data.model.LocationSuggestion
import com.RoyalArk.planngo.data.model.UnsplashImage
import com.RoyalArk.planngo.ui.view.BottomNavBar
import com.RoyalArk.planngo.ui.view.LocationPermissionHandler
import com.RoyalArk.planngo.viewmodel.AuthState
import com.RoyalArk.planngo.viewmodel.AuthViewModel
import com.RoyalArk.planngo.viewmodel.BeautifulPlacesViewModel
import com.RoyalArk.planngo.viewmodel.LocationSuggestionViewModel
import com.RoyalArk.planngo.viewmodel.LocationViewModel
import com.RoyalArk.planngo.viewmodel.UserViewModel


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HomeScreen(
    navController: NavController,
    authViewModel: AuthViewModel = viewModel()
) {
    val suggestionViewModel: LocationSuggestionViewModel = viewModel()
    val suggestions = suggestionViewModel.suggestions
    var searchQuery by rememberSaveable { mutableStateOf("") }

    val userViewModel: UserViewModel = viewModel()
    val user by userViewModel.user.observeAsState()
    val authState by authViewModel.authState.observeAsState()

    val beautifulPlacesViewModel: BeautifulPlacesViewModel = viewModel()
    val beautifulPlaces by beautifulPlacesViewModel.places.observeAsState(emptyList())

    val focusManager = LocalFocusManager.current
    val textFieldFocusRequester = remember { FocusRequester() }
    val interactionSource = remember { MutableInteractionSource() }
    val isTextFieldFocused by interactionSource.collectIsFocusedAsState()

    LaunchedEffect(authState) {
        when (authState) {
            is AuthState.Authenticated -> {
                userViewModel.fetchCurrentUser()
                beautifulPlacesViewModel.fetchBeautifulPlaces("top tourist destinations")
            }

            is AuthState.Unauthenticated -> navController.navigate(Routes.WelcomeScreen)
            else -> Unit
        }
    }



    Scaffold(
        bottomBar = { BottomNavBar(navController, Routes.HomeScreen) }
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
                .padding(horizontal = 16.dp)
                .padding(top = 16.dp)
                .clickable(
                    interactionSource = remember { MutableInteractionSource() },
                    indication = null
                ) {
                    focusManager.clearFocus()
                }
        ) {
            LocationPermissionHandler()
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Image(
                    painter = painterResource(id = R.drawable.profile_picture),
                    contentDescription = "Profile",
                    modifier = Modifier
                        .size(42.dp)
                        .clip(CircleShape)
                )
                Spacer(modifier = Modifier.width(8.dp))
                Column {
                    Text(
                        text = "Welcome",
                        fontWeight = FontWeight.Bold,
                        fontSize = 19.sp
                    )
                    Text(
                        "${user?.firstname?.replaceFirstChar { it.uppercase() }} ${user?.lastname?.replaceFirstChar { it.uppercase() }}",
                        style = MaterialTheme.typography.bodySmall
                    )
                }
            }

            Spacer(modifier = Modifier.height(20.dp))

            LocationDisplay()

            Spacer(modifier = Modifier.height(12.dp))

            OutlinedTextField(
                value = searchQuery,
                onValueChange = {
                    searchQuery = it
                    suggestionViewModel.fetchSuggestions(it)
                },
                placeholder = { Text("Search destination, etc...") },
                leadingIcon = {
                    Icon(imageVector = Icons.Default.Search, contentDescription = null)
                },
                trailingIcon = {
                    if (searchQuery.isNotEmpty()) {
                        IconButton(onClick = { searchQuery = "" }) {
                            Icon(Icons.Default.Close, contentDescription = "Clear")
                        }
                    }
                },
                modifier = Modifier
                    .fillMaxWidth()
                    .height(56.dp)
                    .focusRequester(textFieldFocusRequester),
                shape = RoundedCornerShape(12.dp),
                colors = OutlinedTextFieldDefaults.colors(
                    focusedBorderColor = Color.Transparent,
                    unfocusedBorderColor = Color.Transparent,
                    focusedContainerColor = MaterialTheme.colorScheme.secondary,
                    unfocusedContainerColor = MaterialTheme.colorScheme.secondary,
                    disabledContainerColor = MaterialTheme.colorScheme.secondary
                ),
                interactionSource = interactionSource,
                keyboardOptions = KeyboardOptions.Default.copy(
                    imeAction = ImeAction.Search
                ),
                keyboardActions = KeyboardActions(
                    onSearch = {
                        if (searchQuery.isNotBlank()) {
                            suggestionViewModel.clearSuggestions()
                            beautifulPlacesViewModel.fetchBeautifulPlaces(searchQuery.trim())
                        }
                        focusManager.clearFocus()
                    }
                )
            )

            if (suggestions.isNotEmpty() && isTextFieldFocused) {
                LocationSuggestionsCard(
                    suggestions = suggestions,
                    onSuggestionClick = { suggestion ->
                        searchQuery = suggestion.display_name
                        suggestionViewModel.clearSuggestions()

                        beautifulPlacesViewModel.fetchBeautifulPlaces(suggestion.display_name)
                        focusManager.clearFocus()
                    }
                )
            }

            BeautifulPlacesCardList(places = beautifulPlaces)

        }
    }
}

@Composable
fun LocationDisplay() {
    val context = LocalContext.current
    val locationViewModel: LocationViewModel = viewModel()
    val cityName by locationViewModel.cityName.observeAsState()

    LaunchedEffect(Unit) {
        locationViewModel.fetchLocation(context)
    }

    Row(
        modifier = Modifier
            .fillMaxWidth()
            .clickable {
                locationViewModel.fetchLocation(context) {
                    Toast.makeText(context, "Location updated", Toast.LENGTH_SHORT).show()
                }
            }
            .background(
                MaterialTheme.colorScheme.secondary,
                shape = RoundedCornerShape(12.dp)
            )
            .padding(horizontal = 12.dp, vertical = 10.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Icon(imageVector = Icons.Default.Place, contentDescription = "Location")
        Spacer(modifier = Modifier.width(8.dp))
        Text(text = cityName ?: "Fetching...", fontWeight = FontWeight.Medium)
        Spacer(modifier = Modifier.weight(1f))
        Icon(imageVector = Icons.Default.ArrowDropDown, contentDescription = "Dropdown")
    }
}


@Composable
fun LocationSuggestionsCard(
    suggestions: List<LocationSuggestion>,
    onSuggestionClick: (LocationSuggestion) -> Unit
) {
    if (suggestions.isEmpty()) return

    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(top = 8.dp),
        shape = RoundedCornerShape(12.dp),
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.secondary
        )
    ) {
        Column(modifier = Modifier.padding(8.dp)) {
            suggestions.forEach { suggestion ->
                Text(
                    text = suggestion.display_name,
                    modifier = Modifier
                        .fillMaxWidth()
                        .clickable { onSuggestionClick(suggestion) }
                        .padding(8.dp)
                )
                Divider()
            }
        }
    }
}


@Composable
fun BeautifulPlacesCardList(places: List<UnsplashImage>) {
    val configuration = LocalConfiguration.current
    val screenWidthDp = configuration.screenWidthDp
    val columns = when {
        screenWidthDp < 600 -> 1
        screenWidthDp < 840 -> 2
        else -> 3
    }

    Column(
        modifier = Modifier.padding(top =  10.dp)
    ) {
        Text("Suggested Places", style = MaterialTheme.typography.titleLarge)
        Spacer(modifier = Modifier.height(8.dp))

        LazyVerticalGrid(
            columns = GridCells.Fixed(columns),
            verticalArrangement = Arrangement.spacedBy(12.dp),
            horizontalArrangement = Arrangement.spacedBy(12.dp),
            modifier = Modifier
                .fillMaxSize()
//                .padding(bottom = 64.dp)
        ) {
            items(places) { place ->
                Card(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(220.dp),
                    shape = RoundedCornerShape(16.dp),
                    elevation = CardDefaults.cardElevation(defaultElevation = 6.dp)
                ) {
                    Column {
                        AsyncImage(
                            model = place.urls.regular,
                            contentDescription = place.alt_description,
                            contentScale = ContentScale.Crop,
                            modifier = Modifier
                                .fillMaxWidth()
                                .height(180.dp)
                        )
                        Text(
                            text = place.description ?: place.alt_description ?: "Unknown",
                            modifier = Modifier.padding(8.dp),
                            maxLines = 2,
                            overflow = TextOverflow.Ellipsis
                        )
                    }
                }
            }
        }
    }
}


