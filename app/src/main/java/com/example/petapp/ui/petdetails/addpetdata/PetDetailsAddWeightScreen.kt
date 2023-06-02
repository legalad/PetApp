package com.example.petapp.ui.petdetails.addpetdata

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import com.example.petapp.R
import com.example.petapp.ui.addpet.PetFormsBottomNavButtons
import com.example.petapp.ui.components.forms.*

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AddWeightResultScreen(
    viewModel: PetDetailsAddWeightViewModel,
    navigateBack: () -> Unit,
    navigateToPetDetails: (petId: String) -> Unit
) {
    val uiState = viewModel.successUiState.collectAsState().value
    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text(text = stringResource(id = R.string.components_forms_top_app_bar_title_pet_weight)) },
                navigationIcon = {
                    IconButton(onClick = navigateBack) {
                        Icon(
                            imageVector = Icons.Default.ArrowBack,
                            contentDescription = stringResource(
                                id = R.string.components_top_app_bar_navigation_content_description_back
                            )
                        )
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
                    onLeftButtonClicked = { navigateToPetDetails(viewModel.getPetId()) },
                    onRightButtonClicked = {
                        if (viewModel.onDoneButtonClicked()) navigateToPetDetails(
                            viewModel.getPetId()
                        )
                    })
            }
        }
    ) { innerPadding ->
        FormDefaultColumn(
            headline = R.string.components_forms_title_measurement,
            columnOnClicked = { viewModel.hideKeyboard() },
            modifier = Modifier.padding(innerPadding)
        ) {
            SingleRowDateTimePicker(
                datePickerValue = uiState.datePickerTextFieldValue,
                datePickerOpenDialog = uiState.datePickerOpenDialog,
                datePickerState = uiState.datePickerState,
                datePickerConfirmEnabled = uiState.datePickerConfirmEnabled,
                datePickerOnValueChanged = viewModel::onDatePickerTextFieldValueChanged,
                datePickerOnTextFieldClicked = viewModel::onDatePickerTextFieldClicked,
                datePickerOnDismissRequest = viewModel::datePickerOnDismissRequest,
                datePickerOnConfirmedButtonClicked = viewModel::datePickerOnConfirmedButtonClicked,
                datePickerOnDismissedButtonClicked = viewModel::datePickerOnDismissedButtonClicked,
                timePickerState = uiState.timePickerState,
                timePickerOpenDialog = uiState.showTimePicker,
                timePickerShowingPicker = uiState.showingPicker,
                onTimePickerTextFieldClicked = viewModel::onTimePickerTextFieldClicked,
                onTimePickerDialogCancelClicked = viewModel::onTimePickerDialogCancelClicked,
                onTimePickerDialogConfirmClicked = viewModel::onTimePickerDialogConfirmClicked,
                onTimePickerDialogSwitchIconClicked = viewModel::onTimePickerDialogSwitchIconClicked
            )
            OutlinedTextFieldWithLeadingIcon(
                fieldLabel = R.string.components_forms_text_field_label_pet_weight,
                fieldPlaceholder = R.string.util_unit_weight,
                leadingIcon = R.drawable.weight_24,
                fieldValue = uiState.weightFieldValue,
                onValueChanged = viewModel::onWeightFieldValueChanged,
                onCancelClicked = viewModel::onWeightFieldCancelClicked,
                onFocusClear = viewModel::onFocusCleared,
                isError = !uiState.isWeightValid,
                supportingText = uiState.weightErrorMessage,
                keyboardOptions = KeyboardOptions(
                    keyboardType = KeyboardType.Number,
                    imeAction = ImeAction.Next
                ),
                hideKeyboard = uiState.hideKeyboard
            )
        }
    }
}

