package com.example.petapp.ui.components.forms

import androidx.annotation.StringRes
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.*
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.example.petapp.R

@Composable
fun FormDefaultColumn(
    columnOnClicked: () -> Unit,
    modifier: Modifier = Modifier,
    @StringRes headline: Int = R.string.components_forms_description_fill_out_v2,
    @StringRes supportingText: Int = R.string.components_forms_description_fill_out,
    content: @Composable() (ColumnScope.() -> Unit)
) {
    val interactionSource = remember { MutableInteractionSource() }
    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Top,
        modifier = modifier
            .fillMaxSize()
            .clickable(
                onClick = columnOnClicked,
                interactionSource = interactionSource,
                indication = null
            )
            .padding(top = 20.dp)
    ) {
        Column(
            modifier = Modifier.widthIn(200.dp, 300.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(
                text = stringResource(id = headline),
                style = MaterialTheme.typography.headlineMedium
            )
            Text(
                text = stringResource(id = supportingText),
                style = MaterialTheme.typography.bodySmall
            )
            Spacer(modifier = Modifier.padding(20.dp))
            content()
        }
    }
}