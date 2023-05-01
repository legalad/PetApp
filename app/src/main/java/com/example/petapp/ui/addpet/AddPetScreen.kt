package com.example.petapp.ui.addpet

import android.annotation.SuppressLint
import androidx.annotation.StringRes
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
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
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import com.example.petapp.R
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
    val interactionSource = remember { MutableInteractionSource() }
    val contentModifier: Modifier = Modifier
        .height(340.dp)
        .wrapContentHeight(Alignment.CenterVertically)
    val textFieldHeight: Dp = 90.dp

    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Top,
        modifier = Modifier
            .fillMaxSize()
            .clickable(
                onClick = {
                    viewModel.hideKeyboard()
                },
                interactionSource = interactionSource,
                indication = null
            )
    ) {
        Spacer(modifier = Modifier.padding(30.dp))
        Text(
            text = stringResource(R.string.pet_form_headline),
            style = MaterialTheme.typography.headlineMedium
        )
        Text(
            text = stringResource(R.string.pet_form_description),
            style = MaterialTheme.typography.bodySmall
        )
        Spacer(modifier = Modifier.padding(20.dp))
    when (uiState.screenStage) {
        is AddPetScreenStage.General -> GeneralPetForm(
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
            onCancelButtonClicked = { navigateToDashboard() },
            onNextButtonClicked = viewModel::onGeneralDoneButtonClicked,
            isKeyboardHide = uiState.hideKeyboard,
            onFocusCleared = viewModel::onFocusCleared,
            isNameValid = !uiState.isNameValid,
            nameErrorMessage = uiState.nameErrorMessage,
            modifier = contentModifier,
            textFieldHeight = textFieldHeight,
            isBirthdateValid = !uiState.isBirthDateValid,
            birthDateErrorMessage = uiState.birtDateErrorMessage)
        is AddPetScreenStage.Dimensions -> DimensionsPetForm(
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
            onPrevButtonClicked = viewModel::onNavigateButtonClicked,
            onNextButtonClicked = viewModel::onDimensionsDoneButtonClicked,
            isKeyboardHide = uiState.hideKeyboard,
            onFocusCleared = viewModel::onFocusCleared,
            isWeightValid = !uiState.isWeightValid,
            isHeightValid = !uiState.isHeightValid,
            isLengthValid = !uiState.isLengthValid,
            isCircuitValid = !uiState.isCircuitValid,
            weightErrorMessage = uiState.weightErrorMessage,
            heightErrorMessage = uiState.heightErrorMessage,
            lengthErrorMessage = uiState.lengthErrorMessage,
            circuitErrorMessage = uiState.circuitErrorMessage,
            modifier = contentModifier,
            textFieldHeight = textFieldHeight)
        is AddPetScreenStage.Final -> AdditionalInfoPetForm(
            descriptionTextFieldValue = uiState.descriptionFieldValue,
            onDescriptionTextFieldValueChanged = viewModel::onDescriptionTextFieldValueChanged,
            onPrevButtonClicked = viewModel::onNavigateButtonClicked,
            onDoneButtonClicked = viewModel::onDoneButtonClicked,
            navigateToDashboard = navigateToDashboard,
            modifier = contentModifier)
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
    onCancelButtonClicked: () -> Unit,
    onNextButtonClicked: () -> Unit,
    isKeyboardHide: Boolean,
    onFocusCleared: () -> Unit,
    isNameValid: Boolean,
    @StringRes nameErrorMessage: Int,
    isBirthdateValid: Boolean,
    @StringRes birthDateErrorMessage: Int,
    textFieldHeight: Dp,
    modifier: Modifier = Modifier,

) {
    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = Modifier.width(IntrinsicSize.Max)
    ) {
        PetFormsSubHeadline(headlineStringId = R.string.pet_form_subheadline_general)
        Column(
            modifier = modifier
        ) {
            OutlinedTextFieldWithLeadingIcon(
                fieldLabel = R.string.pet_name,
                fieldPlaceholder = R.string.pet_name_placeholder,
                leadingIcon = R.drawable.baseline_pets_24,
                fieldValue = nameFieldValue,
                onValueChanged = onNameFieldValueChanged,
                onCancelClicked = onNameFieldCancelClicked,
                hideKeyboard = isKeyboardHide,
                onFocusClear = onFocusCleared,
                isError = isNameValid,
                supportingText = nameErrorMessage,
                modifier = Modifier.height(textFieldHeight)
            )
            DatePicker(
                label = R.string.birth_date,
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
                supportingText = birthDateErrorMessage,
                modifier = Modifier.height(textFieldHeight)
            )
            ExposedDropdownMenu(
                label = R.string.pet_species,
                options = speciesOptions,
                expanded = speciesMenuExpanded,
                selectedOption = speciesMenuSelectedOption,
                onExpandedChange = speciesMenuOnExpandedChanged,
                onDropdownMenuItemClicked = speciesMenuOnDropdownMenuItemClicked,
                onDismissRequest = speciesMenuOnDismissRequest,
                modifier = Modifier.height(textFieldHeight)
            )
            ExposedDropdownMenu(
                label = R.string.pet_breed,
                options = breedOptions,
                expanded = breedMenuExpanded,
                enabled = breedMenuEnabled,
                selectedOption = breedMenuSelectedOption,
                onExpandedChange = breedMenuOnExpandedChanged,
                onDropdownMenuItemClicked = breedMenuOnDropdownMenuItemClicked,
                onDismissRequest = breedMenuOnDismissRequest,
                modifier = Modifier.height(textFieldHeight)
            )
        }
        PetFormsBottomNavButtons(
            leftButtonStringId = R.string.cancel,
            rightButtonStringId = R.string.next,
            onLeftButtonClicked = onCancelButtonClicked,
            onRightButtonClicked = { onNextButtonClicked() }
        )
    }
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
private fun PetFormsBottomNavButtons(
    @StringRes leftButtonStringId: Int,
    @StringRes rightButtonStringId: Int,
    onLeftButtonClicked: () -> Unit,
    onRightButtonClicked: () -> Unit
) {
    Spacer(modifier = Modifier.padding(20.dp))
    Row(horizontalArrangement = Arrangement.SpaceEvenly, modifier = Modifier.fillMaxWidth()) {
        Button(onClick = onLeftButtonClicked, modifier = Modifier.weight(1f)) {
            Text(text = stringResource(id = leftButtonStringId))
        }
        Spacer(modifier = Modifier.weight(0.1f))
        Button(onClick = onRightButtonClicked, modifier = Modifier.weight(1f)) {
            Text(text = stringResource(id = rightButtonStringId))
        }
    }
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
    onPrevButtonClicked: (stage: AddPetScreenStage) -> Unit,
    onNextButtonClicked: () -> Unit,
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
    textFieldHeight: Dp,
    modifier: Modifier = Modifier

    ) {
    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = Modifier.width(IntrinsicSize.Max)
    ) {
        PetFormsSubHeadline(headlineStringId = R.string.pet_form_subheadline_dimensions)
        Column(
            modifier = modifier
        ) {
            OutlinedTextFieldWithLeadingIcon(
                fieldLabel = R.string.pet_weight,
                fieldPlaceholder = R.string.pet_weight_unit,
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
                supportingText = weightErrorMessage,
                modifier = Modifier.height(textFieldHeight)
            )
            OutlinedTextFieldWithLeadingIcon(
                fieldLabel = R.string.pet_height,
                fieldPlaceholder = R.string.pet_dimensions_unit,
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
                supportingText = heightErrorMessage,
                modifier = Modifier.height(textFieldHeight)
            )
            OutlinedTextFieldWithLeadingIcon(
                fieldLabel = R.string.pet_width,
                fieldPlaceholder = R.string.pet_dimensions_unit,
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
                supportingText = lengthErrorMessage,
                modifier = Modifier.height(textFieldHeight)
            )
            OutlinedTextFieldWithLeadingIcon(
                fieldLabel = R.string.pet_circuit,
                fieldPlaceholder = R.string.pet_dimensions_unit,
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
                supportingText = circuitErrorMessage,
                modifier = Modifier.height(textFieldHeight)
            )
        }
        PetFormsBottomNavButtons(
            leftButtonStringId = R.string.previous,
            rightButtonStringId = R.string.next,
            onLeftButtonClicked = { onPrevButtonClicked(AddPetScreenStage.General) },
            onRightButtonClicked = { onNextButtonClicked() }
        )
    }
}

