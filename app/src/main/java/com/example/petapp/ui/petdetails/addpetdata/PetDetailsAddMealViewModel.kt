package com.example.petapp.ui.petdetails.addpetdata

import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.TimePickerState
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.android.datastore.UserPreferences
import com.example.petapp.R
import com.example.petapp.data.*
import com.example.petapp.model.WeightUnit
import com.example.petapp.model.util.Contstans
import com.example.petapp.model.util.Formatters
import com.example.petapp.model.util.Validators
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import java.time.*
import java.util.*
import javax.inject.Inject

@OptIn(ExperimentalMaterial3Api::class)
@HiltViewModel
class PetDetailsAddMealViewModel @Inject constructor(
    private val petsDashboardRepository: PetsDashboardRepository,
    private val settingsDataRepository: UserSettingsDataRepository,
    savedStateHandle: SavedStateHandle
) : ViewModel() {
    private val petId: String = checkNotNull(savedStateHandle["petId"])
    private val mealId: String? = savedStateHandle["mealId"]

    private val _successUiState = MutableStateFlow(PetDetailsAddMealUiState.Success())

    private val _asyncData = settingsDataRepository.getUnit().map { unit ->
        _successUiState.update {
            it.copy(
                unit = unit,
                weightFieldValuePlaceholder = when (unit) {
                    UserPreferences.Unit.METRIC -> R.string.util_unit_weight_kg
                    UserPreferences.Unit.IMPERIAL -> R.string.util_unit_weight_pounds
                    UserPreferences.Unit.UNRECOGNIZED -> R.string.util_unit_weight_kg
                },
                selectedWeightUnit = when (unit) {
                    UserPreferences.Unit.METRIC -> WeightUnit.KILOGRAMS
                    UserPreferences.Unit.IMPERIAL -> WeightUnit.POUNDS
                    UserPreferences.Unit.UNRECOGNIZED -> WeightUnit.KILOGRAMS
                }
            )
        }
    }
        .map { Async.Success(_successUiState.value) }
        .catch<Async<PetDetailsAddMealUiState.Success>> { emit(Async.Error("Error")) }

    val uiState: StateFlow<PetDetailsAddMealUiState> =
        combine(_asyncData, _successUiState) { async, success ->
            when (async) {
                Async.Loading -> PetDetailsAddMealUiState.Loading
                is Async.Success -> success
                is Async.Error -> PetDetailsAddMealUiState.Error("Error")
            }
        }.stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(Contstans.TIMEOUT_MILLIS),
            initialValue = PetDetailsAddMealUiState.Loading
        )

    init {
        mealId?.let {
            viewModelScope.launch(Dispatchers.IO) {
                val meal = petsDashboardRepository.getPetMeal(mealId = mealId)
                meal?.let { meal ->
                    _successUiState.update {
                        it.copy(
                            mealTypeMenuSelectedOption = meal.mealType,
                            foodTypeRadioSelectedOption = meal.foodType,
                            weightFieldValue = Formatters.getWeightString(
                                value = meal?.amount ?: 0.0,
                                unit = _successUiState.value.unit
                            ),
                            timePickerState = TimePickerState(
                                initialHour = meal.time.atDate(LocalDate.now(ZoneId.systemDefault())).hour,
                                initialMinute = meal.time.atDate(LocalDate.now(ZoneId.systemDefault())).minute,
                                is24Hour = true
                            )
                        )
                    }
                }
            }
        }
    }

    fun getPetId(): String {
        return petId
    }

    fun onTimePickerTextFieldClicked() {
        _successUiState.update {
            it.copy(
                showTimePicker = !it.showTimePicker
            )
        }
    }

    fun onTimePickerDialogCancelClicked() {
        _successUiState.update {
            it.copy(
                showTimePicker = false
            )
        }
    }

    fun onTimePickerDialogConfirmClicked() {
        _successUiState.update {
            it.copy(
                showTimePicker = false
            )
        }
    }

    fun onTimePickerDialogSwitchIconClicked() {
        _successUiState.update {
            it.copy(
                showingPicker = !it.showingPicker
            )
        }
    }

    fun mealTypeMenuOnExpandedChanged() {
        _successUiState.update {
            it.copy(
                mealTypeMenuExpanded = !it.mealTypeMenuExpanded
            )
        }
    }

    fun mealTypeMenuOnDropdownMenuItemClicked(item: Int) {
        val selectedMeal = MealType.values()[item]
        _successUiState.update {
            it.copy(
                mealTypeMenuSelectedOption = selectedMeal,
                mealTypeMenuExpanded = false,
                isMealTypeValid = true
            )
        }
    }

    fun mealTypeMenuOnDismissRequest() {
        _successUiState.update {
            it.copy(
                mealTypeMenuExpanded = false
            )
        }
    }

    fun foodMenuOnExpandedChanged() {
        _successUiState.update {
            it.copy(
                foodMenuExpanded = !it.foodMenuExpanded
            )
        }
    }

    fun foodMenuOnDropdownMenuItemClicked(item: String) {
        _successUiState.update {
            it.copy(
                foodMenuSelectedOptionText = item,
                foodMenuExpanded = false
            )
        }
    }

    fun foodMenuOnDismissRequest() {
        _successUiState.update {
            it.copy(
                foodMenuExpanded = false
            )
        }
    }

    fun onRadioButtonSelectedOptionClicked(value: Int) {
        _successUiState.update { state ->
            state.copy(
                foodTypeRadioSelectedOption = FoodTypeEnum.values()
                    .firstOrNull() { it.nameId == value } ?: state.foodTypeRadioSelectedOption
            )
        }
    }

    fun onDoneButtonCLicked(): Boolean {
        var output = false
        val isMealTypeValid = validateMealType(_successUiState.value.mealTypeMenuSelectedOption)
        val isWeightValid = validateWeight(_successUiState.value.weightFieldValue)

        if (isMealTypeValid && isWeightValid) {
            Formatters.getMetricWeightValue(
                _successUiState.value.weightFieldValue,
                _successUiState.value.selectedWeightUnit
            ).let { weight ->
                viewModelScope.launch(Dispatchers.IO) {
                    mealId?.let { mealId ->
                        petsDashboardRepository.getPetMeal(mealId = mealId)?.let { meal ->
                            petsDashboardRepository.updatePetMeal(
                                petMealEntity = meal.copy(
                                    time = OffsetTime.of(
                                        LocalTime.of(
                                            _successUiState.value.timePickerState.hour,
                                            _successUiState.value.timePickerState.minute
                                        ), ZoneOffset.UTC
                                    ),
                                    mealType = _successUiState.value.mealTypeMenuSelectedOption,
                                    foodType = _successUiState.value.foodTypeRadioSelectedOption,
                                    amount = weight
                                )
                            )
                        }
                    } ?: petsDashboardRepository.addPetMeal(
                        PetMealEntity(
                            id = UUID.randomUUID(),
                            pet_id = UUID.fromString(petId),
                            time = OffsetTime.of(
                                LocalTime.of(
                                    _successUiState.value.timePickerState.hour,
                                    _successUiState.value.timePickerState.minute
                                ), ZoneOffset.UTC
                            ),
                            mealType = _successUiState.value.mealTypeMenuSelectedOption,
                            foodType = _successUiState.value.foodTypeRadioSelectedOption,
                            amount = weight,
                            petFoodId = null,
                            reminderId = null
                        )
                    )
                }
                output = true
            }
        }
        return output
    }

    private fun validateWeight(string: String): Boolean {
        return if (string.isEmpty()) {
            _successUiState.update {
                it.copy(
                    weightErrorMessage = R.string.components_forms_text_field_supporting_text_error_message_weight,
                    isWeightValid = false
                )
            }
            false
        } else true
    }

    private fun validateMealType(mealType: MealType): Boolean {
        return if (mealType == MealType.NONE) {
            _successUiState.update {
                it.copy(
                    mealTypeErrorMessage = R.string.components_forms_date_picker_supporting_text_error_message_meal_type,
                    isMealTypeValid = false
                )
            }
            false
        } else true
    }

    fun onWeightFieldValueChanged(value: String) {
        _successUiState.update {
            it.copy(
                weightFieldValue = Validators.validateNumberToTwoDecimalPlaces(value),
                isWeightChanged = true,
                isWeightValid = true
            )
        }
    }

    fun onWeightFieldCancelClicked() {
        _successUiState.update {
            it.copy(weightFieldValue = "")
        }
    }

    fun onWeightUnitPickerOnExpandedChange(value: Boolean) {
        _successUiState.update {
            it.copy(
                isWeightUnitPickerExpanded = value
            )
        }
    }

    fun onWeightUnitPickerOnDismissRequest() {
        _successUiState.update {
            it.copy(
                isWeightUnitPickerExpanded = false
            )
        }
    }

    fun onWeightUnitPickerDropdownMenuItemClicked(ordinal: Int) {
        _successUiState.update {
            it.copy(
                selectedWeightUnit = WeightUnit.values()[ordinal],
                isWeightUnitPickerExpanded = false
            )
        }
    }

    fun onFocusCleared() {
        _successUiState.update {
            it.copy(hideKeyboard = false)
        }
    }

    fun hideKeyboard() {
        _successUiState.update {
            it.copy(hideKeyboard = true)
        }
    }


}