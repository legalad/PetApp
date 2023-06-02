package com.example.petapp.data

import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow

class DefaultPetsDashboardRepository (
    private val petsDashboardLocalDatasource: PetsDashboardDatasource,
    private val ioDispatcher: CoroutineDispatcher = Dispatchers.IO
        ): PetsDashboardRepository {
    override fun getPets(): Flow<List<PetGeneralEntity>> {
        return petsDashboardLocalDatasource.getPets()
    }

    override fun getDashboard(): Flow<Map<PetDashboardView, List<PetMealEntity>>> {
        return petsDashboardLocalDatasource.getDashboard()
    }

    override fun getPetDetails(petId: String): Flow<PetDetailsView> {
        return petsDashboardLocalDatasource.getPetDetails(petId = petId)
    }

    override fun getPetWeightHistory(petId: String): Flow<List<PetWeightEntity>> {
        return petsDashboardLocalDatasource.getPetWeightHistory(petId = petId)
    }

    override fun getPetHeightHistory(petId: String): Flow<List<PetHeightEntity>> {
        return petsDashboardLocalDatasource.getPetHeightHistory(petId = petId)
    }

    override fun getPetLengthHistory(petId: String): Flow<List<PetLengthEntity>> {
        return petsDashboardLocalDatasource.getPetLengthHistory(petId = petId)
    }

    override fun getPetCircuitHistory(petId: String): Flow<List<PetCircuitEntity>> {
        return petsDashboardLocalDatasource.getPetCircuitHistory(petId = petId)
    }

    override fun getPetLastWaterChanged(petId: String): Flow<PetWaterEntity?> {
        return petsDashboardLocalDatasource.getPetLastWaterChanged(petId = petId)
    }

    override fun getPetMeals(petId: String): Flow<List<PetMealEntity>> {
        return petsDashboardLocalDatasource.getPetMeals(petId = petId)
    }

    override suspend fun addPetWeight(weightEntity: PetWeightEntity) {
        petsDashboardLocalDatasource.addPetWeight(weightEntity)
    }

    override suspend fun addPetGeneralInfo(pet: PetGeneralEntity) {
        petsDashboardLocalDatasource.addPetGeneralInfo(pet)
    }

    override suspend fun addNewPet(
        petGeneralEntity: PetGeneralEntity,
        petWeightEntity: PetWeightEntity,
        petHeightEntity: PetHeightEntity?,
        petLengthEntity: PetLengthEntity?,
        petCircuitEntity: PetCircuitEntity?
    ) {
        petsDashboardLocalDatasource.addNewPet(
            petGeneralEntity = petGeneralEntity,
            petWeightEntity = petWeightEntity,
            petHeightEntity = petHeightEntity,
            petLengthEntity = petLengthEntity,
            petCircuitEntity = petCircuitEntity
        )
    }

    override suspend fun addPetDimensions(
        petHeightEntity: PetHeightEntity?,
        petLengthEntity: PetLengthEntity?,
        petCircuitEntity: PetCircuitEntity?
    ) {
        petsDashboardLocalDatasource.addPetDimensions(
            petHeightEntity = petHeightEntity,
            petLengthEntity = petLengthEntity,
            petCircuitEntity = petCircuitEntity
        )
    }

    override suspend fun addPetWaterChangeData(waterEntity: PetWaterEntity) {
        petsDashboardLocalDatasource.addPetWaterChangeData(waterEntity)
    }

    override suspend fun addPetMeal(petMealEntity: PetMealEntity) {
        petsDashboardLocalDatasource.addPetMeal(petMealEntity = petMealEntity)
    }

    override suspend fun updatePetMeal(petMealEntity: PetMealEntity) {
        petsDashboardLocalDatasource.updatePetMeal(petMealEntity = petMealEntity)
    }

    override suspend fun deletePetMeal(petMealEntity: PetMealEntity) {
        petsDashboardLocalDatasource.deletePetMeal(petMealEntity = petMealEntity)
    }
}