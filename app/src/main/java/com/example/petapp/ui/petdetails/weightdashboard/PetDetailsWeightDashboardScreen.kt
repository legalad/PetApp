package com.example.petapp.ui.petdetails.weightdashboard

import android.content.Context
import androidx.annotation.DrawableRes
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.example.android.datastore.UserPreferences
import com.example.petapp.R
import com.example.petapp.model.util.Formatters
import com.example.petapp.ui.components.ErrorScreen
import com.example.petapp.ui.components.LoadingScreen
import com.example.petapp.ui.components.MeasureScaffold
import com.example.petapp.ui.components.NoContentPrev
import com.example.petapp.ui.components.charts.rememberMarker
import com.patrykandpatrick.vico.compose.axis.horizontal.bottomAxis
import com.patrykandpatrick.vico.compose.axis.vertical.endAxis
import com.patrykandpatrick.vico.compose.chart.Chart
import com.patrykandpatrick.vico.compose.chart.line.lineChart
import com.patrykandpatrick.vico.compose.chart.line.lineSpec
import com.patrykandpatrick.vico.compose.component.shape.shader.verticalGradient
import com.patrykandpatrick.vico.compose.component.shapeComponent
import com.patrykandpatrick.vico.compose.dimensions.dimensionsOf
import com.patrykandpatrick.vico.core.axis.horizontal.HorizontalAxis
import com.patrykandpatrick.vico.core.chart.values.AxisValuesOverrider
import com.patrykandpatrick.vico.core.component.shape.Shapes
import com.patrykandpatrick.vico.core.entry.ChartEntryModelProducer
import com.patrykandpatrick.vico.core.extension.ceil
import com.patrykandpatrick.vico.core.extension.floor
import com.patrykandpatrick.vico.core.marker.MarkerVisibilityChangeListener
import java.time.ZoneId
import java.time.format.DateTimeFormatter
import java.time.format.FormatStyle
import java.util.*


@Composable
fun PetWeightDashboardScreen(
    viewModel: PetDetailsWeightDashboardViewModel,
    navigateToAddWeightScreen: (petId: String) -> Unit,
    navigateToUpdateWeightScreen: (petId: String, weightId: String) -> Unit,
    navigateBack: () -> Unit
) {
    val uiState = viewModel.successUiState.collectAsState().value
    MeasureScaffold(
        topAppBarTitle = stringResource(
            id = R.string.components_top_app_bar_title_pet_weight,
            uiState.petName
        ),
        topAppBarMenuExpanded = uiState.topAppBarMenuExpanded,
        navigateToAddDataScreen = {
            navigateToAddWeightScreen(uiState.petIdString)
        },
        navigateToUpdateDataScreen = {
            viewModel.getSelectedWeightId()
                ?.let {
                    navigateToUpdateWeightScreen(
                        uiState.petIdString,
                        it
                    )
                }
        },
        deleteDataItem = viewModel::deleteWeightItem,
        navigateBack = navigateBack,
        onDropdownMenuItemClicked = viewModel::onDropdownMenuIconClicked,
        dropdownMenuOnDismissRequest = viewModel::dropdownMenuOnDismissRequest,
        isListNotEmpty = uiState.weightHistoryList.isNotEmpty(),
        actions = {
            if (uiState.weightHistoryList.isNotEmpty()) {
                IconButton(onClick = viewModel::onChartIconClicked) {
                    Icon(
                        painterResource(id = uiState.dataDisplayedType.chartIconId),
                        contentDescription = ""
                    )
                }
            }
        }
    ) {
        when (viewModel.uiState) {
            is PetDetailsWeightDashboardUiState.Error ->
                ErrorScreen(message = "error")
            is PetDetailsWeightDashboardUiState.Loading ->
                LoadingScreen()
            is PetDetailsWeightDashboardUiState.NoData ->
                NoContentPrev()
            is PetDetailsWeightDashboardUiState.Success ->
                PetWeightDashboardResultScreen(
                    viewModel = viewModel,
                    modifier = Modifier.padding(it)
                )
        }
    }
}


@Composable
fun PetWeightDashboardResultScreen(
    viewModel: PetDetailsWeightDashboardViewModel,
    modifier: Modifier = Modifier,

    ) {
    val uiState = viewModel.successUiState.collectAsState().value
    Column(
        modifier = modifier
            .fillMaxSize()
            .padding(8.dp), horizontalAlignment = Alignment.CenterHorizontally
    ) {

        when (uiState.dataDisplayedType) {
            DataDisplayedType.LINE_CHART -> DefaultChart(
                chartModelProducer = uiState.chartEntryModelProducer,
                viewModel = viewModel,
                persistentMarkerX = uiState.persistentMarkerX,
                selectedDateEntry = uiState.selectedDateEntry,
                formattedSelectedDateEntry = Formatters.getFormattedWeightString(
                    weight = uiState.selectedDateEntry.y.toDouble(),
                    unit = uiState.unit,
                    context = LocalContext.current
                ),
                cardIconId = R.drawable.weight_24
            )

            DataDisplayedType.LIST -> DefaultList(
                listDateEntryList = uiState.weightHistoryList,
                unit = uiState.unit,
                valueFormatterToString = Formatters::getFormattedWeightString
            )

        }

    }
}

