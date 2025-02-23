package com.example.harrypotter.data.repository

import com.example.harrypotter.data.mapper.CharacterMapper
import com.example.harrypotter.data.source.local.dao.CharacterDao
import com.example.harrypotter.data.source.local.model.CharacterEntity
import com.example.harrypotter.data.source.remote.HarryPotterApiService
import com.example.harrypotter.util.Results
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.withContext
import javax.inject.Inject
class CharacterRepositoryImpl @Inject constructor(
    private val harryPotterApiService: HarryPotterApiService,
    private val characterDao: CharacterDao,
    private val characterMapper: CharacterMapper,
    private val backgroundDispatcher: CoroutineDispatcher, // 更名為 backgroundDispatcher
    private val uiDispatcher: CoroutineDispatcher // 更名為 uiDispatcher
) : CharacterRepository {

    override fun getCharacter(): Flow<Results<List<CharacterEntity>>> = flow {
        val response = harryPotterApiService.getCharacterList()
        if (response.isSuccessful && response.body() != null) {
            val detail = characterMapper.mapToUiModel(response.body()!!)
            emit(Results.Success(detail))
        } else {
            val errorMessage = response.errorBody()?.string() ?: "Unable to fetch character details"
            emit(Results.Error(errorMessage))
        }
    }.catch {
        emit(Results.Error("Unable to fetch character details: ${it.message}"))
    }.flowOn(uiDispatcher)

    override fun getCachedCharacterData(): Flow<Results<List<CharacterEntity>>> {
        return characterDao.getAllCharacters()
            .map { result ->
                if (result.isNotEmpty()) Results.Success(result)
                else Results.Error("No characters found")
            }
            .catch { e ->
                emit(Results.Error("Unable to fetch cached data: ${e.message}"))
            }
            .flowOn(backgroundDispatcher)
    }

    override fun searchCachedCharacterData(query: String): Flow<Results<List<CharacterEntity>>> {
        return characterDao.searchCharacters(query)
            .map { result ->
                if (result.isNotEmpty()) Results.Success(result)
                else Results.Error("No characters found for the query: $query")
            }
            .catch { e ->
                emit(Results.Error("Unable to fetch cached data: ${e.message}"))
            }
            .flowOn(backgroundDispatcher)
    }

    override suspend fun updateCachedCharacter(characterEntityList: List<CharacterEntity>) {
        withContext(backgroundDispatcher) {
            characterDao.updateCharacters(characterEntityList)
        }
    }
}