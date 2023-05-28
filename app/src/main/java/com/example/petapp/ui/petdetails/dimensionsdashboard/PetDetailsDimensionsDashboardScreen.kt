package com.example.petapp.ui.petdetails.dimensionsdashboard

import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.example.petapp.R
import com.example.petapp.model.util.Formatters
import com.example.petapp.ui.petdetails.weightdashboard.DataDisplayedType
import com.example.petapp.ui.petdetails.weightdashboard.DefaultChart
import com.example.petapp.ui.petdetails.weightdashboard.DefaultList

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun PetDimensionsDashboardResultScreen(
    viewModel: PetDetailsDimensionsDashboardViewModel,
    navigateToAddWeightScreen: (petId: String) -> Unit,
    navigateBack: () -> Unit
) {
    val uiState = viewModel.successUiState.collectAsState().value
    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text(text = stringResource(id = R.string.components_top_app_bar_title_pet_dimensions, uiState.petName)+ " " + stringResource(
                    id = uiState.displayedDimension.dimensionName
                )) },
                navigationIcon = { IconButton(onClick = navigateBack) {
                    Icon(imageVector = Icons.Default.ArrowBack, contentDescription = "back")
                } },
                actions = {
                    IconButton(onClick = viewModel::onChartIconClicked) {
                        Icon(painterResource(id = uiState.dataDisplayedType.chartIconId), contentDescription = "")
                    }
                    IconButton(onClick = viewModel::onDimensionIconClicked) {
                        Icon(painterResource(id = uiState.displayedDimension.dimensionIconId), contentDescription = "")
                    }
                }
            )
        },
        bottomBar = {
            Column (modifier = Modifier
                .fillMaxWidth()
                .padding(4.dp), horizontalAlignment = Alignment.CenterHorizontally){
                Button(onClick = { navigateToAddWeightScreen(uiState.petIdString) }) {
                    Icon(
                        painter = painterResource(id = R.drawable.round_add_24),
                        contentDescription = null
                    )
                    Spacer(modifier = Modifier.padding(4.dp))
                    Text(text = "input")
                }
            }
        }
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
                .padding(8.dp), horizontalAlignment = Alignment.CenterHorizontally
        ) {

            when(uiState.dataDisplayedType) {
                DataDisplayedType.LINE_CHART -> when(uiState.displayedDimension) {
                    DisplayedDimension.HEIGHT -> DefaultChart(
                        chartModelProducer = uiState.heightChartEntryModelProducer,
                        persistentMarkerX = uiState.heightPersistentMarkerX,
                        selectedDateEntry = uiState.heightSelectedDateEntry,
                        cardIconId = uiState.displayedDimension.dimensionIconId,
                        formattedSelectedDateEntry = Formatters.getFormattedDimensionString(value = uiState.heightSelectedDateEntry.y.toDouble(), unit = uiState.unit, context = LocalContext.current),
                        viewModel = viewModel
                    )
                    DisplayedDimension.LENGTH -> DefaultChart(
                        chartModelProducer = uiState.lengthChartEntryModelProducer,
                        persistentMarkerX = uiState.lengthPersistentMarkerX,
                        selectedDateEntry = uiState.lengthSelectedDateEntry,
                        cardIconId = uiState.displayedDimension.dimensionIconId,
                        formattedSelectedDateEntry = Formatters.getFormattedDimensionString(value = uiState.lengthSelectedDateEntry.y.toDouble(), unit = uiState.unit, context = LocalContext.current),
                        viewModel = viewModel
                    )
                    DisplayedDimension.CIRCUIT -> DefaultChart(
                        chartModelProducer = uiState.circuitChartEntryModelProducer,
                        persistentMarkerX = uiState.circuitPersistentMarkerX,
                        selectedDateEntry = uiState.circuitSelectedDateEntry,
                        cardIconId = uiState.displayedDimension.dimensionIconId,
                        formattedSelectedDateEntry = Formatters.getFormattedDimensionString(value = uiState.circuitSelectedDateEntry.y.toDouble(), unit = uiState.unit, context = LocalContext.current),
                        viewModel = viewModel
                    )
                }

                DataDisplayedType.LIST -> when(uiState.displayedDimension) {
                    DisplayedDimension.HEIGHT -> DefaultList(
                        listDateEntryList = uiState.heightHistoryListDateEntry,
                        unit = uiState.unit,
                        valueFormatterToString = Formatters::getFormattedDimensionString
                    )
                    DisplayedDimension.LENGTH -> DefaultList(
                        listDateEntryList = uiState.lengthHistoryListDateEntry,
                        unit = uiState.unit,
                        valueFormatterToString = Formatters::getFormattedDimensionString
                    )
                    DisplayedDimension.CIRCUIT -> DefaultList(
                        listDateEntryList = uiState.circuitHistoryListDateEntry,
                        unit = uiState.unit,
                        valueFormatterToString = Formatters::getFormattedDimensionString
                    )
                }
            }
        }
    }
}