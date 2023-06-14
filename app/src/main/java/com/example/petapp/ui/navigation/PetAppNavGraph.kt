package com.example.petapp.ui.navigation

import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Scaffold
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.example.petapp.ui.addpet.AddPetScreen
import com.example.petapp.ui.addpet.AddPetViewModel
import com.example.petapp.ui.components.PetAppTopAppBar
import com.example.petapp.ui.dashboard.DashboardScreen
import com.example.petapp.ui.dashboard.DashboardViewModel
import com.example.petapp.ui.petdetails.PetDetailsScreen
import com.example.petapp.ui.petdetails.PetDetailsViewModel
import com.example.petapp.ui.petdetails.addpetdata.*
import com.example.petapp.ui.petdetails.dimensionsdashboard.PetDetailsDimensionsDashboardViewModel
import com.example.petapp.ui.petdetails.dimensionsdashboard.PetDimensionsDashboardResultScreen
import com.example.petapp.ui.petdetails.fooddashboard.PetFoodDashboardResultScreen
import com.example.petapp.ui.petdetails.fooddashboard.PetFoodDashboardViewModel
import com.example.petapp.ui.petdetails.weightdashboard.PetDetailsWeightDashboardViewModel
import com.example.petapp.ui.petdetails.weightdashboard.PetWeightDashboardScreen
import com.example.petapp.ui.settings.SettingsScreen
import com.example.petapp.ui.settings.SettingsViewModel

object PetAppNavGraph {
    const val ROOT = "root_graph"
    const val AUTH = "auth_graph"
    const val MAIN = "main_graph"
}

