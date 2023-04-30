package com.example.petapp.ui.navigation

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.petapp.ui.addpet.AddPetScreen
import com.example.petapp.ui.addpet.AddPetViewModel
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
    NavHost(navController = navController, route = PetAppNavGraph.ROOT, startDestination = PetAppNavGraph.MAIN){

    }
}

@Composable
fun PetAppNavGraph(
    modifier: Modifier = Modifier,
    navController: NavHostController = rememberNavController(),
    startDestination: String = PetAppDestination.PET_MANAGER_ROUTE.toString()
) {
    NavHost(
        navController = navController, startDestination = startDestination, modifier = modifier) {
        composable(PetAppDestination.PET_MANAGER_ROUTE.name) {
            AddPetScreen(viewModel = AddPetViewModel(), modifier = Modifier)
        }
        composable(PetAppDestination.SETTINGS_ROUTE.name) {
            val settingsViewModel = hiltViewModel<SettingsViewModel>()
            SettingsScreen(viewModel = settingsViewModel, modifier = Modifier)
        }
    }
}