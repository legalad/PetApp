package com.example.petapp.data.source.local

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.example.petapp.data.*
import com.example.petapp.model.util.DateConverter
import com.example.petapp.model.util.EnumConverter
import com.example.petapp.model.util.UUIDConverter

//TODO add views in future
@Database(
    entities = [
        PetGeneralEntity::class,
        PetWeightEntity::class,
        PetHeightEntity::class,
        PetLengthEntity::class,
        PetCircuitEntity::class
               ],
    version = 1,
    exportSchema = false
)

@TypeConverters(UUIDConverter::class, DateConverter::class, EnumConverter::class)
abstract class PetAppDatabase : RoomDatabase() {
    abstract fun petsDashboardDao(): PetsDashboardDao
}