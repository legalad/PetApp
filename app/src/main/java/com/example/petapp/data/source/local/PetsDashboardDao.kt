package com.example.petapp.data.source.local

import androidx.room.*
import com.example.petapp.data.*
import kotlinx.coroutines.flow.Flow

@Dao
interface PetsDashboardDao {
    @Query("SELECT * FROM PET_GENERAL")
    fun getPets(): Flow<List<PetGeneralEntity>>

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
        if(petHeightEntity != null) addPetHeight(petHeightEntity)
        if(petLengthEntity != null) addPetLength(petLengthEntity)
        if(petCircuitEntity != null) addPetCircuit(petCircuitEntity)
    }



}