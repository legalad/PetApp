package com.example.petapp.ui.petdetails

import android.net.Uri
import android.util.Log
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.PickVisualMediaRequest
import androidx.activity.result.contract.ActivityResultContracts
import androidx.annotation.DrawableRes
import androidx.annotation.StringRes
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.MoreVert
import androidx.compose.material.icons.outlined.Edit
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import coil.compose.rememberAsyncImagePainter
import com.example.petapp.R
import com.example.petapp.data.PetDetailsView
import com.example.petapp.ui.components.CameraView
import com.example.petapp.ui.components.ErrorScreen
import com.example.petapp.ui.components.LoadingScreen
import kotlinx.coroutines.launch
import java.time.ZoneId
import java.time.format.DateTimeFormatter
import java.time.format.FormatStyle
import java.util.*

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun PetDetailsScreen(
    viewModel: PetDetailsViewModel,
    navigateBack: () -> Unit,
    navigateToUpdatePetDataScreen: (petId: String) -> Unit,
    navigateToAddWeightScreen: (petId: String) -> Unit,
    navigateToAddDimensionsScreen: (petId: String) -> Unit,
    navigateToAddMealScreen: (petId: String) -> Unit,
    navigateToWeightDashboardScreen: (petId: String) -> Unit,
    navigateToDimensionsDashboardScreen: (petId: String) -> Unit,
    navigateToMealsDashboardScreen: (petId: String) -> Unit,
    requestCameraPermission: (showCamera: () -> Unit) -> Unit,
    modifier: Modifier = Modifier
) {
    when (val uiState = viewModel.uiState.collectAsState().value) {
        is PetDetailsUiState.Error -> ErrorScreen(message = uiState.errorMessage)
        is PetDetailsUiState.Loading -> LoadingScreen()
        is PetDetailsUiState.Success -> {
            Scaffold(
                topBar = {
                    TopAppBar(
                        title = {
                            Text(
                                text = "",
                                maxLines = 1,
                                overflow = TextOverflow.Ellipsis
                            )
                        },

                        navigationIcon = {
                            IconButton(onClick = { navigateBack() }) {
                                Icon(
                                    imageVector = Icons.Default.ArrowBack,
                                    contentDescription = "back"
                                )
                            }
                        },
                        actions = {
                            IconButton(onClick = viewModel::onDropdownMenuItemClicked) {
                                Icon(
                                    Icons.Default.MoreVert,
                                    contentDescription = stringResource(R.string.components_top_app_bar_menu_content_description_open_menu)
                                )
                            }
                            DropdownMenu(
                                expanded = uiState.topBarMenuExpanded,
                                onDismissRequest = viewModel::dropdownMenuOnDismissRequest
                            ) {
                                DropdownMenuItem(
                                    text = { Text(stringResource(id = R.string.components_top_app_bar_navigation_menu_update)) },
                                    onClick = {
                                        uiState.pet?.petId?.let {
                                            navigateToUpdatePetDataScreen(it.toString())
                                            viewModel.dropdownMenuOnDismissRequest()
                                        } ?: navigateBack()
                                    },
                                    leadingIcon = {
                                        Icon(
                                            Icons.Outlined.Edit,
                                            contentDescription = null
                                        )
                                    })
                                Divider()
                                DropdownMenuItem(
                                    text = { Text(stringResource(id = R.string.components_top_app_bar_navigation_menu_delete)) },
                                    onClick = {
                                        navigateBack()
                                        viewModel.deletePet()
                                    },
                                    leadingIcon = {
                                        Icon(
                                            Icons.Default.Delete,
                                            contentDescription = null
                                        )
                                    })
                            }
                        })

                }
            )
            {
                Column(modifier = modifier.padding(it)) {
                    PetDetailsResultScreen(
                        uiState = uiState,
                        viewModel = viewModel,
                        navigateToAddWeightScreen = navigateToAddWeightScreen,
                        navigateToAddDimensionsScreen = navigateToAddDimensionsScreen,
                        navigateToAddMealScreen = navigateToAddMealScreen,
                        navigateToWeightDashboardScreen = navigateToWeightDashboardScreen,
                        navigateToDimensionsDashboardScreen = navigateToDimensionsDashboardScreen,
                        navigateToMealsDashboardScreen = navigateToMealsDashboardScreen,
                        requestCameraPermission = requestCameraPermission
                    )
                }
            }
        }
    }
}


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun PetDetailsResultScreen(
    uiState: PetDetailsUiState.Success,
    viewModel: PetDetailsViewModel,
    navigateToAddWeightScreen: (petId: String) -> Unit,
    navigateToAddDimensionsScreen: (petId: String) -> Unit,
    navigateToAddMealScreen: (petId: String) -> Unit,
    navigateToWeightDashboardScreen: (petId: String) -> Unit,
    navigateToDimensionsDashboardScreen: (petId: String) -> Unit,
    navigateToMealsDashboardScreen: (petId: String) -> Unit,
    requestCameraPermission: (showCamera: () -> Unit) -> Unit
) {
    val bottomSheetScaffoldState = rememberBottomSheetScaffoldState(
        bottomSheetState = SheetState(
            initialValue = SheetValue.Hidden,
            skipPartiallyExpanded = true,
            skipHiddenState = false
        )
    )
    var photoUri: Uri? by remember { mutableStateOf(null) }
    val coroutineScope = rememberCoroutineScope()
    val singlePhotoPickerLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.PickVisualMedia(),
        onResult = { uri ->
            viewModel.onSelectImage(uri)
            photoUri = uri
            Log.i("info", uri.toString() + " comp")
        })

    uiState.pet?.let {
        BottomSheetScaffold(
            scaffoldState = bottomSheetScaffoldState,
            sheetPeekHeight = 64.dp,
            sheetContent = {

                Column(
                    Modifier
                        .fillMaxWidth()
                        .padding(start = 16.dp, end = 16.dp, bottom = 8.dp),
                    horizontalAlignment = Alignment.Start
                ) {
                    Text("New picture")
                    Spacer(Modifier.height(20.dp))
                    BottomSheetItem(
                        stringId = R.string.components_bottom_sheet_item_camera,
                        iconId = R.drawable.baseline_photo_camera_24
                    ) {
                        requestCameraPermission { viewModel.handleShowCamera() }
                    }
                    BottomSheetItem(
                        stringId = R.string.components_bottom_sheet_item_gallery,
                        iconId = R.drawable.baseline_photo_library_24,
                        onBottomSheetItemClicked = {
                            coroutineScope.launch { bottomSheetScaffoldState.bottomSheetState.hide() }
                            singlePhotoPickerLauncher.launch(
                                PickVisualMediaRequest(
                                    ActivityResultContracts.PickVisualMedia.ImageOnly
                                )
                            )
                        })
                    Divider(Modifier.padding(4.dp))
                    BottomSheetItem(
                        stringId = R.string.components_bottom_sheet_item_cancel,
                        iconId = R.drawable.round_cancel_24,
                        textColor = MaterialTheme.colorScheme.error,
                        onBottomSheetItemClicked = {
                            coroutineScope.launch { bottomSheetScaffoldState.bottomSheetState.hide() }
                        })
                }
            }) { innerPadding ->
            if (uiState.shouldShowCamera) {
                CameraView(
                    filename = uiState.pet.name,
                    outputDirectory = uiState.outputDirectory,
                    onImageCaptured = viewModel::handleImageCapture,
                    onError = {},
                )
            } else {
                Box(Modifier.padding(innerPadding)) {

                    Column(modifier = Modifier.fillMaxSize()) {
                        PetDetailsGeneral(
                            pet = uiState.pet,
                            onSetPhotoIconClicked = { coroutineScope.launch { bottomSheetScaffoldState.bottomSheetState.expand() } },
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
                                text = if (uiState.petMeals.isEmpty()) "Set meals" else uiState.petMeals.size.toString()
                                    .plus(" times a day"),
                                style = MaterialTheme.typography.titleSmall
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
            }
        }
    }
}


@Composable
fun PetDetailsGeneral(
    pet: PetDetailsView,
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
        Box {
            Image(
                modifier = Modifier
                    .size(120.dp)
                    .padding(16.dp)
                    .clip(RoundedCornerShape(50))
                    .background(MaterialTheme.colorScheme.primaryContainer),
                contentScale = ContentScale.Crop,
                painter = rememberAsyncImagePainter(pet.imageUri ?: R.drawable.icons8_pets_96),
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
            Text(text = pet.name, style = MaterialTheme.typography.headlineSmall)
            Divider(Modifier.padding(start = 50.dp, top = 5.dp, bottom = 10.dp, end = 50.dp))
            Row(
                horizontalArrangement = Arrangement.Center,
                verticalAlignment = Alignment.CenterVertically
            ) {

                Icon(
                    painter = painterResource(id = pet.gender.iconId),
                    contentDescription = "Gender",
                    modifier = Modifier
                        .background(MaterialTheme.colorScheme.background, ShapeDefaults.Medium)
                        .padding(4.dp)
                        .size(18.dp)
                )
                Text(
                    text = stringResource(id = pet.species.nameId),
                    style = MaterialTheme.typography.labelLarge
                )
                Spacer(modifier = Modifier.padding(4.dp))
                Icon(
                    painter = painterResource(id = R.drawable.baseline_pets_24),
                    contentDescription = null,
                    modifier = Modifier.size(18.dp),
                    tint = Color.Transparent
                )

                //TODO breed setter
            }
            pet.breed?.let {
                Row(
                    horizontalArrangement = Arrangement.Center,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Icon(
                        painter = painterResource(id = R.drawable.baseline_pets_24),
                        contentDescription = null,
                        modifier = Modifier.size(18.dp)
                    )
                    Spacer(modifier = Modifier.padding(4.dp))
                    Text(
                        text = stringResource(id = pet.species.breeds[it].nameId),
                        style = MaterialTheme.typography.labelMedium
                    )
                    Spacer(modifier = Modifier.padding(4.dp))
                    Icon(
                        painter = painterResource(id = R.drawable.baseline_pets_24),
                        contentDescription = null,
                        modifier = Modifier.size(18.dp),
                        tint = Color.Transparent
                    )
                }

            }
            Spacer(modifier = Modifier.padding(4.dp))

            Row(
                horizontalArrangement = Arrangement.Center,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Icon(
                    painter = painterResource(id = R.drawable.baseline_calendar_month_24),
                    contentDescription = null,
                    modifier = Modifier.size(18.dp)
                )
                Spacer(modifier = Modifier.padding(4.dp))
                Text(
                    text = DateTimeFormatter.ofLocalizedDate(FormatStyle.SHORT)
                        .withLocale(Locale.getDefault()).withZone(
                            ZoneId.systemDefault()
                        ).format(pet.birthDate),
                    style = MaterialTheme.typography.labelLarge
                )
                Spacer(modifier = Modifier.padding(4.dp))
                Icon(
                    painter = painterResource(id = R.drawable.baseline_pets_24),
                    contentDescription = null,
                    modifier = Modifier.size(18.dp),
                    tint = Color.Transparent
                )
            }
            Divider(Modifier.padding(start = 50.dp, top = 10.dp, bottom = 5.dp, end = 50.dp))
            Spacer(modifier = Modifier.padding(2.dp))
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


@Composable
private fun BottomSheetItem(
    @StringRes stringId: Int,
    @DrawableRes iconId: Int? = null,
    textColor: Color = MaterialTheme.colorScheme.onBackground,
    onBottomSheetItemClicked: () -> Unit
) {
    Row(modifier = Modifier
        .clickable {
            onBottomSheetItemClicked()
        }
        .padding(top = 8.dp, bottom = 8.dp)
        .fillMaxWidth(),
        verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.Start) {
        iconId?.let {
            Icon(painter = painterResource(id = it), contentDescription = null)
            Spacer(modifier = Modifier.padding(10.dp))
        }
        Text(
            text = stringResource(id = stringId),
            style = MaterialTheme.typography.titleMedium,
            color = textColor
        )
    }
}

