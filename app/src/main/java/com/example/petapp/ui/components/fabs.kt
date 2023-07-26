package com.example.petapp.ui.components

import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.SmallFloatingActionButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.unit.dp

@Composable
fun AddSimpleFab(
    onClick: () -> Unit
) {
    FloatingActionButton(onClick = onClick, shape = RoundedCornerShape(50.dp), content = {
        Icon(
            imageVector = Icons.Default.Add,
            contentDescription = "Add data"
        )
    })
}