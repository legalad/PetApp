package com.example.petapp.data

import java.time.OffsetTime
import java.util.*

interface WorkManagerReminderRepository {
    suspend fun scheduleMealReminder(offsetTime: OffsetTime, mealId: UUID)
    suspend fun cancelReminder(uuid: UUID)
}