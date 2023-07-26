package com.example.petapp.ui.components

import androidx.annotation.DrawableRes
import androidx.annotation.StringRes
import androidx.compose.animation.*
import androidx.compose.animation.core.EaseInElastic
import androidx.compose.animation.core.animateIntAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import coil.compose.rememberAsyncImagePainter
import com.example.petapp.R
import com.example.petapp.data.PetDashboardView
import com.example.petapp.data.PetMealEntity
import com.example.petapp.model.PetDashboardUiState
import com.example.petapp.model.PetGender
import com.example.petapp.model.PetStatProgressIndicatorEntry
import com.example.petapp.model.Species
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
    getWeightFormattedString: (weight: Double?) -> String,
    waterIconOnClicked: (pet: PetDashboardUiState) -> Unit,
    foodIconOnClicked: (pet: PetDashboardUiState) -> Unit,
    activityIconOnClicked: (pet: PetDashboardUiState) -> Unit,
    onWaterChangedIconClicked: (pet: PetDashboardUiState) -> Unit,
    navigateToPetDetailsScreen: (petId: String) -> Unit,
    navigateToAddMealScreen: (String) -> Unit,
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
                onWaterChangedIconClicked = onWaterChangedIconClicked,
                navigateToAddMealScreen = navigateToAddMealScreen
            )
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class, ExperimentalAnimationApi::class)
@Composable
fun PetItem(
    pet: PetDashboardUiState,
    getAgeFormattedString: (instant: Instant) -> String,
    getWeightFormattedString: (weight: Double?) -> String,
    waterIconOnClicked: (pet: PetDashboardUiState) -> Unit,
    foodIconOnClicked: (pet: PetDashboardUiState) -> Unit,
    activityIconOnClicked: (pet: PetDashboardUiState) -> Unit,
    onWaterChangedIconClicked: (pet: PetDashboardUiState) -> Unit,
    navigateToPetDetailsScreen: (petId: String) -> Unit,
    navigateToAddMealScreen: (String) -> Unit,
    modifier: Modifier = Modifier
) {
    val childModifier: Modifier = Modifier
    Card(
        onClick = { navigateToPetDetailsScreen(pet.petDashboard.petId.toString()) },
        modifier = modifier
            .padding(8.dp)
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(top = 8.dp, bottom = 8.dp, start = 4.dp, end = 4.dp)
        )
        {
            Row(modifier = Modifier.fillMaxWidth()) {
                PetIcon(
                    petDashboardView = pet.petDashboard,
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
                    modifier = childModifier.weight(5f),
                    waterIconOnClicked = { waterIconOnClicked(pet) },
                    foodIconOnClicked = { foodIconOnClicked(pet) },
                    activityIconOnClicked = { activityIconOnClicked(pet) },
                    petStatsEnum = pet.petStat
                )
            }
        }
        AnimatedVisibility(pet.petStat != PetStatsEnum.NONE) {
            Divider(
                modifier = Modifier
                    .padding(start = 8.dp, top = 4.dp, end = 8.dp, bottom = 4.dp)
                    .animateEnterExit(
                        enter = scaleIn(tween(200, 150)),
                        exit = scaleOut(
                            animationSpec = tween(100, 0),

                            )
                    ),
                thickness = 3.dp,
                color = MaterialTheme.colorScheme.onSurface
            )


            AnimatedContent(
                targetState = pet.petStat,
                transitionSpec = {
                    if (pet.petStat == PetStatsEnum.THIRST) {
                        (slideIntoContainer(
                            animationSpec = tween(durationMillis = 500),
                            towards = AnimatedContentTransitionScope.SlideDirection.End
                        ) + fadeIn(
                            animationSpec = tween(
                                durationMillis = 300,
                                100
                            )
                        )).togetherWith(
                            slideOutOfContainer(
                                towards = AnimatedContentTransitionScope.SlideDirection.End
                                ,
                                animationSpec = tween(
                                    durationMillis = 500
                                )
                            ) + scaleOut(animationSpec = tween(durationMillis = 200, 100))
                        )

                    } else {
                        (slideIntoContainer(
                            animationSpec = tween(durationMillis = 500),
                            towards = AnimatedContentTransitionScope.SlideDirection.Start
                        ) + fadeIn(
                            animationSpec = tween(
                                durationMillis = 300,
                                100
                            )
                        )).togetherWith(
                            slideOutOfContainer(
                                towards = AnimatedContentTransitionScope.SlideDirection.Start,
                                animationSpec = tween(
                                    durationMillis = 500
                                )
                            ) + scaleOut(animationSpec = tween(durationMillis = 200, 100))
                        )

                    }
                },
                modifier = Modifier.animateEnterExit(
                    enter = fadeIn(animationSpec = tween(durationMillis = 500, 150)),
                    exit = fadeOut(animationSpec = tween(durationMillis = 200))
                ).padding(top = 4.dp), label = ""
            ) {it ->
                Column(
                    modifier = modifier
                        .padding(
                            start = 16.dp, top = 8.dp, bottom = 16.dp, end = 16.dp
                        )
                        .fillMaxWidth()
                ) {
                    val petStatsEnum by animateIntAsState(
                        targetValue = it.ordinal,
                        animationSpec = tween(
                            durationMillis = 3000,
                            delayMillis = 3000,
                            easing = EaseInElastic
                        ), label = ""
                    )
                    Text(
                        text = PetStatsEnum.values()[petStatsEnum].stringId?.let { stringResource(it) }
                            ?: "",
                        style = MaterialTheme.typography.headlineSmall)
                    when (PetStatsEnum.values()[petStatsEnum]) {
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

                        PetStatsEnum.HUNGER -> PetHunger(
                            pet.petMeals,
                            { navigateToAddMealScreen(pet.petDashboard.petId.toString()) }
                        )
                        PetStatsEnum.ACTIVITY -> PetActivity()

                    }
                }
            }
        }
    }

}

