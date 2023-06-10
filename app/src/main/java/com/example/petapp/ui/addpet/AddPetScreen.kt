package com.example.petapp.ui.addpet

import android.annotation.SuppressLint
import androidx.annotation.StringRes
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.petapp.R
import com.example.petapp.ui.components.AddPetDataScaffold
import com.example.petapp.ui.components.ErrorScreen
import com.example.petapp.ui.components.LoadingScreen
import com.example.petapp.ui.components.forms.*

@Composable
fun AddPetScreen(
    viewModel: AddPetViewModel,
    navigateToDashboard: () -> Unit,
    modifier: Modifier
) {
    when (viewModel.uiState) {
        is AddPetUiState.Error -> ErrorScreen(message = "Can't add new pet")
        is AddPetUiState.Loading -> LoadingScreen()
        is AddPetUiState.Success -> AddPetResultScreen(viewModel, navigateToDashboard)
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AddPetResultScreen(viewModel: AddPetViewModel, navigateToDashboard: () -> Unit) {
    val uiState = viewModel.successUiState.collectAsState().value

    when (uiState.screenStage) {
        AddPetScreenStage.General ->
            AddPetDataScaffold(
                topAppBarTitleId = R.string.util_blank,
                headline = R.string.components_forms_title_new_pet,
                supportingText = R.string.components_forms_description_new_pet,
                rightButtonStringId = R.string.components_forms_dialog_buttons_next,
                onRightButtonClicked = viewModel::onGeneralDoneButtonClicked,
                navigateBack = navigateToDashboard,
                onLeftButtonClicked = navigateToDashboard,
                hideKeyboard = viewModel::hideKeyboard) {
                GeneralPetForm(
                    nameFieldValue = uiState.nameFieldValue,
                    onNameFieldValueChanged = viewModel::onNameFieldValueChanged,
                    onNameFieldCancelClicked = viewModel::onNameFieldCancelClicked,
                    datePickerTextFieldValue = uiState.datePickerTextFieldValue,
                    onDatePickerTextFieldValueChanged = viewModel::onDatePickerTextFieldValueChanged,
                    onDatePickerTextFieldClicked = viewModel::onDatePickerTextFieldClicked,
                    datePickerOpenDialog = uiState.datePickerOpenDialog,
                    datePickerState = uiState.datePickerState,
                    datePickerConfirmEnabled = uiState.datePickerConfirmEnabled,
                    datePickerOnDismissRequest = viewModel::datePickerOnDismissRequest,
                    datePickerOnConfirmedButtonClicked = viewModel::datePickerOnConfirmedButtonClicked,
                    datePickerOnDismissedButtonClicked = viewModel::datePickerOnDismissedButtonClicked,
                    speciesOptions = uiState.speciesMenuOptions,
                    speciesMenuExpanded = uiState.speciesMenuExpanded,
                    speciesMenuSelectedOption = uiState.speciesMenuSelectedOptionText,
                    speciesMenuOnExpandedChanged = viewModel::speciesMenuOnExpandedChanged,
                    speciesMenuOnDropdownMenuItemClicked = viewModel::speciesMenuOnDropdownMenuItemClicked,
                    speciesMenuOnDismissRequest = viewModel::speciesMenuOnDismissRequest,
                    breedOptions = uiState.breedMenuOptions,
                    breedMenuExpanded = uiState.breedMenuExpanded,
                    breedMenuEnabled = uiState.breedMenuEnabled,
                    breedMenuSelectedOption = uiState.breedMenuSelectedOptionText,
                    breedMenuOnExpandedChanged = viewModel::breedMenuOnExpandedChanged,
                    breedMenuOnDropdownMenuItemClicked = viewModel::breedMenuOnDropdownMenuItemClicked,
                    breedMenuOnDismissRequest = viewModel::breedMenuOnDismissRequest,
                    isKeyboardHide = uiState.hideKeyboard,
                    onFocusCleared = viewModel::onFocusCleared,
                    isNameValid = !uiState.isNameValid,
                    nameErrorMessage = uiState.nameErrorMessage,
                    isBirthdateValid = !uiState.isBirthDateValid,
                    birthDateErrorMessage = uiState.birtDateErrorMessage)
            }
        AddPetScreenStage.Dimensions ->
            AddPetDataScaffold(
                topAppBarTitleId = R.string.util_blank,
                headline = R.string.components_forms_title_new_pet,
                supportingText = R.string.components_forms_description_new_pet,
                leftButtonStringId = R.string.components_forms_dialog_buttons_previous ,
                rightButtonStringId = R.string.components_forms_dialog_buttons_next,
                onRightButtonClicked = viewModel::onDimensionsDoneButtonClicked,
                navigateBack = navigateToDashboard,
                onLeftButtonClicked = {viewModel.onNavigateButtonClicked(AddPetScreenStage.General)},
                hideKeyboard = viewModel::hideKeyboard) {
                DimensionsPetForm(
                    weightFieldValue = uiState.weightFieldValue,
                    onWeightFieldValueChanged = viewModel::onWeightFieldValueChanged,
                    onWeightFieldCancelClicked = viewModel::onWeightFieldCancelClicked,
                    heightFieldValue = uiState.heightFieldValue,
                    onHeightFieldValueChanged = viewModel::onHeightFieldValueChanged,
                    onHeightFieldCancelClicked = viewModel::onHeightFieldCancelClicked,
                    lengthFieldValue = uiState.lengthFieldValue,
                    onLengthFieldValueChanged = viewModel::onLengthFieldValueChanged,
                    onLengthFieldCancelClicked = viewModel::onLengthFieldCancelClicked,
                    circuitFieldValue = uiState.circuitFieldValue,
                    onCircuitFieldValueChanged = viewModel::onCircuitFieldValueChanged,
                    onCircuitFieldCancelClicked = viewModel::onCircuitFieldCancelClicked,
                    isKeyboardHide = uiState.hideKeyboard,
                    onFocusCleared = viewModel::onFocusCleared,
                    isWeightValid = !uiState.isWeightValid,
                    isHeightValid = !uiState.isHeightValid,
                    isLengthValid = !uiState.isLengthValid,
                    isCircuitValid = !uiState.isCircuitValid,
                    weightErrorMessage = uiState.weightErrorMessage,
                    heightErrorMessage = uiState.heightErrorMessage,
                    lengthErrorMessage = uiState.lengthErrorMessage,
                    circuitErrorMessage = uiState.circuitErrorMessage)
            }
        AddPetScreenStage.Final -> AddPetDataScaffold(
            topAppBarTitleId = R.string.util_blank,
            headline = R.string.components_forms_title_new_pet,
            supportingText = R.string.components_forms_description_new_pet,
            leftButtonStringId = R.string.components_forms_dialog_buttons_previous ,
            rightButtonStringId = R.string.components_forms_dialog_buttons_done,
            onRightButtonClicked = { if (viewModel.onDoneButtonClicked()) navigateToDashboard() },
            navigateBack = navigateToDashboard,
            onLeftButtonClicked = {viewModel.onNavigateButtonClicked(AddPetScreenStage.Dimensions)},
            hideKeyboard = viewModel::hideKeyboard) {
            AdditionalInfoPetForm(
                descriptionTextFieldValue = uiState.descriptionFieldValue,
                onDescriptionTextFieldValueChanged = viewModel::onDescriptionTextFieldValueChanged)
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun GeneralPetForm(
    nameFieldValue: String,
    onNameFieldValueChanged: (String) -> Unit,
    onNameFieldCancelClicked: () -> Unit,
    datePickerTextFieldValue: String,
    onDatePickerTextFieldValueChanged: (String) -> Unit,
    onDatePickerTextFieldClicked: () -> Unit,
    datePickerOpenDialog: Boolean,
    datePickerState: DatePickerState,
    datePickerConfirmEnabled: Boolean,
    datePickerOnDismissRequest: () -> Unit,
    datePickerOnConfirmedButtonClicked: () -> Unit,
    datePickerOnDismissedButtonClicked: () -> Unit,
    speciesOptions: List<String>,
    speciesMenuExpanded: Boolean,
    speciesMenuSelectedOption: String,
    speciesMenuOnExpandedChanged: () -> Unit,
    speciesMenuOnDropdownMenuItemClicked: (String) -> Unit,
    speciesMenuOnDismissRequest: () -> Unit,
    breedOptions: List<String>,
    breedMenuExpanded: Boolean,
    breedMenuEnabled: Boolean,
    breedMenuSelectedOption: String,
    breedMenuOnExpandedChanged: () -> Unit,
    breedMenuOnDropdownMenuItemClicked: (String) -> Unit,
    breedMenuOnDismissRequest: () -> Unit,
    isKeyboardHide: Boolean,
    onFocusCleared: () -> Unit,
    isNameValid: Boolean,
    @StringRes nameErrorMessage: Int,
    isBirthdateValid: Boolean,
    @StringRes birthDateErrorMessage: Int

    ) {
    PetFormsSubHeadline(headlineStringId = R.string.components_forms_title_new_pet_general)
    OutlinedTextFieldWithLeadingIcon(
                fieldLabel = R.string.components_forms_text_field_label_pet_name,
                fieldPlaceholder = R.string.components_forms_text_field_placeholder_pet_name,
                leadingIcon = R.drawable.baseline_pets_24,
                fieldValue = nameFieldValue,
                onValueChanged = onNameFieldValueChanged,
                onCancelClicked = onNameFieldCancelClicked,
                hideKeyboard = isKeyboardHide,
                onFocusClear = onFocusCleared,
                isError = isNameValid,
                supportingText = nameErrorMessage
            )
            DatePicker(
                label = R.string.components_forms_text_field_label_pet_birth_date,
                value = datePickerTextFieldValue,
                onValueChange = onDatePickerTextFieldValueChanged,
                onTextFieldClicked = onDatePickerTextFieldClicked,
                openDialog = datePickerOpenDialog,
                datePickerState = datePickerState,
                confirmEnabled = datePickerConfirmEnabled,
                onDismissRequest = datePickerOnDismissRequest,
                onConfirmedButtonClicked = datePickerOnConfirmedButtonClicked,
                onDismissButtonClicked = datePickerOnDismissedButtonClicked,
                isError = isBirthdateValid,
                supportingText = birthDateErrorMessage
            )
            ExposedDropdownMenu(
                label = R.string.components_forms_text_field_label_pet_species,
                options = speciesOptions,
                expanded = speciesMenuExpanded,
                selectedOption = speciesMenuSelectedOption,
                onExpandedChange = speciesMenuOnExpandedChanged,
                onDropdownMenuItemClicked = speciesMenuOnDropdownMenuItemClicked,
                onDismissRequest = speciesMenuOnDismissRequest
            )
            ExposedDropdownMenu(
                label = R.string.components_forms_text_field_label_pet_breed,
                options = breedOptions,
                expanded = breedMenuExpanded,
                enabled = breedMenuEnabled,
                selectedOption = breedMenuSelectedOption,
                onExpandedChange = breedMenuOnExpandedChanged,
                onDropdownMenuItemClicked = breedMenuOnDropdownMenuItemClicked,
                onDismissRequest = breedMenuOnDismissRequest
            )
}

@Composable
private fun PetFormsSubHeadline(
    @StringRes headlineStringId: Int
) {
    Text(
        text = stringResource(headlineStringId),
        style = MaterialTheme.typography.headlineSmall
    )
    Spacer(modifier = Modifier.padding(10.dp))
}

@Composable
fun DimensionsPetForm(
    weightFieldValue: String,
    onWeightFieldValueChanged: (String) -> Unit,
    onWeightFieldCancelClicked: () -> Unit,
    heightFieldValue: String,
    onHeightFieldValueChanged: (String) -> Unit,
    onHeightFieldCancelClicked: () -> Unit,
    lengthFieldValue: String,
    onLengthFieldValueChanged: (String) -> Unit,
    onLengthFieldCancelClicked: () -> Unit,
    circuitFieldValue: String,
    onCircuitFieldValueChanged: (String) -> Unit,
    onCircuitFieldCancelClicked: () -> Unit,
    isKeyboardHide: Boolean,
    onFocusCleared: () -> Unit,
    isWeightValid: Boolean,
    isHeightValid: Boolean,
    isLengthValid: Boolean,
    isCircuitValid: Boolean,
    @StringRes weightErrorMessage: Int,
    @StringRes heightErrorMessage: Int,
    @StringRes lengthErrorMessage: Int,
    @StringRes circuitErrorMessage: Int,

) {
        PetFormsSubHeadline(headlineStringId = R.string.components_forms_title_new_pet_dimensions)
            OutlinedTextFieldWithLeadingIcon(
                fieldLabel = R.string.components_forms_text_field_label_pet_weight,
                fieldPlaceholder = R.string.util_unit_weight,
                leadingIcon = R.drawable.weight_24,
                fieldValue = weightFieldValue,
                onValueChanged = onWeightFieldValueChanged,
                onCancelClicked = onWeightFieldCancelClicked,
                onFocusClear = onFocusCleared,
                keyboardOptions = KeyboardOptions(
                    keyboardType = KeyboardType.Number,
                    imeAction = ImeAction.Next
                ),
                hideKeyboard = isKeyboardHide,
                isError = isWeightValid,
                supportingText = weightErrorMessage
            )
            OutlinedTextFieldWithLeadingIcon(
                fieldLabel = R.string.components_forms_text_field_label_pet_height,
                fieldPlaceholder = R.string.util_unit_dimension,
                leadingIcon = R.drawable.baseline_height_24,
                fieldValue = heightFieldValue,
                onValueChanged = onHeightFieldValueChanged,
                onCancelClicked = onHeightFieldCancelClicked,
                onFocusClear = onFocusCleared,
                keyboardOptions = KeyboardOptions(
                    keyboardType = KeyboardType.Number,
                    imeAction = ImeAction.Next
                ),
                hideKeyboard = isKeyboardHide,
                isError = isHeightValid,
                supportingText = heightErrorMessage
            )
            OutlinedTextFieldWithLeadingIcon(
                fieldLabel = R.string.components_forms_text_field_label_pet_width,
                fieldPlaceholder = R.string.util_unit_dimension,
                leadingIcon = R.drawable.width_24,
                fieldValue = lengthFieldValue,
                onValueChanged = onLengthFieldValueChanged,
                onCancelClicked = onLengthFieldCancelClicked,
                onFocusClear = onFocusCleared,
                keyboardOptions = KeyboardOptions(
                    keyboardType = KeyboardType.Number,
                    imeAction = ImeAction.Next
                ),
                hideKeyboard = isKeyboardHide,
                isError = isLengthValid,
                supportingText = lengthErrorMessage
            )
            OutlinedTextFieldWithLeadingIcon(
                fieldLabel = R.string.components_forms_text_field_label_pet_circuit,
                fieldPlaceholder = R.string.util_unit_dimension,
                leadingIcon = R.drawable.restart_alt_24,
                fieldValue = circuitFieldValue,
                onValueChanged = onCircuitFieldValueChanged,
                onCancelClicked = onCircuitFieldCancelClicked,
                onFocusClear = onFocusCleared,
                keyboardOptions = KeyboardOptions(
                    keyboardType = KeyboardType.Number,
                    imeAction = ImeAction.Done
                ),
                hideKeyboard = isKeyboardHide,
                isError = isCircuitValid,
                supportingText = circuitErrorMessage
            )
}

@Composable
fun AdditionalInfoPetForm(
    descriptionTextFieldValue: String,
    onDescriptionTextFieldValueChanged: (String) -> Unit,
    modifier: Modifier = Modifier
) {
    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = Modifier.width(IntrinsicSize.Max)
    ) {
        PetFormsSubHeadline(headlineStringId = R.string.components_forms_title_new_pet_extras)
        Column(
            modifier = modifier
        ) {
            OutlinedTextField(
                value = descriptionTextFieldValue,
                label = { Text(text = stringResource(R.string.components_forms_text_field_label_description)) },
                placeholder = { Text(text = stringResource(R.string.components_forms_text_field_placeholder_description)) },
                onValueChange = onDescriptionTextFieldValueChanged,
                maxLines = 8
            )
        }
    }
}

@SuppressLint("UnrememberedMutableState")
@OptIn(ExperimentalMaterial3Api::class)
@Preview(showBackground = true)
@Composable
fun GeneralPetFormPrev() {
    val value = remember { mutableStateOf("") }
    val openDialog = remember { mutableStateOf(false) }
    val datePickerState = rememberDatePickerState()
    val confirmEnabled = derivedStateOf { datePickerState.selectedDateMillis != null }
    val speciesMenuOptions = listOf("Dog", "Cat", "Fish", "Parrot")
    var speciesMenuExpanded by remember { mutableStateOf(false) }
    var speciesMenuSelectedOptionText by remember { mutableStateOf("") }
    val breedMenuOptions = listOf("Dog", "Cat", "Fish", "Parrot")
    var breedMenuExpanded by remember { mutableStateOf(false) }
    var breedMenuSelectedOptionText by remember { mutableStateOf("") }
    val breedMenuEnabled by remember { mutableStateOf(false) }

    Column(
        horizontalAlignment = Alignment.CenterHorizontally, modifier = Modifier
            .fillMaxSize()
            .padding(60.dp)
    ) {
        Text(
            text = stringResource(R.string.components_forms_title_new_pet),
            style = MaterialTheme.typography.headlineMedium
        )
        Text(
            text = stringResource(R.string.components_forms_description_new_pet),
            style = MaterialTheme.typography.bodySmall
        )
        Spacer(modifier = Modifier.padding(20.dp))
        GeneralPetForm(
            nameFieldValue = value.value,
            onNameFieldValueChanged = { value.value = it },
            onNameFieldCancelClicked = { value.value = "" },
            datePickerTextFieldValue = "",
            onDatePickerTextFieldValueChanged = { /*TODO*/ },
            onDatePickerTextFieldClicked = { openDialog.value = true },
            datePickerOpenDialog = openDialog.value,
            datePickerState = datePickerState,
            datePickerConfirmEnabled = confirmEnabled.value,
            datePickerOnDismissRequest = { openDialog.value = false },
            datePickerOnConfirmedButtonClicked = { openDialog.value = false },
            datePickerOnDismissedButtonClicked = { openDialog.value = false },
            speciesOptions = speciesMenuOptions,
            speciesMenuExpanded = speciesMenuExpanded,
            speciesMenuSelectedOption = speciesMenuSelectedOptionText,
            speciesMenuOnExpandedChanged = { speciesMenuExpanded = !speciesMenuExpanded },
            speciesMenuOnDropdownMenuItemClicked = {
                speciesMenuSelectedOptionText = it
                speciesMenuExpanded = false
            },
            speciesMenuOnDismissRequest = { speciesMenuExpanded = false },
            breedOptions = breedMenuOptions,
            breedMenuExpanded = breedMenuExpanded,
            breedMenuEnabled = breedMenuEnabled,
            breedMenuSelectedOption = breedMenuSelectedOptionText,
            breedMenuOnExpandedChanged = { breedMenuExpanded = !breedMenuExpanded },
            breedMenuOnDropdownMenuItemClicked = {
                breedMenuSelectedOptionText = it
                breedMenuExpanded = false
            },
            breedMenuOnDismissRequest = { breedMenuExpanded = false },
            isKeyboardHide = false,
            onFocusCleared = {},
            isNameValid = false,
            nameErrorMessage = R.string.util_blank,
            isBirthdateValid = true,
            birthDateErrorMessage = R.string.util_blank
        )
    }
}

@Preview(showBackground = true)
@Composable
fun DimensionsPetFormPrev() {
    val weightValue = remember { mutableStateOf("") }
    val heightValue = remember { mutableStateOf("") }
    val lengthValue = remember { mutableStateOf("") }
    val circuitValue = remember { mutableStateOf("") }

    Column(
        horizontalAlignment = Alignment.CenterHorizontally, modifier = Modifier
            .fillMaxSize()
            .padding(60.dp)
    ) {
        Text(
            text = stringResource(R.string.components_forms_title_new_pet),
            style = MaterialTheme.typography.headlineMedium
        )
        Text(
            text = stringResource(R.string.components_forms_description_new_pet),
            style = MaterialTheme.typography.bodySmall
        )
        Spacer(modifier = Modifier.padding(20.dp))
        DimensionsPetForm(
            weightFieldValue = weightValue.value,
            onWeightFieldValueChanged = { weightValue.value = it },
            onWeightFieldCancelClicked = { weightValue.value = "" },
            heightFieldValue = heightValue.value,
            onHeightFieldValueChanged = { heightValue.value = it },
            onHeightFieldCancelClicked = { heightValue.value = "" },
            lengthFieldValue = lengthValue.value,
            onLengthFieldValueChanged = { lengthValue.value = it },
            onLengthFieldCancelClicked = { lengthValue.value = "" },
            circuitFieldValue = circuitValue.value,
            onCircuitFieldValueChanged = { circuitValue.value = it },
            onCircuitFieldCancelClicked = { circuitValue.value = "" },
            isKeyboardHide = false,
            onFocusCleared = {},
            isWeightValid = false,
            isHeightValid = false,
            isLengthValid = false,
            isCircuitValid = false,
            weightErrorMessage = R.string.util_blank,
            heightErrorMessage = R.string.util_blank,
            lengthErrorMessage = R.string.util_blank,
            circuitErrorMessage = R.string.util_blank
        )
    }
}

@Preview(showBackground = true)
@Composable
fun AdditionalInfoPetFormPrev() {
    val descriptionValue = remember { mutableStateOf("") }

    Column(
        horizontalAlignment = Alignment.CenterHorizontally, modifier = Modifier
            .fillMaxSize()
            .padding(60.dp)
    ) {
        Text(
            text = stringResource(R.string.components_forms_title_new_pet),
            style = MaterialTheme.typography.headlineMedium
        )
        Text(
            text = stringResource(R.string.components_forms_description_new_pet),
            style = MaterialTheme.typography.bodySmall
        )
        Spacer(modifier = Modifier.padding(20.dp))
        AdditionalInfoPetForm(
            descriptionTextFieldValue = descriptionValue.value,
            onDescriptionTextFieldValueChanged = { descriptionValue.value = it })
    }
}
