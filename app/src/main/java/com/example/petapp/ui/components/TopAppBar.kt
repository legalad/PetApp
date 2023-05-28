package com.example.petapp.ui.components


import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Info
import androidx.compose.material.icons.filled.MoreVert
import androidx.compose.material.icons.outlined.AccountCircle
import androidx.compose.material.icons.outlined.Settings
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.petapp.R

@Composable
fun TopAppBarDropdownMenu(
    expanded: Boolean,
    onDropdownMenuIconClicked: () -> Unit,
    onDismissRequest: () -> Unit,
    onAccountIconClicked: () -> Unit,
    onSettingsIconClicked: () -> Unit,
    onHelpIconClicked: () -> Unit
) {

    IconButton(onClick = onDropdownMenuIconClicked) {
        Icon(Icons.Default.MoreVert, contentDescription = stringResource(R.string.components_top_app_bar_menu_content_description_open_menu))
    }
    DropdownMenu(
        expanded = expanded,
        onDismissRequest = onDismissRequest
    ) {
        DropdownMenuItem(
            text = { Text(stringResource(R.string.components_top_app_bar_menu_title_account)) },
            onClick = onAccountIconClicked,
            leadingIcon = {
                Icon(
                    Icons.Outlined.AccountCircle,
                    contentDescription = stringResource(R.string.components_top_app_bar_menu_content_description_nav_to_account)
                )
            })
        DropdownMenuItem(
            text = { Text(stringResource(R.string.components_top_app_bar_menu_title_settings)) },
            onClick = onSettingsIconClicked,
            leadingIcon = {
                Icon(
                    Icons.Outlined.Settings,
                    contentDescription = stringResource(R.string.components_top_app_bar_menu_content_description_nav_to_settings)
                )
            })
        Divider()
        DropdownMenuItem(
            text = { Text(stringResource(R.string.components_top_app_bar_menu_title_help)) },
            onClick = onHelpIconClicked,
            leadingIcon = {
                Icon(
                    Icons.Default.Info,
                    contentDescription = stringResource(R.string.components_top_app_bar_menu_content_description_nav_to_help)
                )
            })
    }

}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun PetAppTopAppBar(
    scrollBehavior: TopAppBarScrollBehavior,
    expanded: Boolean,
    onDropdownMenuIconClicked: () -> Unit,
    onDismissRequest: () -> Unit,
    onAccountIconClicked: () -> Unit,
    onSettingsIconClicked: () -> Unit
    ) {
    TopAppBar(
        title = {
            Logo()
        },
        actions = {
            TopAppBarDropdownMenu(
                expanded = expanded,
                onDropdownMenuIconClicked = onDropdownMenuIconClicked,
                onDismissRequest = onDismissRequest,
                onAccountIconClicked = onAccountIconClicked,
                onSettingsIconClicked = onSettingsIconClicked) {

            }
        },
        scrollBehavior = scrollBehavior
    )
}

@Composable
fun Logo() {
    Row (verticalAlignment = Alignment.CenterVertically) {
        Icon(
            painter = painterResource(id = R.drawable.icons8_cat_footprint_96),
            contentDescription = "logo",
            tint = Color.Unspecified,
            modifier = Modifier.padding(end = 5.dp)
        )
        Text(
            text = "PetApp",
            maxLines = 1,
            overflow = TextOverflow.Ellipsis
        )
    }
}

@Preview
@Composable
fun TopAppBarDropdownMenuPrev() {
    var expanded = remember { mutableStateOf(false) }
    TopAppBarDropdownMenu(
        expanded = expanded.value,
        onDropdownMenuIconClicked = { expanded.value = true },
        onDismissRequest = { expanded.value = false },
        onAccountIconClicked = { /*TODO*/ },
        onSettingsIconClicked = { /*TODO*/ }
    ) { /*TODO*/ }
}
