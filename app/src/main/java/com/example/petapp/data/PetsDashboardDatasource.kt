package com.example.petapp.data

import kotlinx.coroutines.flow.Flow

interface PetsDashboardDatasource {
    fun getPets(): Flow<List<PetGeneralEntity>>

    fun getDashboard(): Flow<List<PetDashboardView>>

    fun getPetWeightHistory(petId: String): Flow<List<PetWeightEntity>>

    fun getPetDetails(petId: String): Flow<PetDetailsView>

    suspend fun addPetWeight(weightEntity: PetWeightEntity)

    suspend fun addPetDimensions(petHeightEntity: PetHeightEntity?, petLengthEntity: PetLengthEntity?, petCircuitEntity: PetCircuitEntity?)
    suspend fun addPetGeneralInfo(pet: PetGeneralEntity)
    suspend fun addNewPet(petGeneralEntity: PetGeneralEntity, petWeightEntity: PetWeightEntity, petHeightEntity: PetHeightEntity?, petLengthEntity: PetLengthEntity?, petCircuitEntity: PetCircuitEntity?)
}