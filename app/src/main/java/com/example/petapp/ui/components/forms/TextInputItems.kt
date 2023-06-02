package com.example.petapp.ui.components.forms

import androidx.annotation.DrawableRes
import androidx.annotation.StringRes
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.interaction.collectIsFocusedAsState
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusManager
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.tooling.preview.Preview
import com.example.petapp.R


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun OutlinedTextFieldWithLeadingIcon(
    @StringRes fieldLabel: Int,
    @StringRes fieldPlaceholder: Int,
    @DrawableRes leadingIcon: Int,
    fieldValue: String,
    onValueChanged: (String) -> Unit,
    onCancelClicked: () -> Unit,
    modifier: Modifier = Modifier.fillMaxWidth(),
    enabled: Boolean = true,
    isError: Boolean = false,
    colors: TextFieldColors = OutlinedTextFieldDefaults.colors(
    ),
    @StringRes supportingText: Int = R.string.util_blank,
    onFocusClear: () -> Unit = { },
    onTextFieldClicked: () -> Unit = { },
    hideKeyboard: Boolean = false,
    focusManager: FocusManager = LocalFocusManager.current,
    keyboardOptions: KeyboardOptions = KeyboardOptions(imeAction = ImeAction.Next),
    keyboardActions: KeyboardActions = KeyboardActions(onSearch = {
        focusManager.clearFocus()
    })
) {

    val interactionSource = remember { MutableInteractionSource() }
    val isFocused = interactionSource.collectIsFocusedAsState()
    var inputChanged = remember { mutableStateOf(false) }

    Column(modifier = modifier) {
        OutlinedTextField(
            value = fieldValue,
            label = { Text(text = stringResource(id = fieldLabel)) },
            onValueChange = {
                onValueChanged(it)
                inputChanged.value = true
            },
            placeholder = { Text(text = stringResource(id = fieldPlaceholder)) },
            leadingIcon = {
                Icon(
                    painter = painterResource(id = leadingIcon),
                    contentDescription = null
                )
            },
            trailingIcon = {
                if (fieldValue.isNotBlank() && isFocused.value) IconButton(onClick = onCancelClicked) {
                    Icon(
                        painter = painterResource(id = R.drawable.round_cancel_24),
                        contentDescription = stringResource(
                            R.string.components_forms_dialog_buttons_cancel
                        )
                    )
                }
            },
            singleLine = true,
            enabled = enabled,
            colors = colors,
            isError = (isError && !isFocused.value),
            supportingText = {
                if (isError && !isFocused.value) {
                    Text(
                        text = stringResource(
                            id = supportingText
                        )
                    )
                }
            },
            keyboardOptions = keyboardOptions,
            keyboardActions = keyboardActions,
            interactionSource = interactionSource,
            modifier = modifier.clickable(
                interactionSource = interactionSource,
                onClick = onTextFieldClicked,
                indication = null
            )
        )
        if (hideKeyboard) {
            focusManager.clearFocus()
            onFocusClear()
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun PickerOutlinedTextFieldWithLeadingIcon(
    @StringRes fieldLabel: Int,
    @StringRes fieldPlaceholder: Int,
    @DrawableRes leadingIcon: Int,
    fieldValue: String,
    onValueChanged: (String) -> Unit,
    modifier: Modifier = Modifier,
    isError: Boolean = false,
    @StringRes supportingText: Int = R.string.util_blank,
    onFocusClear: () -> Unit = { },
    onTextFieldClicked: () -> Unit = { },
    hideKeyboard: Boolean = false,
    focusManager: FocusManager = LocalFocusManager.current,
    keyboardOptions: KeyboardOptions = KeyboardOptions(imeAction = ImeAction.Next),
    keyboardActions: KeyboardActions = KeyboardActions(onSearch = {
        focusManager.clearFocus()
    })
) {

    val interactionSource = remember { MutableInteractionSource() }
    val isFocused = interactionSource.collectIsFocusedAsState()
    var inputChanged = remember { mutableStateOf(false) }

    Column(modifier = modifier) {
        OutlinedTextField(
            value = fieldValue,
            label = { Text(text = stringResource(id = fieldLabel)) },
            onValueChange = {
                onValueChanged(it)
                inputChanged.value = true
            },
            placeholder = { Text(text = stringResource(id = fieldPlaceholder)) },
            leadingIcon = {
                Icon(
                    painter = painterResource(id = leadingIcon),
                    contentDescription = null
                )
            },
            singleLine = true,
            enabled = false,
            colors = if (!isError) OutlinedTextFieldDefaults.colors(
                disabledTextColor = MaterialTheme.colorScheme.onSurfaceVariant,
                disabledBorderColor = MaterialTheme.colorScheme.outline,
                disabledLeadingIconColor = MaterialTheme.colorScheme.onSurfaceVariant,
                disabledLabelColor = MaterialTheme.colorScheme.onSurfaceVariant,
                disabledSupportingTextColor = MaterialTheme.colorScheme.onSurfaceVariant,
            ) else OutlinedTextFieldDefaults.colors(
                disabledTextColor = MaterialTheme.colorScheme.error,
                disabledBorderColor = MaterialTheme.colorScheme.error,
                disabledLeadingIconColor = MaterialTheme.colorScheme.onSurfaceVariant,
                disabledLabelColor = MaterialTheme.colorScheme.error,
                disabledSupportingTextColor = MaterialTheme.colorScheme.error,
            ),
            isError = (isError && !isFocused.value),
            supportingText = {
                if (isError && !isFocused.value) {
                    Text(
                        text = stringResource(
                            id = supportingText
                        )
                    )
                }
            },
            keyboardOptions = keyboardOptions,
            keyboardActions = keyboardActions,
            interactionSource = interactionSource,
            modifier = Modifier.clickable(
                interactionSource = interactionSource,
                onClick = onTextFieldClicked,
                indication = null
            )
        )
        if (hideKeyboard) {
            focusManager.clearFocus()
            onFocusClear()
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MenuOutlinedTextField(
    @StringRes fieldLabel: Int,
    @StringRes fieldPlaceholder: Int,
    fieldValue: String,
    expanded: Boolean,
    readOnly: Boolean = true,
    onValueChanged: (String) -> Unit,
    modifier: Modifier = Modifier,
    isError: Boolean = false,
    @StringRes supportingText: Int = R.string.util_blank,
    onFocusClear: () -> Unit = { },
    onTextFieldClicked: () -> Unit = { },
    hideKeyboard: Boolean = false,
    focusManager: FocusManager = LocalFocusManager.current,
    keyboardOptions: KeyboardOptions = KeyboardOptions(imeAction = ImeAction.Next),
    keyboardActions: KeyboardActions = KeyboardActions(onSearch = {
        focusManager.clearFocus()
    })
) {

    val interactionSource = remember { MutableInteractionSource() }
    val isFocused = interactionSource.collectIsFocusedAsState()
    var inputChanged = remember { mutableStateOf(false) }

    Column(modifier = modifier) {
        OutlinedTextField(
            value = fieldValue,
            label = { Text(text = stringResource(id = fieldLabel)) },
            onValueChange = {
                onValueChanged(it)
                inputChanged.value = true
            },
            placeholder = { Text(text = stringResource(id = fieldPlaceholder)) },
            trailingIcon = { ExposedDropdownMenuDefaults.TrailingIcon(expanded = expanded) },
            singleLine = true,
            enabled = true,
            readOnly = readOnly,
            colors = ExposedDropdownMenuDefaults.textFieldColors(
                unfocusedContainerColor = MaterialTheme.colorScheme.surface
            ),
            isError = (isError && !isFocused.value),
            supportingText = {
                if (isError && !isFocused.value) {
                    Text(
                        text = stringResource(
                            id = supportingText
                        )
                    )
                }
            },
            keyboardOptions = keyboardOptions,
            keyboardActions = keyboardActions,
            interactionSource = interactionSource,
            modifier = Modifier.clickable(
                interactionSource = interactionSource,
                onClick = onTextFieldClicked,
                indication = null
            )
        )
        if (hideKeyboard) {
            focusManager.clearFocus()
            onFocusClear()
        }
    }
}

@Composable
fun TextFieldWithPlaceholderWithLabel(
    @StringRes fieldLabel: Int,
    @StringRes fieldPlaceholder: Int,
    fieldValue: String,
    onValueChanged: (String) -> Unit,
    modifier: Modifier = Modifier
) {
    /*var text by rememberSaveable(stateSaver = TextFieldValue.Saver) {
        mutableStateOf(TextFieldValue("example", TextRange(0, 7)))
    }*/
    TextField(
        value = fieldValue,
        label = { Text(stringResource(id = fieldLabel)) },
        onValueChange = { onValueChanged },
        singleLine = true,
        placeholder = { Text(text = stringResource(id = fieldPlaceholder)) }
    )
}

@Preview(showBackground = true)
@Composable
fun OutlinedTextFieldWithLeadingIcon() {
    Column {
        OutlinedTextFieldWithLeadingIcon(
            fieldLabel = R.string.components_forms_text_field_label_pet_name,
            fieldPlaceholder = R.string.components_forms_text_field_placeholder_pet_name,
            fieldValue = "",
            leadingIcon = R.drawable.baseline_pets_24,
            onValueChanged = {},
            onCancelClicked = {},
            supportingText = R.string.util_blank
        )
    }
}

@Preview(showBackground = true)
@Composable
fun TextFieldWithPlaceholderWithLabel() {
    TextFieldWithPlaceholderWithLabel(
        fieldLabel = R.string.components_forms_text_field_label_pet_name,
        fieldPlaceholder = R.string.components_forms_text_field_placeholder_pet_name,
        fieldValue = "",
        onValueChanged = {})
}