@Composable
fun RootNavGraph(
    modifier: Modifier = Modifier,
    navController: NavHostController = rememberNavController()
) {
    NavHost(
        navController = navController,
        route = PetAppNavGraph.ROOT,
        startDestination = PetAppNavGraph.MAIN
    ) {

    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DefaultScaffold(
    navController: NavHostController,
    content: @Composable (PaddingValues) -> Unit
) {
    val scrollBehavior = TopAppBarDefaults.enterAlwaysScrollBehavior()
    val expanded = remember { mutableStateOf(false) }

    Scaffold(
        modifier = Modifier.nestedScroll(scrollBehavior.nestedScrollConnection),
        topBar = {
            PetAppTopAppBar(
                scrollBehavior = scrollBehavior,
                expanded = expanded.value,
                onDropdownMenuIconClicked = { expanded.value = true },
                onDismissRequest = { expanded.value = false },
                onAccountIconClicked = { /*TODO*/ },
                onSettingsIconClicked = { navController.navigate(PetAppDestination.SETTINGS_ROUTE.name) })
        },
        content = content
    )
}

@Composable
fun PetAppNavGraph(
    requestCameraPermission: (showCamera: () -> Unit) -> Unit,
    modifier: Modifier = Modifier,
    navController: NavHostController = rememberNavController(),
    startDestination: String = PetAppDestination.DASHBOARD_ROUTE.toString()
) {
    NavHost(
        navController = navController, startDestination = startDestination, modifier = modifier
    ) {
        composable(PetAppDestination.PET_MANAGER_ROUTE.name) {
            val addPetViewModel = hiltViewModel<AddPetViewModel>()
            AddPetScreen(
                viewModel = addPetViewModel,
                { navController.navigate(PetAppDestination.DASHBOARD_ROUTE.name) },
                modifier = Modifier,
                navigateBack = { navController.navigateUp() }
            )
        }
        composable(PetAppDestination.SETTINGS_ROUTE.name) {
            val settingsViewModel = hiltViewModel<SettingsViewModel>()
            SettingsScreen(viewModel = settingsViewModel, modifier = Modifier, navigateBack = { navController.navigateUp() })
        }
        composable(PetAppDestination.DASHBOARD_ROUTE.name) {
            val dashboardVieModel = hiltViewModel<DashboardViewModel>()
            DefaultScaffold(navController = navController) {
                DashboardScreen(
                    viewModel = dashboardVieModel,
                    { navController.navigate(PetAppDestination.PET_MANAGER_ROUTE.name) },
                    navigateToPetDetailsScreen = { navController.navigate(PetAppDestination.PET_DETAILS_ROUTE.name + "/$it") },
                    modifier = Modifier.padding(it)
                )
            }
        }
        composable(
            route = PetAppDestination.PET_DETAILS_ROUTE.name + "/{petId}",
            arguments = listOf(navArgument("petId") { type = NavType.StringType })
        ) {
            val petDetailsViewModel = hiltViewModel<PetDetailsViewModel>()
            PetDetailsScreen(
                viewModel = petDetailsViewModel,
                navigateBack = { navController.navigateUp() },
                navigateToAddWeightScreen = { navController.navigate(PetAppDestination.PET_DETAILS_ADD_WEIGHT.name + "/$it") },
                navigateToAddDimensionsScreen = { navController.navigate(PetAppDestination.PET_DETAILS_ADD_DIMENSIONS.name + "/$it") },
                navigateToWeightDashboardScreen = { navController.navigate(PetAppDestination.PET_DETAILS_WEIGHT_DASHBOARD.name + "/$it") },
                navigateToDimensionsDashboardScreen = { navController.navigate(PetAppDestination.PET_DETAILS_DIMENSIONS_DASHBOARD.name + "/$it") },
                navigateToMealsDashboardScreen = { navController.navigate(PetAppDestination.PET_DETAILS_MEALS_DASHBOARD.name + "/$it") },
                navigateToAddMealScreen = { navController.navigate(PetAppDestination.PET_DETAILS_ADD_MEAL.name + "/$it") },
                requestCameraPermission = requestCameraPermission
            )

        }
        composable(
            route = PetAppDestination.PET_DETAILS_ADD_WEIGHT.name + "/{petId}",
            arguments = listOf(navArgument("petId") { type = NavType.StringType })
        ) {
            val petDetailsAddWeightViewModel = hiltViewModel<PetDetailsAddWeightViewModel>()
            AddWeightResultScreen(viewModel = petDetailsAddWeightViewModel,
                navigateToPetDetails = {
                    navController.navigateUp()
                },
                navigateBack = { navController.navigateUp() }
            )
        }
        composable(
            route = PetAppDestination.PET_DETAILS_ADD_DIMENSIONS.name + "/{petId}",
            arguments = listOf(navArgument("petId") { type = NavType.StringType })
        ) {
            val petDetailsAddDimensionsViewModel = hiltViewModel<PetDetailsAddDimensionsViewModel>()
            AddDimensionsResultScreen(
                viewModel = petDetailsAddDimensionsViewModel,
                navigateToPetDetails = {
                    navController.navigateUp()
                },
                navigateBack = { navController.navigateUp() }
            )
        }

        composable(
            route = PetAppDestination.PET_DETAILS_UPDATE_WEIGHT.name + "/{petId}" + "/{weightId}",
            arguments = listOf(navArgument("petId") {type = NavType.StringType}, navArgument("weightId") {type = NavType.StringType})
        ) {
            val petDetailsAddWeightViewModel = hiltViewModel<PetDetailsAddWeightViewModel>()
            AddWeightResultScreen(viewModel = petDetailsAddWeightViewModel,
                navigateToPetDetails = {
                    navController.navigateUp()
                },
                navigateBack = { navController.navigateUp() }
            )
        }

        composable(
            route = PetAppDestination.PET_DETAILS_UPDATE_HEIGHT.name + "/{petId}" + "/{heightId}",
            arguments = listOf(navArgument("petId") {type = NavType.StringType}, navArgument("heightId") {type = NavType.StringType})
        ) {
            val petDetailsAddDimensionsViewModel = hiltViewModel<PetDetailsAddDimensionsViewModel>()
            UpdateDimensionsResultScreen(
                viewModel = petDetailsAddDimensionsViewModel,
                navigateToPetDetails = {
                    navController.navigateUp()
                },
                navigateBack = { navController.navigateUp() }
            )
        }

        composable(
            route = PetAppDestination.PET_DETAILS_UPDATE_LENGTH.name + "/{petId}" + "/{lengthId}",
            arguments = listOf(navArgument("petId") {type = NavType.StringType}, navArgument("lengthId") {type = NavType.StringType})
        ) {
            val petDetailsAddDimensionsViewModel = hiltViewModel<PetDetailsAddDimensionsViewModel>()
            UpdateDimensionsResultScreen(
                viewModel = petDetailsAddDimensionsViewModel,
                navigateToPetDetails = {
                    navController.navigateUp()
                },
                navigateBack = { navController.navigateUp() }
            )
        }

        composable(
            route = PetAppDestination.PET_DETAILS_UPDATE_CIRCUIT.name + "/{petId}" + "/{circuitId}",
            arguments = listOf(navArgument("petId") {type = NavType.StringType}, navArgument("circuitId") {type = NavType.StringType})
        ) {
            val petDetailsAddDimensionsViewModel = hiltViewModel<PetDetailsAddDimensionsViewModel>()
            UpdateDimensionsResultScreen(
                viewModel = petDetailsAddDimensionsViewModel,
                navigateToPetDetails = {
                    navController.navigateUp()
                },
                navigateBack = { navController.navigateUp() }
            )
        }

        composable(
            route = PetAppDestination.PET_DETAILS_WEIGHT_DASHBOARD.name + "/{petId}",
            arguments = listOf(navArgument("petId") { type = NavType.StringType })
        ) {
            val petDetailsWeightDashboardViewModel =
                hiltViewModel<PetDetailsWeightDashboardViewModel>()
            PetWeightDashboardScreen(
                viewModel = petDetailsWeightDashboardViewModel,
                navigateToAddWeightScreen = { navController.navigate(PetAppDestination.PET_DETAILS_ADD_WEIGHT.name + "/$it") },
                navigateBack = { navController.navigateUp() },
                navigateToUpdateWeightScreen = { petId, weightId -> navController.navigate(PetAppDestination.PET_DETAILS_UPDATE_WEIGHT.name + "/$petId" + "/$weightId") })
        }

        composable(
            route = PetAppDestination.PET_DETAILS_DIMENSIONS_DASHBOARD.name + "/{petId}",
            arguments = listOf(navArgument("petId") { type = NavType.StringType })
        ) {
            val petDetailsWeightDashboardViewModel =
                hiltViewModel<PetDetailsDimensionsDashboardViewModel>()
            PetDimensionsDashboardResultScreen(
                viewModel = petDetailsWeightDashboardViewModel,
                navigateToAddDimensionsScreen = { navController.navigate(PetAppDestination.PET_DETAILS_ADD_DIMENSIONS.name + "/$it") },
                navigateBack = { navController.navigateUp() },
                navigateToUpdateHeightScreen = { petId, heightId -> navController.navigate(PetAppDestination.PET_DETAILS_UPDATE_HEIGHT.name + "/$petId" + "/$heightId") },
                navigateToUpdateLengthScreen = { petId, lengthId -> navController.navigate(PetAppDestination.PET_DETAILS_UPDATE_LENGTH.name + "/$petId" + "/$lengthId") },
                navigateToUpdateCircuitScreen = { petId, circuitId -> navController.navigate(PetAppDestination.PET_DETAILS_UPDATE_CIRCUIT.name + "/$petId" +"/$circuitId") }
                )
        }

        composable(
            route = PetAppDestination.PET_DETAILS_MEALS_DASHBOARD.name + "/{petId}",
            arguments = listOf(navArgument("petId") { type = NavType.StringType })
        ) {
            val petFoodDashboardViewModel =
                hiltViewModel<PetFoodDashboardViewModel>()
            PetFoodDashboardResultScreen(
                viewModel = petFoodDashboardViewModel,
                navigateBack = { navController.navigateUp() },
                navigateToAddMealScreen = { navController.navigate(PetAppDestination.PET_DETAILS_ADD_MEAL.name + "/$it") })

        }

        composable(
            route = PetAppDestination.PET_DETAILS_ADD_MEAL.name + "/{petId}",
            arguments = listOf(navArgument("petId") { type = NavType.StringType })
        ) {
            val addPetMealViewModel = hiltViewModel<PetDetailsAddMealViewModel>()
            PetDetailsAddMealResultScreen(viewModel = addPetMealViewModel, navigateToPetDetails = {
                navController.navigate(
                    PetAppDestination.PET_DETAILS_ROUTE.name + "/$it"
                )
            }, navigateBack = { navController.navigateUp() })
        }
    }
}