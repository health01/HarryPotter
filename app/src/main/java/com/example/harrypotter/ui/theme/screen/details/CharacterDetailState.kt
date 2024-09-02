package com.example.harrypotter.ui.theme.screen.details

import com.example.harrypotter.data.source.local.model.CharacterEntity

data class CharacterDetailState(
    val isLoading: Boolean = false,
    val character: CharacterEntity? = null,
    val errorMessage: String? = null
)