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

    override suspend fun addPetGeneralInfo(pet: PetGeneralEntity) {
        petsDashboardDao.addPetGeneralInfo(pet)
    }

    override suspend fun addNewPet(
        petGeneralEntity: PetGeneralEntity,
        petWeightEntity: PetWeightEntity,
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
}