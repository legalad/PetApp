package com.example.petapp.ui.petdetails.dimensionsdashboard

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.example.petapp.model.util.Formatters
import com.example.petapp.ui.components.ErrorScreen
import com.example.petapp.ui.components.LoadingScreen
import com.example.petapp.ui.components.MeasureScaffold
import com.example.petapp.ui.components.NoContentPrev
import com.example.petapp.ui.petdetails.weightdashboard.DataDisplayedType
import com.example.petapp.ui.petdetails.weightdashboard.DefaultChart
import com.example.petapp.ui.petdetails.weightdashboard.DefaultList

@Composable
fun PetDimensionsDashboardScreen(
    viewModel: PetDetailsDimensionsDashboardViewModel,
    navigateToAddDimensionsScreen: (petId: String) -> Unit,
    navigateToUpdateHeightScreen: (petId: String, heightId: String) -> Unit,
    navigateToUpdateLengthScreen: (petId: String, lengthId: String) -> Unit,
    navigateToUpdateCircuitScreen: (petId: String, circuitId: String) -> Unit,
    navigateBack: () -> Unit
) {
    when (val uiState = viewModel.uiState.collectAsState().value) {
        PetDetailsDimensionsDashboardUiState.Loading -> LoadingScreen()
        is PetDetailsDimensionsDashboardUiState.Success -> PetDimensionsDashboardResultScreen(
            uiState = uiState,
            viewModel = viewModel,
            navigateToAddDimensionsScreen = navigateToAddDimensionsScreen,
            navigateToUpdateHeightScreen = navigateToUpdateHeightScreen,
            navigateToUpdateLengthScreen = navigateToUpdateLengthScreen,
            navigateToUpdateCircuitScreen = navigateToUpdateCircuitScreen,
            navigateBack = navigateBack
        )
        is PetDetailsDimensionsDashboardUiState.Error -> ErrorScreen(message = uiState.errorMessage)
    }
}

