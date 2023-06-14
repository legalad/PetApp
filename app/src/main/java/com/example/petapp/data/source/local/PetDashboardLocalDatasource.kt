package com.example.petapp.data.source.local

import com.example.petapp.data.*
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow

class PetDashboardLocalDatasource internal constructor(
    private val petsDashboardDao: PetsDashboardDao,
    private val ioDispatcher: CoroutineDispatcher = Dispatchers.IO
) : PetsDashboardDatasource {
    override fun getPets(): Flow<List<PetGeneralEntity>> {
        return petsDashboardDao.getPets()
    }

    override fun getPet(petId: String): PetGeneralEntity? {
       return petsDashboardDao.getPet(petId = petId)
    }

    override fun getWeight(id: String): PetWeightEntity? {
        return petsDashboardDao.getWeight(id = id)
    }

    override fun getHeight(id: String): PetHeightEntity? {
        return petsDashboardDao.getHeight(id = id)
    }

    override fun getLength(id: String): PetLengthEntity? {
        return petsDashboardDao.getLength(id = id)
    }

    override fun getCircuit(id: String): PetCircuitEntity? {
        return petsDashboardDao.getCircuit(id = id)
    }

    override fun getDashboard(): Flow<Map<PetDashboardView, List<PetMealEntity>>>{
        return petsDashboardDao.getDashboardView()
    }

    override fun getPetWeightHistory(petId: String): Flow<List<PetWeightEntity>> {
        return petsDashboardDao.getPetWeightHistory(petId = petId)
    }

    override fun getPetHeightHistory(petId: String): Flow<List<PetHeightEntity>> {
        return petsDashboardDao.getPetHeightHistory(petId = petId)
    }

    override fun getPetLengthHistory(petId: String): Flow<List<PetLengthEntity>> {
        return petsDashboardDao.getPetLengthHistory(petId = petId)
    }

    override fun getPetCircuitHistory(petId: String): Flow<List<PetCircuitEntity>> {
        return petsDashboardDao.getPetCircuitHistory(petId = petId)
    }

    override fun getPetLastWaterChanged(petId: String): Flow<PetWaterEntity?> {
        return petsDashboardDao.getPetLastWaterChanged(petId = petId)
    }

    override fun getPetDetails(petId: String): Flow<PetDetailsView> {
        return petsDashboardDao.getPetDetails(petId = petId)
    }

    override fun getPetMeals(petId: String): Flow<List<PetMealEntity>> {
        return petsDashboardDao.getPetMeals(petId = petId)
    }

    override suspend fun addPetWeight(weightEntity: PetWeightEntity) {
        petsDashboardDao.addPetWeight(weightEntity)
    }

    override suspend fun addPetDimensions(
        petHeightEntity: PetHeightEntity?,
        petLengthEntity: PetLengthEntity?,
        petCircuitEntity: PetCircuitEntity?
    ) {
        petsDashboardDao.addPetDimensions(
            petHeightEntity = petHeightEntity,
            petLengthEntity = petLengthEntity,
            petCircuitEntity = petCircuitEntity
        )
    }

    override suspend fun addPetGeneralInfo(pet: PetGeneralEntity) {
        petsDashboardDao.addPetGeneralInfo(pet)
    }

    override suspend fun addNewPet(
        petGeneralEntity: PetGeneralEntity,
        petWeightEntity: PetWeightEntity?,
        petHeightEntity: PetHeightEntity?,
        petLengthEntity: PetLengthEntity?,
        petCircuitEntity: PetCircuitEntity?
    ) {
        petsDashboardDao.addNewPet(
            petGeneralEntity = petGeneralEntity,
            petWeightEntity = petWeightEntity,
            petHeightEntity = petHeightEntity,
            petLengthEntity = petLengthEntity,
            petCircuitEntity = petCircuitEntity
        )
    }

    override suspend fun addPetWaterChangeData(waterEntity: PetWaterEntity) {
        petsDashboardDao.addPetWaterChangeData(waterEntity)
    }

    override suspend fun addPetMeal(petMealEntity: PetMealEntity) {
        petsDashboardDao.addPetMeal(petMealEntity = petMealEntity)
    }

    override suspend fun updatePetMeal(petMealEntity: PetMealEntity) {
        petsDashboardDao.updatePetMeal(petMealEntity = petMealEntity)
    }

    override suspend fun updatePetGeneral(petGeneralEntity: PetGeneralEntity) {
        petsDashboardDao.updatePetGeneral(petGeneralEntity = petGeneralEntity)
    }

    override suspend fun updateWeight(petWeightEntity: PetWeightEntity) {
        petsDashboardDao.updateWeight(petWeightEntity = petWeightEntity)
    }

    override suspend fun updateDimension(petHeightEntity: PetHeightEntity) {
        petsDashboardDao.updateDimension(petHeightEntity = petHeightEntity)
    }

    override suspend fun updateDimension(petLengthEntity: PetLengthEntity) {
        petsDashboardDao.updateDimension(petLengthEntity = petLengthEntity)
    }

    override suspend fun updateDimension(petCircuitEntity: PetCircuitEntity) {
        petsDashboardDao.updateDimension(petCircuitEntity = petCircuitEntity)
    }

    override suspend fun deletePetMeal(petMealEntity: PetMealEntity) {
        petsDashboardDao.deletePetMeal(petMealEntity = petMealEntity)
    }

    override suspend fun deletePetWeight(petWeightEntity: PetWeightEntity) {
        petsDashboardDao.deletePetWeight(petWeightEntity = petWeightEntity)
    }

    override suspend fun deletePetDimension(petHeightEntity: PetHeightEntity) {
        petsDashboardDao.deletePetDimension(petHeightEntity = petHeightEntity)
    }

    override suspend fun deletePetDimension(petLengthEntity: PetLengthEntity) {
        petsDashboardDao.deletePetDimension(petLengthEntity = petLengthEntity)
    }

    override suspend fun deletePetDimension(petCircuitEntity: PetCircuitEntity) {
        petsDashboardDao.deletePetDimension(petCircuitEntity = petCircuitEntity)
    }
}