@Composable
fun PetHunger(meals: List<PetMealEntity>, navigateToAddMealScreen: () -> Unit, modifier: Modifier = Modifier) {
    Row(
        modifier = modifier
            .fillMaxWidth()
            .height(IntrinsicSize.Max),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        if (meals.isEmpty()) {
            Text(
                text = "Set meals",
                style = MaterialTheme.typography.bodyLarge
            )
            IconButton(onClick = navigateToAddMealScreen) {
                Icon(
                    imageVector = Icons.Default.Add,
                    contentDescription = null
                )
            }
        }
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
fun PetThirst(
    content: String,
    onWaterChangedIconClicked: () -> Unit,
    modifier: Modifier = Modifier
) {
    Row(
        modifier = modifier
            .fillMaxWidth()
            .height(IntrinsicSize.Max),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Text(
            text = content,
            style = MaterialTheme.typography.bodyLarge
        )
        IconButton(onClick = onWaterChangedIconClicked) {
            Icon(
                painter = painterResource(id = R.drawable.restart_alt_24),
                contentDescription = null
            )
        }
    }
}

@Composable
fun PetActivity(modifier: Modifier = Modifier) {

}

//may be change later, depends on image location
@Composable
fun PetIcon(modifier: Modifier = Modifier, petDashboardView: PetDashboardView) {
    Column(
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = modifier.wrapContentHeight(Alignment.CenterVertically)
    ) {
        Image(
            modifier = Modifier
                .size(90.dp)
                .padding(8.dp)
                .clip(RoundedCornerShape(10)),
            contentScale = ContentScale.Crop,
            painter = rememberAsyncImagePainter(
                petDashboardView.imageUri
                    ?: /*petDashboardView.breed?.let { petDashboardView.species.breeds[it] } ?:*/ petDashboardView.species.avatarIconId
            ),
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
            style = MaterialTheme.typography.headlineSmall,
            maxLines = 1,
            overflow = TextOverflow.Ellipsis
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
                style = MaterialTheme.typography.bodySmall,
                maxLines = 1,
                overflow = TextOverflow.Ellipsis
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
                style = MaterialTheme.typography.bodySmall,
                maxLines = 1,
                overflow = TextOverflow.Ellipsis
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
    petStatsEnum: PetStatsEnum,
    modifier: Modifier = Modifier
) {
    Row(
        modifier = modifier
            .height(88.dp)
            .wrapContentHeight(),
        horizontalArrangement = Arrangement.Center,
        verticalAlignment = Alignment.CenterVertically
    ) {
        PetStatus(
            waterIconOnClicked,
            R.drawable.water_128,
            waterStatProgressIndicatorEntry.value,
            waterStatProgressIndicatorEntry.color,
            (petStatsEnum == PetStatsEnum.THIRST)
        )
        PetStatus(
            foodIconOnClicked,
            R.drawable.pet_food_128,
            mealStatProgressIndicatorEntry.value,
            mealStatProgressIndicatorEntry.color,
            (petStatsEnum == PetStatsEnum.HUNGER)
        )
/*
        PetStatus(activityIconOnClicked, R.drawable.icons8_pet_48, 0.2f, Color.Transparent)
*/
    }
}

@Composable
private fun PetStatus(
    onClick: () -> Unit,
    @DrawableRes iconId: Int,
    progress: Float,
    color: Color,
    isClicked: Boolean,
    modifier: Modifier = Modifier
) {
    Column(modifier = Modifier.width(64.dp)) {
        PetStatusIconButton(onClick, iconId, isClicked)
        PetLinearProgressIndicator(progress, color)
    }
}

@Composable
private fun PetStatusIconButton(
    onClick: () -> Unit,
    @DrawableRes iconId: Int,
    isClicked: Boolean,
    modifier: Modifier = Modifier
) {
    val color by animateColorAsState(
        targetValue = if (isClicked) MaterialTheme.colorScheme.onSurface else MaterialTheme.colorScheme.surfaceVariant,
        animationSpec = tween(
            durationMillis = 300
        )
    )
    Column(
        modifier = Modifier
            .clip(RoundedCornerShape(20))
            .padding(bottom = 4.dp)
    ) {
        IconButton(
            onClick = onClick,
            modifier = Modifier
                .size(64.dp)
                .background(color)
        ) {
            Icon(
                painter = painterResource(id = iconId),
                contentDescription = null,
                tint = Color.Unspecified,
                modifier = Modifier.size(44.dp)
            )
        }
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
                imageUri = null,
                gender = PetGender.FEMALE,
                species = Species.DOG,
                breed = 1
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
        onWaterChangedIconClicked = {},
        navigateToAddMealScreen = {}
    )
}

