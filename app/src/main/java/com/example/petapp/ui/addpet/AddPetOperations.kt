package com.example.petapp.ui.addpet

interface AddPetOperations {
    fun onNameFieldValueChanged(name: String)
    fun onNameFieldCancelClicked()
    fun onDatePickerTextFieldValueChanged(value: String)
    fun onDatePickerTextFieldClicked()
    fun datePickerOnDismissRequest()
    fun datePickerOnConfirmedButtonClicked()
    fun datePickerOnDismissedButtonClicked()
    fun speciesMenuOnExpandedChanged()
    fun speciesMenuOnDropdownMenuItemClicked(item: String)
    fun speciesMenuOnDismissRequest()
    fun breedMenuOnExpandedChanged()
    fun breedMenuOnDropdownMenuItemClicked(item: String)
    fun breedMenuOnDismissRequest()
    fun onCancelButtonClicked()
    fun onNavigateButtonClicked(stage: AddPetScreenStage)
    fun onDoneButtonClicked()
    fun onWeightFieldValueChanged(value: String)
    fun onWeightFieldCancelClicked()
    fun onHeightFieldValueChanged(value: String)
    fun onHeightFieldCancelClicked()
    fun onLengthFieldValueChanged(value: String)
    fun onLengthFieldCancelClicked()
    fun onCircuitFieldValueChanged(value: String)
    fun onCircuitFieldCancelClicked()
    fun onDescriptionTextFieldValueChanged(description: String)

}