package com.example.petapp.ui.petdetails.addpetdata

import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import com.example.petapp.R
import com.example.petapp.ui.addpet.PetFormsBottomNavButtons
import com.example.petapp.ui.components.forms.DatePicker
import com.example.petapp.ui.components.forms.OutlinedTextFieldWithLeadingIcon

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AddDimensionsResultScreen(
    viewModel: PetDetailsAddDimensionsViewModel,
    navigateToPetDetails: (petId: String) -> Unit
) {
    val uiState = viewModel.successUiState.collectAsState().value

    FormDefaultColumn(columnOnClicked = { /*TODO*/ }) {
        DatePicker(
            label = R.string.measurement_date,
            value = uiState.datePickerTextFieldValue,
            onValueChange = viewModel::onDatePickerTextFieldValueChanged,
            onTextFieldClicked = viewModel::onDatePickerTextFieldClicked,
            openDialog = uiState.datePickerOpenDialog,
            datePickerState = uiState.datePickerState,
            confirmEnabled = uiState.datePickerConfirmEnabled,
            onDismissRequest = viewModel::datePickerOnDismissRequest,
            onConfirmedButtonClicked = viewModel::datePickerOnConfirmedButtonClicked,
            onDismissButtonClicked = viewModel::datePickerOnDismissedButtonClicked
        )
        OutlinedTextFieldWithLeadingIcon(
            fieldLabel = R.string.pet_height,
            fieldPlaceholder = R.string.pet_dimensions_unit,
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
            fieldLabel = R.string.pet_width,
            fieldPlaceholder = R.string.pet_dimensions_unit,
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
            fieldLabel = R.string.pet_circuit,
            fieldPlaceholder = R.string.pet_dimensions_unit,
            leadingIcon = R.drawable.restart_alt_24,
            fieldValue = uiState.circuitFieldValue,
            onValueChanged = viewModel::onCircuitFieldValueChanged,
            onCancelClicked = viewModel::onCircuitFieldCancelClicked,
            onFocusClear = viewModel::onFocusCleared,
            isError = !uiState.isFormValid,
            keyboardOptions = KeyboardOptions(
                keyboardType = KeyboardType.Number,
                imeAction = ImeAction.Next
            ),
            hideKeyboard = uiState.hideKeyboard
        )
        Text(text = "Fill out at least one field", style = MaterialTheme.typography.labelSmall, color =  if (!uiState.isFormValid) MaterialTheme.colorScheme.error else MaterialTheme.colorScheme.onSurface )
        PetFormsBottomNavButtons(
            leftButtonStringId = R.string.cancel,
            rightButtonStringId = R.string.done,
            onLeftButtonClicked = { navigateToPetDetails(viewModel.getPetId()) },
            onRightButtonClicked = { if(viewModel.onDoneButtonClicked()) navigateToPetDetails(viewModel.getPetId()) })
    }
}