package com.example.harrypotter.data.mapper

import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.toArgb
import com.example.harrypotter.BasicTesting
import com.example.harrypotter.ui.theme.GryffindorColor
import com.example.harrypotter.ui.theme.SlytherinColor
import org.junit.Assert.assertEquals
import org.junit.Assert.assertNull
import org.junit.Test

class CharacterMapperTest : BasicTesting() {

    private val mapper = CharacterMapper()

    @Test
    fun `mapToUiModel should correctly map CharacterApiModel to CharacterEntity`() {
        // Given
        val apiModel = mockApiModels

        // When
        val entities = mapper.mapToUiModel(apiModel)

        // Then
        assertEquals(1, entities.size)
        val entity = entities[0]
        assertEquals("Harry Potter", entity.name)
        assertEquals("Daniel Radcliffe", entity.actor)
        assertEquals("Human", entity.species)
        assertEquals("Gryffindor", entity.house)
        assertEquals(GryffindorColor.toArgb(), entity.houseColor)
        assertEquals("31 Jul 1980", entity.dateOfBirth)
        assertEquals("https://hp-api.herokuapp.com/images/harry.jpg", entity.imageUrl)
        assertEquals("Alive", entity.status)
    }

    @Test
    fun `getHouseColor should return correct color for Gryffindor`() {
        // Given
        val house = "Gryffindor"

        // When
        val color = mapper.getHouseColor(house)

        // Then
        assertEquals(GryffindorColor, color)
    }

    @Test
    fun `getHouseColor should return correct color for Slytherin`() {
        // Given
        val house = "Slytherin"

        // When
        val color = mapper.getHouseColor(house)

        // Then
        assertEquals(SlytherinColor, color)
    }

    @Test
    fun `getHouseColor should return gray color for unknown house`() {
        // Given
        val house = "UnknownHouse"

        // When
        val color = mapper.getHouseColor(house)

        // Then
        assertEquals(Color.Gray, color)
    }

    @Test
    fun `formatDate should correctly format date`() {
        // Given
        val date = "31-07-1980"

        // When
        val formattedDate = mapper.formatDate(date)

        // Then
        assertEquals("31 Jul 1980", formattedDate)
    }

    @Test
    fun `formatDate should return null for invalid date`() {
        // Given
        val invalidDate = "invalid-date"

        // When
        val formattedDate = mapper.formatDate(invalidDate)

        // Then
        assertNull(formattedDate)
    }

    @Test
    fun `formatDate should return null for null input`() {
        // Given
        val nullDate: String? = null

        // When
        val formattedDate = mapper.formatDate(nullDate)

        // Then
        assertNull(formattedDate)
    }
}
