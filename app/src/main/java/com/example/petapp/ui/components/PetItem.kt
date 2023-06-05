package com.example.petapp.ui.components

import android.net.Uri
import androidx.annotation.DrawableRes
import androidx.annotation.StringRes
import androidx.compose.animation.animateContentSize
import androidx.compose.animation.core.Spring
import androidx.compose.animation.core.spring
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
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
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import coil.compose.rememberAsyncImagePainter
import com.example.petapp.R
import com.example.petapp.data.PetDashboardView
import com.example.petapp.data.PetMealEntity
import com.example.petapp.model.*
import com.example.petapp.model.util.Formatters
import java.time.Duration
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
    onWaterChangedIconClicked: (pet: PetDashboardUiState) -> Unit,
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
                navigateToPetDetailsScreen = navigateToPetDetailsScreen,
                onWaterChangedIconClicked = onWaterChangedIconClicked
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
    onWaterChangedIconClicked: (pet: PetDashboardUiState) -> Unit,
    navigateToPetDetailsScreen: (petId: String) -> Unit,
    modifier: Modifier = Modifier
) {
    val childModifier: Modifier = Modifier
    Card(
        onClick = { navigateToPetDetailsScreen(pet.petDashboard.petId.toString()) },
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
                    petImageUri = pet.petDashboard.imageUri,
                    modifier = childModifier.weight(3f)
                )
                PetInfo(
                    name = pet.petDashboard.name,
                    age = getAgeFormattedString(pet.petDashboard.birthDate),
                    weight = getWeightFormattedString(pet.petDashboard.weight),
                    modifier = childModifier.weight(3f)
                )
                PetIconStats(
                    waterStatProgressIndicatorEntry = pet.waterStat,
                    mealStatProgressIndicatorEntry = pet.mealStat,
                    modifier = childModifier.weight(6f),
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
                    PetStatsEnum.THIRST -> {
                        PetThirst(
                            Formatters.waterLastChangedFormatter(
                                pet.petDashboard.waterLastChanged?.let {
                                    //TODO check why null value is converted to Instant.MIN
                                    if (it > Instant.ofEpochMilli(0)) {
                                        Duration.between(
                                            it,
                                            Instant.now()
                                        )
                                    } else null
                                },
                                LocalContext.current
                            ),
                            onWaterChangedIconClicked = { onWaterChangedIconClicked(pet) }
                        )
                    }
                    PetStatsEnum.HUNGER -> PetHunger(pet.petMeals)
                    PetStatsEnum.ACTIVITY -> PetActivity()
                }
            }
        }
    }
}

@Composable
fun PetHunger(meals: List<PetMealEntity>, modifier: Modifier = Modifier) {
    Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
        if (meals.isNullOrEmpty()) Text(
            text = "Set meals"
        )
        else {
            meals.forEach {
                Column(horizontalAlignment = Alignment.CenterHorizontally) {
                    IconButton(onClick = { /*TODO*/ }) {
                        Icon(
                            painter = painterResource(id = R.drawable.icons8_dog_bowl_white_30),
                            contentDescription = null,
                            tint = Color.Unspecified,
                            modifier = Modifier.size(56.dp)
                        )
                    }
                    Text(
                        text = DateTimeFormatter.ofLocalizedTime(FormatStyle.SHORT)
                            .withZone(ZoneId.systemDefault()).format(it.time)
                    )
                }
            }
        }
    }
}

@Composable
fun PetThirst(content: String, onWaterChangedIconClicked: () -> Unit, modifier: Modifier = Modifier) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .height(IntrinsicSize.Max), horizontalArrangement = Arrangement.SpaceBetween, verticalAlignment = Alignment.CenterVertically
    ) {
        Text(
            text = content,
            style = MaterialTheme.typography.bodyLarge
        )
        IconButton(onClick = onWaterChangedIconClicked) {
            Icon(painter = painterResource(id = R.drawable.restart_alt_24), contentDescription = null)
        }
    }
}

@Composable
fun PetActivity(modifier: Modifier = Modifier) {

}

//may be change later, depends on image location
@Composable
fun PetIcon(modifier: Modifier = Modifier, petImageUri: Uri?) {
    Column(
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = modifier.wrapContentHeight(Alignment.CenterVertically)
    ) {
        Image(
            modifier = Modifier
                .size(90.dp)
                .padding(8.dp)
                .clip(RoundedCornerShape(50))
                .background(MaterialTheme.colorScheme.surface),
            contentScale = ContentScale.Crop,
            painter = rememberAsyncImagePainter( petImageUri ?: R.drawable.icons8_pets_96),
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
            style = MaterialTheme.typography.headlineSmall
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
    waterStatProgressIndicatorEntry: PetStatProgressIndicatorEntry,
    mealStatProgressIndicatorEntry: PetStatProgressIndicatorEntry,
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
        PetStatus(waterIconOnClicked, R.drawable.icons8_water_48, waterStatProgressIndicatorEntry.value, waterStatProgressIndicatorEntry.color)
        PetStatus(foodIconOnClicked, R.drawable.icons8_pet_food_64, mealStatProgressIndicatorEntry.value, mealStatProgressIndicatorEntry.color)
        PetStatus(activityIconOnClicked, R.drawable.icons8_pet_48, 0.2f, Color.Transparent)
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
        trackColor = if (color == Color.Transparent) Color.Transparent else color.copy(0.2f),
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
fun PetItemPrev() {
    PetItem(
        pet = PetDashboardUiState(
            petDashboard = PetDashboardView(
                petId = UUID.randomUUID(),
                name = "Zeus",
                birthDate = Instant.now(),
                waterLastChanged = Instant.now(),
                weight = 7.1,
                imageUri = null
            ),
            petMeals = emptyList(),
            waterStat = PetStatProgressIndicatorEntry(0.7f, Color.Yellow),
            mealStat = PetStatProgressIndicatorEntry(0.9f, Color.Green)
        ),
        getAgeFormattedString = { it.toString() },
        getWeightFormattedString = { it.toString() },
        waterIconOnClicked = {},
        foodIconOnClicked = {},
        activityIconOnClicked = {},
        navigateToPetDetailsScreen = {},
        onWaterChangedIconClicked = {}
    )
}