@Composable
fun DefaultList(
    listDateEntryList: List<ListDateEntry>,
    valueFormatterToString: (value: Double, unit: UserPreferences.Unit, context: Context) -> String,
    unit: UserPreferences.Unit
) {

    LazyColumn() {
        items(listDateEntryList) { petValue ->
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(8.dp), horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Text(
                    text = DateTimeFormatter.ofLocalizedDate(FormatStyle.SHORT).withLocale(
                        Locale.getDefault()
                    ).withZone(ZoneId.systemDefault()).format(petValue.localDate),
                    modifier = Modifier.weight(1f)
                )
                Row(
                    modifier = Modifier
                        .width(IntrinsicSize.Max)
                        .weight(1f),
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Row() {
                        Icon(
                            painter = painterResource(id = petValue.changeIconId),
                            contentDescription = null,
                            tint = petValue.changeIconColor
                        )
                        Text(
                            text = valueFormatterToString(
                                petValue.changeValue,
                                unit,
                                LocalContext.current
                            )
                        )
                    }

                    Text(
                        text = valueFormatterToString(petValue.value, unit, LocalContext.current)
                    )
                }
            }
        }
    }
}

@Composable
fun DefaultChart(
    chartModelProducer: ChartEntryModelProducer,
    persistentMarkerX: Float,
    selectedDateEntry: ChartDateEntry,
    @DrawableRes cardIconId: Int,
    viewModel: MarkerVisibilityChangeListener,
    formattedSelectedDateEntry: String
) {
    val marker = rememberMarker()
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(10.dp)
    ) {
        Row(

        ) {
            Chart(
                chart = lineChart(
                    persistentMarkers = remember(marker) { mapOf(persistentMarkerX to marker) },
                    lines = listOf(
                        lineSpec(
                            lineColor = MaterialTheme.colorScheme.primary,
                            lineBackgroundShader = verticalGradient(
                                arrayOf(
                                    MaterialTheme.colorScheme.primary.copy(0.5f),
                                    MaterialTheme.colorScheme.secondary.copy(alpha = 0f)
                                ),
                            ),
                            point = shapeComponent(
                                shape = Shapes.pillShape,
                                color = MaterialTheme.colorScheme.onPrimary,
                                strokeWidth = 1.dp,
                                strokeColor = MaterialTheme.colorScheme.primary,
                                margins = dimensionsOf(4.dp)
                            )
                        ),
                    ),
                    axisValuesOverrider = AxisValuesOverrider.fixed(
                        minY = (chartModelProducer.getModel().minY * 0.9f).floor,
                        maxY = (chartModelProducer.getModel().maxY * 1.1f).ceil,
                    ),
                    spacing = 60.dp
                ),
                endAxis = endAxis(
                ),
                bottomAxis = bottomAxis(
                    tickPosition = HorizontalAxis.TickPosition.Edge,
                    valueFormatter = Formatters.getAxisValueFormatterWithDate()
                ),
                chartModelProducer = chartModelProducer,
                marker = marker,
                markerVisibilityChangeListener = viewModel
            )
        }
        Spacer(modifier = Modifier.padding(16.dp))
        ElevatedCard() {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(10.dp)
            ) {
                Text(
                    text = DateTimeFormatter.ofLocalizedDate(FormatStyle.FULL)
                        .withLocale(Locale.getDefault()).withZone(
                            ZoneId.systemDefault()
                        )
                        .format(selectedDateEntry.localDate) + " - " + DateTimeFormatter.ofLocalizedTime(
                        FormatStyle.SHORT
                    ).withZone(
                        ZoneId.systemDefault()
                    ).withLocale(Locale.getDefault()).format(selectedDateEntry.localDate)
                )
                Column(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Spacer(modifier = Modifier.padding(8.dp))
                    Icon(painter = painterResource(id = cardIconId), contentDescription = "")
                    Spacer(modifier = Modifier.padding(4.dp))
                    Text(
                        text = formattedSelectedDateEntry,
                        style = MaterialTheme.typography.titleMedium
                    )
                }
            }

        }
    }
}