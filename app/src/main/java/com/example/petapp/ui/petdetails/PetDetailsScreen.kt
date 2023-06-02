package com.example.petapp.ui.petdetails

import androidx.annotation.DrawableRes
import androidx.annotation.StringRes
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.example.petapp.R
import com.example.petapp.ui.components.ErrorScreen
import com.example.petapp.ui.components.LoadingScreen
import java.time.Instant
import java.time.ZoneId
import java.time.format.DateTimeFormatter
import java.time.format.FormatStyle
import java.util.*

@Composable
fun PetDetailsScreen(
    viewModel: PetDetailsViewModel,
    navigateToAddWeightScreen: (petId: String) -> Unit,
    navigateToAddDimensionsScreen: (petId: String) -> Unit,
    navigateToAddMealScreen: (petId: String) -> Unit,
    navigateToWeightDashboardScreen: (petId: String) -> Unit,
    navigateToDimensionsDashboardScreen: (petId: String) -> Unit,
    navigateToMealsDashboardScreen: (petId: String) -> Unit,
    modifier: Modifier = Modifier
) {
    Column(modifier = modifier) {
        when (viewModel.uiState) {
            is PetDetailsUiState.Error -> ErrorScreen(message = "Can't load pets")
            is PetDetailsUiState.Loading -> LoadingScreen()
            is PetDetailsUiState.Success -> PetDetailsResultScreen(
                viewModel,
                navigateToAddWeightScreen = navigateToAddWeightScreen,
                navigateToAddDimensionsScreen = navigateToAddDimensionsScreen,
                navigateToAddMealScreen = navigateToAddMealScreen,
                navigateToWeightDashboardScreen = navigateToWeightDashboardScreen,
                navigateToDimensionsDashboardScreen = navigateToDimensionsDashboardScreen,
                navigateToMealsDashboardScreen = navigateToMealsDashboardScreen
            )
        }
    }
}

@Composable
fun PetDetailsResultScreen(
    viewModel: PetDetailsViewModel,
    navigateToAddWeightScreen: (petId: String) -> Unit,
    navigateToAddDimensionsScreen: (petId: String) -> Unit,
    navigateToAddMealScreen: (petId: String) -> Unit,
    navigateToWeightDashboardScreen: (petId: String) -> Unit,
    navigateToDimensionsDashboardScreen: (petId: String) -> Unit,
    navigateToMealsDashboardScreen: (petId: String) -> Unit
) {
    val uiState = viewModel.successUiState.collectAsState().value
    Column(modifier = Modifier.fillMaxSize()) {
        /*PetItem(
            pet = PetDashboardUiState(
                petDashboard = PetDashboardView(
                    petId = uiState.pet.petId,
                    name = uiState.pet.name,
                    birthDate = uiState.pet.birthDate,
                    weight = uiState.pet.weight,
                    waterLastChanged = null
                ),
                petMeals = emptyList(),
                waterStat = PetStatProgressIndicatorEntry(1f, Color.Transparent),
                mealStat = PetStatProgressIndicatorEntry(1f, Color.Transparent)
            ),
            getAgeFormattedString = viewModel::getPetAgeFormattedString,
            getWeightFormattedString = viewModel::getPetWeightFormattedString,
            waterIconOnClicked = {},
            foodIconOnClicked = {},
            activityIconOnClicked = {},
            navigateToPetDetailsScreen = {},
            onWaterChangedIconClicked = {}
        )*/
        PetDetailsGeneral(
            petName = uiState.pet.name,
            petBirthDate = uiState.pet.birthDate,
            onSetPhotoIconClicked = {}
        )
        PetBasicCardWithTrailingIcon(
            title = R.string.components_forms_text_field_label_pet_weight,
            onCardItemClicked = {
                navigateToWeightDashboardScreen(
                    uiState.pet.petId.toString()
                )
            },
            onTrailingIconClicked = {
                navigateToAddWeightScreen(
                    uiState.pet.petId.toString()
                )
            }) {
            Icon(
                painter = painterResource(id = R.drawable.weight_24),
                contentDescription = null
            )
            Spacer(modifier = Modifier.padding(4.dp))
            Text(
                text = viewModel.getPetWeightFormattedString(uiState.pet.weight),
                style = MaterialTheme.typography.titleSmall
            )
        }
        PetBasicCardWithTrailingIcon(
            title = R.string.components_forms_title_new_pet_dimensions,
            onTrailingIconClicked = {
                navigateToAddDimensionsScreen(uiState.pet.petId.toString())
            },
            onCardItemClicked = { navigateToDimensionsDashboardScreen(uiState.pet.petId.toString()) }) {
            Icon(
                painter = painterResource(id = R.drawable.baseline_height_24),
                contentDescription = null
            )
            Spacer(modifier = Modifier.padding(4.dp))
            Text(
                text = viewModel.getPetDimensionsFormattedString(uiState.pet.height),
                style = MaterialTheme.typography.titleSmall
            )
            Spacer(modifier = Modifier.padding(4.dp))
            Icon(
                painter = painterResource(id = R.drawable.width_24),
                contentDescription = null
            )
            Spacer(modifier = Modifier.padding(4.dp))
            Text(
                text = viewModel.getPetDimensionsFormattedString(uiState.pet.length),
                style = MaterialTheme.typography.titleSmall
            )
            Spacer(modifier = Modifier.padding(4.dp))
            Icon(
                painter = painterResource(id = R.drawable.restart_alt_24),
                contentDescription = null
            )
            Spacer(modifier = Modifier.padding(4.dp))
            Text(
                text = viewModel.getPetDimensionsFormattedString(uiState.pet.circuit),
                style = MaterialTheme.typography.titleSmall
            )
        }
        PetBasicCardWithTrailingIcon(
            title = R.string.components_dashboard_card_title_meals,
            onCardItemClicked = { navigateToMealsDashboardScreen(uiState.pet.petId.toString()) },
            onTrailingIconClicked = { navigateToAddMealScreen(uiState.pet.petId.toString()) }) {
            Text(
                text = if (uiState.petMeals.isNullOrEmpty()) "Set meals" else uiState.petMeals.size.toString()
                    .plus(" times a day"), style = MaterialTheme.typography.titleSmall
            )
        }
        PetBasicCardWithTrailingIcon(
            title = R.string.components_dashboard_card_title_water,
            leadingIcon = R.drawable.round_refresh_24,
            onTrailingIconClicked = viewModel::onWaterRefillIconClicked,
            onCardItemClicked = {}) {
            Text(text = uiState.lastWaterChanged?.let {
                "Last changed: " + it.toHours().toString().plus(" hours ago")
            } ?: "Last changed: never", style = MaterialTheme.typography.titleSmall)
        }
    }
}


