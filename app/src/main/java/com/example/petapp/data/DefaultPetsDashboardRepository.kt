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
}