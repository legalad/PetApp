package com.example.petapp.data

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import java.util.Date
import java.util.UUID

@Entity(tableName = "pet_height_history")
data class PetHeightEntity(
    @PrimaryKey(autoGenerate = false)
    val id: UUID,
    @ColumnInfo(name = "pet_id")
    val pet_id: UUID,
    @ColumnInfo(name = "measurement_date")
    val measurementDate: Date,
    @ColumnInfo(name = "height")
    val value: Double
)
