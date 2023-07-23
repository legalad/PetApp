package com.example.petapp.ui.components

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.combinedClickable
import androidx.compose.material3.ListItem
import androidx.compose.material3.ListItemDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun ClickableListItem(
    headlineContent: @Composable () -> Unit,
    supportingContent: @Composable() (() -> Unit)?,
    trailingContent: @Composable() (() -> Unit)?,
    onClick: () -> Unit,
    onLongClick: (() -> Unit),
    isClicked: Boolean
) {
    ListItem(
        headlineContent = headlineContent,
        supportingContent = supportingContent,
        trailingContent = trailingContent,
        modifier = Modifier.combinedClickable(
            onClick = onClick,
            onLongClick = onLongClick
        ),
        colors = ListItemDefaults.colors(
            containerColor = if (isClicked) MaterialTheme.colorScheme.secondaryContainer else MaterialTheme.colorScheme.surface
        )
    )
}

