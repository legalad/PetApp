package com.example.petapp.ui.petdetails.addpetdata

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import com.example.petapp.R
import com.example.petapp.ui.addpet.TextFieldUnitPicker
import com.example.petapp.ui.components.AddPetDataScaffold
import com.example.petapp.ui.components.forms.*


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AddWeightResultScreen(
    viewModel: PetDetailsAddWeightViewModel,
    navigateBack: () -> Unit,
    navigateToPetDetails: (petId: String) -> Unit
) {
    val uiState = viewModel.successUiState.collectAsState().value
    AddPetDataScaffold(
        topAppBarTitleId = R.string.components_forms_top_app_bar_title_pet_weight,
        onRightButtonClicked = {
            if (viewModel.onDoneButtonClicked()) {
                navigateToPetDetails(
                    viewModel.getPetId()
                )
            }
        },
        navigateBack = { navigateToPetDetails(viewModel.getPetId()) },
        onLeftButtonClicked = navigateBack,
        hideKeyboard = viewModel::hideKeyboard
    ) {
        val focusManager = LocalFocusManager.current
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
            fieldPlaceholder = R.string.util_unit_weight_placeholder,
            leadingIcon = R.drawable.weight_24,
            trailingIcon = {
                TextFieldUnitPicker(
                    expanded = uiState.isWeightUnitPickerExpanded,
                    onExpandedChange = viewModel::onWeightUnitPickerOnExpandedChange,
                    onDismissRequest = viewModel::onWeightUnitPickerOnDismissRequest,
                    onDropdownMenuItemClicked = viewModel::onWeightUnitPickerDropdownMenuItemClicked,
                    options = uiState.weightUnitList,
                    selectedOption = uiState.selectedWeightUnit
                )
            },
            fieldValue = uiState.weightFieldValue,
            onValueChanged = viewModel::onWeightFieldValueChanged,
            onCancelClicked = viewModel::onWeightFieldCancelClicked,
            focusManager = focusManager,
            onFocusClear = viewModel::onFocusCleared.also { if (uiState.isWeightUnitPickerExpanded) focusManager.clearFocus() },
            readOnly = uiState.isWeightUnitPickerExpanded,
            isError = !uiState.isWeightValid,
            supportingText = uiState.weightErrorMessage,
            keyboardOptions = KeyboardOptions(
                keyboardType = KeyboardType.Number,
                imeAction = ImeAction.Done
            ),
            hideKeyboard = uiState.hideKeyboard
        )
    }
}

