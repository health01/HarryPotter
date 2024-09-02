package com.example.harrypotter.ui.theme.screen.home

import com.example.harrypotter.data.source.local.model.CharacterEntity
import kotlinx.collections.immutable.ImmutableList
import kotlinx.collections.immutable.persistentListOf

data class HomeUIState(
    val characters: ImmutableList<CharacterEntity> = persistentListOf(),
    val isLoading: Boolean = false,
    val errorMessage: String? = null
)