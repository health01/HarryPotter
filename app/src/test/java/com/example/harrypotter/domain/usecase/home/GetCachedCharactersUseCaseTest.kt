package com.example.harrypotter.domain.usecase.home

import com.example.harrypotter.BasicTesting
import com.example.harrypotter.data.repository.CharacterRepository
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

class GetCachedCharactersUseCaseTest : BasicTesting() {

    private lateinit var characterRepository: CharacterRepository
    private lateinit var getCachedCharactersUseCase: GetCachedCharactersUseCase

    @Before
    fun setup() {
        characterRepository = mockk()
        getCachedCharactersUseCase = GetCachedCharactersUseCase(characterRepository)
    }

    @After
    fun tearDown() {
        unmockkAll()
    }

    @Test
    fun `invoke should return cached characters when repository returns success`() = runTest {
        // Given

        coEvery { characterRepository.getCachedCharacterData() } returns flowOf(
            Results.Success(
                mockEntities
            )
        )

        // When
        val results = getCachedCharactersUseCase().toList()

        // Then
        assertEquals(1, results.size)
        assertTrue(results.first() is Results.Success)
        assertEquals(mockEntities, (results.first() as Results.Success).data)
    }

    @Test
    fun `invoke should return error when repository returns error`() = runTest {
        // Given
        val errorMessage = "Unable to fetch cached data"
        coEvery { characterRepository.getCachedCharacterData() } returns flowOf(
            Results.Error(
                errorMessage
            )
        )

        // When
        val results = getCachedCharactersUseCase().toList()

        // Then
        assertEquals(1, results.size)
        assertTrue(results.first() is Results.Error)
        assertEquals(errorMessage, (results.first() as Results.Error).message)
    }
}
