package com.example.petapp.ui.petdetails.fooddashboard

import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.example.petapp.R
import com.example.petapp.model.util.Formatters
import com.example.petapp.ui.components.*
import java.time.LocalDate
import java.time.format.DateTimeFormatter

@Composable
fun PetFoodDashboardScreen(
    viewModel: PetFoodDashboardViewModel,
    navigateToAddMealScreen: (String) -> Unit,
    navigateToUpdateMealScreen: (String, String) -> Unit,
    navigateBack: () -> Unit,
    requestPostNotificationPermission: (postNotification: () -> Unit) -> Unit
) {
    when (val uiState = viewModel.uiState.collectAsState().value) {
        PetFoodDashboardUiState.Loading -> LoadingScreen()
        is PetFoodDashboardUiState.Success -> PetFoodDashboardResultScreen(
            uiState = uiState,
            viewModel = viewModel,
            navigateToAddMealScreen = navigateToAddMealScreen,
            navigateToUpdateMealScreen = navigateToUpdateMealScreen,
            navigateBack = navigateBack,
            requestPostNotificationPermission = requestPostNotificationPermission
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
    navigateToUpdateMealScreen: (String, String) -> Unit,
    navigateBack: () -> Unit,
    requestPostNotificationPermission: (postNotification: () -> Unit) -> Unit
) {
    Scaffold(
        topBar = {
            if (uiState.selectedMeals.isEmpty()) {
                TopAppBar(
                    title = {
                        Text(
                            text = stringResource(
                                id = R.string.components_top_app_bar_title_pet_meals,
                                uiState.petName
                            )
                        )
                    },
                    navigationIcon = {
                        IconButton(onClick = navigateBack) {
                            Icon(imageVector = Icons.Default.ArrowBack, contentDescription = "back")
                        }
                    },
                    actions = {
                        IconButton(onClick = { /*TODO*/ }) {
                            Icon(imageVector = Icons.Default.MoreVert, contentDescription = "")
                        }
                    }
                )
            } else {
                TopAppBar(
                    title = {
                        Text(
                            text = uiState.selectedMeals.size.toString()
                        )
                    },
                    navigationIcon = {
                        IconButton(onClick = { viewModel.clearSelectedMealItems(0) }) {
                            Icon(imageVector = Icons.Default.Close, contentDescription = "Close")
                        }
                    },
                    actions = {
                        if (uiState.selectedMeals.size == 1) IconButton(onClick = {
                            navigateToUpdateMealScreen(
                                viewModel.getPetId(),
                                uiState.selectedMeals.first().id.toString()
                            ).apply {
                                viewModel.clearSelectedMealItems(300)
                                }
                        }) {
                            Icon(imageVector = Icons.Default.Edit, contentDescription = "")
                        }
                        IconButton(onClick = {
                            viewModel.deletePetMeals()
                        }) {
                            Icon(imageVector = Icons.Default.Delete, contentDescription = "")
                        }
                    }
                )
            }
        },
        floatingActionButton = {
            AddSimpleFab {
                navigateToAddMealScreen(viewModel.getPetId()).apply { viewModel.clearSelectedMealItems(300) }
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
                    ClickableListItem(
                        headlineContent = {
                            Text(
                                it.mealEntity.mealType.name + " - " + Formatters.getFormattedWeightUnitString(
                                    it.mealEntity.amount,
                                    uiState.unit,
                                    LocalContext.current
                                ) + " (${
                                    stringResource(
                                        id = it.mealEntity.foodType.nameId
                                    )
                                }) "
                            )
                        },
                        supportingContent = {
                            Text(
                                text = it.mealEntity.time.atDate(LocalDate.now()).format(
                                    DateTimeFormatter.ISO_LOCAL_TIME
                                )
                            )
                        },
                        trailingContent = {
                            IconButton(onClick = { requestPostNotificationPermission {
                                viewModel.onRemindIconClicked(
                                    it
                                )
                            }
                            }) {
                                Icon(
                                    painter = if (it.isReminderIconClicked) painterResource(id = R.drawable.round_notifications_active_24) else painterResource(
                                        id = R.drawable.round_notifications_none_24
                                    ),
                                    contentDescription = null
                                )
                            }
                        },
                        onClick = { viewModel.onMealItemClicked(it) },
                        onLongClick = { viewModel.onMealItemLongClicked(it) },
                        isClicked = it.isClicked
                    )
                }
            }
            Text(text = "Tap and hold on meal for more options", style = MaterialTheme.typography.labelSmall, modifier = Modifier.padding(top = 30.dp))
        }
    }
}