@Composable
fun PetDimensionsDashboardResultScreen(
    uiState: PetDetailsDimensionsDashboardUiState.Success,
    viewModel: PetDetailsDimensionsDashboardViewModel,
    navigateToAddDimensionsScreen: (petId: String) -> Unit,
    navigateToUpdateHeightScreen: (petId: String, heightId: String) -> Unit,
    navigateToUpdateLengthScreen: (petId: String, lengthId: String) -> Unit,
    navigateToUpdateCircuitScreen: (petId: String, circuitId: String) -> Unit,
    navigateBack: () -> Unit
) {
    MeasureScaffold(
        topAppBarTitle = uiState.petName + " " + stringResource(
            id = uiState.displayedDimension.dimensionName
        ),
        topAppBarMenuExpanded = uiState.topAppBarMenuExpanded,
        navigateToAddDataScreen = { navigateToAddDimensionsScreen(uiState.petIdString) },
        navigateToUpdateDataScreen = {
            when (uiState.displayedDimension) {
                DisplayedDimension.HEIGHT -> viewModel.getSelectedHeightId()
                    ?.let { navigateToUpdateHeightScreen(uiState.petIdString, it) }
                DisplayedDimension.LENGTH -> viewModel.getSelectedLengthId()
                    ?.let { navigateToUpdateLengthScreen(uiState.petIdString, it) }
                DisplayedDimension.CIRCUIT -> viewModel.getSelectedCircuitId()
                    ?.let { navigateToUpdateCircuitScreen(uiState.petIdString, it) }
            }
        },
        deleteDataItem = viewModel::deleteSelectedDataItem,
        navigateBack = navigateBack,
        onDropdownMenuItemClicked = viewModel::onDropdownMenuIconClicked,
        dropdownMenuOnDismissRequest = viewModel::dropdownMenuOnDismissRequest,
        isListNotEmpty = when (uiState.displayedDimension) {
            DisplayedDimension.HEIGHT -> uiState.heightHistoryListDateEntry.isNotEmpty()
            DisplayedDimension.LENGTH -> uiState.lengthHistoryListDateEntry.isNotEmpty()
            DisplayedDimension.CIRCUIT -> uiState.circuitHistoryListDateEntry.isNotEmpty()
        },
        actions = {
            if (uiState.selectedDimensionsItems.isNotEmpty()) {
                if (uiState.selectedDimensionsItems.size == 1) IconButton(onClick = {
                    when (uiState.displayedDimension) {
                        DisplayedDimension.HEIGHT -> navigateToUpdateHeightScreen(
                            uiState.petIdString,
                            uiState.selectedDimensionsItems.first().id.toString()
                        ).apply {
                            viewModel.clearSelectedDimensionItems(300)
                        }
                        DisplayedDimension.LENGTH -> navigateToUpdateLengthScreen(
                            uiState.petIdString,
                            uiState.selectedDimensionsItems.first().id.toString()
                        ).apply {
                            viewModel.clearSelectedDimensionItems(300)
                        }
                        DisplayedDimension.CIRCUIT -> navigateToUpdateCircuitScreen(
                            uiState.petIdString,
                            uiState.selectedDimensionsItems.first().id.toString()
                        ).apply {
                            viewModel.clearSelectedDimensionItems(300)
                        }
                    }

                }) {
                    Icon(imageVector = Icons.Default.Edit, contentDescription = "")
                }
                IconButton(onClick = {
                    viewModel.deletePetDimensions()
                }) {
                    Icon(imageVector = Icons.Default.Delete, contentDescription = "")
                }
            } else {
                when (uiState.displayedDimension) {
                    DisplayedDimension.HEIGHT -> if (uiState.heightHistoryListDateEntry.isNotEmpty()) {
                        IconButton(onClick = viewModel::onChartIconClicked) {
                            Icon(
                                painterResource(id = uiState.dataDisplayedType.chartIconId),
                                contentDescription = ""
                            )
                        }
                    }
                    DisplayedDimension.LENGTH -> if (uiState.lengthHistoryListDateEntry.isNotEmpty()) {
                        IconButton(onClick = viewModel::onChartIconClicked) {
                            Icon(
                                painterResource(id = uiState.dataDisplayedType.chartIconId),
                                contentDescription = ""
                            )
                        }
                    }
                    DisplayedDimension.CIRCUIT -> if (uiState.circuitHistoryListDateEntry.isNotEmpty()) {
                        IconButton(onClick = viewModel::onChartIconClicked) {
                            Icon(
                                painterResource(id = uiState.dataDisplayedType.chartIconId),
                                contentDescription = ""
                            )
                        }
                    }
                }
                IconButton(onClick = viewModel::onDimensionIconClicked) {
                    Icon(
                        painterResource(id = uiState.displayedDimension.dimensionIconId),
                        contentDescription = ""
                    )
                }
            }
        },
        clearSelectedItems = viewModel::clearSelectedDimensionItems,
        itemsSelected = uiState.selectedDimensionsItems.isEmpty()
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(it)
                .padding(8.dp), horizontalAlignment = Alignment.CenterHorizontally
        ) {
            when (uiState.displayedDimension) {
                DisplayedDimension.HEIGHT -> if (uiState.heightHistoryListDateEntry.isEmpty()) {
                    NoContentPrev()
                } else {
                    when (uiState.dataDisplayedType) {
                        DataDisplayedType.LINE_CHART -> DefaultChart(
                            chartModelProducer = uiState.heightChartEntryModelProducer,
                            persistentMarkerX = uiState.heightPersistentMarkerX,
                            selectedDateEntry = uiState.heightSelectedDateEntry,
                            cardIconId = uiState.displayedDimension.dimensionIconId,
                            formattedSelectedDateEntry = Formatters.getFormattedDimensionUnitString(
                                value = uiState.heightSelectedDateEntry.y.toDouble(),
                                unit = uiState.unit,
                                context = LocalContext.current
                            ),
                            viewModel = viewModel
                        )
                        DataDisplayedType.LIST -> DefaultList(
                            listDateEntryList = uiState.heightHistoryListDateEntry,
                            unit = uiState.unit,
                            valueFormatterToString = Formatters::getFormattedDimensionUnitString,
                            onWeightItemLongClicked = viewModel::onDimensionItemLongClicked,
                            onWeightItemClicked = viewModel::onDimensionItemClicked
                        )
                    }
                }
                DisplayedDimension.LENGTH -> if (uiState.lengthHistoryListDateEntry.isEmpty()) {
                    NoContentPrev()
                } else {
                    when (uiState.dataDisplayedType) {
                        DataDisplayedType.LINE_CHART -> DefaultChart(
                            chartModelProducer = uiState.lengthChartEntryModelProducer,
                            persistentMarkerX = uiState.lengthPersistentMarkerX,
                            selectedDateEntry = uiState.lengthSelectedDateEntry,
                            cardIconId = uiState.displayedDimension.dimensionIconId,
                            formattedSelectedDateEntry = Formatters.getFormattedDimensionUnitString(
                                value = uiState.lengthSelectedDateEntry.y.toDouble(),
                                unit = uiState.unit,
                                context = LocalContext.current
                            ),
                            viewModel = viewModel
                        )
                        DataDisplayedType.LIST -> DefaultList(
                            listDateEntryList = uiState.lengthHistoryListDateEntry,
                            unit = uiState.unit,
                            valueFormatterToString = Formatters::getFormattedDimensionUnitString,
                            onWeightItemLongClicked = viewModel::onDimensionItemLongClicked,
                            onWeightItemClicked = viewModel::onDimensionItemClicked
                        )
                    }
                }
                DisplayedDimension.CIRCUIT -> if (uiState.circuitHistoryListDateEntry.isEmpty()) {
                    NoContentPrev()
                } else {
                    when (uiState.dataDisplayedType) {
                        DataDisplayedType.LINE_CHART -> DefaultChart(
                            chartModelProducer = uiState.circuitChartEntryModelProducer,
                            persistentMarkerX = uiState.circuitPersistentMarkerX,
                            selectedDateEntry = uiState.circuitSelectedDateEntry,
                            cardIconId = uiState.displayedDimension.dimensionIconId,
                            formattedSelectedDateEntry = Formatters.getFormattedDimensionUnitString(
                                value = uiState.circuitSelectedDateEntry.y.toDouble(),
                                unit = uiState.unit,
                                context = LocalContext.current
                            ),
                            viewModel = viewModel
                        )
                        DataDisplayedType.LIST -> DefaultList(
                            listDateEntryList = uiState.circuitHistoryListDateEntry,
                            unit = uiState.unit,
                            valueFormatterToString = Formatters::getFormattedDimensionUnitString,
                            onWeightItemLongClicked = viewModel::onDimensionItemLongClicked,
                            onWeightItemClicked = viewModel::onDimensionItemClicked
                        )
                    }
                }
            }
        }
    }
}