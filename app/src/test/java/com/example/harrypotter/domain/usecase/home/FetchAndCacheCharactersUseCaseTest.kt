package com.example.harrypotter.domain.usecase.home

import com.example.harrypotter.BasicTesting
import com.example.harrypotter.data.repository.CharacterRepository
import com.example.harrypotter.util.Results
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.just
import io.mockk.mockk
import io.mockk.runs
import io.mockk.unmockkAll
import junit.framework.TestCase.assertEquals
import junit.framework.TestCase.assertTrue
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.flow.toList
import kotlinx.coroutines.test.runTest
import org.junit.After
import org.junit.Before
import org.junit.Test

class FetchAndCacheCharactersUseCaseTest : BasicTesting() {

    private lateinit var characterRepository: CharacterRepository
    private lateinit var fetchAndCacheCharactersUseCase: FetchAndCacheCharactersUseCase

    @Before
    fun setup() {
        characterRepository = mockk()
        fetchAndCacheCharactersUseCase = FetchAndCacheCharactersUseCase(characterRepository)
    }

    @After
    fun tearDown() {
        unmockkAll()
    }

    @Test
    fun `invoke should cache characters when getCharacter returns success`() = runTest {
        // Given
        coEvery { characterRepository.getCharacter() } returns flowOf(Results.Success(mockEntities))
        coEvery { characterRepository.updateCachedCharacter(any()) } just runs

        // When
        val results = fetchAndCacheCharactersUseCase().toList()

        // Then
        assertEquals(1, results.size)
        assertTrue(results.first() is Results.Success)
        assertEquals(mockEntities, (results.first() as Results.Success).data)

        coVerify { characterRepository.updateCachedCharacter(mockEntities) }
    }

    @Test
    fun `invoke should not cache characters when getCharacter returns error`() = runTest {
        // Given
        val errorMessage = "Unable to fetch character details"
        coEvery { characterRepository.getCharacter() } returns flowOf(Results.Error(errorMessage))
        coEvery { characterRepository.updateCachedCharacter(any()) } just runs

        // When
        val results = fetchAndCacheCharactersUseCase().toList()

        // Then
        assertEquals(1, results.size)
        assertTrue(results.first() is Results.Error)
        assertEquals(errorMessage, (results.first() as Results.Error).message)

        coVerify(exactly = 0) { characterRepository.updateCachedCharacter(any()) }
    }
}