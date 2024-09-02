package com.example.harrypotter.domain.usecase.home

import com.example.harrypotter.BasicTesting
import com.example.harrypotter.util.Results
import junit.framework.TestCase
import junit.framework.TestCase.assertTrue
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.test.runTest
import org.junit.Before
import org.junit.Test

@ExperimentalCoroutinesApi
class SearchCharactersUseCaseTest : BasicTesting() {

    private lateinit var searchCharactersUseCase: SearchCharactersUseCase

    @Before
    fun setup() {
        searchCharactersUseCase = SearchCharactersUseCase()
    }

    @Test
    fun `invoke should return success when characters are found`() = runTest {
        // Given
        val query = "Harry"

        // When
        val result = searchCharactersUseCase(query, mockEntities).first()

        // Then
        assertTrue(result is Results.Success)
        TestCase.assertEquals(mockEntities, (result as Results.Success).data)
    }

    @Test
    fun `invoke should return error when no characters are found`() = runTest {
        // Given
        val query = "Nonexistent"
        val errorMessage = "No characters found for the query: $query"

        // When
        val result = searchCharactersUseCase(query, mockEntities).first()

        // Then
        assertTrue(result is Results.Error)
        TestCase.assertEquals(errorMessage, (result as Results.Error).message)
    }
}
