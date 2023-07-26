package com.example.petapp.ui.components


import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.slideInHorizontally
import androidx.compose.animation.slideOutHorizontally
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
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
import androidx.compose.ui.semantics.isContainer
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.zIndex
import com.example.petapp.R
import com.example.petapp.ui.dashboard.DashboardUiState
import com.example.petapp.ui.dashboard.DashboardViewModel

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
        Icon(
            Icons.Default.MoreVert,
            contentDescription = stringResource(R.string.components_top_app_bar_menu_content_description_open_menu)
        )
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
    expanded: Boolean,
    onDropdownMenuIconClicked: () -> Unit,
    onDismissRequest: () -> Unit,
    onAccountIconClicked: () -> Unit,
    onSettingsIconClicked: () -> Unit,
    onCancelSearchIconClicked: () -> Unit,
    onSearchIconClicked: () -> Unit,
    scrollBehavior: TopAppBarScrollBehavior,
    isSearchBarActive: Boolean,
    searchBar: @Composable (() -> Unit)

) {
    AnimatedVisibility(
        visible = isSearchBarActive,
        enter = slideInHorizontally(),
        exit = slideOutHorizontally()
    ) {
        searchBar()
    }
    AnimatedVisibility(visible = !isSearchBarActive,
        enter = slideInHorizontally(initialOffsetX = { it }),
        exit = slideOutHorizontally(targetOffsetX = { it })
    ) {
        TopAppBar(
            title = {
                Logo()
            },
            actions = {
                IconButton(onClick = onSearchIconClicked) {
                    Icon(Icons.Default.Search, contentDescription = "Search")
                }
                TopAppBarDropdownMenu(
                    expanded = expanded,
                    onDropdownMenuIconClicked = onDropdownMenuIconClicked,
                    onDismissRequest = onDismissRequest,
                    onAccountIconClicked = onAccountIconClicked,
                    onSettingsIconClicked = onSettingsIconClicked
                ) {

                }
            },
            scrollBehavior = scrollBehavior
        )
    }
    /*if (isSearchBarActive) {
        searchBar()
    } else {
        TopAppBar(
            title = {
                Logo()
            },
            actions = {
                IconButton(onClick = onSearchIconClicked) {
                    Icon(Icons.Default.Search, contentDescription = "Search")
                }
                TopAppBarDropdownMenu(
                    expanded = expanded,
                    onDropdownMenuIconClicked = onDropdownMenuIconClicked,
                    onDismissRequest = onDismissRequest,
                    onAccountIconClicked = onAccountIconClicked,
                    onSettingsIconClicked = onSettingsIconClicked
                ) {

                }
            },
            scrollBehavior = scrollBehavior
        )
    }*/
}

@Composable
fun Logo() {
    Row(verticalAlignment = Alignment.CenterVertically) {
        Icon(
            painter = painterResource(id = R.drawable.hamster_app),
            contentDescription = "logo",
            tint = Color.Unspecified,
            modifier = Modifier.size(48.dp)
        )
        Text(
            text = stringResource(id = R.string.app_name),
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

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SearchBar(
    uiState: DashboardUiState.Success,
    viewModel: DashboardViewModel
) {
    Box(
        Modifier
            .fillMaxWidth()
            .padding(top = 8.dp)
    ) {
        Box(
            Modifier
                .semantics {
                    isContainer = true
                }
                .zIndex(1f)
                .fillMaxWidth()) {
            DockedSearchBar(
                modifier = Modifier.align(Alignment.TopCenter),
                query = uiState.searchBarText,
                onQueryChange = viewModel::searchBarOnQueryChange,
                onSearch = viewModel::searchBarOnSearchClicked,
                active = uiState.isSearchBarActive,
                onActiveChange = viewModel::searchBarOnActiveChange,
                placeholder = { Text("Enter pet name") },
                leadingIcon = {
                    IconButton(onClick = viewModel::onCancelSearchIconClicked) {
                        Icon(Icons.Default.ArrowBack, contentDescription = null)
                    }
                },
                trailingIcon = {
                    if (uiState.searchBarText.isNotBlank()) IconButton(onClick = viewModel::searchBarOnCancelClicked) {
                        Icon(
                            painter = painterResource(id = R.drawable.round_cancel_24),
                            contentDescription = stringResource(
                                R.string.components_forms_dialog_buttons_cancel
                            )
                        )
                    }
                })
            {
                LazyColumn(
                    modifier = Modifier.fillMaxWidth(),
                    contentPadding = PaddingValues(16.dp),
                    verticalArrangement = Arrangement.spacedBy(4.dp)
                ) {
                    items(
                        uiState.filteredPets.subList(
                            0,
                            if (uiState.filteredPets.size > 3) 3 else uiState.filteredPets.size
                        )
                    ) { pet ->
                        ListItem(
                            headlineContent = { Text(pet.petDashboard.name) },
                            supportingContent = { Text(stringResource(id = pet.petDashboard.species.nameId) + " " + "") },
                            leadingContent = {
                                Icon(
                                    painter = painterResource(
                                        id = pet.petDashboard.species.avatarIconId
                                            ?: R.drawable.round_female_24
                                    ),
                                    contentDescription = null,
                                    tint = Color.Unspecified,
                                    modifier = Modifier.size(40.dp)
                                )
                            },
                            modifier = Modifier.clickable {
                                viewModel.searchBarOnQueryChange(pet.petDashboard.name)
                                viewModel.searchBarOnSearchClicked(pet.petDashboard.name)
                            }
                        )
                    }
                }
            }
        }
    }
}
