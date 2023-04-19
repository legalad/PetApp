package com.example.petapp.ui.components.forms

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
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.tooling.preview.Preview
import com.example.petapp.R


@Composable
fun OutlinedTextFieldWithPlaceholderAndLabelAndCancel(
    @StringRes fieldLabel: Int,
    @StringRes fieldPlaceholder: Int,
    fieldValue: String,
    onValueChanged: (TextFieldValue) -> Unit,
    onCancelClicked: () -> Unit,
    onFocusClear: () -> Unit = {},
    hideKeyboard: Boolean = false,
    focusManager: FocusManager = LocalFocusManager.current,
    keyboardOptions: KeyboardOptions = KeyboardOptions(imeAction = ImeAction.Next),
    keyboardActions: KeyboardActions = KeyboardActions(onSearch = {
        focusManager.clearFocus()
    }),
    modifier: Modifier = Modifier
) {
    /*var text by rememberSaveable(stateSaver = TextFieldValue.Saver) {
        mutableStateOf(TextFieldValue("example", TextRange(0, 7)))
    }*/
    OutlinedTextField(
        value = fieldValue,
        label = { Text(text = stringResource(id = fieldLabel)) },
        onValueChange = { onValueChanged },
        placeholder = { Text(text = stringResource(id = fieldPlaceholder)) },
        trailingIcon = {
            if (fieldValue.isNotBlank()) IconButton(onClick = onCancelClicked) {
                Icon(
                    painter = painterResource(id = R.drawable.round_cancel_24),
                    contentDescription = stringResource(
                        R.string.cancel
                    )
                )
            } else null
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
    onValueChanged: (TextFieldValue) -> Unit,
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
fun TextFieldWithPlaceholderWithoutLabel() {
    Column {
        OutlinedTextFieldWithPlaceholderAndLabelAndCancel(
            fieldLabel = R.string.pet_name,
            fieldPlaceholder = R.string.pet_name_placeholder,
            fieldValue = "",
            onValueChanged = {},
            onCancelClicked = {})
        OutlinedTextFieldWithPlaceholderAndLabelAndCancel(
            fieldLabel = R.string.pet_name,
            fieldPlaceholder = R.string.pet_name_placeholder,
            fieldValue = "zeus",
            onValueChanged = {},
            onCancelClicked = {})
        OutlinedTextFieldWithPlaceholderAndLabelAndCancel(
            fieldLabel = R.string.pet_name,
            fieldPlaceholder = R.string.pet_name_placeholder,
            fieldValue = "",
            onValueChanged = {},
            onCancelClicked = {})
    }
}

@Preview(showBackground = true)
@Composable
fun TextFieldWithPlaceholderWithLabel() {
    TextFieldWithPlaceholderWithLabel(fieldLabel = R.string.pet_name, fieldPlaceholder = R.string.pet_name_placeholder, fieldValue = "", onValueChanged = {})
}