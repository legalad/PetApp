package com.example.petapp.data

import kotlinx.coroutines.flow.Flow

interface PetsDashboardRepository {
    fun getPets(): Flow<List<PetGeneralEntity>>
    fun getDashboard(): Flow<List<PetDashboardView>>

    fun getPetDetails(petId: String): Flow<PetDetailsView>

    fun getPetWeightHistory(petId: String): Flow<List<PetWeightEntity>>
    fun getPetHeightHistory(petId: String): Flow<List<PetHeightEntity>>
    fun getPetLengthHistory(petId: String): Flow<List<PetLengthEntity>>
    fun getPetCircuitHistory(petId: String): Flow<List<PetCircuitEntity>>
    suspend fun addPetWeight(weightEntity: PetWeightEntity)
    suspend fun addPetGeneralInfo(pet: PetGeneralEntity)
    suspend fun addNewPet(
        petGeneralEntity: PetGeneralEntity,
        petWeightEntity: PetWeightEntity,
        petHeightEntity: PetHeightEntity?,
        petLengthEntity: PetLengthEntity?,
        petCircuitEntity: PetCircuitEntity?
    )

    suspend fun addPetDimensions(petHeightEntity: PetHeightEntity?, petLengthEntity: PetLengthEntity?, petCircuitEntity: PetCircuitEntity?)
}