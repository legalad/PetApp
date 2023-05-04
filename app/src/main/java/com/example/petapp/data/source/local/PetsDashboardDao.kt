package com.example.petapp.data.source.local

import androidx.room.*
import com.example.petapp.data.*
import kotlinx.coroutines.flow.Flow
import java.time.Instant
import java.util.*

@Dao
interface PetsDashboardDao {
    @Query("SELECT * FROM PET_GENERAL")
    fun getPets(): Flow<List<PetGeneralEntity>>

    @Query("SELECT * FROM pet_dashboard_view")
    fun getDashboardView(): Flow<List<PetDashboardView>>

    @Query("SELECT * FROM pet_details_view where petId = :petId")
    fun getPetDetails(petId: String): Flow<PetDetailsView>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun addPetGeneralInfo(pet: PetGeneralEntity)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun addPetWeight(weightEntity: PetWeightEntity)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun addPetHeight(weightEntity: PetHeightEntity)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun addPetLength(weightEntity: PetLengthEntity)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun addPetCircuit(weightEntity: PetCircuitEntity)

    @Transaction
    suspend fun addNewPet(petGeneralEntity: PetGeneralEntity, petWeightEntity: PetWeightEntity, petHeightEntity: PetHeightEntity?, petLengthEntity: PetLengthEntity?, petCircuitEntity: PetCircuitEntity?) {
        addPetGeneralInfo(petGeneralEntity)
        addPetWeight(petWeightEntity)
        if(petHeightEntity != null) addPetHeight(petHeightEntity) else addPetHeight(PetHeightEntity(
            UUID.randomUUID(), petGeneralEntity.id, measurementDate = Instant.now(), 0.0))
        if(petLengthEntity != null) addPetLength(petLengthEntity) else addPetLength(
            PetLengthEntity(
            UUID.randomUUID(), petGeneralEntity.id, measurementDate = Instant.now(), 0.0)
        )
        if(petCircuitEntity != null) addPetCircuit(petCircuitEntity) else addPetCircuit(PetCircuitEntity(
            UUID.randomUUID(), petGeneralEntity.id, measurementDate = Instant.now(), 0.0))
    }



}