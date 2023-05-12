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

    @Query("SELECT * FROM pet_weight_history where pet_id = :petId ORDER BY measurement_timestamp ASC")
    fun getPetWeightHistory(petId: String): Flow<List<PetWeightEntity>>

    @Query("SELECT * FROM pet_height_history where pet_id = :petId ORDER BY measurement_timestamp ASC")
    fun getPetHeightHistory(petId: String): Flow<List<PetHeightEntity>>

    @Query("SELECT * FROM pet_length_history where pet_id = :petId ORDER BY measurement_timestamp ASC")
    fun getPetLengthHistory(petId: String): Flow<List<PetLengthEntity>>

    @Query("SELECT * FROM pet_circuit_history where pet_id = :petId ORDER BY measurement_timestamp ASC")
    fun getPetCircuitHistory(petId: String): Flow<List<PetCircuitEntity>>

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
    suspend fun addNewPet(
        petGeneralEntity: PetGeneralEntity,
        petWeightEntity: PetWeightEntity,
        petHeightEntity: PetHeightEntity?,
        petLengthEntity: PetLengthEntity?,
        petCircuitEntity: PetCircuitEntity?
    ) {
        addPetGeneralInfo(petGeneralEntity)
        addPetWeight(petWeightEntity)
        petHeightEntity?.let { addPetHeight(it) } ?: addPetHeight(
            PetHeightEntity(
                UUID.randomUUID(), petGeneralEntity.id, measurementDate = Instant.now(), 0.0
            )
        )
        petLengthEntity?.let { addPetLength(it) } ?: addPetLength(
            PetLengthEntity(
                UUID.randomUUID(), petGeneralEntity.id, measurementDate = Instant.now(), 0.0
            )
        )
        petCircuitEntity?.let { addPetCircuit(it) } ?: addPetCircuit(
            PetCircuitEntity(
                UUID.randomUUID(), petGeneralEntity.id, measurementDate = Instant.now(), 0.0
            )
        )
    }

    @Transaction
    suspend fun addPetDimensions(
        petHeightEntity: PetHeightEntity?,
        petLengthEntity: PetLengthEntity?,
        petCircuitEntity: PetCircuitEntity?
    ) {
        petHeightEntity?.let { addPetHeight(it) }
        petLengthEntity?.let { addPetLength(it) }
        petCircuitEntity?.let { addPetCircuit(it) }

    }


}