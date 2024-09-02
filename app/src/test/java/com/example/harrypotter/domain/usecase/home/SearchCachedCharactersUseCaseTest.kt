package com.example.harrypotter.domain.usecase.home

import com.example.harrypotter.BasicTesting
import com.example.harrypotter.data.repository.CharacterRepository
import com.example.harrypotter.data.source.local.model.CharacterEntity
import com.example.harrypotter.util.Results
import io.mockk.coEvery
import io.mockk.mockk
import io.mockk.unmockkAll
import junit.framework.TestCase.assertEquals
import junit.framework.TestCase.assertTrue
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.flow.toList
import kotlinx.coroutines.test.runTest
import org.junit.After
import org.junit.Before
import org.junit.Test

class SearchCachedCharactersUseCaseTest : BasicTesting() {

    private lateinit var characterRepository: CharacterRepository
    private lateinit var searchCachedCharactersUseCase: SearchCachedCharactersUseCase

    @Before
    fun setup() {
        characterRepository = mockk()
        searchCachedCharactersUseCase = SearchCachedCharactersUseCase(characterRepository)
    }

    @After
    fun tearDown() {
        unmockkAll()
    }

    @Test
    fun `invoke should return cached characters when query is not blank and search is successful`() =
        runTest {
            // Given
            val query = "Harry"
            coEvery { characterRepository.searchCachedCharacterData(query) } returns flowOf(
                Results.Success(
                    mockEntities
                )
            )

            // When
            val results = searchCachedCharactersUseCase(query).toList()

            // Then
            assertEquals(1, results.size)
            assertTrue(results.first() is Results.Success)
            assertEquals(mockEntities, (results.first() as Results.Success).data)
        }

    @Test
    fun `invoke should return error when query is not blank and search fails`() = runTest {
        // Given
        val query = "Harry"
        val errorMessage = "Unable to fetch cached data"
        coEvery { characterRepository.searchCachedCharacterData(query) } returns flowOf(
            Results.Error(
                errorMessage
            )
        )

        // When
        val results = searchCachedCharactersUseCase(query).toList()

        // Then
        assertEquals(1, results.size)
        assertTrue(results.first() is Results.Error)
        assertEquals(errorMessage, (results.first() as Results.Error).message)
    }

    @Test
    fun `invoke should return empty list when query is blank`() = runTest {
        // Given
        val query = ""

        // When
        val results = searchCachedCharactersUseCase(query).toList()

        // Then
        assertEquals(1, results.size)
        assertTrue(results.first() is Results.Success)
        assertEquals(emptyList<CharacterEntity>(), (results.first() as Results.Success).data)
    }
}