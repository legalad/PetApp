package com.example.petapp.ui.addpet

import android.annotation.SuppressLint
import androidx.annotation.StringRes
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.slideOutVertically
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.*
import androidx.compose.material3.IconButtonDefaults.outlinedIconToggleButtonColors
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.petapp.R
import com.example.petapp.model.*
import com.example.petapp.ui.components.AddPetDataScaffold
import com.example.petapp.ui.components.ErrorScreen
import com.example.petapp.ui.components.LoadingScreen
import com.example.petapp.ui.components.forms.*

@Composable
fun AddPetScreen(
    viewModel: AddPetViewModel,
    navigateToDashboard: () -> Unit,
    navigateBack: () -> Unit,
    modifier: Modifier = Modifier
) {
    when (val uiState = viewModel.uiState.collectAsState().value) {
        is AddPetUiState.Error -> ErrorScreen(message = uiState.errorMessage)
        is AddPetUiState.Loading -> LoadingScreen()
        is AddPetUiState.Success -> AddPetResultScreen(
            uiState = uiState,
            viewModel = viewModel,
            navigateToDashboard = navigateToDashboard,
            navigateBack = navigateBack
        )
    }
}

@Composable
fun UpdatePetScreen(
    viewModel: AddPetViewModel,
    navigateBack: () -> Unit,
    modifier: Modifier = Modifier
) {
    when (val uiState = viewModel.uiState.collectAsState().value) {
        is AddPetUiState.Error -> ErrorScreen(message = uiState.errorMessage)
        is AddPetUiState.Loading -> LoadingScreen()
        is AddPetUiState.Success -> UpdatePetResultScreen(
            uiState = uiState,
            viewModel = viewModel,
            navigateBack = navigateBack
        )
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun UpdatePetResultScreen(
    uiState: AddPetUiState.Success,
    viewModel: AddPetViewModel,
    navigateBack: () -> Unit
) {
    AddPetDataScaffold(
        topAppBarTitleId = R.string.util_blank,
        headline = R.string.components_forms_title_update_pet,
        supportingText = R.string.components_forms_description_new_pet,
        rightButtonStringId = R.string.components_forms_dialog_buttons_done,
        onRightButtonClicked = { if (viewModel.onUpdateButtonClicked()) navigateBack() },
        navigateBack = navigateBack,
        onLeftButtonClicked = navigateBack,
        hideKeyboard = viewModel::hideKeyboard
    ) {
        UpdatePetForm(
            nameFieldValue = uiState.nameFieldValue,
            onNameFieldValueChanged = viewModel::onNameFieldValueChanged,
            onNameFieldCancelClicked = viewModel::onNameFieldCancelClicked,
            weightFieldValue = uiState.weightFieldValue,
            isWeightUnitPickerExpanded = uiState.isWeightUnitPickerExpanded,
            weightUnitList = uiState.weightUnitList,
            selectedWeightUnit = uiState.selectedWeightUnit,
            onWeightFieldValueChanged = viewModel::onWeightFieldValueChanged,
            onWeightFieldCancelClicked = viewModel::onWeightFieldCancelClicked,
            onWeightUnitPickerOnExpandedChange = viewModel::onWeightUnitPickerOnExpandedChange,
            onWeightUnitPickerOnDismissRequest = viewModel::onWeightUnitPickerOnDismissRequest,
            onWeightUnitPickerDropdownMenuItemClicked = viewModel::onWeightUnitPickerDropdownMenuItemClicked,
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
            speciesMenuSelectedOption = uiState.speciesMenuSelectedOption,
            speciesMenuOnExpandedChanged = viewModel::speciesMenuOnExpandedChanged,
            speciesMenuOnDropdownMenuItemClicked = viewModel::speciesMenuOnDropdownMenuItemClicked,
            speciesMenuOnDismissRequest = viewModel::speciesMenuOnDismissRequest,
            breedOptions = uiState.breedMenuOptions,
            breedMenuExpanded = uiState.breedMenuExpanded,
            breedMenuEnabled = uiState.breedMenuEnabled,
            breedMenuSelectedOption = uiState.breedMenuSelectedOption,
            breedMenuOnExpandedChanged = viewModel::breedMenuOnExpandedChanged,
            breedMenuOnDropdownMenuItemClicked = viewModel::breedMenuOnDropdownMenuItemClicked,
            breedMenuOnDismissRequest = viewModel::breedMenuOnDismissRequest,
            isKeyboardHide = uiState.hideKeyboard,
            onFocusCleared = viewModel::onFocusCleared,
            isNameValid = !uiState.isNameValid,
            nameErrorMessage = uiState.nameErrorMessage,
            isWeightValid = !uiState.isWeightValid,
            weightErrorMessage = uiState.weightErrorMessage,
            isBirthdateValid = !uiState.isBirthDateValid,
            birthDateErrorMessage = uiState.birtDateErrorMessage,
            isSpeciesValid = !uiState.isSpeciesValid,
            speciesMenuErrorMessage = uiState.speciesErrorMessage,
            isGenderValid = !uiState.isGenderValid,
            maleIconChecked = uiState.maleIconChecked,
            femaleIconChecked = uiState.femaleIconChecked,
            onMaleIconButtonClicked = viewModel::onMaleIconButtonClicked,
            onFemaleIconButtonClicked = viewModel::onFemaleIconButtonClicked,
            isUpdate = true
        )
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AddPetResultScreen(
    uiState: AddPetUiState.Success,
    viewModel: AddPetViewModel,
    navigateToDashboard: () -> Unit,
    navigateBack: () -> Unit
) {
    /*val uiState = viewModel.successUiState.collectAsState().value*/

    when (uiState.screenStage) {
        AddPetScreenStage.GeneralOne -> AddPetDataScaffold(
            topAppBarTitleId = R.string.util_blank,
            iconId = R.drawable.owner_512,
            headline = R.string.components_forms_title_new_pet,
            supportingText = R.string.components_forms_description_new_pet,
            rightButtonStringId = R.string.components_forms_dialog_buttons_next,
            onRightButtonClicked = viewModel::onGeneralOneDoneButtonClicked,
            navigateBack = navigateBack,
            onLeftButtonClicked = navigateBack,
            hideKeyboard = viewModel::hideKeyboard
        ) {
            GeneralPetFormOne(
                speciesOptions = uiState.speciesMenuOptions,
                speciesMenuExpanded = uiState.speciesMenuExpanded,
                speciesMenuSelectedOption = uiState.speciesMenuSelectedOption,
                speciesMenuOnExpandedChanged = viewModel::speciesMenuOnExpandedChanged,
                speciesMenuOnDropdownMenuItemClicked = viewModel::speciesMenuOnDropdownMenuItemClicked,
                speciesMenuOnDismissRequest = viewModel::speciesMenuOnDismissRequest,
                breedOptions = uiState.breedMenuOptions,
                breedMenuExpanded = uiState.breedMenuExpanded,
                breedMenuEnabled = uiState.breedMenuEnabled,
                breedMenuSelectedOption = uiState.breedMenuSelectedOption,
                breedMenuOnExpandedChanged = viewModel::breedMenuOnExpandedChanged,
                breedMenuOnDropdownMenuItemClicked = viewModel::breedMenuOnDropdownMenuItemClicked,
                breedMenuOnDismissRequest = viewModel::breedMenuOnDismissRequest,
                isSpeciesValid = !uiState.isSpeciesValid,
                speciesMenuErrorMessage = uiState.speciesErrorMessage,
                isGenderValid = !uiState.isGenderValid,
                maleIconChecked = uiState.maleIconChecked,
                femaleIconChecked = uiState.femaleIconChecked,
                onMaleIconButtonClicked = viewModel::onMaleIconButtonClicked,
                onFemaleIconButtonClicked = viewModel::onFemaleIconButtonClicked
            )
        }

        AddPetScreenStage.GeneralTwo ->  AddPetDataScaffold(
            topAppBarTitleId = R.string.util_blank,
            iconId = R.drawable.story_512,
            headline = R.string.components_forms_title_new_pet,
            supportingText = R.string.components_forms_description_new_pet,
            rightButtonStringId = R.string.components_forms_dialog_buttons_next,
            leftButtonStringId = R.string.components_forms_dialog_buttons_previous,
            onRightButtonClicked = viewModel::onGeneralDoneButtonClicked,
            navigateBack = navigateBack,
            onLeftButtonClicked = { viewModel.onNavigateButtonClicked(AddPetScreenStage.GeneralOne) },
            hideKeyboard = viewModel::hideKeyboard
        ) {
            GeneralPetFormTwo(
                nameFieldValue = uiState.nameFieldValue,
                onNameFieldValueChanged = viewModel::onNameFieldValueChanged,
                onNameFieldCancelClicked = viewModel::onNameFieldCancelClicked,
                weightFieldValue = uiState.weightFieldValue,
                isWeightUnitPickerExpanded = uiState.isWeightUnitPickerExpanded,
                weightUnitList = uiState.weightUnitList,
                selectedWeightUnit = uiState.selectedWeightUnit,
                onWeightFieldValueChanged = viewModel::onWeightFieldValueChanged,
                onWeightFieldCancelClicked = viewModel::onWeightFieldCancelClicked,
                onWeightUnitPickerOnExpandedChange = viewModel::onWeightUnitPickerOnExpandedChange,
                onWeightUnitPickerOnDismissRequest = viewModel::onWeightUnitPickerOnDismissRequest,
                onWeightUnitPickerDropdownMenuItemClicked = viewModel::onWeightUnitPickerDropdownMenuItemClicked,
                datePickerTextFieldValue = uiState.datePickerTextFieldValue,
                onDatePickerTextFieldValueChanged = viewModel::onDatePickerTextFieldValueChanged,
                onDatePickerTextFieldClicked = viewModel::onDatePickerTextFieldClicked,
                datePickerOpenDialog = uiState.datePickerOpenDialog,
                datePickerState = uiState.datePickerState,
                datePickerConfirmEnabled = uiState.datePickerConfirmEnabled,
                datePickerOnDismissRequest = viewModel::datePickerOnDismissRequest,
                datePickerOnConfirmedButtonClicked = viewModel::datePickerOnConfirmedButtonClicked,
                datePickerOnDismissedButtonClicked = viewModel::datePickerOnDismissedButtonClicked,
                isKeyboardHide = uiState.hideKeyboard,
                onFocusCleared = viewModel::onFocusCleared,
                isNameValid = !uiState.isNameValid,
                nameErrorMessage = uiState.nameErrorMessage,
                isWeightValid = !uiState.isWeightValid,
                weightErrorMessage = uiState.weightErrorMessage,
                isBirthdateValid = !uiState.isBirthDateValid,
                birthDateErrorMessage = uiState.birtDateErrorMessage,
            )
        }

        AddPetScreenStage.Dimensions ->
            AddPetDataScaffold(
                topAppBarTitleId = R.string.util_blank,
                iconId = R.drawable.measuring,
                headline = R.string.components_forms_title_new_pet_dimensions,
                supportingText = R.string.components_forms_description_dimension,
                leftButtonStringId = R.string.components_forms_dialog_buttons_previous,
                rightButtonStringId = R.string.components_forms_dialog_buttons_done,
                onRightButtonClicked = { if (viewModel.onDimensionsDoneButtonClicked()) navigateToDashboard() },
                navigateBack = navigateToDashboard,
                onLeftButtonClicked = { viewModel.onNavigateButtonClicked(AddPetScreenStage.GeneralTwo) },
                hideKeyboard = viewModel::hideKeyboard
            ) {
                DimensionsPetForm(
                    dimensionUnitList = uiState.dimensionUnitList,
                    heightFieldValue = uiState.heightFieldValue,
                    isHeightUnitPickerExpanded = uiState.isHeightUnitPickerExpanded,
                    selectedHeightUnit = uiState.selectedHeightUnit,
                    onHeightUnitPickerOnExpandedChange = viewModel::onHeightUnitPickerOnExpandedChange,
                    onHeightUnitPickerOnDismissRequest = viewModel::onHeightUnitPickerOnDismissRequest,
                    onHeightUnitPickerDropdownMenuItemClicked = viewModel::onHeightUnitPickerDropdownMenuItemClicked,
                    onHeightFieldValueChanged = viewModel::onHeightFieldValueChanged,
                    onHeightFieldCancelClicked = viewModel::onHeightFieldCancelClicked,
                    lengthFieldValue = uiState.lengthFieldValue,
                    isLengthUnitPickerExpanded = uiState.isLengthUnitPickerExpanded,
                    selectedLengthUnit = uiState.selectedLengthUnit,
                    onLengthUnitPickerOnExpandedChange = viewModel::onLengthUnitPickerOnExpandedChange,
                    onLengthUnitPickerOnDismissRequest = viewModel::onLengthUnitPickerOnDismissRequest,
                    onLengthUnitPickerDropdownMenuItemClicked = viewModel::onLengthUnitPickerDropdownMenuItemClicked,
                    onLengthFieldValueChanged = viewModel::onLengthFieldValueChanged,
                    onLengthFieldCancelClicked = viewModel::onLengthFieldCancelClicked,
                    circuitFieldValue = uiState.circuitFieldValue,
                    isCircuitUnitPickerExpanded = uiState.isCircuitUnitPickerExpanded,
                    selectedCircuitUnit = uiState.selectedCircuitUnit,
                    onCircuitUnitPickerOnExpandedChange = viewModel::onCircuitUnitPickerOnExpandedChange,
                    onCircuitUnitPickerOnDismissRequest = viewModel::onCircuitUnitPickerOnDismissRequest,
                    onCircuitUnitPickerDropdownMenuItemClicked = viewModel::onCircuitUnitPickerDropdownMenuItemClicked,
                    onCircuitFieldValueChanged = viewModel::onCircuitFieldValueChanged,
                    onCircuitFieldCancelClicked = viewModel::onCircuitFieldCancelClicked,
                    isKeyboardHide = uiState.hideKeyboard,
                    onFocusCleared = viewModel::onFocusCleared,
                    isHeightValid = !uiState.isHeightValid,
                    isLengthValid = !uiState.isLengthValid,
                    isCircuitValid = !uiState.isCircuitValid,
                    heightErrorMessage = uiState.heightErrorMessage,
                    lengthErrorMessage = uiState.lengthErrorMessage,
                    circuitErrorMessage = uiState.circuitErrorMessage
                )
            }

        AddPetScreenStage.UpdateGeneral ->
            AddPetDataScaffold(
                topAppBarTitleId = R.string.util_blank,
                iconId = R.drawable.owner_512,
                headline = R.string.components_forms_title_new_pet,
                supportingText = R.string.components_forms_description_new_pet,
                rightButtonStringId = R.string.components_forms_dialog_buttons_next,
                onRightButtonClicked = viewModel::onGeneralDoneButtonClicked,
                navigateBack = navigateBack,
                onLeftButtonClicked = navigateBack,
                hideKeyboard = viewModel::hideKeyboard
            ) {
                UpdatePetForm(
                    nameFieldValue = uiState.nameFieldValue,
                    onNameFieldValueChanged = viewModel::onNameFieldValueChanged,
                    onNameFieldCancelClicked = viewModel::onNameFieldCancelClicked,
                    weightFieldValue = uiState.weightFieldValue,
                    isWeightUnitPickerExpanded = uiState.isWeightUnitPickerExpanded,
                    weightUnitList = uiState.weightUnitList,
                    selectedWeightUnit = uiState.selectedWeightUnit,
                    onWeightFieldValueChanged = viewModel::onWeightFieldValueChanged,
                    onWeightFieldCancelClicked = viewModel::onWeightFieldCancelClicked,
                    onWeightUnitPickerOnExpandedChange = viewModel::onWeightUnitPickerOnExpandedChange,
                    onWeightUnitPickerOnDismissRequest = viewModel::onWeightUnitPickerOnDismissRequest,
                    onWeightUnitPickerDropdownMenuItemClicked = viewModel::onWeightUnitPickerDropdownMenuItemClicked,
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
                    speciesMenuSelectedOption = uiState.speciesMenuSelectedOption,
                    speciesMenuOnExpandedChanged = viewModel::speciesMenuOnExpandedChanged,
                    speciesMenuOnDropdownMenuItemClicked = viewModel::speciesMenuOnDropdownMenuItemClicked,
                    speciesMenuOnDismissRequest = viewModel::speciesMenuOnDismissRequest,
                    breedOptions = uiState.breedMenuOptions,
                    breedMenuExpanded = uiState.breedMenuExpanded,
                    breedMenuEnabled = uiState.breedMenuEnabled,
                    breedMenuSelectedOption = uiState.breedMenuSelectedOption,
                    breedMenuOnExpandedChanged = viewModel::breedMenuOnExpandedChanged,
                    breedMenuOnDropdownMenuItemClicked = viewModel::breedMenuOnDropdownMenuItemClicked,
                    breedMenuOnDismissRequest = viewModel::breedMenuOnDismissRequest,
                    isKeyboardHide = uiState.hideKeyboard,
                    onFocusCleared = viewModel::onFocusCleared,
                    isNameValid = !uiState.isNameValid,
                    nameErrorMessage = uiState.nameErrorMessage,
                    isWeightValid = !uiState.isWeightValid,
                    weightErrorMessage = uiState.weightErrorMessage,
                    isBirthdateValid = !uiState.isBirthDateValid,
                    birthDateErrorMessage = uiState.birtDateErrorMessage,
                    isSpeciesValid = !uiState.isSpeciesValid,
                    speciesMenuErrorMessage = uiState.speciesErrorMessage,
                    isGenderValid = !uiState.isGenderValid,
                    maleIconChecked = uiState.maleIconChecked,
                    femaleIconChecked = uiState.femaleIconChecked,
                    onMaleIconButtonClicked = viewModel::onMaleIconButtonClicked,
                    onFemaleIconButtonClicked = viewModel::onFemaleIconButtonClicked
                )
            }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun UpdatePetForm(
    nameFieldValue: String,
    onNameFieldValueChanged: (String) -> Unit,
    onNameFieldCancelClicked: () -> Unit,
    weightFieldValue: String,
    onWeightFieldValueChanged: (String) -> Unit,
    onWeightFieldCancelClicked: () -> Unit,
    isWeightUnitPickerExpanded: Boolean,
    weightUnitList: List<com.example.petapp.model.Unit>,
    selectedWeightUnit: WeightUnit,
    onWeightUnitPickerOnExpandedChange: (Boolean) -> Unit,
    onWeightUnitPickerOnDismissRequest: () -> Unit,
    onWeightUnitPickerDropdownMenuItemClicked: (Int) -> Unit,
    datePickerTextFieldValue: String,
    onDatePickerTextFieldValueChanged: (String) -> Unit,
    onDatePickerTextFieldClicked: () -> Unit,
    datePickerOpenDialog: Boolean,
    datePickerState: DatePickerState,
    datePickerConfirmEnabled: Boolean,
    datePickerOnDismissRequest: () -> Unit,
    datePickerOnConfirmedButtonClicked: () -> Unit,
    datePickerOnDismissedButtonClicked: () -> Unit,
    speciesOptions: List<Species>,
    speciesMenuExpanded: Boolean,
    speciesMenuSelectedOption: Species,
    speciesMenuOnExpandedChanged: () -> Unit,
    speciesMenuOnDropdownMenuItemClicked: (Int) -> Unit,
    speciesMenuOnDismissRequest: () -> Unit,
    breedOptions: List<Breed>,
    breedMenuExpanded: Boolean,
    breedMenuEnabled: Boolean,
    breedMenuSelectedOption: Breed?,
    breedMenuOnExpandedChanged: () -> Unit,
    breedMenuOnDropdownMenuItemClicked: (Int) -> Unit,
    breedMenuOnDismissRequest: () -> Unit,
    isKeyboardHide: Boolean,
    onFocusCleared: () -> Unit,
    isNameValid: Boolean,
    @StringRes nameErrorMessage: Int,
    isWeightValid: Boolean,
    @StringRes weightErrorMessage: Int,
    isBirthdateValid: Boolean,
    @StringRes birthDateErrorMessage: Int,
    isSpeciesValid: Boolean,
    @StringRes speciesMenuErrorMessage: Int,
    isGenderValid: Boolean,
    maleIconChecked: Boolean,
    femaleIconChecked: Boolean,
    onMaleIconButtonClicked: (Boolean) -> Unit,
    onFemaleIconButtonClicked: (Boolean) -> Unit,
    isUpdate: Boolean = false
) {
    val focusManager = LocalFocusManager.current
    GenderPicker(
        isGenderValid = isGenderValid,
        maleIconChecked = maleIconChecked,
        femaleIconChecked = femaleIconChecked,
        onMaleIconButtonClicked = onMaleIconButtonClicked,
        onFemaleIconButtonClicked = onFemaleIconButtonClicked
    )
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
    if (!isUpdate) {
        OutlinedTextFieldWithLeadingIcon(
            fieldLabel = R.string.components_forms_text_field_label_pet_weight,
            fieldPlaceholder = R.string.util_unit_weight_placeholder,
            leadingIcon = R.drawable.weight_24,
            trailingIcon = {
                TextFieldUnitPicker(
                    expanded = isWeightUnitPickerExpanded,
                    onExpandedChange = onWeightUnitPickerOnExpandedChange,
                    onDismissRequest = onWeightUnitPickerOnDismissRequest,
                    onDropdownMenuItemClicked = onWeightUnitPickerDropdownMenuItemClicked,
                    options = weightUnitList,
                    selectedOption = selectedWeightUnit
                )
            },
            onTextFieldClicked = {},
            fieldValue = weightFieldValue,
            onValueChanged = onWeightFieldValueChanged,
            onCancelClicked = onWeightFieldCancelClicked,
            focusManager = focusManager,
            onFocusClear = onFocusCleared.also { if (isWeightUnitPickerExpanded) focusManager.clearFocus() },
            readOnly = isWeightUnitPickerExpanded,
            keyboardOptions = KeyboardOptions(
                keyboardType = KeyboardType.Number,
                imeAction = ImeAction.Next
            ),
            hideKeyboard = isKeyboardHide,
            isError = isWeightValid,
            supportingText = weightErrorMessage
        )
    }
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
    ExposedDropdownMenuV2(
        label = R.string.components_forms_text_field_label_pet_species,
        options = speciesOptions,
        expanded = speciesMenuExpanded,
        selectedOption = speciesMenuSelectedOption,
        onExpandedChange = speciesMenuOnExpandedChanged,
        onDropdownMenuItemClicked = speciesMenuOnDropdownMenuItemClicked,
        onDismissRequest = speciesMenuOnDismissRequest,
        isError = isSpeciesValid,
        supportingText = speciesMenuErrorMessage
    )
    ExposedDropdownMenuV2(
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
fun GeneralPetFormOne(
    speciesOptions: List<Species>,
    speciesMenuExpanded: Boolean,
    speciesMenuSelectedOption: Species,
    speciesMenuOnExpandedChanged: () -> Unit,
    speciesMenuOnDropdownMenuItemClicked: (Int) -> Unit,
    speciesMenuOnDismissRequest: () -> Unit,
    breedOptions: List<Breed>,
    breedMenuExpanded: Boolean,
    breedMenuEnabled: Boolean,
    breedMenuSelectedOption: Breed?,
    breedMenuOnExpandedChanged: () -> Unit,
    breedMenuOnDropdownMenuItemClicked: (Int) -> Unit,
    breedMenuOnDismissRequest: () -> Unit,
    isSpeciesValid: Boolean,
    @StringRes speciesMenuErrorMessage: Int,
    isGenderValid: Boolean,
    maleIconChecked: Boolean,
    femaleIconChecked: Boolean,
    onMaleIconButtonClicked: (Boolean) -> Unit,
    onFemaleIconButtonClicked: (Boolean) -> Unit
) {
    GenderPicker(
        isGenderValid = isGenderValid,
        maleIconChecked = maleIconChecked,
        femaleIconChecked = femaleIconChecked,
        onMaleIconButtonClicked = onMaleIconButtonClicked,
        onFemaleIconButtonClicked = onFemaleIconButtonClicked
    )
    ExposedDropdownMenuV2(
        label = R.string.components_forms_text_field_label_pet_species,
        options = speciesOptions,
        expanded = speciesMenuExpanded,
        selectedOption = speciesMenuSelectedOption,
        onExpandedChange = speciesMenuOnExpandedChanged,
        onDropdownMenuItemClicked = speciesMenuOnDropdownMenuItemClicked,
        onDismissRequest = speciesMenuOnDismissRequest,
        isError = isSpeciesValid,
        supportingText = speciesMenuErrorMessage
    )
    ExposedDropdownMenuV2(
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

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun GeneralPetFormTwo(
    nameFieldValue: String,
    onNameFieldValueChanged: (String) -> Unit,
    onNameFieldCancelClicked: () -> Unit,
    weightFieldValue: String,
    onWeightFieldValueChanged: (String) -> Unit,
    onWeightFieldCancelClicked: () -> Unit,
    isWeightUnitPickerExpanded: Boolean,
    weightUnitList: List<com.example.petapp.model.Unit>,
    selectedWeightUnit: WeightUnit,
    onWeightUnitPickerOnExpandedChange: (Boolean) -> Unit,
    onWeightUnitPickerOnDismissRequest: () -> Unit,
    onWeightUnitPickerDropdownMenuItemClicked: (Int) -> Unit,
    datePickerTextFieldValue: String,
    onDatePickerTextFieldValueChanged: (String) -> Unit,
    onDatePickerTextFieldClicked: () -> Unit,
    datePickerOpenDialog: Boolean,
    datePickerState: DatePickerState,
    datePickerConfirmEnabled: Boolean,
    datePickerOnDismissRequest: () -> Unit,
    datePickerOnConfirmedButtonClicked: () -> Unit,
    datePickerOnDismissedButtonClicked: () -> Unit,
    isKeyboardHide: Boolean,
    onFocusCleared: () -> Unit,
    isNameValid: Boolean,
    @StringRes nameErrorMessage: Int,
    isWeightValid: Boolean,
    @StringRes weightErrorMessage: Int,
    isBirthdateValid: Boolean,
    @StringRes birthDateErrorMessage: Int,
) {
    val focusManager = LocalFocusManager.current
    OutlinedTextFieldWithLeadingIcon(
        fieldLabel = R.string.components_forms_text_field_label_pet_name,
        fieldPlaceholder = R.string.components_forms_text_field_placeholder_pet_name,
        leadingIcon = R.drawable.product_development_64,
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
    OutlinedTextFieldWithLeadingIcon(
        fieldLabel = R.string.components_forms_text_field_label_pet_weight,
        fieldPlaceholder = R.string.util_unit_weight_placeholder,
        leadingIcon = R.drawable.weight_24,
        trailingIcon = {
            TextFieldUnitPicker(
                expanded = isWeightUnitPickerExpanded,
                onExpandedChange = onWeightUnitPickerOnExpandedChange,
                onDismissRequest = onWeightUnitPickerOnDismissRequest,
                onDropdownMenuItemClicked = onWeightUnitPickerDropdownMenuItemClicked,
                options = weightUnitList,
                selectedOption = selectedWeightUnit
            )
        },
        onTextFieldClicked = {},
        fieldValue = weightFieldValue,
        onValueChanged = onWeightFieldValueChanged,
        onCancelClicked = onWeightFieldCancelClicked,
        focusManager = focusManager,
        onFocusClear = onFocusCleared.also { if (isWeightUnitPickerExpanded) focusManager.clearFocus() },
        readOnly = isWeightUnitPickerExpanded,
        keyboardOptions = KeyboardOptions(
            keyboardType = KeyboardType.Number,
            imeAction = ImeAction.Next
        ),
        hideKeyboard = isKeyboardHide,
        isError = isWeightValid,
        supportingText = weightErrorMessage
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
    dimensionUnitList: List<com.example.petapp.model.Unit>,
    heightFieldValue: String,
    isHeightUnitPickerExpanded: Boolean,
    selectedHeightUnit: DimensionUnit,
    onHeightUnitPickerOnExpandedChange: (Boolean) -> Unit,
    onHeightUnitPickerOnDismissRequest: () -> Unit,
    onHeightUnitPickerDropdownMenuItemClicked: (Int) -> Unit,
    isLengthUnitPickerExpanded: Boolean,
    selectedLengthUnit: DimensionUnit,
    onLengthUnitPickerOnExpandedChange: (Boolean) -> Unit,
    onLengthUnitPickerOnDismissRequest: () -> Unit,
    onLengthUnitPickerDropdownMenuItemClicked: (Int) -> Unit,
    isCircuitUnitPickerExpanded: Boolean,
    selectedCircuitUnit: DimensionUnit,
    onCircuitUnitPickerOnExpandedChange: (Boolean) -> Unit,
    onCircuitUnitPickerOnDismissRequest: () -> Unit,
    onCircuitUnitPickerDropdownMenuItemClicked: (Int) -> Unit,
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
    isHeightValid: Boolean,
    isLengthValid: Boolean,
    isCircuitValid: Boolean,
    @StringRes heightErrorMessage: Int,
    @StringRes lengthErrorMessage: Int,
    @StringRes circuitErrorMessage: Int,

    ) {
    val focusManager = LocalFocusManager.current
    OutlinedTextFieldWithLeadingIcon(
        fieldLabel = R.string.components_forms_text_field_label_pet_height,
        fieldPlaceholder = R.string.util_unit_dimensions_placeholder,
        leadingIcon = R.drawable.baseline_height_24,
        trailingIcon = {
            TextFieldUnitPicker(
                expanded = isHeightUnitPickerExpanded,
                onExpandedChange = onHeightUnitPickerOnExpandedChange,
                onDismissRequest = onHeightUnitPickerOnDismissRequest,
                onDropdownMenuItemClicked = onHeightUnitPickerDropdownMenuItemClicked,
                options = dimensionUnitList,
                selectedOption = selectedHeightUnit
            )
        },
        fieldValue = heightFieldValue,
        onValueChanged = onHeightFieldValueChanged,
        onCancelClicked = onHeightFieldCancelClicked,
        focusManager = focusManager,
        onFocusClear = onFocusCleared.also { if (isHeightUnitPickerExpanded) focusManager.clearFocus() },
        readOnly = isHeightUnitPickerExpanded,
        keyboardOptions = KeyboardOptions(
            keyboardType = KeyboardType.Number,
            imeAction = ImeAction.Next
        ),
        hideKeyboard = isKeyboardHide,
        isError = isHeightValid,
        supportingText = heightErrorMessage
    )
    OutlinedTextFieldWithLeadingIcon(
        fieldLabel = R.string.components_forms_text_field_label_pet_length,
        fieldPlaceholder = R.string.util_unit_dimensions_placeholder,
        leadingIcon = R.drawable.width_24,
        trailingIcon = {
            TextFieldUnitPicker(
                expanded = isLengthUnitPickerExpanded,
                onExpandedChange = onLengthUnitPickerOnExpandedChange,
                onDismissRequest = onLengthUnitPickerOnDismissRequest,
                onDropdownMenuItemClicked = onLengthUnitPickerDropdownMenuItemClicked,
                options = dimensionUnitList,
                selectedOption = selectedLengthUnit
            )
        },
        fieldValue = lengthFieldValue,
        onValueChanged = onLengthFieldValueChanged,
        onCancelClicked = onLengthFieldCancelClicked,
        focusManager = focusManager,
        onFocusClear = onFocusCleared.also { if (isLengthUnitPickerExpanded) focusManager.clearFocus() },
        readOnly = isLengthUnitPickerExpanded,
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
        fieldPlaceholder = R.string.util_unit_dimensions_placeholder,
        leadingIcon = R.drawable.restart_alt_24,
        trailingIcon = {
            TextFieldUnitPicker(
                expanded = isCircuitUnitPickerExpanded,
                onExpandedChange = onCircuitUnitPickerOnExpandedChange,
                onDismissRequest = onCircuitUnitPickerOnDismissRequest,
                onDropdownMenuItemClicked = onCircuitUnitPickerDropdownMenuItemClicked,
                options = dimensionUnitList,
                selectedOption = selectedCircuitUnit
            )
        },
        fieldValue = circuitFieldValue,
        onValueChanged = onCircuitFieldValueChanged,
        onCancelClicked = onCircuitFieldCancelClicked,
        focusManager = focusManager,
        readOnly = isCircuitUnitPickerExpanded,
        onFocusClear = onFocusCleared.also { if (isCircuitUnitPickerExpanded) focusManager.clearFocus() },
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
    val speciesMenuOptions = Species.values().toList()
    var speciesMenuExpanded by remember { mutableStateOf(false) }
    var speciesMenuSelectedOptionText by remember { mutableStateOf(Species.BIRD) }
    val breedMenuOptions = CatBreed.values().toList()
    var breedMenuExpanded by remember { mutableStateOf(false) }
    var breedMenuSelectedOptionText by remember { mutableStateOf(DogBreed.NONE) }
    val breedMenuEnabled by remember { mutableStateOf(false) }
    val maleChecked = remember {
        mutableStateOf(false)
    }
    val femaleChecked = remember {
        mutableStateOf(false)
    }
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
        UpdatePetForm(
            nameFieldValue = value.value,
            onNameFieldValueChanged = { value.value = it },
            onNameFieldCancelClicked = { value.value = "" },
            weightFieldValue = "",
            weightErrorMessage = R.string.util_blank,
            isWeightValid = true,
            isWeightUnitPickerExpanded = false,
            weightUnitList = WeightUnit.values().toList(),
            selectedWeightUnit = WeightUnit.KILOGRAMS,
            onWeightFieldCancelClicked = {},
            onWeightFieldValueChanged = {},
            onWeightUnitPickerDropdownMenuItemClicked = {},
            onWeightUnitPickerOnDismissRequest = {},
            onWeightUnitPickerOnExpandedChange = {},
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
                speciesMenuSelectedOptionText = Species.values()[it]
                speciesMenuExpanded = false
            },
            speciesMenuOnDismissRequest = { speciesMenuExpanded = false },
            breedOptions = breedMenuOptions,
            breedMenuExpanded = breedMenuExpanded,
            breedMenuEnabled = breedMenuEnabled,
            breedMenuSelectedOption = breedMenuSelectedOptionText,
            breedMenuOnExpandedChanged = { breedMenuExpanded = !breedMenuExpanded },
            breedMenuOnDropdownMenuItemClicked = {
                breedMenuSelectedOptionText = DogBreed.values()[it]
                breedMenuExpanded = false
            },
            breedMenuOnDismissRequest = { breedMenuExpanded = false },
            isKeyboardHide = false,
            onFocusCleared = {},
            isNameValid = false,
            nameErrorMessage = R.string.util_blank,
            isBirthdateValid = true,
            birthDateErrorMessage = R.string.util_blank,
            isSpeciesValid = true,
            speciesMenuErrorMessage = R.string.util_blank,
            isGenderValid = true,
            femaleIconChecked = femaleChecked.value,
            maleIconChecked = maleChecked.value,
            onMaleIconButtonClicked = {
                maleChecked.value = it
                femaleChecked.value = false
            },
            onFemaleIconButtonClicked = {
                femaleChecked.value = it
                maleChecked.value = false
            }
        )
    }
}

@Composable
fun GenderPicker(
    isGenderValid: Boolean,
    maleIconChecked: Boolean,
    femaleIconChecked: Boolean,
    onMaleIconButtonClicked: (Boolean) -> Unit,
    onFemaleIconButtonClicked: (Boolean) -> Unit
) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(bottom = 6.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {

        Row(
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            FilledTonalIconToggleButton(
                checked = maleIconChecked,
                onCheckedChange = onMaleIconButtonClicked,
                colors = outlinedIconToggleButtonColors(
                    checkedContainerColor = Color(0xFF6994FF),
                    containerColor = Color.Gray.copy(0.2f),
                    checkedContentColor = Color.White
                ),
                modifier = Modifier.size(48.dp)
            ) {
                Icon(
                    painter = painterResource(id = R.drawable.round_male_24),
                    contentDescription = null,
                    modifier = Modifier.size(32.dp)
                )
            }
            Spacer(modifier = Modifier.padding(12.dp))
            FilledTonalIconToggleButton(
                checked = femaleIconChecked,
                onCheckedChange = onFemaleIconButtonClicked,
                colors = outlinedIconToggleButtonColors(
                    checkedContainerColor = Color(0xFFFE929F),
                    containerColor = Color.Gray.copy(0.2f),
                    checkedContentColor = Color.White
                ),
                modifier = Modifier.size(48.dp)
            ) {
                Icon(
                    painter = painterResource(id = R.drawable.round_female_24),
                    contentDescription = null,
                    modifier = Modifier.size(32.dp)
                )
            }
        }
        Spacer(modifier = Modifier.padding(4.dp))
        AnimatedVisibility(
            visible = isGenderValid,
            enter = fadeIn(),
            exit = fadeOut() + slideOutVertically()
        ) {
            Text(
                text = "Pick gender",
                color = MaterialTheme.colorScheme.error,
                style = MaterialTheme.typography.bodySmall
            )
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TextFieldUnitPicker(
    expanded: Boolean,
    onExpandedChange: (Boolean) -> Unit,
    onDismissRequest: () -> Unit,
    onDropdownMenuItemClicked: (Int) -> Unit,
    options: List<com.example.petapp.model.Unit>,
    selectedOption: com.example.petapp.model.Unit
) {
    Row(
        modifier = Modifier
            .height(IntrinsicSize.Min)
            .width(80.dp), horizontalArrangement = Arrangement.SpaceEvenly
    ) {
        Divider(
            color = MaterialTheme.colorScheme.outline,
            modifier = Modifier
                .fillMaxHeight()  //fill the max height
                .width(1.dp)
        )
        ExposedDropdownMenuBox(
            expanded = expanded,
            onExpandedChange = onExpandedChange,
            modifier = Modifier.fillMaxWidth()
        ) {
            Column(
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.Center,
                modifier = Modifier
                    .fillMaxWidth()
                    .fillMaxHeight()
                    .menuAnchor()
            ) {
                Text(
                    text = stringResource(id = selectedOption.nameId)
                )
            }
            ExposedDropdownMenu(
                expanded = expanded,
                onDismissRequest = onDismissRequest,
                modifier = Modifier.height(140.dp)
            ) {
                options.forEach {
                    DropdownMenuItem(
                        text = { Text(stringResource(id = it.nameId)) },
                        onClick = { onDropdownMenuItemClicked(it.ordinal) })
                }
            }
        }
    }
}

@Preview
@Composable
fun GenderPickerPrev() {
    val maleChecked = remember {
        mutableStateOf(false)
    }
    val femaleChecked = remember {
        mutableStateOf(false)
    }
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(bottom = 6.dp),
        horizontalArrangement = Arrangement.Center,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Row {
            FilledTonalIconToggleButton(
                checked = maleChecked.value,
                onCheckedChange = {
                    maleChecked.value = it
                    femaleChecked.value = false
                },
                colors = outlinedIconToggleButtonColors(
                    checkedContainerColor = Color.Blue.copy(0.2f),
                    containerColor = Color.Gray.copy(0.1f)
                )
            ) {
                Icon(
                    painter = painterResource(id = R.drawable.round_male_24),
                    contentDescription = null
                )
            }
            FilledTonalIconToggleButton(
                checked = femaleChecked.value,
                onCheckedChange = {
                    femaleChecked.value = it
                    maleChecked.value = false
                },
                colors = outlinedIconToggleButtonColors(
                    checkedContainerColor = Color.Red.copy(0.2f),
                    containerColor = Color.Gray.copy(0.1f)
                )
            ) {
                Icon(
                    painter = painterResource(id = R.drawable.round_female_24),
                    contentDescription = null
                )
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun DimensionsPetFormPrev() {
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
            dimensionUnitList = DimensionUnit.values().toList(),
            heightFieldValue = heightValue.value,
            isHeightUnitPickerExpanded = false,
            selectedHeightUnit = DimensionUnit.METERS,
            onHeightUnitPickerDropdownMenuItemClicked = {},
            onHeightUnitPickerOnDismissRequest = {},
            onHeightUnitPickerOnExpandedChange = {},
            onHeightFieldValueChanged = { heightValue.value = it },
            onHeightFieldCancelClicked = { heightValue.value = "" },
            lengthFieldValue = lengthValue.value,
            isLengthUnitPickerExpanded = false,
            selectedLengthUnit = DimensionUnit.METERS,
            onLengthUnitPickerDropdownMenuItemClicked = {},
            onLengthUnitPickerOnDismissRequest = {},
            onLengthUnitPickerOnExpandedChange = {},
            onLengthFieldValueChanged = { lengthValue.value = it },
            onLengthFieldCancelClicked = { lengthValue.value = "" },
            circuitFieldValue = circuitValue.value,
            selectedCircuitUnit = DimensionUnit.METERS,
            isCircuitUnitPickerExpanded = false,
            onCircuitUnitPickerDropdownMenuItemClicked = {},
            onCircuitUnitPickerOnExpandedChange = {},
            onCircuitUnitPickerOnDismissRequest = {},
            onCircuitFieldValueChanged = { circuitValue.value = it },
            onCircuitFieldCancelClicked = { circuitValue.value = "" },
            isKeyboardHide = false,
            onFocusCleared = {},
            isHeightValid = false,
            isLengthValid = false,
            isCircuitValid = false,
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
