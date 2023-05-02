package com.example.petapp.model

import androidx.annotation.DrawableRes
import com.example.petapp.R
import java.time.Instant

enum class MealStatusEnum (@DrawableRes val iconId: Int) {
    EATEN(R.drawable.icons8_dog_bowl_green_80),
    MISSED(R.drawable.icons8_dog_bowl_red_80),
    SKIPPED(R.drawable.icons8_dog_bowl_blue_80),
    WAIT(R.drawable.icons8_dog_bowl_white_80)

}

data class PetMeal(
    val name: String,
    val dateTimestamp: Instant,
    val status: MealStatusEnum
)
