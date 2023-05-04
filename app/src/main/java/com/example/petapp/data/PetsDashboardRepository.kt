package com.example.petapp.data

import kotlinx.coroutines.flow.Flow

interface PetsDashboardRepository {
    fun getPets(): Flow<List<PetGeneralEntity>>
    fun getDashboard(): Flow<List<PetDashboardView>>

    fun getPetDetails(petId: String): Flow<PetDetailsView>
    suspend fun addPetWeight(weightEntity: PetWeightEntity)
    suspend fun addPetGeneralInfo(pet: PetGeneralEntity)
    suspend fun addNewPet(
        petGeneralEntity: PetGeneralEntity,
        petWeightEntity: PetWeightEntity,
        petHeightEntity: PetHeightEntity?,
        petLengthEntity: PetLengthEntity?,
        petCircuitEntity: PetCircuitEntity?
    )
}