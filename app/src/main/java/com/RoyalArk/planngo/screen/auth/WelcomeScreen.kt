package com.RoyalArk.planngo.screen.auth

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

@Composable
fun WelcomeScreen() {
    Text(
        "Welcome",
        fontSize = 50.sp,
        color = MaterialTheme.colorScheme.primary,
        modifier = Modifier.fillMaxSize().padding(50.dp),
    )
}