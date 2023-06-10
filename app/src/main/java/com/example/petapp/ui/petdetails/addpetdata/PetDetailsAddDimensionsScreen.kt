package com.example.petapp.ui.petdetails.addpetdata

import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import com.example.petapp.R
import com.example.petapp.ui.components.AddPetDataScaffold
import com.example.petapp.ui.components.forms.OutlinedTextFieldWithLeadingIcon
import com.example.petapp.ui.components.forms.SingleRowDateTimePicker

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AddDimensionsResultScreen(
    viewModel: PetDetailsAddDimensionsViewModel,
    navigateBack: () -> Unit,
    navigateToPetDetails: (petId: String) -> Unit
) {
    val uiState = viewModel.successUiState.collectAsState().value
    AddPetDataScaffold(
        topAppBarTitleId = R.string.components_forms_top_app_bar_title_pet_dimensions,
        onRightButtonClicked = {
            if (viewModel.onDoneButtonClicked()) navigateToPetDetails(
                viewModel.getPetId()
            )
        },
        navigateBack = { navigateToPetDetails(viewModel.getPetId()) },
        onLeftButtonClicked = navigateBack,
        hideKeyboard = viewModel::hideKeyboard) {
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
                fieldLabel = R.string.components_forms_text_field_label_pet_height,
                fieldPlaceholder = uiState.defaultFiledValuePlaceholder,
                leadingIcon = R.drawable.baseline_height_24,
                fieldValue = uiState.heightFieldValue,
                onValueChanged = viewModel::onHeightFieldValueChanged,
                onCancelClicked = viewModel::onHeightFieldCancelClicked,
                onFocusClear = viewModel::onFocusCleared,
                isError = !uiState.isFormValid,
                keyboardOptions = KeyboardOptions(
                    keyboardType = KeyboardType.Number,
                    imeAction = ImeAction.Next
                ),
                hideKeyboard = uiState.hideKeyboard
            )
            OutlinedTextFieldWithLeadingIcon(
                fieldLabel = R.string.components_forms_text_field_label_pet_width,
                fieldPlaceholder = uiState.defaultFiledValuePlaceholder,
                leadingIcon = R.drawable.width_24,
                fieldValue = uiState.lengthFieldValue,
                onValueChanged = viewModel::onLengthFieldValueChanged,
                onCancelClicked = viewModel::onLengthFieldCancelClicked,
                onFocusClear = viewModel::onFocusCleared,
                isError = !uiState.isFormValid,
                keyboardOptions = KeyboardOptions(
                    keyboardType = KeyboardType.Number,
                    imeAction = ImeAction.Next
                ),
                hideKeyboard = uiState.hideKeyboard
            )

            OutlinedTextFieldWithLeadingIcon(
                fieldLabel = R.string.components_forms_text_field_label_pet_circuit,
                fieldPlaceholder = uiState.defaultFiledValuePlaceholder,
                leadingIcon = R.drawable.restart_alt_24,
                fieldValue = uiState.circuitFieldValue,
                onValueChanged = viewModel::onCircuitFieldValueChanged,
                onCancelClicked = viewModel::onCircuitFieldCancelClicked,
                onFocusClear = viewModel::onFocusCleared,
                isError = !uiState.isFormValid,
                keyboardOptions = KeyboardOptions(
                    keyboardType = KeyboardType.Number,
                    imeAction = ImeAction.Done
                ),
                hideKeyboard = uiState.hideKeyboard
            )
            Text(
                text = "Fill out at least one field",
                style = MaterialTheme.typography.labelSmall,
                color = if (!uiState.isFormValid) MaterialTheme.colorScheme.error else MaterialTheme.colorScheme.onSurface
            )
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun UpdateDimensionsResultScreen(
    viewModel: PetDetailsAddDimensionsViewModel,
    navigateBack: () -> Unit,
    navigateToPetDetails: (petId: String) -> Unit
) {
    val uiState = viewModel.successUiState.collectAsState().value
    AddPetDataScaffold(
        topAppBarTitleId = R.string.components_forms_top_app_bar_title_pet_dimensions,
        onRightButtonClicked = {
            if (viewModel.onDoneButtonClicked()) navigateToPetDetails(
                viewModel.getPetId()
            )
        },
        navigateBack = { navigateToPetDetails(viewModel.getPetId()) },
        onLeftButtonClicked = navigateBack,
        hideKeyboard = viewModel::hideKeyboard) {
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
            fieldLabel = uiState.updatedFieldLabel,
            fieldPlaceholder = uiState.defaultFiledValuePlaceholder,
            leadingIcon = uiState.updatedFieldLeadingIcon,
            fieldValue = uiState.updatedDimensionFieldValue,
            onValueChanged = viewModel::onUpdatedFieldValueChanged,
            onCancelClicked = viewModel::onUpdatedFieldCancelClicked,
            onFocusClear = viewModel::onFocusCleared,
            isError = !uiState.isFormValid,
            keyboardOptions = KeyboardOptions(
                keyboardType = KeyboardType.Number,
                imeAction = ImeAction.Next
            ),
            hideKeyboard = uiState.hideKeyboard
        )
    }
}