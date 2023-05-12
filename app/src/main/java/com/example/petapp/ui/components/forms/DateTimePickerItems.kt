package com.example.petapp.ui.components.forms

import android.annotation.SuppressLint
import android.app.TimePickerDialog
import androidx.annotation.StringRes
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusManager
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.tooling.preview.Preview
import com.example.petapp.R
import java.util.*


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
    modifier: Modifier = Modifier,
    isError: Boolean = false,
    @StringRes supportingText: Int = R.string.blank

) {
    OutlinedTextFieldWithLeadingIcon(
        fieldLabel = label,
        fieldPlaceholder = R.string.blank,
        leadingIcon = R.drawable.baseline_calendar_month_24,
        fieldValue = value,
        onValueChanged = onValueChange,
        onCancelClicked = onTextFieldClicked,
        supportingText = supportingText,
        onTextFieldClicked = onTextFieldClicked,
        enabled = false,
        isError = isError,
        colors = if (!isError) TextFieldDefaults.outlinedTextFieldColors(
            disabledBorderColor = MaterialTheme.colorScheme.outline,
            disabledLeadingIconColor = MaterialTheme.colorScheme.onSurfaceVariant,
            disabledTextColor = MaterialTheme.colorScheme.onSurfaceVariant,
            disabledLabelColor = MaterialTheme.colorScheme.onSurfaceVariant,
            disabledSupportingTextColor = MaterialTheme.colorScheme.onSurfaceVariant
        ) else TextFieldDefaults.outlinedTextFieldColors(
            disabledBorderColor = MaterialTheme.colorScheme.error,
            disabledLeadingIconColor = MaterialTheme.colorScheme.onSurfaceVariant,
            disabledTextColor = MaterialTheme.colorScheme.error,
            disabledLabelColor = MaterialTheme.colorScheme.error,
            disabledSupportingTextColor = MaterialTheme.colorScheme.error
        ),
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
            colors = TextFieldDefaults.outlinedTextFieldColors(
                disabledBorderColor = MaterialTheme.colorScheme.outline,
                disabledLeadingIconColor = MaterialTheme.colorScheme.onSurfaceVariant,
                disabledTextColor = MaterialTheme.colorScheme.onSurfaceVariant,
                disabledLabelColor = MaterialTheme.colorScheme.onSurfaceVariant
            )
        )
    }
}

//sprawdzic czy tu state powinien byc w sumie
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DatePickerDialogItem(
    openDialog: Boolean,
    datePickerState: DatePickerState,
    confirmEnabled: Boolean,
    onDismissRequest: () -> Unit,
    onConfirmedButtonClicked: () -> Unit,
    onDismissButtonClicked: () -> Unit
) {
    if (openDialog) {
        DatePickerDialog(
            onDismissRequest = onDismissRequest,
            confirmButton = {
                TextButton(
                    onClick = onConfirmedButtonClicked,
                    enabled = confirmEnabled
                ) {
                    Text(stringResource(R.string.ok))
                }
            },
            dismissButton = {
                TextButton(
                    onClick = onDismissButtonClicked
                ) {
                    Text(text = stringResource(R.string.cancel))
                }
            }) {
            DatePicker(state = datePickerState)
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
        label = R.string.pet_age,
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