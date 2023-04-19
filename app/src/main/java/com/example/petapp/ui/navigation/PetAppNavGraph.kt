package com.example.petapp.ui.navigation

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.petapp.ui.components.PetItemsPrev

@Composable
fun PetAppNavGraph(
    modifier: Modifier = Modifier,
    navController: NavHostController = rememberNavController(),
    startDestination: String = PetAppDestination.PET_MANAGER_ROUTE.toString()
) {
    NavHost(
        navController = navController, startDestination = startDestination, modifier = modifier) {
        composable(PetAppDestination.PET_MANAGER_ROUTE.name) {
            PetItemsPrev()
        }
    }
}