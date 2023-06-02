package com.example.petapp.ui.petdetails.addpetdata

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import com.example.petapp.R
import com.example.petapp.ui.addpet.PetFormsBottomNavButtons
import com.example.petapp.ui.components.forms.FormDefaultColumn
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

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text(text = "Dimension form") },
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
                    onLeftButtonClicked = { navigateToPetDetails(viewModel.getPetId()) },
                    onRightButtonClicked = {
                        if (viewModel.onDoneButtonClicked()) navigateToPetDetails(
                            viewModel.getPetId()
                        )
                    })
            }
        }
    ) { innerPadding ->
        FormDefaultColumn(headline = R.string.components_forms_title_measurement,columnOnClicked = { viewModel.hideKeyboard() }, modifier = Modifier.padding(innerPadding)) {
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
                fieldPlaceholder = R.string.util_unit_dimension,
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
                fieldPlaceholder = R.string.util_unit_dimension,
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
                fieldPlaceholder = R.string.util_unit_dimension,
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
            Text(
                text = "Fill out at least one field",
                style = MaterialTheme.typography.labelSmall,
                color = if (!uiState.isFormValid) MaterialTheme.colorScheme.error else MaterialTheme.colorScheme.onSurface
            )

        }
    }
}