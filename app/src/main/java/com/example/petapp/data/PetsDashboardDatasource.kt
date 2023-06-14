package com.example.petapp.data

import kotlinx.coroutines.flow.Flow

interface PetsDashboardDatasource {
    fun getPets(): Flow<List<PetGeneralEntity>>
    fun getPet(petId: String): PetGeneralEntity?
    fun getWeight(id: String): PetWeightEntity?

    fun getHeight(id: String): PetHeightEntity?

    fun getLength(id: String): PetLengthEntity?

    fun getCircuit(id: String): PetCircuitEntity?

    fun getDashboard(): Flow<Map<PetDashboardView, List<PetMealEntity>>>

    fun getPetWeightHistory(petId: String): Flow<List<PetWeightEntity>>
    fun getPetHeightHistory(petId: String): Flow<List<PetHeightEntity>>
    fun getPetLengthHistory(petId: String): Flow<List<PetLengthEntity>>
    fun getPetCircuitHistory(petId: String): Flow<List<PetCircuitEntity>>
    fun getPetLastWaterChanged(petId: String): Flow<PetWaterEntity?>
    fun getPetDetails(petId: String): Flow<PetDetailsView>
    fun getPetMeals(petId: String): Flow<List<PetMealEntity>>

    suspend fun addPetWeight(weightEntity: PetWeightEntity)

    suspend fun addPetDimensions(petHeightEntity: PetHeightEntity?, petLengthEntity: PetLengthEntity?, petCircuitEntity: PetCircuitEntity?)
    suspend fun addPetGeneralInfo(pet: PetGeneralEntity)
    suspend fun addNewPet(petGeneralEntity: PetGeneralEntity, petWeightEntity: PetWeightEntity?, petHeightEntity: PetHeightEntity?, petLengthEntity: PetLengthEntity?, petCircuitEntity: PetCircuitEntity?)
    suspend fun addPetWaterChangeData(waterEntity: PetWaterEntity)
    suspend fun addPetMeal(petMealEntity: PetMealEntity)
    suspend fun updatePetMeal(petMealEntity: PetMealEntity)
    suspend fun updatePetGeneral(petGeneralEntity: PetGeneralEntity)
    suspend fun updateWeight(petWeightEntity: PetWeightEntity)

    suspend fun updateDimension(petHeightEntity: PetHeightEntity)

    suspend fun updateDimension(petLengthEntity: PetLengthEntity)

    suspend fun updateDimension(petCircuitEntity: PetCircuitEntity)
    suspend fun deletePetMeal(petMealEntity: PetMealEntity)
    suspend fun deletePetWeight(petWeightEntity: PetWeightEntity)
    suspend fun deletePetDimension(petHeightEntity: PetHeightEntity)
    suspend fun deletePetDimension(petLengthEntity: PetLengthEntity)
    suspend fun deletePetDimension(petCircuitEntity: PetCircuitEntity)
}