package com.example.petapp.ui.petdetails.addpetdata

import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import com.example.petapp.R
import com.example.petapp.ui.addpet.TextFieldUnitPicker
import com.example.petapp.ui.components.AddPetDataScaffold
import com.example.petapp.ui.components.ErrorScreen
import com.example.petapp.ui.components.LoadingScreen
import com.example.petapp.ui.components.forms.OutlinedTextFieldWithLeadingIcon
import com.example.petapp.ui.components.forms.SingleRowDateTimePicker

@Composable
fun AddDimensionsScreen(
    viewModel: PetDetailsAddDimensionsViewModel,
    navigateBack: () -> Unit,
    navigateToPetDetails: (petId: String) -> Unit
) {
    AddPetDataScaffold(
        topAppBarTitleId = R.string.components_forms_top_app_bar_title_pet_dimensions,
        onRightButtonClicked = {
            if (viewModel.onDoneButtonClicked()) navigateToPetDetails(
                viewModel.getPetId()
            )
        },
        navigateBack = { navigateToPetDetails(viewModel.getPetId()) },
        onLeftButtonClicked = navigateBack,
        hideKeyboard = viewModel::hideKeyboard
    ) {
        when (val uiState = viewModel.uiState.collectAsState().value) {
            PetDetailsAddDimensionsUiState.Loading -> LoadingScreen()
            is PetDetailsAddDimensionsUiState.Success -> AddDimensionsResultScreen(
                uiState = uiState,
                viewModel = viewModel
            )
            is PetDetailsAddDimensionsUiState.Error -> ErrorScreen(message = uiState.errorMessage)
        }
    }
}

