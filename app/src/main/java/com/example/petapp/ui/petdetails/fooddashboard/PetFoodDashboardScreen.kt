package com.example.petapp.ui.petdetails.fooddashboard

import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.example.petapp.R
import com.example.petapp.ui.components.ErrorScreen
import com.example.petapp.ui.components.LoadingScreen
import com.example.petapp.ui.components.NoContentPrev
import java.time.LocalDate
import java.time.format.DateTimeFormatter

@Composable
fun PetFoodDashboardScreen(
    viewModel: PetFoodDashboardViewModel,
    navigateToAddMealScreen: (String) -> Unit,
    navigateBack: () -> Unit
) {
    when (val uiState = viewModel.uiState.collectAsState().value) {
        PetFoodDashboardUiState.Loading -> LoadingScreen()
        is PetFoodDashboardUiState.Success -> PetFoodDashboardResultScreen(
            uiState = uiState,
            viewModel = viewModel,
            navigateToAddMealScreen = navigateToAddMealScreen,
            navigateBack = navigateBack
        )
        is PetFoodDashboardUiState.Error -> ErrorScreen(message = uiState.errorMessage)
    }
}
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun PetFoodDashboardResultScreen(
    uiState: PetFoodDashboardUiState.Success,
    viewModel: PetFoodDashboardViewModel,
    navigateToAddMealScreen: (String) -> Unit,
    navigateBack: () -> Unit
) {
    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text(text = stringResource(id = R.string.components_top_app_bar_title_pet_meals, uiState.petName)) },
                navigationIcon = { IconButton(onClick = navigateBack) {
                    Icon(imageVector = Icons.Default.ArrowBack, contentDescription = "back")
                } },
                actions = {
                    IconButton(onClick = { navigateToAddMealScreen(viewModel.getPetId()) }) {
                        Icon(imageVector = Icons.Default.Edit, contentDescription = "")
                    }
                }
            )
        },
        bottomBar = {
            Column (modifier = Modifier
                .fillMaxWidth()
                .padding(4.dp), horizontalAlignment = Alignment.CenterHorizontally){
                Button(onClick = { navigateToAddMealScreen(viewModel.getPetId()) }) {
                    Icon(
                        painter = painterResource(id = R.drawable.round_add_24),
                        contentDescription = null
                    )
                    Spacer(modifier = Modifier.padding(4.dp))
                    Text(text = "input")
                }
            }
        }
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
                .padding(8.dp), horizontalAlignment = Alignment.CenterHorizontally
        ) {
            if (uiState.petMeals.isEmpty()) NoContentPrev()
            else {
                uiState.petMeals.forEach {
                    ListItem(
                        headlineContent = { Text(it.type.name) },
                        supportingContent = {
                            Text(
                                text = it.time.atDate(LocalDate.now()).format(
                                    DateTimeFormatter.ISO_LOCAL_TIME
                                )
                            )
                        }
                    )
                }
            }
        }
    }
}