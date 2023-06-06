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

    @Query("SELECT * FROM PET_GENERAL where pet_id = :petId")
    fun getPet(petId: String): PetGeneralEntity?

    @Query("SELECT * FROM PET_WEIGHT_HISTORY where id = :id")
    fun getWeight(id: String): PetWeightEntity?

    @Query("SELECT * FROM PET_HEIGHT_HISTORY where id = :id")
    fun getHeight(id: String): PetHeightEntity?

    @Query("SELECT * FROM PET_LENGTH_HISTORY where id = :id")
    fun getLength(id: String): PetLengthEntity?

    @Query("SELECT * FROM PET_CIRCUIT_HISTORY where id = :id")
    fun getCircuit(id: String): PetCircuitEntity?

    @Query("SELECT * FROM pet_dashboard_view pd LEFT JOIN pet_meal pm ON pd.petId = pm.pet_id")
    fun getDashboardView(): Flow<Map<PetDashboardView, List<PetMealEntity>>>

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

    @Query("SELECT * FROM pet_water_change_history where pet_id = :petId ORDER BY measurement_timestamp DESC LIMIT 1")
    fun getPetLastWaterChanged(petId: String): Flow<PetWaterEntity?>

    @Query("SELECT * FROM pet_meal where pet_id = :petId ORDER BY time ASC")
    fun getPetMeals(petId: String): Flow<List<PetMealEntity>>

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

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun addPetWaterChangeData(waterEntity: PetWaterEntity)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun addPetMeal(petMealEntity: PetMealEntity)

    @Update
    suspend fun updatePetMeal(petMealEntity: PetMealEntity)

    @Update
    suspend fun updatePetGeneral(petGeneralEntity: PetGeneralEntity)

    @Update
    suspend fun updateWeight(petWeightEntity: PetWeightEntity)

    @Update
    suspend fun updateDimension(petHeightEntity: PetHeightEntity)

    @Update
    suspend fun updateDimension(petLengthEntity: PetLengthEntity)

    @Update
    suspend fun updateDimension(petCircuitEntity: PetCircuitEntity)

    @Delete
    suspend fun deletePetMeal(petMealEntity: PetMealEntity)

    @Delete
    suspend fun deletePetWeight(petWeightEntity: PetWeightEntity)
    @Delete
    suspend fun deletePetDimension(petHeightEntity: PetHeightEntity)
    @Delete
    suspend fun deletePetDimension(petLengthEntity: PetLengthEntity)
    @Delete
    suspend fun deletePetDimension(petCircuitEntity: PetCircuitEntity)
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