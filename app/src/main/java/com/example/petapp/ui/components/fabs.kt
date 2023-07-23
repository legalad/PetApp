package com.example.petapp.ui.components

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material3.Icon
import androidx.compose.material3.SmallFloatingActionButton
import androidx.compose.runtime.Composable

@Composable
fun AddSimpleFab(
    onClick: () -> Unit
) {
    SmallFloatingActionButton(onClick = onClick, content = {
        Icon(
            imageVector = Icons.Default.Add,
            contentDescription = "Add data"
        )
    })
}