package com.RoyalArk.planngo.ui.view

import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp

@Composable
fun ExitConfirmationDialog(
    onDismissRequest: () -> Unit,
    onConfirmExit: () -> Unit
) {
    AlertDialog(
        onDismissRequest = onDismissRequest,
        title = {
            Text(text = "Exit App?", style = MaterialTheme.typography.titleLarge)
        },
        text = {
            Text(
                text = "Are you sure you want to exit?",
                style = MaterialTheme.typography.bodyMedium
            )
        },
        confirmButton = {
            TextButton(onClick = onConfirmExit) {
                Text("Exit", color = Color.Red)
            }
        },
        dismissButton = {
            TextButton(onClick = onDismissRequest) {
                Text("Cancel")
            }
        },
        shape = RoundedCornerShape(16.dp),
    )
}
