package com.example.harrypotter.ui.theme.screen.details

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import com.example.harrypotter.data.source.local.model.CharacterEntity
import com.example.harrypotter.navigation.CharDetail
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.update
import javax.inject.Inject

// No use case for this class, just reserve for further if we need to fetch data from internet
@HiltViewModel
class CharacterDetailViewModel @Inject constructor(savedStateHandle: SavedStateHandle) :
    ViewModel() {

    private val _uiState = MutableStateFlow(CharacterDetailState())
    val uiState: StateFlow<CharacterDetailState> = _uiState

    init {
        try {
            savedStateHandle.get<String>(CharDetail.charArg)?.let { name ->
                _uiState.update {
                    it.copy(
                        character = CharacterEntity(
                            name = name,
                            actor = "Daniel Radcliffe",
                            species = "Human",
                            house = "Gryffindor",
                            houseColor = 0xFF740001.toInt(),
                            dateOfBirth = "31 July 1980",
                            imageUrl = "https://hp-api.herokuapp.com/images/harry.jpg",
                            status = "Alive"
                        )
                    )
                }
            }

        } catch (throwable: Throwable) {
            _uiState.value = _uiState.value.copy(
                isLoading = false,
                errorMessage = ""
            )
        }
    }
}