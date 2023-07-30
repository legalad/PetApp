package com.example.petapp.ui.components.forms

import android.annotation.SuppressLint
import androidx.annotation.StringRes
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusManager
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.petapp.R
import java.time.Instant
import java.util.*


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SingleRowDateTimePicker(
    datePickerValue: String,
    datePickerOpenDialog: Boolean,
    datePickerState: DatePickerState,
    datePickerConfirmEnabled: Boolean,
    datePickerOnValueChanged: (String) -> Unit,
    datePickerOnTextFieldClicked: () -> Unit,
    datePickerOnDismissRequest: () -> Unit,
    datePickerOnConfirmedButtonClicked: () -> Unit,
    datePickerOnDismissedButtonClicked: () -> Unit,
    timePickerState: TimePickerState,
    timePickerOpenDialog: Boolean,
    timePickerShowingPicker: Boolean,
    onTimePickerTextFieldClicked: () -> Unit,
    onTimePickerDialogCancelClicked: () -> Unit,
    onTimePickerDialogConfirmClicked: () -> Unit,
    onTimePickerDialogSwitchIconClicked: () -> Unit
) {

    Row (modifier = Modifier.fillMaxWidth()){
        DatePicker(
            label = R.string.components_forms_text_field_label_measurement,
            value = datePickerValue,
            onValueChange = datePickerOnValueChanged,
            onTextFieldClicked = datePickerOnTextFieldClicked,
            openDialog = datePickerOpenDialog,
            datePickerState = datePickerState,
            confirmEnabled = datePickerConfirmEnabled,
            onDismissRequest = datePickerOnDismissRequest,
            onConfirmedButtonClicked = datePickerOnConfirmedButtonClicked,
            onDismissButtonClicked = datePickerOnDismissedButtonClicked,
            modifier = Modifier.weight(1f)
        )

        Spacer(modifier = Modifier.padding(5.dp))
        SwitchableTimePicker(
            showTimePicker = timePickerOpenDialog,
            showingPicker = timePickerShowingPicker,
            state = timePickerState,
            configuration = LocalConfiguration.current,
            onTextFiledClicked = onTimePickerTextFieldClicked,
            onDialogCanceledClicked = onTimePickerDialogCancelClicked,
            onDialogConfirmedClicked = onTimePickerDialogConfirmClicked,
            onShowingPickerIconClicked = onTimePickerDialogSwitchIconClicked,
            modifier = Modifier.weight(1f)
        )
    }

}
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DatePicker(
    @StringRes label: Int,
    value: String,
    onValueChange: (String) -> Unit,
    onTextFieldClicked: () -> Unit,
    openDialog: Boolean,
    datePickerState: DatePickerState,
    confirmEnabled: Boolean,
    onDismissRequest: () -> Unit,
    onConfirmedButtonClicked: () -> Unit,
    onDismissButtonClicked: () -> Unit,
    modifier: Modifier = Modifier.fillMaxWidth(),
    isError: Boolean = false,
    @StringRes supportingText: Int = R.string.util_blank

) {
    PickerOutlinedTextFieldWithLeadingIcon(
        fieldLabel = label,
        fieldPlaceholder = R.string.util_blank,
        leadingIcon = R.drawable.baseline_calendar_month_24,
        fieldValue = value,
        onValueChanged = onValueChange,
        supportingText = supportingText,
        onTextFieldClicked = onTextFieldClicked,
        isError = isError,
        modifier = modifier
       )
    if (openDialog) DatePickerDialogItem(
        openDialog = openDialog,
        datePickerState = datePickerState,
        confirmEnabled = confirmEnabled,
        onDismissRequest = onDismissRequest,
        onConfirmedButtonClicked = onConfirmedButtonClicked,
        onDismissButtonClicked = onDismissButtonClicked
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DatePickerOutlinedTextField(
    @StringRes label: Int,
    value: String,
    onValueChange: (value: String) -> Unit,
    onTextFieldClicked: () -> Unit,
    modifier: Modifier = Modifier,
    enabled: Boolean = false,
    focusManager: FocusManager = LocalFocusManager.current,
    keyboardOptions: KeyboardOptions = KeyboardOptions(imeAction = ImeAction.Next, keyboardType = KeyboardType.Number),
    keyboardActions: KeyboardActions = KeyboardActions(onNext = {
        focusManager.clearFocus()
    })
) {
    val interactionSource = remember { MutableInteractionSource() }
    Column (modifier = modifier){
        OutlinedTextField(
            label = { Text(text = "Birth date") },
            value = value,
            onValueChange = { onValueChange },
            leadingIcon = {
                Icon(
                    painter = painterResource(id = R.drawable.baseline_calendar_month_24),
                    contentDescription = null
                )
            },
            enabled = enabled,
            modifier = Modifier.clickable(
                interactionSource = interactionSource,
                onClick = onTextFieldClicked,
                indication = null
            ),
            keyboardOptions = keyboardOptions,
            keyboardActions = keyboardActions,
            colors = OutlinedTextFieldDefaults.colors(
                disabledTextColor = MaterialTheme.colorScheme.onSurfaceVariant,
                disabledBorderColor = MaterialTheme.colorScheme.outline,
                disabledLeadingIconColor = MaterialTheme.colorScheme.onSurfaceVariant,
                disabledLabelColor = MaterialTheme.colorScheme.onSurfaceVariant,
            )
        )
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DatePickerDialogItem(
    openDialog: Boolean,
    datePickerState: DatePickerState,
    confirmEnabled: Boolean,
    onDismissRequest: () -> Unit,
    onConfirmedButtonClicked: () -> Unit,
    onDismissButtonClicked: () -> Unit,
    dateValidator: (Long) -> Boolean = { it <= Instant.now().toEpochMilli() }
) {
    if (openDialog) {
        DatePickerDialog(
            onDismissRequest = onDismissRequest,
            confirmButton = {
                TextButton(
                    onClick = onConfirmedButtonClicked,
                    enabled = confirmEnabled
                ) {
                    Text(stringResource(R.string.components_forms_dialog_buttons_ok))
                }
            },
            dismissButton = {
                TextButton(
                    onClick = onDismissButtonClicked
                ) {
                    Text(text = stringResource(R.string.components_forms_dialog_buttons_cancel))
                }
            }) {
            DatePicker(state = datePickerState, dateValidator = dateValidator)
        }
    }
}

@SuppressLint("UnrememberedMutableState")
@OptIn(ExperimentalMaterial3Api::class)
@Preview
@Composable
fun DatePickerDialogItemPrev() {
    val openDialog = remember { mutableStateOf(true) }
    val datePickerState = rememberDatePickerState()
    val confirmEnabled = derivedStateOf { datePickerState.selectedDateMillis != null }
    DatePickerDialogItem(
        openDialog = openDialog.value,
        datePickerState = datePickerState,
        confirmEnabled = confirmEnabled.value,
        onDismissRequest = { openDialog.value = false },
        onConfirmedButtonClicked = { openDialog.value = false },
        onDismissButtonClicked = { openDialog.value = false })
}

@SuppressLint("UnrememberedMutableState")
@OptIn(ExperimentalMaterial3Api::class)
@Preview
@Composable
fun DatePickerPrev() {
    val value = remember { mutableStateOf("") }
    val openDialog = remember { mutableStateOf(false) }
    val datePickerState = rememberDatePickerState()
    val confirmEnabled = derivedStateOf { datePickerState.selectedDateMillis != null }
    DatePicker(
        label = R.string.components_forms_text_field_label_pet_age,
        value = value.value,
        onValueChange = { /*TODO*/ },
        onTextFieldClicked = { openDialog.value = true },
        openDialog = openDialog.value,
        datePickerState = datePickerState,
        confirmEnabled = confirmEnabled.value,
        onDismissRequest = { openDialog.value = false },
        onConfirmedButtonClicked = { openDialog.value = false },
        onDismissButtonClicked = { openDialog.value = false })
}

@OptIn(ExperimentalMaterial3Api::class)
@Preview
@Composable
fun SingleRowDateTimePickerPrev() {
    SingleRowDateTimePicker(
        datePickerValue = "12.10.2034",
        datePickerOpenDialog = false,
        datePickerState = rememberDatePickerState(),
        datePickerConfirmEnabled = false,
        datePickerOnValueChanged = {  },
        datePickerOnTextFieldClicked = {  },
        datePickerOnDismissRequest = {  },
        datePickerOnConfirmedButtonClicked = {  },
        datePickerOnDismissedButtonClicked = { },
        timePickerState = rememberTimePickerState(),
        timePickerOpenDialog = false,
        timePickerShowingPicker = false,
        onTimePickerTextFieldClicked = {  },
        onTimePickerDialogCancelClicked = {  },
        onTimePickerDialogConfirmClicked = {  }) {
        
    }
}