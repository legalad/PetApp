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

    override fun getPet(petId: String): PetGeneralEntity? {
        return petsDashboardLocalDatasource.getPet(petId = petId)
    }

    override fun getWeight(id: String): PetWeightEntity? {
        return petsDashboardLocalDatasource.getWeight(id = id)
    }

    override fun getHeight(id: String): PetHeightEntity? {
        return petsDashboardLocalDatasource.getHeight(id = id)
    }

    override fun getLength(id: String): PetLengthEntity? {
        return petsDashboardLocalDatasource.getLength(id = id)
    }

    override fun getCircuit(id: String): PetCircuitEntity? {
        return petsDashboardLocalDatasource.getCircuit(id = id)
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
        petWeightEntity: PetWeightEntity?,
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

    override suspend fun updatePetGeneral(petGeneralEntity: PetGeneralEntity) {
        petsDashboardLocalDatasource.updatePetGeneral(petGeneralEntity = petGeneralEntity)
    }

    override suspend fun updateWeight(petWeightEntity: PetWeightEntity) {
        petsDashboardLocalDatasource.updateWeight(petWeightEntity = petWeightEntity)
    }

    override suspend fun updateDimension(petHeightEntity: PetHeightEntity) {
        petsDashboardLocalDatasource.updateDimension(petHeightEntity = petHeightEntity)
    }

    override suspend fun updateDimension(petLengthEntity: PetLengthEntity) {
        petsDashboardLocalDatasource.updateDimension(petLengthEntity = petLengthEntity)
    }

    override suspend fun updateDimension(petCircuitEntity: PetCircuitEntity) {
        petsDashboardLocalDatasource.updateDimension(petCircuitEntity = petCircuitEntity)
    }

    override suspend fun deletePetMeal(petMealEntity: PetMealEntity) {
        petsDashboardLocalDatasource.deletePetMeal(petMealEntity = petMealEntity)
    }
    override suspend fun deletePetWeight(petWeightEntity: PetWeightEntity) {
        petsDashboardLocalDatasource.deletePetWeight(petWeightEntity = petWeightEntity)
    }

    override suspend fun deletePetDimension(petHeightEntity: PetHeightEntity) {
        petsDashboardLocalDatasource.deletePetDimension(petHeightEntity = petHeightEntity)
    }

    override suspend fun deletePetDimension(petLengthEntity: PetLengthEntity) {
        petsDashboardLocalDatasource.deletePetDimension(petLengthEntity = petLengthEntity)
    }

    override suspend fun deletePetDimension(petCircuitEntity: PetCircuitEntity) {
        petsDashboardLocalDatasource.deletePetDimension(petCircuitEntity = petCircuitEntity)
    }
}