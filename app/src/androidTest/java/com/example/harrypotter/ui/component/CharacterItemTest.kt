package com.example.harrypotter.ui.component

import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.onNodeWithText
import androidx.compose.ui.test.performClick
import com.example.harrypotter.data.source.local.model.CharacterEntity
import com.example.harrypotter.ui.base.BaseComposeTest
import com.example.harrypotter.ui.theme.component.CharacterItem
import org.junit.Test

class CharacterItemTest : BaseComposeTest() {
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
    fun characterItem_showsCorrectInfo() {
        var clicked = false
        launchComposable {
            CharacterItem(
                character = mockCharacter,
                onClick = { clicked = true }
            )
        }

        composeRule.onNodeWithText(mockCharacter.name).assertIsDisplayed()
        composeRule.onNodeWithText("Actor: ${mockCharacter.actor}").assertIsDisplayed()
        composeRule.onNodeWithText("Species: ${mockCharacter.species}").assertIsDisplayed()
    }
} 