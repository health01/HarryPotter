package com.example.harrypotter.ui.theme.screen.home

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.SearchBar
import androidx.compose.material3.SnackbarDuration
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.example.harrypotter.R
import com.example.harrypotter.data.source.local.model.CharacterEntity
import com.example.harrypotter.ui.theme.HarryPotterTheme
import com.example.harrypotter.ui.theme.component.CharacterItem
import kotlinx.collections.immutable.ImmutableList
import kotlinx.collections.immutable.persistentListOf

@Composable
fun HomeScreen(
    viewModel: HomeViewModel = hiltViewModel(),
    onNavigationRequested: (itemId: CharacterEntity) -> Unit
) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()

    HomeScreenContent(
        uiState = uiState,
        searchQuery = viewModel.searchQuery,
        onSearchQueryChange = { viewModel.onSearchQueryChanged(it) },
        onNavigationRequested = onNavigationRequested
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HomeScreenContent(
    uiState: HomeUIState,
    searchQuery: String,
    onSearchQueryChange: (String) -> Unit,
    onNavigationRequested: (itemId: CharacterEntity) -> Unit,
) {
    val snackBarHostState = remember { SnackbarHostState() }
    val keyboardController = LocalSoftwareKeyboardController.current

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background)
    ) {
        Column(
            modifier = Modifier.fillMaxSize()
        ) {
            // App Title
            Text(
                text = stringResource(R.string.app_name),
                style = MaterialTheme.typography.headlineMedium,
                color = MaterialTheme.colorScheme.primary,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp),
                textAlign = TextAlign.Center
            )

            // Search Bar with elevated style
            SearchBar(
                query = searchQuery,
                onQueryChange = onSearchQueryChange,
                onSearch = { keyboardController?.hide() },
                active = true,
                enabled = true,
                leadingIcon = {
                    Icon(
                        imageVector = Icons.Default.Search,
                        contentDescription = null,
                        tint = MaterialTheme.colorScheme.primary
                    )
                },
                onActiveChange = {},
                placeholder = {
                    Text(
                        text = stringResource(R.string.search_by_name_or_actor),
                        style = MaterialTheme.typography.bodyMedium
                    )
                },
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp, vertical = 8.dp)
                    .testTag("SearchBar")
            ) {
                when {
                    uiState.isLoading -> {
                        LoadingState()
                    }

                    uiState.errorMessage != null -> {
                        ErrorState(
                            errorMessage = uiState.errorMessage,
                            snackBarHostState = snackBarHostState
                        )
                    }

                    else -> {
                        CharacterList(
                            characters = uiState.characters,
                            onNavigationRequested = onNavigationRequested
                        )
                    }
                }
            }
        }

        // SnackbarHost at the bottom
        SnackbarHost(
            hostState = snackBarHostState,
            modifier = Modifier
                .align(Alignment.BottomCenter)
                .padding(16.dp)
        )
    }
}

@Composable
private fun LoadingState() {
    Box(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
    ) {
        CircularProgressIndicator(
            modifier = Modifier
                .align(Alignment.Center)
                .testTag("LoadingIndicator"),
            color = MaterialTheme.colorScheme.primary
        )
    }
}

@Composable
private fun ErrorState(
    errorMessage: String,
    snackBarHostState: SnackbarHostState
) {
    Box(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
    ) {
        LaunchedEffect(errorMessage) {
            snackBarHostState.showSnackbar(
                message = errorMessage,
                duration = SnackbarDuration.Short
            )
        }
        Text(
            text = errorMessage,
            color = MaterialTheme.colorScheme.error,
            style = MaterialTheme.typography.bodyLarge,
            modifier = Modifier.align(Alignment.Center),
            textAlign = TextAlign.Center
        )
    }
}

@Composable
fun CharacterList(
    characters: ImmutableList<CharacterEntity>,
    onNavigationRequested: (itemId: CharacterEntity) -> Unit
) {
    LazyColumn(
        modifier = Modifier.fillMaxSize(),
        contentPadding = PaddingValues(vertical = 8.dp)
    ) {
        items(characters) { character ->
            CharacterItem(
                character = character,
                onClick = { onNavigationRequested(character) }
            )
            HorizontalDivider(
                modifier = Modifier.padding(horizontal = 16.dp),
                color = MaterialTheme.colorScheme.outlineVariant,
                thickness = 0.5.dp
            )
        }
    }
}

@Preview(showBackground = true)
@Composable
fun HomeScreenPreview() {
    val mockCharacters = persistentListOf(
        CharacterEntity(
            name = "Harry Potter",
            actor = "Daniel Radcliffe",
            species = "Human",
            house = "Gryffindor",
            houseColor = 0xFF740001.toInt(),
            dateOfBirth = "31 July 1980",
            imageUrl = "https://hp-api.herokuapp.com/images/harry.jpg",
            status = "Alive"
        ),
        CharacterEntity(
            name = "Hermione Granger",
            actor = "Emma Watson",
            species = "Human",
            house = "Gryffindor",
            houseColor = 0xFF740001.toInt(),
            dateOfBirth = "19 September 1979",
            imageUrl = "https://hp-api.herokuapp.com/images/hermione.jpg",
            status = "Alive"
        )
    )

    val mockUiState = HomeUIState(
        characters = mockCharacters,
        isLoading = false,
        errorMessage = null
    )

    HarryPotterTheme {
        HomeScreenContent(
            uiState = mockUiState,
            searchQuery = "",
            onSearchQueryChange = {},
            onNavigationRequested = {},
        )
    }
}