@Composable
fun AdditionalInfoPetForm(
    descriptionTextFieldValue: String,
    onDescriptionTextFieldValueChanged: (String) -> Unit,
    onPrevButtonClicked: (stage: AddPetScreenStage) -> Unit,
    onDoneButtonClicked: () -> Unit,
    navigateToDashboard: () -> Unit,
    modifier: Modifier = Modifier
) {
    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = Modifier.width(IntrinsicSize.Max)
    ) {
        PetFormsSubHeadline(headlineStringId = R.string.pet_form_subheadline_extras)
        Column(
            modifier = modifier
        ) {
            OutlinedTextField(
                value = descriptionTextFieldValue,
                label = {Text(text = stringResource(R.string.pet_description_label))},
                placeholder = {Text(text = stringResource(R.string.pet_description_placeholder))},
                onValueChange = onDescriptionTextFieldValueChanged,
                maxLines = 8
            )
        }
        PetFormsBottomNavButtons(
            leftButtonStringId = R.string.previous,
            rightButtonStringId = R.string.done,
            onLeftButtonClicked = { onPrevButtonClicked(AddPetScreenStage.Dimensions) },
            onRightButtonClicked = { onDoneButtonClicked()
                navigateToDashboard()
            }
        )
    }
}

@SuppressLint("UnrememberedMutableState")
@OptIn(ExperimentalMaterial3Api::class)
@Preview (showBackground = true)
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
            text = stringResource(R.string.pet_form_headline),
            style = MaterialTheme.typography.headlineMedium
        )
        Text(
            text = stringResource(R.string.pet_form_description),
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
            onCancelButtonClicked = { /*TODO*/ },
            onNextButtonClicked = { /*TODO*/ },
            isKeyboardHide = false,
            onFocusCleared = {},
            isNameValid = false,
            nameErrorMessage = R.string.blank,
            textFieldHeight = 90.dp,
            isBirthdateValid = true,
            birthDateErrorMessage = R.string.blank)
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
            text = stringResource(R.string.pet_form_headline),
            style = MaterialTheme.typography.headlineMedium
        )
        Text(
            text = stringResource(R.string.pet_form_description),
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
            onNextButtonClicked = {/*TODO*/},
            onPrevButtonClicked = { /*TODO*/ },
            isKeyboardHide = false,
            onFocusCleared = {},
            isWeightValid = false,
            isHeightValid = false,
            isLengthValid = false,
            isCircuitValid = false,
            weightErrorMessage = R.string.blank,
            heightErrorMessage = R.string.blank,
            lengthErrorMessage = R.string.blank,
            circuitErrorMessage = R.string.blank,
            textFieldHeight = 90.dp)
    }
}

@Preview (showBackground = true)
@Composable
fun AdditionalInfoPetFormPrev() {
    val descriptionValue = remember { mutableStateOf("") }

    Column(
        horizontalAlignment = Alignment.CenterHorizontally, modifier = Modifier
            .fillMaxSize()
            .padding(60.dp)
    ) {
        Text(
            text = stringResource(R.string.pet_form_headline),
            style = MaterialTheme.typography.headlineMedium
        )
        Text(
            text = stringResource(R.string.pet_form_description),
            style = MaterialTheme.typography.bodySmall
        )
        Spacer(modifier = Modifier.padding(20.dp))
        AdditionalInfoPetForm(
            descriptionTextFieldValue = descriptionValue.value,
            onDescriptionTextFieldValueChanged = { descriptionValue.value = it },
            onPrevButtonClicked = { },
            onDoneButtonClicked = {  },
            navigateToDashboard = {})
    }
}
