package com.example.harrypotter.data.repository

import com.example.harrypotter.BasicTesting
import com.example.harrypotter.data.mapper.CharacterMapper
import com.example.harrypotter.data.repository.util.MainDispatcherRule
import com.example.harrypotter.data.source.local.dao.CharacterDao
import com.example.harrypotter.data.source.remote.HarryPotterApiService
import com.example.harrypotter.data.source.remote.model.CharacterApiModel
import com.example.harrypotter.util.Results
import io.mockk.MockKAnnotations
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.every
import io.mockk.impl.annotations.MockK
import io.mockk.just
import io.mockk.runs
import io.mockk.unmockkAll
import junit.framework.TestCase.assertEquals
import junit.framework.TestCase.assertTrue
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.test.runTest
import okhttp3.ResponseBody.Companion.toResponseBody
import org.junit.After
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import retrofit2.Response

class CharacterRepositoryTest : BasicTesting() {

    @get:Rule
    val mainDispatcherRule = MainDispatcherRule()

    @MockK
    private lateinit var harryPotterApiService: HarryPotterApiService

    @MockK
    private lateinit var characterDao: CharacterDao

    private lateinit var characterRepository: CharacterRepositoryImpl


    @Before
    fun setup() {
        MockKAnnotations.init(this)
        characterRepository = CharacterRepositoryImpl(
            harryPotterApiService,
            characterDao,
            CharacterMapper(),
            mainDispatcherRule.testDispatcher,
            mainDispatcherRule.testDispatcher
        )
    }

    @After
    fun tearDown() {
        unmockkAll()
    }

    @Test
    fun `getCharacter should return success when API call is successful`() = runTest {
        // Given
        val mockResponse = Response.success(mockApiModels)
        coEvery { harryPotterApiService.getCharacterList() } returns mockResponse

        // When
        val result = characterRepository.getCharacter().first()
        val expectedResult = Results.Success(
            mockEntities
        )
        assertTrue(result is Results.Success)
        assertEquals((result as Results.Success).data, expectedResult.data)
    }

    @Test
    fun `getCharacter should return error when API call fails`() = runTest {
        // Given
        coEvery { harryPotterApiService.getCharacterList() } returns Response.error(
            404,
            "".toResponseBody()
        )

        // When
        val result = characterRepository.getCharacter().first()

        // Then
        assertTrue(result is Results.Error)
        assertEquals("Unable to fetch character details", (result as Results.Error).message)
    }

    @Test
    fun `getCharacter should return error when response body is null`() = runTest {
        // Given
        val mockResponse: Response<CharacterApiModel> = Response.success(null)
        coEvery { harryPotterApiService.getCharacterList() } returns mockResponse

        // When
        val result = characterRepository.getCharacter().first()

        // Then
        assertTrue(result is Results.Error)
        assertEquals("Unable to fetch character details", (result as Results.Error).message)
    }

    @Test
    fun `getCachedCharacterData should return success when data is available`() = runTest {
        // Given
        every { characterDao.getAllCharacters() } returns flowOf(mockEntities)

        // When
        val result = characterRepository.getCachedCharacterData().first()

        // Then
        assertTrue(result is Results.Success)
        assertEquals(mockEntities, (result as Results.Success).data)
    }

    @Test
    fun `getCachedCharacterData should return error when exception is thrown`() = runTest {
        // Given
        val errorTitle = "Unable to fetch cached data:"
        val errorMessage = "Exception"
        every { characterDao.getAllCharacters() } returns flow { throw Exception(errorMessage) }

        // When
        val result = characterRepository.getCachedCharacterData().first()

        // Then
        assertTrue(result is Results.Error)
        assertEquals("$errorTitle $errorMessage", (result as Results.Error).message)
    }

    @Test
    fun `searchCachedCharacterData should return success when search results are available`() =
        runTest {
            // Given
            val query = "Harry"
            every { characterDao.searchCharacters(query) } returns flowOf(mockEntities)

            // When
            val result = characterRepository.searchCachedCharacterData(query).first()

            // Then
            assertTrue(result is Results.Success)
            assertEquals(mockEntities, (result as Results.Success).data)
        }

    @Test
    fun `searchCachedCharacterData should return error when exception is thrown`() = runTest {
        // Given
        val query = "Harry"
        val errorTitle = "Unable to fetch cached data:"
        val errorMessage = "Exception"
        every { characterDao.searchCharacters(query) } returns flow { throw Exception(errorMessage) }

        // When
        val result = characterRepository.searchCachedCharacterData(query).first()

        assertTrue(result is Results.Error)
        assertEquals("$errorTitle $errorMessage", (result as Results.Error).message)
    }

    @Test
    fun `updateCachedCharacter should update cache with given characters`() = runTest {
        // Given
        coEvery { characterDao.updateCharacters(mockEntities) } just runs

        // When
        characterRepository.updateCachedCharacter(mockEntities)

        // Then
        coVerify { characterDao.updateCharacters(mockEntities) }
    }
}