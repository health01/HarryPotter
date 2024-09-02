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
    val characterMapper: CharacterMapper,
    private val ioDispatcher: CoroutineDispatcher,
    private val mainDispatcher: CoroutineDispatcher
) : CharacterRepository {
    override fun getCharacter(): Flow<Results<List<CharacterEntity>>> = flow {
        val response = harryPotterApiService.getCharacterList()
        val body = response.body()
        if (response.isSuccessful && body != null) {
            val detail = characterMapper.mapToUiModel(body)
            emit(Results.Success(detail))
        } else {
            emit(Results.Error("Unable to fetch character details"))
        }
    }.catch {
        emit(Results.Error("Unable to fetch character details"))
    }.flowOn(mainDispatcher)


    override fun getCachedCharacterData(): Flow<Results<List<CharacterEntity>>> {

        return characterDao.getAllCharacters().map { result ->
            if (result.isNotEmpty()) {
                Results.Success(result)
            } else {
                Results.Error("No characters found")
            }
        }
            .catch { e ->
                emit(Results.Error<List<CharacterEntity>>("Unable to fetch cached data: ${e.message}"))
            }
            .flowOn(ioDispatcher)
    }

    override fun searchCachedCharacterData(query: String): Flow<Results<List<CharacterEntity>>> {
        return characterDao.searchCharacters(query)
            .map { result ->
                if (result.isNotEmpty()) {
                    Results.Success(result)
                } else {
                    Results.Error("No characters found for the query: $query")
                }
            }
            .catch { e ->
                emit(Results.Error<List<CharacterEntity>>("Unable to fetch cached data: ${e.message}"))
            }
            .flowOn(ioDispatcher)
    }

    override suspend fun updateCachedCharacter(characterEntityList: List<CharacterEntity>) {
        withContext(ioDispatcher) {
            characterDao.updateCharacters(characterEntityList)
        }
    }
}