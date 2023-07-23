package com.example.petapp.ui.petdetails.fooddashboard

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.petapp.data.Async
import com.example.petapp.data.PetsDashboardRepository
import com.example.petapp.data.UserSettingsDataRepository
import com.example.petapp.data.WorkManagerReminderRepository
import com.example.petapp.model.PetMealUiState
import com.example.petapp.model.util.Contstans
import com.example.petapp.model.util.toPetMealUiStateList
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class PetFoodDashboardViewModel @Inject constructor(
    settingsDataRepository: UserSettingsDataRepository,
    private val petsDashboardRepository: PetsDashboardRepository,
    private val workManagerReminderRepository: WorkManagerReminderRepository,
    savedStateHandle: SavedStateHandle
) : ViewModel() {

    private val petId: String = checkNotNull(savedStateHandle["petId"])

    private val _successUiState = MutableStateFlow(PetFoodDashboardUiState.Success())

    private val _asyncData = combine(
        petsDashboardRepository.getPetMeals(petId = petId),
        petsDashboardRepository.getPetDetails(petId = petId),
        settingsDataRepository.getUnit()
    ) {
            meals, pet, unit ->
        _successUiState.update {
            it.copy(
                petName = pet.name,
                petMeals = meals.toPetMealUiStateList(unit),
                unit = unit
            )
        }
    }
        .map { Async.Success(_successUiState.value) }
        .catch<Async<PetFoodDashboardUiState.Success>> { emit(Async.Error("Error")) }

    val uiState: StateFlow<PetFoodDashboardUiState> =
        combine(_asyncData, _successUiState) { async, success ->
            when (async) {
                Async.Loading -> PetFoodDashboardUiState.Loading
                is Async.Success -> success
                is Async.Error -> PetFoodDashboardUiState.Error("Error")
            }
        }.stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(Contstans.TIMEOUT_MILLIS),
            initialValue = PetFoodDashboardUiState.Loading
        )


    fun getPetId(): String {
        return petId
    }

    //check if list is empty (checks whether long clicked action was taken before), if not check if list contain element passed by argument, if true minus it from list else add
    fun onMealItemClicked(item: PetMealUiState) {
        val selectedMeals = _successUiState.value.selectedMeals
        if (selectedMeals.isNotEmpty()) {
            val outputList = selectedMeals.find {
                it == item.mealEntity
            }?.let {
                selectedMeals.minus(it)
            } ?: selectedMeals.plus(item.mealEntity)
            _successUiState.update {
                it.copy(
                    selectedMeals = outputList,
                    petMeals = it.petMeals.map { meal -> if(meal.mealEntity == item.mealEntity) meal.copy(isClicked = !meal.isClicked) else meal}
                )
            }
        }
    }

    fun onMealItemLongClicked(item: PetMealUiState) {
        val selectedMeals = _successUiState.value.selectedMeals
        val outputList = selectedMeals.find {
            it == item.mealEntity
        }?.let {
            selectedMeals.minus(it)
        } ?: selectedMeals.plus(item.mealEntity)
        _successUiState.update {
            it.copy(
                selectedMeals = outputList,
                petMeals = it.petMeals.map { meal -> if(meal.mealEntity == item.mealEntity) meal.copy(isClicked = !meal.isClicked) else meal}
            )
        }
    }

    fun clearSelectedMealItems(delay: Long) {
        viewModelScope.launch {
            delay(delay)
            _successUiState.update { success ->
                success.copy(
                    selectedMeals = emptyList(),
                    petMeals = success.petMeals.map { it.copy(isClicked = false) }
                )
            }
        }
    }

    fun onRemindIconClicked(meal: PetMealUiState) {
        if (meal.isReminderIconClicked) {
            viewModelScope.launch(Dispatchers.IO) {
                workManagerReminderRepository.cancelReminder(
                    meal.mealEntity.id
                )
            }
        } else {
            viewModelScope.launch(Dispatchers.IO) {
                workManagerReminderRepository.scheduleMealReminder(
                    offsetTime = meal.mealEntity.time,
                    mealId = meal.mealEntity.id
                )
            }
        }
    }

    fun deletePetMeals() {
        viewModelScope.launch(Dispatchers.IO) {
            _successUiState.value.selectedMeals.forEach {meal ->
                petsDashboardRepository.deletePetMeal(meal)
                workManagerReminderRepository.cancelReminder(meal.id)
            }
            clearSelectedMealItems(0)
        }
    }
}