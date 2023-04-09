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
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
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
import com.example.petapp.model.Pet
import com.example.petapp.R
import com.example.petapp.model.MealStatusEnum
import com.example.petapp.model.PetMeal
import java.util.Calendar

private enum class PetStatsEnum (@StringRes val stringId: Int?) {
    NONE(null),
    THIRST(R.string.water_change),
    HUNGER(R.string.pet_hunger),
    ACTIVITY(R.string.pet_activities)
}
@Composable
fun PetItem(pet: Pet, modifier: Modifier = Modifier) {
    var petStat by rememberSaveable{ mutableStateOf(PetStatsEnum.NONE)}
    ElevatedCard(
        modifier = modifier
            .padding(8.dp)
            .animateContentSize(
                animationSpec = spring(
                    dampingRatio = Spring.DampingRatioLowBouncy,
                    stiffness = Spring.StiffnessLow
                )
            )
    ) {
        Column (modifier = Modifier
            .fillMaxWidth()
            .padding(4.dp)
        ){
            Row (modifier = Modifier.fillMaxWidth()){
                PetIcon(petIcon = pet.petIconId?:R.drawable.nika, modifier = modifier.weight(2.7f))
                PetInfo(name = pet.name, age = pet.birthDate.year, weight = pet.weight, modifier = modifier.weight(3f))
                PetIconStats(modifier = Modifier.weight(6f),
                    waterIconOnClicked = {petStat = if(petStat != PetStatsEnum.THIRST) PetStatsEnum.THIRST else PetStatsEnum.NONE },
                    foodIconOnClicked = {petStat = if(petStat != PetStatsEnum.HUNGER) PetStatsEnum.HUNGER else PetStatsEnum.NONE },
                    activityIconOnClicked = {petStat = if(petStat != PetStatsEnum.ACTIVITY) PetStatsEnum.ACTIVITY else PetStatsEnum.NONE })
            }
        }
        if (petStat != PetStatsEnum.NONE) {
            Divider(modifier = Modifier.padding(start = 8.dp, top = 4.dp, end = 8.dp))
            Column(
                modifier = modifier
                    .padding(
                        start = 16.dp,
                        top = 8.dp,
                        bottom = 16.dp,
                        end = 16.dp
                    )
                    .fillMaxWidth()
            ) {
                Text(
                    text = petStat.stringId?.let { stringResource(it) } ?: "",
                    style = MaterialTheme.typography.headlineSmall
                )
                when (petStat) {
                    PetStatsEnum.NONE -> {}
                    PetStatsEnum.THIRST -> PetThirst()
                    PetStatsEnum.HUNGER -> PetHunger(listOf(
                        PetMeal("as", Calendar.getInstance().time, MealStatusEnum.EATEN),
                        PetMeal("as", Calendar.getInstance().time, MealStatusEnum.MISSED),
                        PetMeal("as", Calendar.getInstance().time, MealStatusEnum.WAIT)
                    ))
                    PetStatsEnum.ACTIVITY -> PetActivity()
                }
            }
        }
    }
}

@Composable
fun PetHunger(meals: List<PetMeal>) {
    Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
        meals.forEach {
            Column (horizontalAlignment = Alignment.CenterHorizontally) {
                IconButton(onClick = { /*TODO*/ }) {
                    Icon(painter = painterResource(id = it.status.iconId), contentDescription = null, tint = Color.Unspecified, modifier = Modifier.size(56.dp))
                }
                Text(text = "${it.date.hours}:${it.date.minutes}")
            }
        }
    }
}

@Composable
fun PetThirst(modifier: Modifier = Modifier) {
    Text(
        text = stringResource(R.string.water_last_change) + " 12 " + stringResource(R.string.water_time_hours),
        style = MaterialTheme.typography.bodyLarge
    )
}

@Composable
fun PetActivity() {

}

@Composable
fun PetItemButton(
    expanded: Boolean,
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    IconButton(onClick = onClick, modifier = modifier) {
        Icon(
            painter =
            if (expanded) painterResource(
                id = R.drawable.baseline_expand_more_24
            )
            else painterResource(
                id = R.drawable.baseline_expand_more_24
            ),
            contentDescription = stringResource(
                id = R.string.expand_button_content_description
            )
        )
    }
}

//may be change later, depends on image location
@Composable
fun PetIcon(@DrawableRes petIcon: Int = R.drawable.nika, modifier: Modifier = Modifier) {
    Image(
        modifier = modifier
            .size(88.dp)
            .padding(8.dp)
            .clip(RoundedCornerShape(50)),
        contentScale = ContentScale.Crop,
        painter = painterResource(id = petIcon),
        contentDescription = null)
}

@Composable
fun PetInfo(
    name: String,
    age: Int,
    weight: Double,
    modifier: Modifier = Modifier
) {
    Column (modifier = modifier) {
        Text(
            text = name,
            style = MaterialTheme.typography.headlineMedium,
            modifier = Modifier.padding(top = 8.dp)
        )
        Row () {
            Text(
                text = stringResource(id = R.string.pet_age) + ":",
                style = MaterialTheme.typography.bodySmall,
                modifier = Modifier.weight(1f)
            )
            Text(
                text = age.toString(),
                style = MaterialTheme.typography.bodySmall,
                modifier = Modifier.weight(1f)
            )
        }
        //add unit in preferences in the end of weight (preferences/data store)
        Row () {
            Text(
                text = stringResource(id = R.string.pet_weight) + ":",
                style = MaterialTheme.typography.bodySmall,
                modifier = Modifier.weight(1f)
            )
            Text(
                text = weight.toString(),
                style = MaterialTheme.typography.bodySmall,
                modifier = Modifier.weight(1f)
            )
        }
    }
}

@Composable
fun PetIconStats(waterIconOnClicked: () -> Unit,foodIconOnClicked: () -> Unit, activityIconOnClicked: () -> Unit,modifier: Modifier = Modifier) {
    Row (modifier = modifier
        .height(88.dp)
        .wrapContentHeight(), horizontalArrangement = Arrangement.Center, verticalAlignment = Alignment.CenterVertically){
        PetStatus(waterIconOnClicked, R.drawable.icons8_water_48, 0.9f, Color.Green)
        PetStatus(foodIconOnClicked, R.drawable.icons8_pet_food_64, 0.5f, Color.Yellow)
        PetStatus(activityIconOnClicked, R.drawable.icons8_pet_48, 0.2f, Color.Red)
    }
}

@Composable
private fun PetStatus(onClick: () -> Unit, @DrawableRes iconId: Int, progress: Float, color: Color, modifier: Modifier = Modifier) {
    Column(modifier = Modifier.width(64.dp)) {
        PetStatusIconButton(onClick, iconId)
        PetLinearProgressIndicator(progress, color)
    }
}

@Composable
private fun PetStatusIconButton(onClick: () -> Unit, @DrawableRes iconId: Int, modifier: Modifier = Modifier) {
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
private fun PetLinearProgressIndicator(progress: Float, color: Color, modifier: Modifier = Modifier) {
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
    PetInfo(name = "Zeus", age = 1, weight = 3.1)
}

@Preview(showBackground = true)
@Composable
fun IconPrev() {
    PetIconStats({}, {}, {})
}

@Preview(showBackground = true)
@Composable
fun PetItemPrev() {
    PetItem(pet = Pet(null, "Nika", "Kot", Calendar.getInstance().time, 3.1))
}