@Composable
fun PetDetailsGeneral(
    petName: String,
    petBirthDate: Instant,
    onSetPhotoIconClicked: () -> Unit
) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .height(IntrinsicSize.Max)
            .padding(bottom = 8.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Top
    ) {
        Box() {
            Image(
                modifier = Modifier
                    .size(120.dp)
                    .padding(16.dp)
                    .clip(RoundedCornerShape(50))
                    .background(MaterialTheme.colorScheme.primaryContainer),
                contentScale = ContentScale.Crop,
                painter = painterResource(id = R.drawable.icons8_pets_96),
                contentDescription = null
            )
            Box(
                modifier = Modifier
                    .clip(RoundedCornerShape(50))
                    .align(Alignment.BottomEnd)
            ) {
                IconButton(onClick = onSetPhotoIconClicked) {
                    Icon(
                        painter = painterResource(id = R.drawable.baseline_photo_camera_24),
                        contentDescription = "Set photo",
                        modifier = Modifier
                            .background(MaterialTheme.colorScheme.background, ShapeDefaults.Medium)
                            .padding(4.dp)
                    )
                }
            }
        }
        Column(
            modifier = Modifier.fillMaxWidth(),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            Text(text = petName, style = MaterialTheme.typography.titleLarge)
            Spacer(modifier = Modifier.padding(2.dp))
            Row(
                horizontalArrangement = Arrangement.Center,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Icon(
                    painter = painterResource(id = R.drawable.baseline_calendar_month_24),
                    contentDescription = null
                )
                Spacer(modifier = Modifier.padding(4.dp))
                Text(
                    text = DateTimeFormatter.ofLocalizedDate(FormatStyle.SHORT)
                        .withLocale(Locale.getDefault()).withZone(
                        ZoneId.systemDefault()
                    ).format(petBirthDate)
                )
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CardItem(onCardItemClicked: () -> Unit, content: @Composable() (ColumnScope.() -> Unit)) {
    Card(
        onClick = onCardItemClicked, content = content, modifier = Modifier
            .padding(8.dp)
            .fillMaxWidth()
    )
}

@Composable
fun PetBasicCardWithTrailingIcon(
    @StringRes title: Int,
    @DrawableRes leadingIcon: Int = R.drawable.round_add_24,
    onCardItemClicked: () -> Unit,
    onTrailingIconClicked: () -> Unit,
    content: @Composable() (RowScope.() -> Unit)
) {
    CardItem(onCardItemClicked = onCardItemClicked) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(12.dp)
                .height(IntrinsicSize.Max), horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Column {
                Text(
                    text = stringResource(id = title),
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.SemiBold
                )
                Spacer(modifier = Modifier.padding(4.dp))
                Row(
                    modifier = Modifier.height(IntrinsicSize.Max),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    content()
                }
            }
            Column(
                modifier = Modifier.fillMaxHeight(),
                verticalArrangement = Arrangement.Center
            ) {
                IconButton(onClick = onTrailingIconClicked) {
                    Icon(painter = painterResource(id = leadingIcon), contentDescription = null)
                }
            }
        }
    }
}