package com.example.petapp.ui.components.forms

import android.content.res.Configuration
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.semantics.isContainer
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import androidx.compose.ui.window.DialogProperties
import androidx.compose.ui.zIndex
import com.example.petapp.R
import kotlinx.coroutines.launch
import java.text.SimpleDateFormat
import java.time.LocalTime
import java.time.ZoneId
import java.time.format.DateTimeFormatter
import java.time.format.FormatStyle
import java.util.*

@OptIn(ExperimentalMaterial3Api::class)
@Composable
@Preview
fun TimePickerSample() {
    var showTimePicker by remember { mutableStateOf(false) }
    val state = rememberTimePickerState()
    val formatter = remember { SimpleDateFormat("hh:mm a", Locale.getDefault()) }
    val snackState = remember { SnackbarHostState() }
    val snackScope = rememberCoroutineScope()

    Box(propagateMinConstraints = false) {
        Button(
            modifier = Modifier.align(Alignment.Center),
            onClick = { showTimePicker = true }
        ) { Text("Set Time") }
        SnackbarHost(hostState = snackState)
    }

    if (showTimePicker) {
        TimePickerDialog(
            onCancel = { showTimePicker = false },
            onConfirm = {
                val cal = Calendar.getInstance()
                cal.set(Calendar.HOUR_OF_DAY, state.hour)
                cal.set(Calendar.MINUTE, state.minute)
                cal.isLenient = false
                snackScope.launch {
                    snackState.showSnackbar("Entered time: ${formatter.format(cal.time)}")
                }
                showTimePicker = false
            },
        ) {
            TimePicker(state = state)
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
@Preview
fun TimeInputSample() {
    var showTimePicker by remember { mutableStateOf(false) }
    val state = rememberTimePickerState()
    val formatter = remember { SimpleDateFormat("hh:mm a", Locale.getDefault()) }
    val snackState = remember { SnackbarHostState() }
    val snackScope = rememberCoroutineScope()

    Box(propagateMinConstraints = false) {
        Button(
            modifier = Modifier.align(Alignment.Center),
            onClick = { showTimePicker = true }
        ) { Text("Set Time") }
        SnackbarHost(hostState = snackState)
    }

    if (showTimePicker) {
        TimePickerDialog(
            onCancel = { showTimePicker = false },
            onConfirm = {
                val cal = Calendar.getInstance()
                cal.set(Calendar.HOUR_OF_DAY, state.hour)
                cal.set(Calendar.MINUTE, state.minute)
                cal.isLenient = false
                snackScope.launch {
                    snackState.showSnackbar("Entered time: ${formatter.format(cal.time)}")
                }
                showTimePicker = false
            },
        ) {
            TimeInput(state = state)
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
@Preview
fun TimePickerSwitchableSample() {
    var showTimePicker by remember { mutableStateOf(false) }
    var showingPicker by remember { mutableStateOf(true) }
    val state = rememberTimePickerState()
    val configuration = LocalConfiguration.current

    SwitchableTimePicker(
        showTimePicker,
        showingPicker,
        state,
        configuration,
        onTextFiledClicked = {showTimePicker = !showTimePicker},
        onDialogConfirmedClicked = {showTimePicker = false},
        onDialogCanceledClicked = {showTimePicker = false},
        onShowingPickerIconClicked = {showingPicker = !showingPicker}
    )
}

@Composable
@OptIn(ExperimentalMaterial3Api::class)
fun SwitchableTimePicker(
    showTimePicker: Boolean,
    showingPicker: Boolean,
    state: TimePickerState,
    configuration: Configuration,
    onTextFiledClicked: () -> Unit,
    onDialogCanceledClicked: () -> Unit,
    onDialogConfirmedClicked: () -> Unit,
    onShowingPickerIconClicked: () -> Unit,
    modifier: Modifier = Modifier
) {
    Box(propagateMinConstraints = false, modifier = modifier) {
        PickerOutlinedTextFieldWithLeadingIcon(
            fieldLabel = R.string.components_forms_text_field_label_time,
            fieldPlaceholder = R.string.components_forms_text_field_placeholder_pet_name,
            leadingIcon = R.drawable.outline_schedule_24,
            fieldValue = DateTimeFormatter.ofLocalizedTime(FormatStyle.SHORT)
                .withLocale(Locale.getDefault()).withZone(
                    ZoneId.systemDefault()
                ).format(LocalTime.of(state.hour, state.minute)),
            onValueChanged = {},
            onTextFieldClicked = onTextFiledClicked
        )
    }

    if (showTimePicker) {
        TimePickerDialog(
            title = if (showingPicker) {
                stringResource(R.string.components_forms_time_picker_dialog_title_select_time)
            } else {
                stringResource(R.string.components_forms_time_picker_dialog_title_enter_time)
            },
            onCancel = onDialogCanceledClicked,
            onConfirm = {
                        onDialogConfirmedClicked()
            },
            toggle = {
                if (configuration.screenHeightDp > 400) {
                    // Make this take the entire viewport. This will guarantee that Screen readers
                    // focus the toggle first.
                    Box(
                        Modifier
                            .fillMaxSize()
                            .semantics {
                                @Suppress("DEPRECATION")
                                isContainer = true
                            }
                    ) {
                        IconButton(
                            modifier = Modifier
                                // This is a workaround so that the Icon comes up first
                                // in the talkback traversal order. So that users of a11y
                                // services can use the text input. When talkback traversal
                                // order is customizable we can remove this.
                                .size(64.dp, 72.dp)
                                .align(Alignment.BottomStart)
                                .zIndex(5f),
                            onClick = onShowingPickerIconClicked
                        ) {
                            val iconId = if (showingPicker) {
                                R.drawable.outline_keyboard_24
                            } else {
                                R.drawable.outline_schedule_24
                            }
                            Icon(
                                painterResource(id = iconId),
                                contentDescription = if (showingPicker) {
                                    stringResource(R.string.components_forms_time_picker_dialog_switch_icon_content_description_to_text_input)
                                } else {
                                    stringResource(R.string.components_forms_time_picker_dialog_switch_icon_content_description_to_touch_input)
                                }
                            )
                        }
                    }
                }
            }
        ) {
            if (showingPicker && configuration.screenHeightDp > 400) {
                TimePicker(state = state)
            } else {
                TimeInput(state = state)
            }
        }
    }
}

@Composable
fun TimePickerDialog(
    title: String = "Select Time",
    onCancel: () -> Unit,
    onConfirm: () -> Unit,
    toggle: @Composable () -> Unit = {},
    content: @Composable () -> Unit,
) {
    Dialog(
        onDismissRequest = onCancel,
        properties = DialogProperties(
            usePlatformDefaultWidth = false
        ),
    ) {
        Surface(
            shape = MaterialTheme.shapes.extraLarge,
            tonalElevation = 6.dp,
            modifier = Modifier
                .width(IntrinsicSize.Min)
                .height(IntrinsicSize.Min)
                .background(
                    shape = MaterialTheme.shapes.extraLarge,
                    color = MaterialTheme.colorScheme.surface
                ),
        ) {
            toggle()
            Column(
                modifier = Modifier.padding(24.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Text(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(bottom = 20.dp),
                    text = title,
                    style = MaterialTheme.typography.labelMedium
                )
                content()
                Row(
                    modifier = Modifier
                        .height(40.dp)
                        .fillMaxWidth()
                ) {
                    Spacer(modifier = Modifier.weight(1f))
                    TextButton(
                        onClick = onCancel
                    ) { Text(text = stringResource(id = R.string.components_forms_dialog_buttons_cancel)) }
                    TextButton(
                        onClick = onConfirm
                    ) { Text(text = stringResource(id = R.string.components_forms_dialog_buttons_ok)) }
                }
            }
        }
    }
}
