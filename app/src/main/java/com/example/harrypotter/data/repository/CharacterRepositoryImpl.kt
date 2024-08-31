package com.example.harrypotter.data.repository

import com.example.harrypotter.data.mapper.CharacterMapper
import com.example.harrypotter.data.source.local.dao.CharacterDao
import com.example.harrypotter.data.source.local.model.CharacterEntity
import com.example.harrypotter.data.source.remote.HarryPotterApiService
import com.example.harrypotter.data.source.remote.model.CharacterApiModel
import com.example.harrypotter.di.IoDispatcher
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
    @IoDispatcher private val ioDispatcher: CoroutineDispatcher,

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
    }.flowOn(ioDispatcher)


    override fun getCachedCharacterApiData(): Flow<Results<List<CharacterEntity>>> {

        val aa = characterDao.getAllCharacters().map {
            Results.Success(it)
        }.catch {
            Results.Error<List<CharacterEntity>>("Unable to fetch cached coins")
        }.flowOn(ioDispatcher)
        return aa
    }

    override suspend fun updateCachedCharacter(characterEntityList: List<CharacterEntity>) {
        withContext(ioDispatcher) {
            characterDao.updateCoins(characterEntityList)
        }
    }
}