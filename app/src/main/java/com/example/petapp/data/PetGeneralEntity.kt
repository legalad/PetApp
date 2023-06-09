package com.example.petapp.data

import android.net.Uri
import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.example.petapp.model.PetGender
import com.example.petapp.model.Species
import java.time.Instant
import java.util.UUID

@Entity(
    tableName = "pet_general"
)
data class PetGeneralEntity(
    @PrimaryKey(autoGenerate = false)
    @ColumnInfo(name = "pet_id")
    val id: UUID,
    val name: String,
    val gender: PetGender,
    val species: Species,
    val breed: Int?,
    @ColumnInfo(name = "birth_timestamp")
    val birthDate: Instant,
    val description: String?,
    val imageUri: Uri?
)
