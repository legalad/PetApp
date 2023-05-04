package com.example.petapp.ui.petdetails.addpetdata

import androidx.annotation.StringRes
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import com.example.petapp.R
import com.example.petapp.ui.addpet.PetFormsBottomNavButtons
import com.example.petapp.ui.components.forms.DatePicker
import com.example.petapp.ui.components.forms.OutlinedTextFieldWithLeadingIcon

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AddWeightResultScreen(
    viewModel: PetDetailsAddDimensionsViewModel,
    navigateToPetDetails: (petId: String) -> Unit
) {
    val uiState = viewModel.successUiState.collectAsState().value
    FormDefaultColumn(columnOnClicked = { viewModel.hideKeyboard() }) {
        DatePicker(
            label = R.string.pet_weight,
            value = uiState.datePickerTextFieldValue,
            onValueChange = viewModel::onWeightFieldValueChanged,
            onTextFieldClicked = viewModel::onDatePickerTextFieldClicked,
            openDialog = uiState.datePickerOpenDialog,
            datePickerState = uiState.datePickerState,
            confirmEnabled = uiState.datePickerConfirmEnabled,
            onDismissRequest = viewModel::datePickerOnDismissRequest,
            onConfirmedButtonClicked = viewModel::datePickerOnConfirmedButtonClicked,
            onDismissButtonClicked = viewModel::datePickerOnDismissedButtonClicked)
        OutlinedTextFieldWithLeadingIcon(
            fieldLabel = R.string.pet_weight,
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
        PetFormsBottomNavButtons(
            leftButtonStringId = R.string.cancel,
            rightButtonStringId = R.string.done,
            onLeftButtonClicked = { navigateToPetDetails(viewModel.getPetId()) },
            onRightButtonClicked = { if(viewModel.onDoneButtonClicked()) navigateToPetDetails(viewModel.getPetId()) })
    }
}

@Composable
fun FormDefaultColumn(@StringRes headline: Int = R.string.input_data, columnOnClicked: () -> Unit, content: @Composable() (ColumnScope.() -> Unit)) {
    val interactionSource = remember { MutableInteractionSource() }
    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Top,
        modifier = Modifier
            .fillMaxSize()
            .clickable(
                onClick = columnOnClicked,
                interactionSource = interactionSource,
                indication = null
            )
    )    {
        Column (modifier = Modifier.width(IntrinsicSize.Max), horizontalAlignment = Alignment.CenterHorizontally){
            Text(
                text = stringResource(id = headline),
                style = MaterialTheme.typography.headlineMedium
            )
            Spacer(modifier = Modifier.padding(20.dp))
            content()
        }
    }
}