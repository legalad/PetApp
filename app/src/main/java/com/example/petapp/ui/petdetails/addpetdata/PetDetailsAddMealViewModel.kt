package com.example.petapp.ui.petdetails.addpetdata

import android.app.Application
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.petapp.R
import com.example.petapp.data.*
import com.example.petapp.model.util.Contstans
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import java.time.LocalTime
import java.time.OffsetTime
import java.time.ZoneOffset
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

    var uiState: PetDetailsAddMealUiState by mutableStateOf(PetDetailsAddMealUiState.Loading)
        private set

    private val _successUiState = MutableStateFlow(PetDetailsAddMealUiState.Success())

    val successUiState: StateFlow<PetDetailsAddMealUiState.Success> = _successUiState
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(Contstans.TIMEOUT_MILLIS),
            initialValue = _successUiState.value
        )

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
        //TODO change
        _successUiState.update {
            it.copy(
                mealTypeMenuSelectedOption = MealType.values().firstOrNull { it.nameId == item }
                    ?: MealType.NONE,
                mealTypeMenuExpanded = false
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
        var output = true
        if (_successUiState.value.mealTypeMenuSelectedOption != MealType.NONE)
            viewModelScope.launch(Dispatchers.IO) {
                petsDashboardRepository.addPetMeal(
                    PetMealEntity(
                        id = UUID.randomUUID(),
                        pet_id = UUID.fromString(petId),
                        time = OffsetTime.of(
                            LocalTime.of(
                                _successUiState.value.timePickerState.hour,
                                _successUiState.value.timePickerState.minute
                            ), ZoneOffset.UTC
                        ),
                        type = _successUiState.value.mealTypeMenuSelectedOption,
                        petFoodId = null
                    )
                )
            }
        else {
            _successUiState.update {
                it.copy(
                    mealTypeErrorMessage = R.string.components_forms_text_field_supporting_text_error_message_weight,
                    isMealTypeValid = false
                )
            }
            output = false
        }
        return output
    }


}