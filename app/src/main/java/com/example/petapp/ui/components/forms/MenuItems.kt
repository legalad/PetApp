package com.example.petapp.ui.components.forms

import androidx.annotation.StringRes
import androidx.compose.foundation.layout.Column
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import com.example.android.datastore.UserPreferences
import com.example.petapp.R


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ExposedDropdownMenu(
    @StringRes label: Int,
    options: List<String>,
    expanded: Boolean,
    selectedOption: String,
    onExpandedChange: () -> Unit,
    onDropdownMenuItemClicked: (String) -> Unit,
    onDismissRequest: () -> Unit,
    modifier: Modifier = Modifier,
    enabled: Boolean = true,
    readOnly: Boolean = true,
    textFieldOnValueChanged: (String) -> Unit = {}
) {

    ExposedDropdownMenuBox(
        expanded = expanded,
        onExpandedChange = { onExpandedChange() }
    ) {
        Column (modifier = modifier) {
            OutlinedTextField(
                label = { Text(text = stringResource(id = label)) },
                value = selectedOption,
                onValueChange = { textFieldOnValueChanged(it) },
                enabled = enabled,
                modifier = Modifier.menuAnchor(),
                readOnly = readOnly,
                trailingIcon = { ExposedDropdownMenuDefaults.TrailingIcon(expanded = expanded) },
                colors = ExposedDropdownMenuDefaults.textFieldColors(
                    unfocusedContainerColor = MaterialTheme.colorScheme.surface
                ),
            )
        }
        ExposedDropdownMenu(
            expanded = expanded,
            onDismissRequest = onDismissRequest
        ) {
            options.forEach { item ->
                DropdownMenuItem(
                    text = { Text(item) },
                    onClick = {onDropdownMenuItemClicked(item)},
                    contentPadding = ExposedDropdownMenuDefaults.ItemContentPadding
                )
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SettingsExposedDropdownMenu(
    @StringRes label: Int,
    expanded: Boolean,
    selectedOption: UserPreferences.Language,
    onExpandedChange: () -> Unit,
    onDropdownMenuItemClicked: (UserPreferences.Language) -> Unit,
    onDismissRequest: () -> Unit,
    modifier: Modifier = Modifier,
    enabled: Boolean = true,
    readOnly: Boolean = true,
    textFieldOnValueChanged: (String) -> Unit = {}
) {
    val options = UserPreferences.Language.values().dropLast(1)

    ExposedDropdownMenuBox(
        expanded = expanded,
        onExpandedChange = { onExpandedChange() }
    ) {
        Column (modifier = modifier) {
            OutlinedTextField(
                label = { Text(text = stringResource(id = label)) },
                value = selectedOption.name.lowercase(),
                onValueChange = { textFieldOnValueChanged(it) },
                enabled = enabled,
                modifier = Modifier.menuAnchor(),
                readOnly = readOnly,
                trailingIcon = { ExposedDropdownMenuDefaults.TrailingIcon(expanded = expanded) },
                colors = ExposedDropdownMenuDefaults.textFieldColors(
                    unfocusedContainerColor = MaterialTheme.colorScheme.surface
                ),
            )
        }
        ExposedDropdownMenu(
            expanded = expanded,
            onDismissRequest = { onDismissRequest() }
        ) {
            options.forEach { item ->
                DropdownMenuItem(
                    text = { Text(item.name.lowercase()) },
                    onClick = {onDropdownMenuItemClicked(item)
                              },
                    contentPadding = ExposedDropdownMenuDefaults.ItemContentPadding
                )
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SettingsExposedDropdownMenu(
    @StringRes label: Int,
    expanded: Boolean,
    selectedOption: UserPreferences.Unit,
    onExpandedChange: () -> Unit,
    onDropdownMenuItemClicked: (UserPreferences.Unit) -> Unit,
    onDismissRequest: () -> Unit,
    modifier: Modifier = Modifier,
    enabled: Boolean = true,
    readOnly: Boolean = true,
    textFieldOnValueChanged: (String) -> Unit = {}
) {
    val options = UserPreferences.Unit.values().dropLast(1)

    ExposedDropdownMenuBox(
        expanded = expanded,
        onExpandedChange = { onExpandedChange() }
    ) {
        Column (modifier = modifier) {
            OutlinedTextField(
                label = { Text(text = stringResource(id = label)) },
                value = selectedOption.name.lowercase(),
                onValueChange = { textFieldOnValueChanged(it) },
                enabled = enabled,
                modifier = Modifier.menuAnchor(),
                readOnly = readOnly,
                trailingIcon = { ExposedDropdownMenuDefaults.TrailingIcon(expanded = expanded) },
                colors = ExposedDropdownMenuDefaults.textFieldColors(
                    unfocusedContainerColor = MaterialTheme.colorScheme.surface
                ),
            )
        }
        ExposedDropdownMenu(
            expanded = expanded,
            onDismissRequest = { onDismissRequest()}
        ) {
            options.forEach { item ->
                DropdownMenuItem(
                    text = { Text(item.name.lowercase()) },
                    onClick = {onDropdownMenuItemClicked(item)
                    },
                    contentPadding = ExposedDropdownMenuDefaults.ItemContentPadding
                )
            }
        }
    }
}

@Preview
@Composable
fun ExposedDropdownMenuPrev() {
    val options = listOf("Dog", "Cat", "Fish", "Parrot")
    var expanded by remember { mutableStateOf(false) }
    var selectedOptionText by remember { mutableStateOf(options[0]) }

    ExposedDropdownMenu(
        label = R.string.pet_species,
        options = options,
        expanded = expanded,
        enabled = true,
        selectedOption = selectedOptionText,
        onExpandedChange = { expanded = !expanded },
        onDropdownMenuItemClicked = {
            selectedOptionText = it
            expanded = false
        },
        onDismissRequest = { expanded = false })

}

@Preview
@Composable
fun EditableExposedDropdownMenuPrev() {
    val options = listOf("Dog", "Cat", "Fish", "Parrot")
    var expanded by remember { mutableStateOf(false) }
    var selectedOptionText by remember { mutableStateOf(options[0]) }

    ExposedDropdownMenu(
        label = R.string.pet_species,
        options = options,
        expanded = expanded,
        enabled = true,
        selectedOption = selectedOptionText,
        onExpandedChange = { expanded = !expanded },
        onDropdownMenuItemClicked = {
            selectedOptionText = it
            expanded = false
        },
        onDismissRequest = { expanded = false },
        readOnly = false,
        textFieldOnValueChanged = { selectedOptionText = it
            if (!expanded) expanded = true
        }
    )

}
