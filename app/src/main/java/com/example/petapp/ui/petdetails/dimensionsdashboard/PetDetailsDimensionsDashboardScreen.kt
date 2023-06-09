package com.example.petapp.ui.petdetails.dimensionsdashboard

import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.example.petapp.model.util.Formatters
import com.example.petapp.ui.components.MeasureScaffold
import com.example.petapp.ui.components.NoContentPrev
import com.example.petapp.ui.petdetails.weightdashboard.DataDisplayedType
import com.example.petapp.ui.petdetails.weightdashboard.DefaultChart
import com.example.petapp.ui.petdetails.weightdashboard.DefaultList

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun PetDimensionsDashboardResultScreen(
    viewModel: PetDetailsDimensionsDashboardViewModel,
    navigateToAddDimensionsScreen: (petId: String) -> Unit,
    navigateToUpdateHeightScreen: (petId: String, heightId: String) -> Unit,
    navigateToUpdateLengthScreen: (petId: String, lengthId: String) -> Unit,
    navigateToUpdateCircuitScreen: (petId: String, circuitId: String) -> Unit,
    navigateBack: () -> Unit
) {
    val uiState = viewModel.successUiState.collectAsState().value
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
                Icon(painterResource(id = uiState.displayedDimension.dimensionIconId), contentDescription = "")
            }
        }
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
                            valueFormatterToString = Formatters::getFormattedDimensionUnitString
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
                            valueFormatterToString = Formatters::getFormattedDimensionUnitString
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
                            valueFormatterToString = Formatters::getFormattedDimensionUnitString
                        )
                    }

                }
            }
        }
    }
}