@Composable
fun UpdateDimensionsScreen(
    viewModel: PetDetailsAddDimensionsViewModel,
    navigateBack: () -> Unit,
    navigateToPetDetails: (petId: String) -> Unit
) {
    AddPetDataScaffold(
        topAppBarTitleId = R.string.components_forms_top_app_bar_title_pet_dimensions,
        onRightButtonClicked = {
            if (viewModel.onDoneButtonClicked()) navigateToPetDetails(
                viewModel.getPetId()
            )
        },
        navigateBack = { navigateToPetDetails(viewModel.getPetId()) },
        onLeftButtonClicked = navigateBack,
        hideKeyboard = viewModel::hideKeyboard
    ) {
        when (val uiState = viewModel.uiState.collectAsState().value) {
            PetDetailsAddDimensionsUiState.Loading -> LoadingScreen()
            is PetDetailsAddDimensionsUiState.Success -> UpdateDimensionsResultScreen(
                uiState = uiState,
                viewModel = viewModel
            )
            is PetDetailsAddDimensionsUiState.Error -> TODO()
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AddDimensionsResultScreen(
    uiState: PetDetailsAddDimensionsUiState.Success,
    viewModel: PetDetailsAddDimensionsViewModel
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
        fieldLabel = R.string.components_forms_text_field_label_pet_height,
        fieldPlaceholder = uiState.defaultFiledValuePlaceholder,
        leadingIcon = R.drawable.baseline_height_24,
        trailingIcon = {
            TextFieldUnitPicker(
                expanded = uiState.isHeightUnitPickerExpanded,
                onExpandedChange = viewModel::onHeightUnitPickerOnExpandedChange,
                onDismissRequest = viewModel::onHeightUnitPickerOnDismissRequest,
                onDropdownMenuItemClicked = viewModel::onHeightUnitPickerDropdownMenuItemClicked,
                options = uiState.dimensionUnitList,
                selectedOption = uiState.selectedHeightUnit
            )
        },
        fieldValue = uiState.heightFieldValue,
        onValueChanged = viewModel::onHeightFieldValueChanged,
        onCancelClicked = viewModel::onHeightFieldCancelClicked,
        focusManager = focusManager,
        onFocusClear = viewModel::onFocusCleared.also { if (uiState.isHeightUnitPickerExpanded) focusManager.clearFocus() },
        readOnly = uiState.isHeightUnitPickerExpanded,
        isError = !uiState.isFormValid,
        keyboardOptions = KeyboardOptions(
            keyboardType = KeyboardType.Number,
            imeAction = ImeAction.Next
        ),
        hideKeyboard = uiState.hideKeyboard
    )
    OutlinedTextFieldWithLeadingIcon(
        fieldLabel = R.string.components_forms_text_field_label_pet_length,
        fieldPlaceholder = uiState.defaultFiledValuePlaceholder,
        leadingIcon = R.drawable.width_24,
        trailingIcon = {
            TextFieldUnitPicker(
                expanded = uiState.isLengthUnitPickerExpanded,
                onExpandedChange = viewModel::onLengthUnitPickerOnExpandedChange,
                onDismissRequest = viewModel::onLengthUnitPickerOnDismissRequest,
                onDropdownMenuItemClicked = viewModel::onLengthUnitPickerDropdownMenuItemClicked,
                options = uiState.dimensionUnitList,
                selectedOption = uiState.selectedLengthUnit
            )
        },
        fieldValue = uiState.lengthFieldValue,
        onValueChanged = viewModel::onLengthFieldValueChanged,
        onCancelClicked = viewModel::onLengthFieldCancelClicked,
        focusManager = focusManager,
        onFocusClear = viewModel::onFocusCleared.also { if (uiState.isLengthUnitPickerExpanded) focusManager.clearFocus() },
        readOnly = uiState.isLengthUnitPickerExpanded,
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
        trailingIcon = {
            TextFieldUnitPicker(
                expanded = uiState.isCircuitUnitPickerExpanded,
                onExpandedChange = viewModel::onCircuitUnitPickerOnExpandedChange,
                onDismissRequest = viewModel::onCircuitUnitPickerOnDismissRequest,
                onDropdownMenuItemClicked = viewModel::onCircuitUnitPickerDropdownMenuItemClicked,
                options = uiState.dimensionUnitList,
                selectedOption = uiState.selectedCircuitUnit
            )
        },
        fieldValue = uiState.circuitFieldValue,
        onValueChanged = viewModel::onCircuitFieldValueChanged,
        onCancelClicked = viewModel::onCircuitFieldCancelClicked,
        focusManager = focusManager,
        onFocusClear = viewModel::onFocusCleared.also { if (uiState.isCircuitUnitPickerExpanded) focusManager.clearFocus() },
        readOnly = uiState.isCircuitUnitPickerExpanded,
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

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun UpdateDimensionsResultScreen(
    uiState: PetDetailsAddDimensionsUiState.Success,
    viewModel: PetDetailsAddDimensionsViewModel
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
        fieldLabel = uiState.updatedFieldLabel,
        fieldPlaceholder = uiState.defaultFiledValuePlaceholder,
        leadingIcon = uiState.updatedFieldLeadingIcon,
        trailingIcon = {
            TextFieldUnitPicker(
                expanded = uiState.isUpdatedUnitPickerExpanded,
                onExpandedChange = viewModel::onUpdatedUnitPickerOnExpandedChange,
                onDismissRequest = viewModel::onUpdatedUnitPickerOnDismissRequest,
                onDropdownMenuItemClicked = viewModel::onUpdatedUnitPickerDropdownMenuItemClicked,
                options = uiState.dimensionUnitList,
                selectedOption = uiState.selectedUpdatedUnit
            )
        },
        fieldValue = uiState.updatedDimensionFieldValue,
        onValueChanged = viewModel::onUpdatedFieldValueChanged,
        onCancelClicked = viewModel::onUpdatedFieldCancelClicked,
        focusManager = focusManager,
        onFocusClear = viewModel::onFocusCleared.also { if (uiState.isUpdatedUnitPickerExpanded) focusManager.clearFocus() },
        readOnly = uiState.isUpdatedUnitPickerExpanded,
        isError = !uiState.isFormValid,
        keyboardOptions = KeyboardOptions(
            keyboardType = KeyboardType.Number,
            imeAction = ImeAction.Next
        ),
        hideKeyboard = uiState.hideKeyboard
    )
}