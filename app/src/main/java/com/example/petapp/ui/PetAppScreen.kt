package com.example.petapp.ui

import androidx.compose.runtime.Composable
import androidx.compose.ui.tooling.preview.Preview
import androidx.navigation.compose.rememberNavController
import com.example.petapp.ui.navigation.PetAppNavGraph


@Composable
fun PetAppScreen(
    requestCameraPermission: (showCamera: () -> Unit) -> Unit
) {

    val navController = rememberNavController()
    PetAppNavGraph(navController = navController, requestCameraPermission = requestCameraPermission)
}

@Preview
@Composable
fun Test() {
    PetAppScreen(requestCameraPermission = {})
}