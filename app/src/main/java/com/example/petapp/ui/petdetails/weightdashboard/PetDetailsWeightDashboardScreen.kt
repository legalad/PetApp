package com.example.petapp.ui.petdetails.weightdashboard

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.example.petapp.R
import com.example.petapp.model.util.Formatters
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

    Column (modifier = Modifier.fillMaxSize().padding(8.dp), horizontalAlignment = Alignment.CenterHorizontally){
        Text(text = stringResource(id = R.string.weight_dashboard_headline), style = MaterialTheme.typography.headlineMedium, modifier = Modifier.padding(20.dp))
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
                            Text(text = "%.2f".format(petWeight.value - tmpWeight))
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