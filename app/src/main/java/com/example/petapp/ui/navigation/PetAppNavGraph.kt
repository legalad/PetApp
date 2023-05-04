package com.example.petapp.ui.navigation

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.example.petapp.ui.addpet.AddPetScreen
import com.example.petapp.ui.addpet.AddPetViewModel
import com.example.petapp.ui.dashboard.DashboardScreen
import com.example.petapp.ui.dashboard.DashboardViewModel
import com.example.petapp.ui.petdetails.PetDetailsScreen
import com.example.petapp.ui.petdetails.PetDetailsViewModel
import com.example.petapp.ui.petdetails.addpetdata.AddWeightResultScreen
import com.example.petapp.ui.petdetails.addpetdata.PetDetailsAddDimensionsViewModel
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

@Composable
fun PetAppNavGraph(
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
                modifier = Modifier
            )
        }
        composable(PetAppDestination.SETTINGS_ROUTE.name) {
            val settingsViewModel = hiltViewModel<SettingsViewModel>()
            SettingsScreen(viewModel = settingsViewModel, modifier = Modifier)
        }
        composable(PetAppDestination.DASHBOARD_ROUTE.name) {
            val dashboardVieModel = hiltViewModel<DashboardViewModel>()
            DashboardScreen(
                viewModel = dashboardVieModel,
                { navController.navigate(PetAppDestination.PET_MANAGER_ROUTE.name) },
                navigateToPetDetailsScreen = { navController.navigate(PetAppDestination.PET_DETAILS_ROUTE.name + "/$it") },
                modifier = Modifier
            )
        }
        composable(
            route = PetAppDestination.PET_DETAILS_ROUTE.name + "/{petId}",
            arguments = listOf(navArgument("petId") { type = NavType.StringType })
        ) {
            val petDetailsViewModel = hiltViewModel<PetDetailsViewModel>()
            PetDetailsScreen(
                viewModel = petDetailsViewModel,
                navigateToAddWeightScreen = { navController.navigate(PetAppDestination.PET_DETAILS_ADD_WEIGHT.name + "/$it") })
        }
        composable(
            route = PetAppDestination.PET_DETAILS_ADD_WEIGHT.name + "/{petId}",
            arguments = listOf(navArgument("petId") { type = NavType.StringType })
        ) {
            val petDetailsAddDimensionsViewModel = hiltViewModel<PetDetailsAddDimensionsViewModel>()
            AddWeightResultScreen(viewModel = petDetailsAddDimensionsViewModel,
                navigateToPetDetails = {
                    navController.navigate(PetAppDestination.PET_DETAILS_ROUTE.name + "/$it") {
                        popUpTo(navController.currentBackStackEntry?.destination?.route ?: return@navigate) {
                            inclusive = true
                        }
                    }
                }
            )
        }
    }
}