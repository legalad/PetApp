package com.example.petapp.data

import androidx.room.ColumnInfo
import androidx.room.DatabaseView
import java.time.Instant
import java.util.UUID

@DatabaseView("SELECT pg.pet_id AS petId, pg.name, pg.birth_timestamp AS birthDate, pw.weight " +
        "FROM PET_GENERAL pg INNER JOIN (SELECT pet_id, MAX(measurement_timestamp), weight FROM pet_weight_history GROUP BY pet_id) pw " +
        "ON pg.pet_id = pw.pet_id",
    "pet_dashboard_view")
data class PetDashboardView(
    val petId: UUID,
    val name: String,
    val birthDate: Instant,
    @ColumnInfo(name = "weight")
    val weight: Double
)