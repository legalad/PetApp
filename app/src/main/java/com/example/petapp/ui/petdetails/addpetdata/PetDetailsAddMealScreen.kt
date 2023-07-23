package com.example.petapp.ui.petdetails.addpetdata

import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import com.example.petapp.R
import com.example.petapp.ui.addpet.TextFieldUnitPicker
import com.example.petapp.ui.components.AddPetDataScaffold
import com.example.petapp.ui.components.ErrorScreen
import com.example.petapp.ui.components.LoadingScreen
import com.example.petapp.ui.components.forms.*

@Composable
fun PetDetailsAddMealScreen(
    viewModel: PetDetailsAddMealViewModel,
    navigateBack: () -> Unit,
    navigateToPetDetails: (petId: String) -> Unit
) {
    AddPetDataScaffold(
        topAppBarTitleId = R.string.util_blank,
        headline = R.string.components_forms_text_field_label_meal,
        onRightButtonClicked = {
            if (viewModel.onDoneButtonCLicked()) {
                navigateToPetDetails(
                    viewModel.getPetId()
                )
            }
        },
        navigateBack = navigateBack,
        onLeftButtonClicked = navigateBack,
        hideKeyboard = viewModel::hideKeyboard
    ) {
        when (val uiState = viewModel.uiState.collectAsState().value) {
            PetDetailsAddMealUiState.Loading -> LoadingScreen()
            is PetDetailsAddMealUiState.Success -> PetDetailsAddMealResultScreen(
                uiState = uiState,
                viewModel = viewModel
            )
            is PetDetailsAddMealUiState.Error -> ErrorScreen(message = uiState.errorMessage)
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun PetDetailsAddMealResultScreen(
    uiState: PetDetailsAddMealUiState.Success,
    viewModel: PetDetailsAddMealViewModel
) {
    val focusManager = LocalFocusManager.current
    ExposedDropdownMenuV2(
        label = R.string.components_forms_text_field_label_meal,
        options = uiState.mealTypeMenuOptions,
        expanded = uiState.mealTypeMenuExpanded,
        selectedOption = uiState.mealTypeMenuSelectedOption.nameId,
        onExpandedChange = viewModel::mealTypeMenuOnExpandedChanged,
        onDropdownMenuItemClicked = viewModel::mealTypeMenuOnDropdownMenuItemClicked,
        onDismissRequest = viewModel::mealTypeMenuOnDismissRequest,
        isError = !uiState.isMealTypeValid,
        supportingText = uiState.mealTypeErrorMessage
    )

    SwitchableTimePicker(
        showTimePicker = uiState.showTimePicker,
        showingPicker = uiState.showingPicker,
        state = uiState.timePickerState,
        configuration = LocalConfiguration.current,
        onTextFiledClicked = viewModel::onTimePickerTextFieldClicked,
        onDialogCanceledClicked = viewModel::onTimePickerDialogCancelClicked,
        onDialogConfirmedClicked = viewModel::onTimePickerDialogConfirmClicked,
        onShowingPickerIconClicked = viewModel::onTimePickerDialogSwitchIconClicked
    )

    OutlinedTextFieldWithLeadingIcon(
        fieldLabel = R.string.components_forms_text_field_label_pet_weight,
        fieldPlaceholder = R.string.util_unit_weight_placeholder,
        leadingIcon = R.drawable.weight_24,
        trailingIcon = {
            TextFieldUnitPicker(
                expanded = uiState.isWeightUnitPickerExpanded,
                onExpandedChange = viewModel::onWeightUnitPickerOnExpandedChange,
                onDismissRequest = viewModel::onWeightUnitPickerOnDismissRequest,
                onDropdownMenuItemClicked = viewModel::onWeightUnitPickerDropdownMenuItemClicked,
                options = uiState.weightUnitList,
                selectedOption = uiState.selectedWeightUnit
            )
        },
        fieldValue = uiState.weightFieldValue,
        onValueChanged = viewModel::onWeightFieldValueChanged,
        onCancelClicked = viewModel::onWeightFieldCancelClicked,
        focusManager = focusManager,
        onFocusClear = viewModel::onFocusCleared.also { if (uiState.isWeightUnitPickerExpanded) focusManager.clearFocus() },
        readOnly = uiState.isWeightUnitPickerExpanded,
        isError = !uiState.isWeightValid,
        supportingText = uiState.weightErrorMessage,
        keyboardOptions = KeyboardOptions(
            keyboardType = KeyboardType.Number,
            imeAction = ImeAction.Done
        ),
        hideKeyboard = uiState.hideKeyboard
    )

    RadioGroupList(
        radioOptions = uiState.foodTypeRadioOptions.map { it.nameId },
        selectedOption = uiState.foodTypeRadioSelectedOption.nameId,
        onOptionSelected = viewModel::onRadioButtonSelectedOptionClicked
    )

}