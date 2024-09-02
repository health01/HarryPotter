package com.example.harrypotter.ui.theme.screen.home

import com.example.harrypotter.BasicTesting
import com.example.harrypotter.data.repository.util.MainDispatcherRule
import com.example.harrypotter.domain.usecase.home.FetchAndCacheCharactersUseCase
import com.example.harrypotter.domain.usecase.home.GetCachedCharactersUseCase
import com.example.harrypotter.domain.usecase.home.SearchCachedCharactersUseCase
import com.example.harrypotter.util.NetworkMonitor
import com.example.harrypotter.util.Results
import io.mockk.MockKAnnotations
import io.mockk.coEvery
import io.mockk.impl.annotations.RelaxedMockK
import io.mockk.unmockkAll
import junit.framework.TestCase.assertEquals
import junit.framework.TestCase.assertFalse
import junit.framework.TestCase.assertNull
import junit.framework.TestCase.assertTrue
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.test.runTest
import org.junit.After
import org.junit.Before
import org.junit.Rule
import org.junit.Test


class HomeViewModelTest : BasicTesting() {
    @get:Rule
    val mainDispatcherRule = MainDispatcherRule()

    private lateinit var viewModel: HomeViewModel

    @RelaxedMockK
    private lateinit var getCachedCharactersUseCase: GetCachedCharactersUseCase

    @RelaxedMockK
    private lateinit var fetchAndCacheCharactersUseCase: FetchAndCacheCharactersUseCase

    @RelaxedMockK
    private lateinit var searchCachedCharactersUseCase: SearchCachedCharactersUseCase

    @RelaxedMockK
    private lateinit var networkMonitor: NetworkMonitor

    @Before
    fun setup() {
        MockKAnnotations.init(this)
        viewModel = HomeViewModel(
            getCachedCharactersUseCase = getCachedCharactersUseCase,
            fetchAndCacheCharactersUseCase = fetchAndCacheCharactersUseCase,
            searchCachedCharactersUseCase = searchCachedCharactersUseCase,
            networkMonitor = networkMonitor
        )
    }

    @After
    fun tearDown() {
        unmockkAll()
    }

    @Test
    fun `initial uiState should have empty query`() = runTest {
        // Given
        coEvery { networkMonitor.isNetworkConnected() } returns true
        coEvery { fetchAndCacheCharactersUseCase() } returns flowOf(Results.Success(emptyList()))

        // When
        val initialQuery = viewModel.searchQuery

        // Then
        assertTrue(initialQuery.isEmpty())
    }

    @Test
    fun `getCharacters should fetch from network and update originalCharacters when network is connected`() =
        runTest {
            // Given
            coEvery { networkMonitor.isNetworkConnected() } returns true
            coEvery { fetchAndCacheCharactersUseCase() } returns flowOf(Results.Success(mockEntities))

            // When
            viewModel.getCharacters()
            mainDispatcherRule.testDispatcher.scheduler.advanceUntilIdle()

            // Then
            val uiState = viewModel.uiState.value
            assertFalse(uiState.isLoading)
            assertEquals(mockEntities, uiState.characters)
            assertEquals(mockEntities, viewModel.originalCharacters)
            assertNull(uiState.errorMessage)
        }

    @Test
    fun `getCharacters should fetch from cache and update originalCharacters when network is disconnected`() =
        runTest {
            // Given
            coEvery { networkMonitor.isNetworkConnected() } returns false
            coEvery { getCachedCharactersUseCase() } returns flowOf(Results.Success(mockEntities))

            // When
            viewModel.getCharacters()
            mainDispatcherRule.testDispatcher.scheduler.advanceUntilIdle()

            // Then
            val uiState = viewModel.uiState.value
            assertFalse(uiState.isLoading)
            assertEquals(mockEntities, uiState.characters)
            assertEquals(mockEntities, viewModel.originalCharacters)
            assertNull(uiState.errorMessage)
        }

    @Test
    fun `getCharacters should handle error and not update originalCharacters`() = runTest {
        // Given
        val errorMessage = "Unable to fetch character details"
        coEvery { networkMonitor.isNetworkConnected() } returns true
        coEvery { fetchAndCacheCharactersUseCase() } returns flowOf(Results.Error(errorMessage))

        // When
        viewModel.getCharacters()
        mainDispatcherRule.testDispatcher.scheduler.advanceUntilIdle()

        // Then
        val uiState = viewModel.uiState.value
        assertFalse(uiState.isLoading)
        assertEquals(0, uiState.characters.size)
        assertEquals(0, viewModel.originalCharacters.size)
        assertEquals(errorMessage, uiState.errorMessage)
    }

