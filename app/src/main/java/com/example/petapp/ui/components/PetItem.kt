package com.example.petapp.ui.components

import androidx.annotation.DrawableRes
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.petapp.model.Pet
import com.example.petapp.R
import java.util.Calendar

@Composable
fun PetItem(pet: Pet, modifier: Modifier = Modifier) {
    var expanded by rememberSaveable{mutableStateOf(false)}
    ElevatedCard(
        modifier = modifier.padding(8.dp).wrapContentSize()
    ) {
        Column (modifier = Modifier.fillMaxWidth()){
            Row (modifier = Modifier.fillMaxWidth()){
                PetIcon(petIcon = pet.petIconId?:R.drawable.nika, modifier = modifier.weight(3.5f))
                PetInfo(name = pet.name, age = pet.birthDate.year, weight = pet.weight, modifier = modifier.weight(4f))
                PetIconStats(modifier = Modifier.weight(6f))
                PetItemButton(expanded = expanded, onClick = { expanded = !expanded }, modifier = modifier.weight(1f))
            }
        }
    }
}

@Composable
fun PetItemButton(
    expanded: Boolean,
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    IconButton(onClick = onClick, modifier = modifier) {
        Icon(
            painter =
            if (expanded) painterResource(
                id = R.drawable.baseline_expand_more_24
            )
            else painterResource(
                id = R.drawable.baseline_expand_more_24
            ),
            contentDescription = stringResource(
                id = R.string.expand_button_content_description
            )
        )
    }
}

//may be change later, depends on image location
@Composable
fun PetIcon(@DrawableRes petIcon: Int = R.drawable.nika, modifier: Modifier = Modifier) {
    Image(
        modifier = modifier
            .size(88.dp)
            .padding(8.dp)
            .clip(RoundedCornerShape(50)),
        contentScale = ContentScale.Crop,
        painter = painterResource(id = petIcon),
        contentDescription = null)
}

@Composable
fun PetInfo(
    name: String,
    age: Int,
    weight: Double,
    modifier: Modifier = Modifier
) {
    Column (modifier = modifier) {
        Text(
            text = name,
            style = MaterialTheme.typography.headlineMedium,
            modifier = Modifier.padding(top = 8.dp)
        )
        Row () {
            Text(
                text = stringResource(id = R.string.pet_age) + ":",
                style = MaterialTheme.typography.bodySmall,
                modifier = Modifier.weight(1f)
            )
            Text(
                text = age.toString(),
                style = MaterialTheme.typography.bodySmall,
                modifier = Modifier.weight(1f)
            )
        }
        //add unit in preferences in the end of weight (preferences/data store)
        Row () {
            Text(
                text = stringResource(id = R.string.pet_weight) + ":",
                style = MaterialTheme.typography.bodySmall,
                modifier = Modifier.weight(1f)
            )
            Text(
                text = weight.toString(),
                style = MaterialTheme.typography.bodySmall,
                modifier = Modifier.weight(1f)
            )
        }
    }
}

@Composable
fun PetIconStats(modifier: Modifier = Modifier) {
    Row (modifier = modifier.height(88.dp), horizontalArrangement = Arrangement.Center, verticalAlignment = Alignment.CenterVertically){
        Icon(painter = painterResource(id = R.drawable.twotone_water_drop_24), contentDescription = null, tint = Color.Green, modifier = Modifier.size(44.dp))
        Icon(painter = painterResource(id = R.drawable.twotone_restaurant_24), contentDescription = null, tint = Color.Red, modifier = Modifier.size(44.dp))
        Icon(painter = painterResource(id = R.drawable.twotone_sports_football_24), contentDescription = null, tint = Color.Green, modifier = Modifier.size(44.dp))
    }
}

@Preview(showBackground = true)
@Composable
fun PetInfoPrev() {
    PetInfo(name = "Zeus", age = 1, weight = 3.1)
}

@Preview(showBackground = true)
@Composable
fun IconPrev() {
    PetIconStats()
}

@Preview(showBackground = true)
@Composable
fun PetItemPrev() {
    PetItem(pet = Pet(null, "Zeus", "Kot", Calendar.getInstance().time, 7.1))
}

