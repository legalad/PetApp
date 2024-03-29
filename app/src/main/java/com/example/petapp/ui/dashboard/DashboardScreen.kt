package com.example.petapp.ui.dashboard

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.example.petapp.ui.components.AddSimpleFab
import com.example.petapp.ui.components.ErrorScreen
import com.example.petapp.ui.components.LoadingScreen
import com.example.petapp.ui.components.NoContentPrev
import com.example.petapp.ui.components.PetItem
import com.example.petapp.ui.navigation.DefaultScaffold

@Composable
fun DashboardScreen(
    viewModel: DashboardViewModel,
    navigateToAddingPetScreen: () -> Unit,
    navigateToPetDetailsScreen: (petId: String) -> Unit,
    navigateToAddMealScreen: (String) -> Unit,
    navigateToSettings: () -> Unit,
    modifier: Modifier = Modifier
) {
    Column(modifier = modifier) {
        when (val uiState = viewModel.uiState.collectAsState().value) {
            is DashboardUiState.Error -> ErrorScreen(message = "Can't load pets")
            is DashboardUiState.Loading -> LoadingScreen()
            is DashboardUiState.Success -> DashboardResultScreen(
                uiState = uiState,
                viewModel = viewModel,
                navigateToAddingPetScreen = navigateToAddingPetScreen,
                navigateToPetDetailsScreen = navigateToPetDetailsScreen,
                navigateToAddMealScreen = navigateToAddMealScreen,
                navigateToSettings = navigateToSettings
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
    navigateToPetDetailsScreen: (petId: String) -> Unit,
    navigateToAddMealScreen: (String) -> Unit,
    navigateToSettings: () -> Unit
) {
    DefaultScaffold(
        navigateToSettings = navigateToSettings,
        onSearchIconClicked = viewModel::onSearchIconClicked,
        onCancelSearchIconClicked = viewModel::onCancelSearchIconClicked,
        isSearchBarActive = uiState.isSearchBarIconClicked,
        searchBar = {
            com.example.petapp.ui.components.SearchBar(uiState = uiState, viewModel = viewModel)
        },
        floatingActionButton = {
            AddSimpleFab {
                navigateToAddingPetScreen()
            }

        }) { innerPadding ->

        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Top,
            modifier = Modifier
                .padding(innerPadding)
                .fillMaxSize()
        ) {
            Spacer(modifier = Modifier.padding(10.dp))
            if (uiState.pets.isEmpty()) {
                NoContentPrev()
            } else {
                LazyColumn(modifier = Modifier.fillMaxHeight()) {
                    items(uiState.filteredPets) { pet ->
                        PetItem(
                            pet = pet,
                            getAgeFormattedString = viewModel::getPetAgeFormattedString,
                            getWeightFormattedString = viewModel::getPetWeightFormattedString,
                            waterIconOnClicked = viewModel::waterIconOnClicked,
                            foodIconOnClicked = viewModel::foodIconOnClicked,
                            activityIconOnClicked = viewModel::activityIconOnClicked,
                            navigateToPetDetailsScreen = navigateToPetDetailsScreen,
                            onWaterChangedIconClicked = viewModel::onWaterChangedIconClicked,
                            navigateToAddMealScreen = navigateToAddMealScreen
                        )
                    }
                }
            }
        }
    }
}


