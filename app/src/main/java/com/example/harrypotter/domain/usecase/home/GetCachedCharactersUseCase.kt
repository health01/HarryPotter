package com.example.harrypotter.domain.usecase.home

import com.example.harrypotter.data.repository.CharacterRepository
import com.example.harrypotter.data.source.local.model.CharacterEntity
import com.example.harrypotter.util.Results
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class GetCachedCharactersUseCase @Inject constructor(
    private val characterRepository: CharacterRepository
) {
    operator fun invoke(): Flow<Results<List<CharacterEntity>>> {
        return characterRepository.getCachedCharacterData()
    }
}