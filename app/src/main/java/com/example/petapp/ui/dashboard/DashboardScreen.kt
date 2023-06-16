package com.example.petapp.ui.dashboard

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Search
import androidx.compose.material.icons.filled.Star
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.semantics.isContainer
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.unit.dp
import androidx.compose.ui.zIndex
import com.example.petapp.ui.components.ErrorScreen
import com.example.petapp.ui.components.LoadingScreen
import com.example.petapp.ui.components.NoContentPrev
import com.example.petapp.ui.components.PetItem

@Composable
fun DashboardScreen(
    viewModel: DashboardViewModel,
    navigateToAddingPetScreen: () -> Unit,
    navigateToPetDetailsScreen: (petId: String) -> Unit,
    modifier: Modifier
) {
    Column(modifier = modifier) {
        when (val uiState = viewModel.uiState.collectAsState().value) {
            is DashboardUiState.Error -> ErrorScreen(message = "Can't load pets")
            is DashboardUiState.Loading -> LoadingScreen()
            is DashboardUiState.Success -> DashboardResultScreen(
                uiState = uiState,
                viewModel = viewModel,
                navigateToAddingPetScreen = navigateToAddingPetScreen,
                navigateToPetDetailsScreen = navigateToPetDetailsScreen
            )
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DashboardResultScreen(
    uiState: DashboardUiState.Success,
    viewModel: DashboardViewModel,
    navigateToAddingPetScreen: () -> Unit,
    navigateToPetDetailsScreen: (petId: String) -> Unit
) {
    Scaffold(
        topBar = {
            val text = rememberSaveable { mutableStateOf("") }
            val active = rememberSaveable { mutableStateOf(false) }

            Box(Modifier.fillMaxWidth()) {
                Box(
                    Modifier
                        .semantics {
                            isContainer = true
                        }
                        .zIndex(1f)
                        .fillMaxWidth()) {
                    DockedSearchBar(
                        modifier = Modifier.align(Alignment.TopCenter),
                        query = text.value,
                        onQueryChange = { text.value = it },
                        onSearch = { active.value = false },
                        active = active.value,
                        onActiveChange = { active.value = it },
                        placeholder = { Text("Hinted search text") },
                        leadingIcon = { Icon(Icons.Default.Search, contentDescription = null) }
                    ) {
                        LazyColumn(
                            modifier = Modifier.fillMaxWidth(),
                            contentPadding = PaddingValues(16.dp),
                            verticalArrangement = Arrangement.spacedBy(4.dp)
                        ) {
                            items(3) { idx ->
                                val resultText = "Suggestion $idx"
                                ListItem(
                                    headlineContent = { Text(resultText) },
                                    supportingContent = { Text("Additional info") },
                                    leadingContent = {
                                        Icon(
                                            Icons.Filled.Star,
                                            contentDescription = null
                                        )
                                    },
                                    modifier = Modifier.clickable {
                                        text.value = resultText
                                        active.value = false
                                    }
                                )
                            }
                        }
                    }
                }
            }
        },
        floatingActionButton = {
            SmallFloatingActionButton(onClick = { navigateToAddingPetScreen() }, content = {
                Icon(
                    imageVector = Icons.Default.Add,
                    contentDescription = "Add new pet"
                )
            })
        }) { innerPadding ->

        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Top,
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
        ) {
            Spacer(modifier = Modifier.padding(10.dp))
            if (uiState.pets.isEmpty()) {
                NoContentPrev()
            } else {
                LazyColumn(modifier = Modifier.fillMaxHeight()) {
                    items(uiState.pets) { pet ->
                        PetItem(
                            pet = pet,
                            getAgeFormattedString = viewModel::getPetAgeFormattedString,
                            getWeightFormattedString = viewModel::getPetWeightFormattedString,
                            waterIconOnClicked = viewModel::waterIconOnClicked,
                            foodIconOnClicked = viewModel::foodIconOnClicked,
                            activityIconOnClicked = viewModel::activityIconOnClicked,
                            navigateToPetDetailsScreen = navigateToPetDetailsScreen,
                            onWaterChangedIconClicked = viewModel::onWaterChangedIconClicked
                        )
                    }
                }
            }
        }
    }
}


