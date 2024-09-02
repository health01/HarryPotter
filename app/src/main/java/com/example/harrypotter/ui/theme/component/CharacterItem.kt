package com.example.harrypotter.ui.theme.component

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.toArgb
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import coil.compose.AsyncImage
import com.example.harrypotter.R
import com.example.harrypotter.data.source.local.model.CharacterEntity
import com.example.harrypotter.ui.theme.GryffindorColor
import com.example.harrypotter.ui.theme.SlytherinColor

@Composable
fun CharacterItem(character: CharacterEntity, onClick: () -> Unit = {}) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .background(color = Color(character.houseColor))
            .padding(8.dp)
            .clickable(onClick = onClick)
    ) {
        AsyncImage(
            model = character.imageUrl,
            contentDescription = "${character.name}'s image",
            contentScale = ContentScale.Crop,
            placeholder = painterResource(R.drawable.ic_launcher_foreground),
            error = painterResource(R.drawable.ic_launcher_foreground),
            modifier = Modifier
                .size(100.dp)
                .clip(RoundedCornerShape(percent = 20))
                .align(Alignment.CenterVertically)
        )

        Spacer(modifier = Modifier.width(8.dp))

        Column {
            Text(text = character.name, color = Color.White)
            Text(text = "Actor: ${character.actor}", color = Color.White)
            Text(text = "Species: ${character.species}", color = Color.White)
            Text(text = "imageUrl: ${character.imageUrl}", color = Color.White)
        }
    }
}

@Preview(showBackground = true)
@Composable
fun PreviewCharacterRow() {
    val sampleCharacter = CharacterEntity(
        name = "Harry Potter",
        actor = "Daniel Radcliffe",
        species = "Human",
        house = "Gryffindor",
        houseColor = 0xFF740001.toInt(), // Gryffindor color
        dateOfBirth = "31 July 1980",
        imageUrl = "https://hp-api.herokuapp.com/images/harry.jpg",
        status = "Alive"
    )
    CharacterItem(character = sampleCharacter)
}

@Preview(showBackground = true)
@Composable
fun PreviewCharacterListScreen() {
    val sampleCharacters = listOf(
        CharacterEntity(
            name = "Harry Potter",
            actor = "Daniel Radcliffe",
            species = "Human",
            house = "Gryffindor",
            houseColor = GryffindorColor.toArgb(),
            dateOfBirth = "31 July 1980",
            imageUrl = null,
            status = "Alive"
        ),
        CharacterEntity(
            name = "Hermione Granger",
            actor = "Emma Watson",
            species = "Human",
            house = "Gryffindor",
            houseColor = SlytherinColor.toArgb(),
            dateOfBirth = "19 September 1979",
            imageUrl = null,
            status = "Alive"
        )
    )
    CharacterListScreenPreview(characters = sampleCharacters)
}

@Composable
fun CharacterListScreenPreview(characters: List<CharacterEntity>) {
    LazyColumn {
        items(characters) { character ->
            CharacterItem(character = character)
        }
    }
}