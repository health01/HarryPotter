package com.example.harrypotter.ui.screen.home

import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.onNodeWithTag
import androidx.compose.ui.test.onNodeWithText
import com.example.harrypotter.data.source.local.model.CharacterEntity
import com.example.harrypotter.ui.base.BaseComposeTest
import com.example.harrypotter.ui.theme.screen.home.HomeScreenContent
import com.example.harrypotter.ui.theme.screen.home.HomeUIState
import kotlinx.collections.immutable.persistentListOf
import org.junit.Test

class HomeScreenTest : BaseComposeTest() {
    @Test
    fun homeScreen_whenLoading_showsLoadingIndicator() {
        launchComposable {
            HomeScreenContent(
                uiState = HomeUIState(isLoading = true),
                searchQuery = "",
                onSearchQueryChange = {},
                onNavigationRequested = {}
            )
        }

        // First verify SearchBar is displayed since loading indicator is inside it
        composeRule.onNodeWithTag("SearchBar").assertIsDisplayed()
        composeRule.onNodeWithTag("LoadingIndicator").assertIsDisplayed()
    }

    @Test
    fun homeScreen_showsCharacterList() {
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
            )
        )

        launchComposable {
            HomeScreenContent(
                uiState = HomeUIState(isLoading = false, characters = mockCharacters),
                searchQuery = "",
                onSearchQueryChange = {},
                onNavigationRequested = {}
            )
        }

        composeRule.onNodeWithText("Harry Potter").assertIsDisplayed()
    }
} 