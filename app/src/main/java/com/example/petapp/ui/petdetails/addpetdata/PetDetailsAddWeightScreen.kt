package com.example.petapp.ui.petdetails.addpetdata

import androidx.annotation.StringRes
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
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
                title = { Text(text = "Weight form") },
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
                    leftButtonStringId = R.string.cancel,
                    rightButtonStringId = R.string.done,
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
            headline = R.string.dimension_form_headline,
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
                fieldPlaceholder = R.string.pet_weight_unit,
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

@Composable
fun FormDefaultColumn(
    columnOnClicked: () -> Unit,
    modifier: Modifier = Modifier,
    @StringRes headline: Int = R.string.input_data,
    @StringRes supportingText: Int = R.string.fill_out_info,
    content: @Composable() (ColumnScope.() -> Unit)
) {
    val interactionSource = remember { MutableInteractionSource() }
    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Top,
        modifier = modifier
            .fillMaxSize()
            .clickable(
                onClick = columnOnClicked,
                interactionSource = interactionSource,
                indication = null
            )
            .padding(top = 20.dp)
    ) {
        Column(
            modifier = Modifier.widthIn(200.dp, 300.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(
                text = stringResource(id = headline),
                style = MaterialTheme.typography.headlineMedium
            )
            Text(
                text = stringResource(id = supportingText),
                style = MaterialTheme.typography.bodySmall
            )
            Spacer(modifier = Modifier.padding(20.dp))
            content()
        }
    }
}