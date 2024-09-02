package com.example.harrypotter.ui.theme.screen.details

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.SnackbarDuration
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import coil.compose.AsyncImage
import com.example.harrypotter.R
import com.example.harrypotter.data.source.local.model.CharacterEntity

@Composable
fun CharacterDetailScreen(
    viewModel: CharacterDetailViewModel = hiltViewModel()
) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()

    Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
        val snackBarHostState = remember { SnackbarHostState() }
        when {
            uiState.isLoading -> {
                Box(modifier = Modifier.fillMaxSize()) {
                    CircularProgressIndicator(
                        modifier = Modifier
                            .align(Alignment.Center)
                    )
                }
            }

            uiState.errorMessage != null -> {
                Box(modifier = Modifier.fillMaxSize()) {
                    LaunchedEffect(uiState.errorMessage) {
                        snackBarHostState.showSnackbar(
                            message = uiState.errorMessage.toString(),
                            duration = SnackbarDuration.Short
                        )
                    }
                    Text(
                        text = stringResource(id = R.string.empty_data_error),
                        color = Color.Red,
                        modifier = Modifier.align(Alignment.Center)
                    )
                }
            }

            else -> {
                CharacterDetailContent(uiState.character)
            }
        }
    }
}

@Composable
fun CharacterDetailContent(character: CharacterEntity?) {
    if (character == null) {
        Text(text = stringResource(id = R.string.empty_data_error), color = Color.Red)
        return
    }

    Column(

        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        AsyncImage(
            model = character.imageUrl,
            contentDescription = "${character.name}'s image",
            contentScale = ContentScale.Crop,
            modifier = Modifier
                .size(200.dp)
                .clip(CircleShape)
                .align(Alignment.CenterHorizontally)
        )
        Spacer(modifier = Modifier.height(16.dp))
        Text(
            text = character.name,
            style = MaterialTheme.typography.titleMedium,
            textAlign = TextAlign.Center,
            color = Color.White,
            modifier = Modifier
                .align(Alignment.CenterHorizontally)
                .background(color = Color(character.houseColor))
                .fillMaxWidth()
        )

        Spacer(modifier = Modifier.height(8.dp))
        Text(
            text = stringResource(R.string.actor, character.actor),
            style = MaterialTheme.typography.bodyMedium
        )
        Spacer(modifier = Modifier.height(4.dp))
        Text(
            text = stringResource(R.string.species, character.species),
            style = MaterialTheme.typography.bodyMedium
        )
        Spacer(modifier = Modifier.height(4.dp))
        Text(
            text = stringResource(R.string.status, character.status),
            style = MaterialTheme.typography.bodyMedium
        )
        character.dateOfBirth?.let {
            Spacer(modifier = Modifier.height(4.dp))
            Text(
                text = stringResource(R.string.date_of_birth, it),
                style = MaterialTheme.typography.bodyMedium
            )
        }
        character.house?.let {
            Spacer(modifier = Modifier.height(4.dp))
            Text(
                text = stringResource(R.string.house, it),
                style = MaterialTheme.typography.bodyMedium,
                color = Color(character.houseColor)
            )
        }
    }
}

@Preview(showBackground = true)
@Composable
fun CharacterDetailScreenPreview() {
    val mockCharacter = CharacterEntity(
        name = "Harry Potter",
        actor = "Daniel Radcliffe",
        species = "Human",
        house = "Gryffindor",
        houseColor = 0xFF740001.toInt(),
        dateOfBirth = "31 July 1980",
        imageUrl = "https://hp-api.herokuapp.com/images/harry.jpg",
        status = "Alive"
    )

    CharacterDetailContent(mockCharacter)
}