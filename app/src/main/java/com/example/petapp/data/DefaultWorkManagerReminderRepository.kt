package com.example.petapp.data

import android.content.Context
import android.util.Log
import androidx.work.Data
import androidx.work.ExistingPeriodicWorkPolicy
import androidx.work.PeriodicWorkRequestBuilder
import androidx.work.WorkManager
import com.example.petapp.workers.MealRemindWorker
import java.time.*
import java.util.*
import javax.inject.Inject

class DefaultWorkManagerReminderRepository @Inject constructor(
    private val context: Context,
    private val petsDashboardRepository: PetsDashboardRepository,
    private val settingsDataRepository: UserSettingsDataRepository
) :
    WorkManagerReminderRepository {
    private val workManager = WorkManager.getInstance(context)


    override suspend fun scheduleMealReminder(offsetTime: OffsetTime, mealId: UUID) {

        /*var unit = UserPreferences.Unit.METRIC
        val flow = settingsDataRepository.getUnit().collect() { unit = it }*/
        val meal = petsDashboardRepository.getPetMeal(mealId = mealId.toString())
        val pet = meal?.let { petsDashboardRepository.getPet(it.pet_id.toString()) }

        val duration = Duration.between(
            LocalTime.now().atDate(LocalDate.now()).atOffset(
                ZoneOffset.UTC
            ),
            LocalTime.from(offsetTime).atDate(LocalDate.now()).atOffset(ZoneOffset.UTC)
        )

        Log.e("błąd", duration.seconds.toString())

        pet?.let {
            val data = Data.Builder()
            data.putString(MealRemindWorker.nameKey, it.name + " - " + meal.mealType.name + " - " + meal.amount?.toString())

            val workRequestBuilder = PeriodicWorkRequestBuilder<MealRemindWorker>(Duration.ofDays(1))
                .setInitialDelay(if (duration.isNegative) duration.plusHours(24) else duration)
                .setInputData(data.build())
                .build()

            workManager.enqueueUniquePeriodicWork(
                mealId.toString(),
                ExistingPeriodicWorkPolicy.KEEP,
                workRequestBuilder
            )
            petsDashboardRepository.updatePetMeal(
                meal.copy(
                    reminderId = mealId
                )
            )
            Log.i("info", "work has been scheduled")
        }
    }

    override suspend fun cancelReminder(uuid: UUID) {
        workManager.cancelUniqueWork(uuid.toString())
        petsDashboardRepository.getPetMeal(uuid.toString())?.let {
            petsDashboardRepository.updatePetMeal(
                it.copy(
                    reminderId = null
                )
            )
        }
        Log.i("info", "work has been cancelled")
    }

}