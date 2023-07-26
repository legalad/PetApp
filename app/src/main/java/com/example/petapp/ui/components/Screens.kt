package com.example.petapp.ui.components

import androidx.compose.animation.core.*
import androidx.compose.foundation.Image
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.scale
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.modifier.modifierLocalConsumer
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.petapp.R

@Composable
fun LoadingScreen(modifier: Modifier = Modifier){
    Box(
        modifier = modifier.fillMaxSize()
    ) {
        Image(painter = painterResource(id = R.drawable.hamster_loading), contentDescription = null, modifier = Modifier.align(
            Alignment.Center))
        LoadingAnimation(modifier = Modifier.align(Alignment.Center))
    }
}

@Composable
fun ErrorScreen(message: String, modifier: Modifier = Modifier){
    Column(
        modifier = modifier.fillMaxSize(),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Image(painter = painterResource(id = R.drawable.hamster_error), contentDescription = null)
        Text(text = "Ups! Something went wrong")
        Text(text = message)
    }
}

@Composable
fun LoadingAnimation(
    circleColor: Color = MaterialTheme.colorScheme.primary,
    animationDelay: Int = 1000,
    modifier: Modifier = Modifier
) {

    // circle's scale state
    var circleScale by remember {
        mutableStateOf(0f)
    }

    // animation
    val circleScaleAnimate = animateFloatAsState(
        targetValue = circleScale,
        animationSpec = infiniteRepeatable(
            animation = tween(
                durationMillis = animationDelay
            )
        )
    )

    // This is called when the app is launched
    LaunchedEffect(Unit) {
        circleScale = 1f
    }

    // animating circle
    Box(
        modifier = modifier
            .size(size = 64.dp)
            .scale(scale = circleScaleAnimate.value)
            .border(
                width = 4.dp,
                color = circleColor.copy(alpha = 1 - circleScaleAnimate.value),
                shape = CircleShape
            )
    )
}

@Preview
@Composable
fun LoadingScreenPrev() {
    LoadingScreen()
}

@Preview
@Composable
fun ErrorScreenPrev() {
    ErrorScreen(message = "Can't load page")
}