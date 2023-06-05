package com.example.petapp.data

import android.net.Uri
import androidx.room.ColumnInfo
import androidx.room.DatabaseView
import java.time.Instant
import java.util.*

@DatabaseView("SELECT pg.pet_id AS petId, pg.name, pg.birth_timestamp AS birthDate, pg.imageUri, pw.weight, ph.height, pl.length, pc.circuit FROM PET_GENERAL pg " +
        "INNER JOIN (SELECT pet_id, MAX(measurement_timestamp), weight FROM pet_weight_history GROUP BY pet_id) pw  ON pg.pet_id = pw.pet_id " +
        "INNER JOIN (SELECT pet_id, MAX(measurement_timestamp), height FROM pet_height_history GROUP BY pet_id) ph ON pg.pet_id = ph.pet_id " +
        "INNER JOIN (SELECT pet_id, MAX(measurement_timestamp), length FROM pet_length_history GROUP BY pet_id) pl ON pg.pet_id = pl.pet_id " +
        "INNER JOIN (SELECT pet_id, MAX(measurement_timestamp), circuit FROM PET_CIRCUIT_HISTORY GROUP BY pet_id) pc ON pg.pet_id = pc.pet_id",
viewName = "pet_details_view")
data class PetDetailsView(
    val petId: UUID,
    val name: String,
    val birthDate: Instant,
    val imageUri: Uri?,
    @ColumnInfo(name = "weight")
    val weight: Double,
    val height: Double,
    val length: Double,
    val circuit: Double
)
