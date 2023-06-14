package com.example.petapp.ui.petdetails.addpetdata

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.unit.dp
import com.example.petapp.R
import com.example.petapp.ui.components.PetFormsBottomNavButtons
import com.example.petapp.ui.components.forms.*

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun PetDetailsAddMealResultScreen(
    viewModel: PetDetailsAddMealViewModel,
    navigateBack: () -> Unit,
    navigateToPetDetails: (petId: String) -> Unit
) {
    val uiState = viewModel.successUiState.collectAsState().value

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text(text = "Meal form") },
                navigationIcon = {
                    IconButton(onClick = navigateBack) {
                        Icon(imageVector = Icons.Default.ArrowBack, contentDescription = "back")
                    }
                }
            )
        },
        bottomBar = {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(4.dp), horizontalAlignment = Alignment.CenterHorizontally
            ) {
                PetFormsBottomNavButtons(
                    leftButtonStringId = R.string.components_forms_dialog_buttons_cancel,
                    rightButtonStringId = R.string.components_forms_dialog_buttons_done,
                    onLeftButtonClicked = navigateBack,
                    onRightButtonClicked = {
                        if (viewModel.onDoneButtonCLicked()) navigateBack()
                    })
            }
        }
    ) { innerPadding ->
        FormDefaultColumn(headline = R.string.components_forms_text_field_label_meal,columnOnClicked = { /*TODO*/ }, modifier = Modifier.padding(innerPadding)) {
            ExposedDropdownMenuV2(
                label = R.string.components_forms_text_field_label_meal,
                options = uiState.mealTypeMenuOptions,
                expanded = uiState.mealTypeMenuExpanded,
                selectedOption = uiState.mealTypeMenuSelectedOption.nameId,
                onExpandedChange = viewModel::mealTypeMenuOnExpandedChanged,
                onDropdownMenuItemClicked = viewModel::mealTypeMenuOnDropdownMenuItemClicked,
                onDismissRequest = viewModel::mealTypeMenuOnDismissRequest,
                isError = !uiState.isMealTypeValid,
                supportingText = uiState.mealTypeErrorMessage
            )

            SwitchableTimePicker(
                showTimePicker = uiState.showTimePicker,
                showingPicker = uiState.showingPicker,
                state = uiState.timePickerState,
                configuration = LocalConfiguration.current,
                onTextFiledClicked = viewModel::onTimePickerTextFieldClicked,
                onDialogCanceledClicked = viewModel::onTimePickerDialogCancelClicked,
                onDialogConfirmedClicked = viewModel::onTimePickerDialogConfirmClicked,
                onShowingPickerIconClicked = viewModel::onTimePickerDialogSwitchIconClicked
            )

            RadioGroupList(
                radioOptions = uiState.foodTypeRadioOptions.map { it.nameId },
                selectedOption = uiState.foodTypeRadioSelectedOption.nameId,
                onOptionSelected = viewModel::onRadioButtonSelectedOptionClicked
            )

            if (uiState.foodTypeRadioSelectedOption == FoodTypeEnum.OTHER) {
                ExposedDropdownMenu(
                    label = R.string.components_forms_text_field_label_food,
                    options = emptyList(),
                    expanded = uiState.foodMenuExpanded,
                    selectedOption = uiState.foodMenuSelectedOptionText,
                    onExpandedChange = viewModel::foodMenuOnExpandedChanged,
                    onDropdownMenuItemClicked = viewModel::foodMenuOnDropdownMenuItemClicked,
                    onDismissRequest = viewModel::foodMenuOnDismissRequest
                )
            }
        }
    }
}