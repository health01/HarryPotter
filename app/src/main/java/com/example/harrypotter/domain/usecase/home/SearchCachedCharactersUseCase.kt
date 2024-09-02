package com.example.harrypotter.domain.usecase.home

import com.example.harrypotter.data.repository.CharacterRepository
import com.example.harrypotter.data.source.local.model.CharacterEntity
import com.example.harrypotter.util.Results
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flowOf
import javax.inject.Inject

class SearchCachedCharactersUseCase @Inject constructor(
    private val characterRepository: CharacterRepository
) {
    operator fun invoke(query: String): Flow<Results<List<CharacterEntity>>> {
        return if (query.isNotBlank()) {
            characterRepository.searchCachedCharacterData(query)
        } else {
            flowOf(Results.Success(emptyList()))
        }
    }
}