package com.example.petapp.ui.dashboard

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.example.petapp.R
import com.example.petapp.ui.components.ErrorScreen
import com.example.petapp.ui.components.LoadingScreen
import com.example.petapp.ui.components.PetItem

@Composable
fun DashboardScreen(
    viewModel: DashboardViewModel,
    navigateToAddingPetScreen: () -> Unit,
    modifier: Modifier
) {
    when (viewModel.uiState) {
        is DashboardUiState.Error -> ErrorScreen(message = "Can't load pets")
        is DashboardUiState.Loading -> LoadingScreen()
        is DashboardUiState.Success -> DashboardResultScreen(viewModel, navigateToAddingPetScreen)
    }
}

@Composable
fun DashboardResultScreen(viewModel: DashboardViewModel, navigateToAddingPetScreen: () -> Unit) {
    val uiState = viewModel.successUiState.collectAsState().value

    Scaffold(floatingActionButton = { SmallFloatingActionButton(onClick = { navigateToAddingPetScreen() }, content = { Icon(
        imageVector = Icons.Default.Add,
        contentDescription = "Add new pet"
    )}) }) { innerPadding ->
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Top,
            modifier = Modifier
                .fillMaxSize().padding(innerPadding)
        ) {
            Spacer(modifier = Modifier.padding(10.dp))
            Text(
                text = stringResource(R.string.dashboard_headline),
                style = MaterialTheme.typography.headlineMedium
            )
            LazyColumn(modifier = Modifier.fillMaxHeight()) {
                items(uiState.pets) { pet ->
                    PetItem(pet = pet, getAgeFormattedString = viewModel::getPetAgeFormattedString, getWeightFormattedString = viewModel::getPetWeightFormattedString)
                }
            }
        }
    }
}
