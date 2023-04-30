package com.example.petapp.ui

import androidx.compose.foundation.layout.padding
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Scaffold
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.compose.ui.tooling.preview.Preview
import androidx.navigation.compose.rememberNavController
import com.example.petapp.ui.components.PetAppTopAppBar
import com.example.petapp.ui.navigation.PetAppDestination
import com.example.petapp.ui.navigation.PetAppNavGraph

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun PetAppScreen() {

    val navController = rememberNavController()
    val scrollBehavior = TopAppBarDefaults.enterAlwaysScrollBehavior()
    val expanded = remember { mutableStateOf(false)}

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
        content = {innerPadding ->
            PetAppNavGraph(navController = navController ,modifier = Modifier.padding(innerPadding))
        }
    )
}

@Preview
@Composable
fun Test() {
    PetAppScreen()
}