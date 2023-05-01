package com.example.petapp.ui.settings

import androidx.compose.foundation.layout.*
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.example.petapp.R
import com.example.petapp.ui.components.ErrorScreen
import com.example.petapp.ui.components.LoadingScreen
import com.example.petapp.ui.components.forms.SettingsExposedDropdownMenu

@Composable
fun SettingsScreen(
    viewModel: SettingsViewModel,
    modifier: Modifier
) {
    when (viewModel.uiState) {
        is SettingsUiState.Success -> SettingsResultScreen(viewModel = viewModel)
        is SettingsUiState.Loading -> LoadingScreen()
        is SettingsUiState.Error -> ErrorScreen(message = "Can't load settings")
    }
}

@Composable
fun SettingsResultScreen(viewModel: SettingsViewModel) {
    val uiState = viewModel.successUiState.collectAsState().value
    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Top,
        modifier = Modifier
            .fillMaxSize()
    ){
        Spacer(modifier = Modifier.padding(30.dp))
        Text(
            text = stringResource(R.string.settings),
            style = MaterialTheme.typography.headlineMedium
        )
        Spacer(modifier = Modifier.padding(20.dp))
        SettingsExposedDropdownMenu(
            label = R.string.settings_language,
            expanded = uiState.languageMenuExpanded,
            selectedOption = uiState.language,
            onExpandedChange = viewModel::languageMenuOnExpandedChanged,
            onDropdownMenuItemClicked = viewModel::onDropdownMenuLanguageClicked,
            onDismissRequest = viewModel::languageMenuOnDismissRequest)
        Spacer(modifier = Modifier.padding(5.dp))
        SettingsExposedDropdownMenu(
            label = R.string.settings_unit,
            expanded = uiState.unitMenuExpanded,
            selectedOption = uiState.unit,
            onExpandedChange = viewModel::unitMenuOnExpandedChanged,
            onDropdownMenuItemClicked = viewModel::onDropdownMenuUnitClicked,
            onDismissRequest = viewModel::unitMenuOnDismissRequest)
    }
}
