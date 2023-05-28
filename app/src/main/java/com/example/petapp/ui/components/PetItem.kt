package com.example.petapp.ui.components

import androidx.annotation.DrawableRes
import androidx.annotation.StringRes
import androidx.compose.animation.animateContentSize
import androidx.compose.animation.core.Spring
import androidx.compose.animation.core.spring
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.petapp.R
import com.example.petapp.data.PetDashboardView
import com.example.petapp.model.*
import java.time.Instant
import java.time.ZoneId
import java.time.format.DateTimeFormatter
import java.time.format.FormatStyle
import java.util.*

enum class PetStatsEnum(@StringRes val stringId: Int?) {
    NONE(null),
    THIRST(R.string.components_dashboard_card_title_water),
    HUNGER(R.string.components_dashboard_card_title_meals),
    ACTIVITY(R.string.components_dashboard_card_title_activities)
}

@Composable
fun PetItems(
    pets: List<PetDashboardUiState>,
    getAgeFormattedString: (instant: Instant) -> String,
    getWeightFormattedString: (weight: Double) -> String,
    waterIconOnClicked: (pet: PetDashboardUiState) -> Unit,
    foodIconOnClicked: (pet: PetDashboardUiState) -> Unit,
    activityIconOnClicked: (pet: PetDashboardUiState) -> Unit,
    navigateToPetDetailsScreen: (petId: String) -> Unit,
    modifier: Modifier = Modifier
) {
    Column {
        pets.forEach {
            PetItem(
                pet = it,
                getAgeFormattedString = getAgeFormattedString,
                getWeightFormattedString = getWeightFormattedString,
                waterIconOnClicked = waterIconOnClicked,
                foodIconOnClicked = foodIconOnClicked,
                activityIconOnClicked = activityIconOnClicked,
                navigateToPetDetailsScreen = navigateToPetDetailsScreen
            )
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun PetItem(
    pet: PetDashboardUiState,
    getAgeFormattedString: (instant: Instant) -> String,
    getWeightFormattedString: (weight: Double) -> String,
    waterIconOnClicked: (pet: PetDashboardUiState) -> Unit,
    foodIconOnClicked: (pet: PetDashboardUiState) -> Unit,
    activityIconOnClicked: (pet: PetDashboardUiState) -> Unit,
    navigateToPetDetailsScreen: (petId: String) -> Unit,
    modifier: Modifier = Modifier,
    pettmp: Pet = Pet(
        null, "Nika", "Kot", "Tajski", Instant.now(), 3.1, listOf(
            PetMeal("as", Instant.now(), MealStatusEnum.EATEN),
            PetMeal("as", Instant.now(), MealStatusEnum.MISSED),
            PetMeal("as", Instant.now(), MealStatusEnum.WAIT)
        ), listOf(PetThirst("pragnienie")), listOf(PetActivity("aktywność"))
    ),
) {
    val childModifier: Modifier = Modifier
    Card(onClick = {navigateToPetDetailsScreen(pet.petDashboard.petId.toString())},
        modifier = modifier
            .padding(8.dp)
            .animateContentSize(
                animationSpec = spring(
                    dampingRatio = Spring.DampingRatioLowBouncy, stiffness = Spring.StiffnessLow
                )
            )
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(top = 8.dp, bottom = 8.dp, start = 4.dp, end = 4.dp)
        )
        {
            Row(modifier = Modifier.fillMaxWidth()) {
                PetIcon(
                    petIcon = pettmp.petIconId ?: R.drawable.nika,
                    modifier = childModifier.weight(3f)
                )
                PetInfo(
                    name = pet.petDashboard.name,
                    age = getAgeFormattedString(pet.petDashboard.birthDate),
                    weight = getWeightFormattedString(pet.petDashboard.weight),
                    modifier = childModifier.weight(3f)
                )
                PetIconStats(modifier = childModifier.weight(6f),
                    waterIconOnClicked = { waterIconOnClicked(pet) },
                    foodIconOnClicked = { foodIconOnClicked(pet) },
                    activityIconOnClicked = { activityIconOnClicked(pet) })
            }
        }
        if (pet.petStat != PetStatsEnum.NONE) {
            Divider(modifier = Modifier.padding(start = 8.dp, top = 4.dp, end = 8.dp))
            Column(
                modifier = modifier
                    .padding(
                        start = 16.dp, top = 8.dp, bottom = 16.dp, end = 16.dp
                    )
                    .fillMaxWidth()
            ) {
                Text(
                    text = pet.petStat.stringId?.let { stringResource(it) } ?: "",
                    style = MaterialTheme.typography.headlineSmall
                )
                when (pet.petStat) {
                    PetStatsEnum.NONE -> {}
                    PetStatsEnum.THIRST -> PetThirst(pettmp.petThirst)
                    PetStatsEnum.HUNGER -> PetHunger(pettmp.petMeals)
                    PetStatsEnum.ACTIVITY -> PetActivity(pettmp.petActivities)
                }
            }
        }
    }
}

@Composable
fun PetHunger(meals: List<PetMeal>, modifier: Modifier = Modifier) {
    Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
        meals.forEach {
            Column(horizontalAlignment = Alignment.CenterHorizontally) {
                IconButton(onClick = { /*TODO*/ }) {
                    Icon(
                        painter = painterResource(id = it.status.iconId),
                        contentDescription = null,
                        tint = Color.Unspecified,
                        modifier = Modifier.size(56.dp)
                    )
                }
                Text(
                    text = DateTimeFormatter.ofLocalizedTime(FormatStyle.SHORT).withZone(ZoneId.systemDefault()).format(it.dateTimestamp)
                )
            }
        }
    }
}

@Composable
fun PetThirst(thirsts: List<PetThirst>, modifier: Modifier = Modifier) {
    Text(
        text = stringResource(R.string.components_dashboard_card_content_water_last_changed) + " 12 " + stringResource(R.string.components_dashboard_card_content_water_time_hours),
        style = MaterialTheme.typography.bodyLarge
    )
}

@Composable
fun PetActivity(activities: List<PetActivity>, modifier: Modifier = Modifier) {

}

//may be change later, depends on image location
@Composable
fun PetIcon(modifier: Modifier = Modifier, @DrawableRes petIcon: Int = R.drawable.nika) {
    Column(
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = modifier.wrapContentHeight(Alignment.CenterVertically)
    ) {
        Image(
            modifier = Modifier
                .size(90.dp)
                .padding(8.dp)
                .clip(RoundedCornerShape(50)),
            contentScale = ContentScale.Crop,
            painter = painterResource(id = petIcon),
            contentDescription = null
        )
    }
}

@Composable
fun PetInfo(
    name: String,
    age: String,
    weight: String,
    modifier: Modifier = Modifier
) {
    Column(modifier = modifier, verticalArrangement = Arrangement.Center) {
        Text(
            text = name,
            style = MaterialTheme.typography.headlineMedium
        )
        Row(
            horizontalArrangement = Arrangement.Start,
            verticalAlignment = Alignment.CenterVertically
        ) {

            Icon(
                painter = painterResource(id = R.drawable.baseline_calendar_month_24),
                contentDescription = stringResource(
                    id = R.string.components_forms_text_field_label_pet_age
                ),
                Modifier
                    .size(24.dp)
                    .padding(end = 4.dp)
            )
            Text(
                text = age,
                style = MaterialTheme.typography.bodySmall
            )
        }
        //add unit in preferences in the end of weight (preferences/data store)
        Row(
            horizontalArrangement = Arrangement.Start,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Icon(
                painter = painterResource(id = R.drawable.weight_24),
                contentDescription = stringResource(
                    id = R.string.components_forms_text_field_label_pet_age
                ),
                Modifier
                    .size(24.dp)
                    .padding(end = 4.dp)
            )
            Text(
                text = weight,
                style = MaterialTheme.typography.bodySmall
            )
        }
    }
}

@Composable
fun PetIconStats(
    waterIconOnClicked: () -> Unit,
    foodIconOnClicked: () -> Unit,
    activityIconOnClicked: () -> Unit,
    modifier: Modifier = Modifier
) {
    Row(
        modifier = modifier
            .height(88.dp)
            .wrapContentHeight(),
        horizontalArrangement = Arrangement.Center,
        verticalAlignment = Alignment.CenterVertically
    ) {
        PetStatus(waterIconOnClicked, R.drawable.icons8_water_48, 0.9f, Color.Green)
        PetStatus(foodIconOnClicked, R.drawable.icons8_pet_food_64, 0.5f, Color.Yellow)
        PetStatus(activityIconOnClicked, R.drawable.icons8_pet_48, 0.2f, Color.Red)
    }
}

@Composable
private fun PetStatus(
    onClick: () -> Unit,
    @DrawableRes iconId: Int,
    progress: Float,
    color: Color,
    modifier: Modifier = Modifier
) {
    Column(modifier = Modifier.width(64.dp)) {
        PetStatusIconButton(onClick, iconId)
        PetLinearProgressIndicator(progress, color)
    }
}

@Composable
private fun PetStatusIconButton(
    onClick: () -> Unit,
    @DrawableRes iconId: Int,
    modifier: Modifier = Modifier
) {
    IconButton(onClick = onClick, modifier = Modifier.size(64.dp)) {
        Icon(
            painter = painterResource(id = iconId),
            contentDescription = null,
            tint = Color.Unspecified,
            modifier = Modifier.size(44.dp)
        )
    }
}

@Composable
private fun PetLinearProgressIndicator(
    progress: Float,
    color: Color,
    modifier: Modifier = Modifier
) {
    LinearProgressIndicator(
        progress = progress,
        modifier = Modifier
            .height(8.dp)
            .padding(2.dp)
            .clip(ShapeDefaults.Large),
        color = color,
        strokeCap = StrokeCap.Round
    )
}

@Preview(showBackground = true)
@Composable
fun PetInfoPrev() {
    PetInfo(name = "Zeus", age = "10 years old", weight = "3.1")
}

@Preview(showBackground = true)
@Composable
fun IconPrev() {
    PetIconStats({}, {}, {})
}

@Preview(showBackground = true)
@Composable
fun PetItemPrev() {
    PetItem(
        pettmp = Pet(
            null, "Nika", "Kot", "Tajski", Instant.now(), 3.1, listOf(
                PetMeal("as", Instant.now(), MealStatusEnum.EATEN),
                PetMeal("as", Instant.now(), MealStatusEnum.MISSED),
                PetMeal("as", Instant.now(), MealStatusEnum.WAIT)
            ), listOf(PetThirst("pragnienie")), listOf(PetActivity("aktywność"))
        ),
        pet = PetDashboardUiState(
            petDashboard = PetDashboardView(
                petId = UUID.randomUUID(),
                name = "Zeus",
                birthDate = Instant.now(),
                weight = 7.1
            )
        ) ,
        getAgeFormattedString = { it.toString() },
        getWeightFormattedString = { it.toString() },
        waterIconOnClicked = {},
        foodIconOnClicked = {},
        activityIconOnClicked = {},
        navigateToPetDetailsScreen = {}
    )
}

@Preview(showBackground = true)
@Composable
fun PetItemsPrev() {
    PetItems(
        pets = listOf(
            PetDashboardUiState(
                petDashboard = PetDashboardView(
                    petId = UUID.randomUUID(),
                    name = "Zeus",
                    birthDate = Instant.now(),
                    weight = 7.1
                )
            )),
        getAgeFormattedString = { it.toString() },
        getWeightFormattedString = { it.toString() },
        waterIconOnClicked = {},
        foodIconOnClicked = {},
        activityIconOnClicked = {},
        navigateToPetDetailsScreen = {}
    )
}

