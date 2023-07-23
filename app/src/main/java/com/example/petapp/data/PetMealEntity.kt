package com.example.petapp.data

import androidx.annotation.StringRes
import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.example.petapp.R
import com.example.petapp.model.Menu
import com.example.petapp.ui.petdetails.addpetdata.FoodTypeEnum
import java.time.Instant
import java.time.OffsetTime
import java.util.*

@Entity(tableName = "pet_meal")
data class PetMealEntity(
    @PrimaryKey(autoGenerate = false)
    val id: UUID,
    @ColumnInfo(name = "pet_id")
    val pet_id: UUID,
    val time: OffsetTime,
    val mealType: MealType,
    val foodType: FoodTypeEnum,
    val petFoodId: UUID?,
    val amount: Double?,
    val reminderId: UUID?
)

@Entity(tableName = "pet_meal_history")
data class PetMealHistoryEntity(
    @PrimaryKey(autoGenerate = false)
    val id: UUID,
    @ColumnInfo(name = "pet_id")
    val pet_id: UUID,
    @ColumnInfo(name = "serving_datetime")
    val time: Instant,
    val mealType: MealType,
    val foodType: FoodTypeEnum,
    val petFoodId: UUID?,
    val amount: Double?
)

@Entity(tableName = "pet_food")
data class PetFoodEntity(
    @PrimaryKey(autoGenerate = false)
    val id: UUID
)

enum class MealType (@StringRes override val nameId: Int) : Menu {
    BREAKFAST(R.string.util_enums_meal_type_breakfast),
    LUNCH(R.string.util_enums_meal_type_lunch),
    DINNER(R.string.util_enums_meal_type_dinner),
    SNACK(R.string.util_enums_meal_type_snack),
    NONE(R.string.util_blank)
}
