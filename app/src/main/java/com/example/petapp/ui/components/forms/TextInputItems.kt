package com.example.petapp.ui.components.forms

import androidx.annotation.DrawableRes
import androidx.annotation.StringRes
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusManager
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.tooling.preview.Preview
import com.example.petapp.R


@Composable
fun OutlinedTextFieldWithLeadingIcon(
    @StringRes fieldLabel: Int,
    @StringRes fieldPlaceholder: Int,
    @DrawableRes leadingIcon: Int,
    fieldValue: String,
    onValueChanged: (String) -> Unit,
    onCancelClicked: () -> Unit,
    modifier: Modifier = Modifier,
    onFocusClear: () -> Unit = {},
    hideKeyboard: Boolean = false,
    focusManager: FocusManager = LocalFocusManager.current,
    keyboardOptions: KeyboardOptions = KeyboardOptions(imeAction = ImeAction.Next),
    keyboardActions: KeyboardActions = KeyboardActions(onSearch = {
        focusManager.clearFocus()
    })
) {
    /*var text by rememberSaveable(stateSaver = TextFieldValue.Saver) {
        mutableStateOf(TextFieldValue("example", TextRange(0, 7)))
    }*/
    OutlinedTextField(
        value = fieldValue,
        label = { Text(text = stringResource(id = fieldLabel)) },
        onValueChange = onValueChanged,
        placeholder = { Text(text = stringResource(id = fieldPlaceholder)) },
        leadingIcon = {
                Icon(
                    painter =  painterResource(id = leadingIcon),
                    contentDescription = null
                )
        },
        trailingIcon = {
            if (fieldValue.isNotBlank()) IconButton(onClick = onCancelClicked) {
                Icon(
                    painter = painterResource(id = R.drawable.round_cancel_24),
                    contentDescription = stringResource(
                        R.string.cancel
                    )
                )
            }
        },
        singleLine = true,
        keyboardOptions = keyboardOptions,
        keyboardActions = keyboardActions,
        modifier = modifier
    )
    if (hideKeyboard) {
        focusManager.clearFocus()
        onFocusClear()
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
            fieldLabel = R.string.pet_name,
            fieldPlaceholder = R.string.pet_name_placeholder,
            fieldValue = "",
            leadingIcon = R.drawable.baseline_pets_24,
            onValueChanged = {},
            onCancelClicked = {})
    }
}

@Preview(showBackground = true)
@Composable
fun TextFieldWithPlaceholderWithLabel() {
    TextFieldWithPlaceholderWithLabel(fieldLabel = R.string.pet_name, fieldPlaceholder = R.string.pet_name_placeholder, fieldValue = "", onValueChanged = {})
}