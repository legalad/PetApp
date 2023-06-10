package com.example.petapp.ui.settings

import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.example.petapp.R
import com.example.petapp.ui.components.BasicScaffoldWithNavButton
import com.example.petapp.ui.components.ErrorScreen
import com.example.petapp.ui.components.LoadingScreen
import com.example.petapp.ui.components.forms.SettingsExposedDropdownMenu

@Composable
fun SettingsScreen(
    viewModel: SettingsViewModel,
    navigateBack: () -> Unit,
    modifier: Modifier
) {
    when (viewModel.uiState) {
        is SettingsUiState.Success -> SettingsResultScreen(
            viewModel = viewModel,
            navigateBack = navigateBack
        )
        is SettingsUiState.Loading -> LoadingScreen()
        is SettingsUiState.Error -> ErrorScreen(message = "Can't load settings")
    }
}

@Composable
fun SettingsResultScreen(viewModel: SettingsViewModel, navigateBack: () -> Unit) {
    val uiState = viewModel.successUiState.collectAsState().value
    BasicScaffoldWithNavButton(
        navigateBack = navigateBack,
        onColumnClicked = { /*TODO*/ },
        topAppBarTitleId = R.string.components_top_app_bar_menu_title_settings
    ) {

        SettingsExposedDropdownMenu(
            label = R.string.components_forms_text_field_label_language,
            expanded = uiState.languageMenuExpanded,
            selectedOption = uiState.language,
            onExpandedChange = viewModel::languageMenuOnExpandedChanged,
            onDropdownMenuItemClicked = viewModel::onDropdownMenuLanguageClicked,
            onDismissRequest = viewModel::languageMenuOnDismissRequest
        )
        Spacer(modifier = Modifier.padding(5.dp))
        SettingsExposedDropdownMenu(
            label = R.string.components_forms_text_field_label_unit,
            expanded = uiState.unitMenuExpanded,
            selectedOption = uiState.unit,
            onExpandedChange = viewModel::unitMenuOnExpandedChanged,
            onDropdownMenuItemClicked = viewModel::onDropdownMenuUnitClicked,
            onDismissRequest = viewModel::unitMenuOnDismissRequest
        )
    }
}