    @Test
    fun `searchCharacters should filter characters when online`() = runTest {
        // Given
        val query = "Harry"
        coEvery { networkMonitor.isNetworkConnected() } returns true
        coEvery { fetchAndCacheCharactersUseCase() } returns flowOf(Results.Success(mockEntities))

        // When
        viewModel.searchCharacters(query)
        mainDispatcherRule.testDispatcher.scheduler.advanceUntilIdle()

        // Then
        val uiState = viewModel.uiState.value
        assertFalse(uiState.isLoading)
        assertEquals(1, uiState.characters.size)
        assertEquals("Harry Potter", uiState.characters.first().name)
        assertNull(uiState.errorMessage)
    }


    @Test
    fun `searchCharacters should invoke searchCachedCharactersUseCase when offline`() = runTest {
        // Given
        val query = "Harry"
        coEvery { networkMonitor.isNetworkConnected() } returns false
        coEvery { getCachedCharactersUseCase() } returns flowOf(Results.Success(mockEntities))

        // When
        viewModel.searchCharacters(query)
        mainDispatcherRule.testDispatcher.scheduler.advanceUntilIdle()

        // Then
        val uiState = viewModel.uiState.value
        assertFalse(uiState.isLoading)
        assertEquals(mockEntities, uiState.characters)
        assertNull(uiState.errorMessage)
    }

    @Test
    fun `searchCharacters should handle error when offline`() = runTest {
        // Given
        val query = "Harry"
        val errorMessage = "Error fetching cached data"
        coEvery { networkMonitor.isNetworkConnected() } returns false
        coEvery { getCachedCharactersUseCase() } returns flowOf(Results.Error(errorMessage))

        // When
        viewModel.searchCharacters(query)
        mainDispatcherRule.testDispatcher.scheduler.advanceUntilIdle()

        // Then
        val uiState = viewModel.uiState.value
        assertFalse(uiState.isLoading)
        assertTrue(uiState.characters.isEmpty())
        assertEquals(errorMessage, uiState.errorMessage)
    }

    @Test
    fun `onSearchQueryChanged should trigger search when query is not blank when network is connected`() =
        runTest {
            // Given
            val query = "Harry"
            coEvery { networkMonitor.isNetworkConnected() } returns true
            coEvery { fetchAndCacheCharactersUseCase() } returns flowOf(Results.Success(mockEntities))

            // When
            viewModel.getCharacters()
            viewModel.initializeSearch()
            viewModel.onSearchQueryChanged(query)
            mainDispatcherRule.testDispatcher.scheduler.advanceUntilIdle()

            // Then
            val uiState = viewModel.uiState.value
            assertFalse(uiState.isLoading)
            assertEquals(mockEntities, uiState.characters)
            assertNull(uiState.errorMessage)
        }

    @Test
    fun `onSearchQueryChanged should trigger search when query is not blank when network is disconnected`() =
        runTest {
            // Given
            val query = "Harry"
            coEvery { networkMonitor.isNetworkConnected() } returns false
            coEvery { searchCachedCharactersUseCase(query) } returns flowOf(
                Results.Success(
                    mockEntities
                )
            )
            coEvery { getCachedCharactersUseCase() } returns flowOf(Results.Success(mockEntities))

            // When
            viewModel.getCharacters()
            viewModel.initializeSearch()
            viewModel.onSearchQueryChanged(query)
            mainDispatcherRule.testDispatcher.scheduler.advanceUntilIdle()

            // Then
            val uiState = viewModel.uiState.value
            assertFalse(uiState.isLoading)
            assertEquals(mockEntities, uiState.characters)
            assertNull(uiState.errorMessage)
        }

    @Test
    fun `onSearchQueryChanged should reload characters when query is blank is connected`() =
        runTest {
            // Given
            coEvery { networkMonitor.isNetworkConnected() } returns true
            coEvery { fetchAndCacheCharactersUseCase() } returns flowOf(Results.Success(mockEntities))

            // When
            viewModel.onSearchQueryChanged("")
            viewModel.initializeSearch()
            mainDispatcherRule.testDispatcher.scheduler.advanceUntilIdle()

            // Then
            val uiState = viewModel.uiState.value
            assertFalse(uiState.isLoading)
            assertEquals(mockEntities, uiState.characters)
            assertNull(uiState.errorMessage)
        }

    @Test
    fun `onSearchQueryChanged should reload characters when query is blank is disconnected`() =
        runTest {
            // Given
            coEvery { networkMonitor.isNetworkConnected() } returns false
            coEvery { getCachedCharactersUseCase() } returns flowOf(Results.Success(mockEntities))

            // When
            viewModel.onSearchQueryChanged("")
            viewModel.initializeSearch()
            mainDispatcherRule.testDispatcher.scheduler.advanceUntilIdle()

            // Then
            val uiState = viewModel.uiState.value
            assertFalse(uiState.isLoading)
            assertEquals(mockEntities, uiState.characters)
            assertNull(uiState.errorMessage)
        }


}
