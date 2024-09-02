package com.example.harrypotter.data.repository

import com.example.harrypotter.data.source.local.model.CharacterEntity
import com.example.harrypotter.util.Results
import kotlinx.coroutines.flow.Flow

interface CharacterRepository {
    fun getCharacter(): Flow<Results<List<CharacterEntity>>>

    fun getCachedCharacterData(): Flow<Results<List<CharacterEntity>>>
    fun searchCachedCharacterData(query: String): Flow<Results<List<CharacterEntity>>>
    suspend fun updateCachedCharacter(characterEntityList: List<CharacterEntity>)
}