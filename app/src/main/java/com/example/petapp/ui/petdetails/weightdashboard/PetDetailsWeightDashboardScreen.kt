package com.example.petapp.ui.petdetails.weightdashboard

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.example.petapp.R
import com.example.petapp.model.util.Formatters
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
import java.time.ZoneId
import java.time.format.DateTimeFormatter
import java.time.format.FormatStyle
import java.util.*

@Composable
fun PetWeightDashboardResultScreen(
    viewModel: PetDetailsWeightDashboardViewModel
) {
    val uiState = viewModel.successUiState.collectAsState().value
    var tmpWeight = 0.0

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(8.dp), horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(
            text = stringResource(id = R.string.weight_dashboard_headline),
            style = MaterialTheme.typography.headlineMedium,
            modifier = Modifier.padding(20.dp)
        )

        DefaultChart(chartModelProducer = uiState.chartEntryModelProducer)

        Spacer(modifier = Modifier.padding(20.dp))

        LazyColumn() {
            items(uiState.weightHistoryList) { petWeight ->
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(8.dp), horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Text(
                        text = DateTimeFormatter.ofLocalizedDate(FormatStyle.SHORT).withLocale(
                            Locale.getDefault()
                        ).withZone(ZoneId.systemDefault()).format(petWeight.measurementDate),
                        modifier = Modifier.weight(1f)
                    )
                    Row(
                        modifier = Modifier
                            .width(IntrinsicSize.Max)
                            .weight(1f),
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        Row() {
                            if (petWeight.value - tmpWeight < 0) Icon(
                                painter = painterResource(id = R.drawable.round_arrow_drop_down_24),
                                contentDescription = "",
                                tint = Color.Red
                            )
                            else if (petWeight.value - tmpWeight > 0) Icon(
                                painter = painterResource(
                                    id = R.drawable.round_arrow_drop_up_24
                                ), contentDescription = "", tint = Color.Green
                            )
                            Text(text = Formatters.getWeightString( petWeight.value - tmpWeight, uiState.unit))
                        }

                        Text(
                            text = Formatters.getFormattedWeightString(
                                petWeight.value,
                                uiState.unit,
                                context = LocalContext.current
                            )
                        )
                    }
                }
                tmpWeight = petWeight.value
            }
        }
    }
}

@Composable
fun DefaultChart(
    chartModelProducer: ChartEntryModelProducer
) {
    val marker = rememberMarker()
    Row(modifier = Modifier
        .fillMaxWidth()
        .padding(10.dp)) {
        Chart(
            chart = lineChart(
                persistentMarkers = remember(marker) { mapOf(PERSISTENT_MARKER_X to marker) },
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
            marker = marker)
    }
}
private const val PERSISTENT_MARKER_X = 10f