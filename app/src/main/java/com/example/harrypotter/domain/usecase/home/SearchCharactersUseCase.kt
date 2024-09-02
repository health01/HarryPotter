package com.example.harrypotter.domain.usecase.home

import com.example.harrypotter.data.source.local.model.CharacterEntity
import com.example.harrypotter.util.Results
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import javax.inject.Inject

class SearchCharactersUseCase @Inject constructor() {
    suspend operator fun invoke(
        query: String,
        originalCharacters: List<CharacterEntity>
    ): Flow<Results<List<CharacterEntity>>> = flow {
        val filteredCharacters = originalCharacters.filter {
            it.name.contains(query, ignoreCase = true) ||
                    it.actor.contains(query, ignoreCase = true)
        }
        if (filteredCharacters.isEmpty()) {
            emit(Results.Error("No characters found for the query: $query"))
        } else {
            emit(Results.Success(filteredCharacters))
        }
    }
}