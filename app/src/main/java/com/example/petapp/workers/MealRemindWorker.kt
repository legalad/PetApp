package com.example.petapp.workers

import android.content.Context
import androidx.work.CoroutineWorker
import androidx.work.WorkerParameters

class MealRemindWorker(ctx: Context, params: WorkerParameters) : CoroutineWorker(ctx, params) {
    override suspend fun doWork(): Result {

        val mealName = inputData.getString(nameKey)

        makeStatusNotification(
            notificationType = NotificationType.MEAL_REMINDER,
            mealName?: "error",
            applicationContext
        )

        return Result.success()
    }

    companion object {
        const val nameKey = "NAME"
    }
}