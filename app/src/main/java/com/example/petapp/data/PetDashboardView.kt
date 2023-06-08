package com.example.petapp.data

import android.net.Uri
import androidx.room.ColumnInfo
import androidx.room.DatabaseView
import java.time.Instant
import java.util.UUID

@DatabaseView("SELECT pg.pet_id AS petId, pg.name, pg.birth_timestamp AS birthDate, pg.imageUri, pw.weight, pt.measure AS waterLastChanged " +
        "FROM PET_GENERAL pg LEFT JOIN (SELECT pet_id, MAX(measurement_timestamp), weight FROM pet_weight_history GROUP BY pet_id) pw " +
        "ON pg.pet_id = pw.pet_id LEFT JOIN (SELECT pet_id, MAX(measurement_timestamp) AS measure FROM pet_water_change_history  GROUP BY pet_id) pt ON pg.pet_id = pt.pet_id",
    "pet_dashboard_view")
data class PetDashboardView(
    val petId: UUID,
    val name: String,
    val birthDate: Instant,
    val imageUri: Uri?,
    @ColumnInfo(name = "weight")
    val weight: Double?,
    val waterLastChanged: Instant?
)