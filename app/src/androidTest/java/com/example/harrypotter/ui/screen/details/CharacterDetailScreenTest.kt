package com.example.harrypotter.ui.screen.details

import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.onNodeWithTag
import androidx.compose.ui.test.onNodeWithText
import com.example.harrypotter.data.source.local.model.CharacterEntity
import com.example.harrypotter.ui.base.BaseComposeTest
import com.example.harrypotter.ui.theme.screen.details.CharacterDetailContent
import org.junit.Test

class CharacterDetailScreenTest : BaseComposeTest() {
    private val mockCharacter = CharacterEntity(
        name = "Harry Potter",
        actor = "Daniel Radcliffe",
        species = "Human",
        house = "Gryffindor",
        houseColor = 0xFF740001.toInt(),
        dateOfBirth = "31 July 1980",
        imageUrl = "https://hp-api.herokuapp.com/images/harry.jpg",
        status = "Alive"
    )

    @Test
    fun characterDetailContent_showsCharacterInfo() {
        launchComposable {
            CharacterDetailContent(character = mockCharacter)
        }

        composeRule.onNodeWithText(mockCharacter.name).assertIsDisplayed()
        composeRule.onNodeWithText("Actor: ${mockCharacter.actor}").assertIsDisplayed()
        composeRule.onNodeWithText("House: ${mockCharacter.house}").assertIsDisplayed()
        composeRule.onNodeWithText("Species: ${mockCharacter.species}").assertIsDisplayed()
        composeRule.onNodeWithText("Status: ${mockCharacter.status}").assertIsDisplayed()
        composeRule.onNodeWithText("Date of Birth: ${mockCharacter.dateOfBirth}")
            .assertIsDisplayed()
    }

    @Test
    fun characterDetailContent_whenNull_showsError() {
        launchComposable {
            CharacterDetailContent(character = null)
        }

        composeRule.onNodeWithText("No data available").assertIsDisplayed()
    }
} 