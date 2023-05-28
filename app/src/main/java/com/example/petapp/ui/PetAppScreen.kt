package com.example.petapp.ui

import androidx.compose.runtime.Composable
import androidx.compose.ui.tooling.preview.Preview
import androidx.navigation.compose.rememberNavController
import com.example.petapp.ui.navigation.PetAppNavGraph


@Composable
fun PetAppScreen() {

    val navController = rememberNavController()
    PetAppNavGraph(navController = navController)
}

@Preview
@Composable
fun Test() {
    PetAppScreen()
}