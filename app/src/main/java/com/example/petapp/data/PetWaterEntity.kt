package com.example.petapp.data

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import java.time.Instant
import java.util.*

@Entity(tableName = "pet_water_change_history")
data class PetWaterEntity(
    @PrimaryKey(autoGenerate = false)
    val id: UUID,
    @ColumnInfo(name = "pet_id")
    val pet_id: UUID,
    @ColumnInfo(name = "measurement_timestamp")
    val measurementDate: Instant
)
