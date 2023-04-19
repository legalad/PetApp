package com.example.petapp.ui.components.forms

import android.annotation.SuppressLint
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.petapp.R
import java.util.*

@OptIn(ExperimentalMaterial3Api::class)
@Preview(showBackground = true)
@Composable
fun DatePickerPrevV1() {
    Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
        val state = rememberDatePickerState(initialDisplayMode = DisplayMode.Input)
        DatePicker(state = state, modifier = Modifier.padding(16.dp))

        Text("Entered date timestamp: ${state.selectedDateMillis ?: "no input"}")
    }
}

//sprawdzic czy tu state powinien byc w sumie
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DatePickerDialogItem(
    openDialog: State<Boolean>,
    datePickerState: DatePickerState,
    confirmEnabled: State<Boolean>,
    onDismissRequest: () -> Unit,
    onConfirmedButtonClicked: () -> Unit,
    onDismissButtonClicked: () -> Unit
) {
    if (openDialog.value) {
        DatePickerDialog(
            onDismissRequest = onDismissRequest,
            confirmButton = {
                TextButton(
                    onClick = onConfirmedButtonClicked,
                    enabled = confirmEnabled.value
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
    val openDialog = remember {  mutableStateOf(true)  }
    val datePickerState = rememberDatePickerState()
    val confirmEnabled = derivedStateOf { datePickerState.selectedDateMillis != null }
    DatePickerDialogItem(
        openDialog = openDialog,
        datePickerState = datePickerState,
        confirmEnabled = confirmEnabled,
        onDismissRequest = { openDialog.value = false },
        onConfirmedButtonClicked = { openDialog.value = false },
        onDismissButtonClicked = { openDialog.value = false })
}