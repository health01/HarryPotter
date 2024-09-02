package com.example.harrypotter.ui.theme.screen.home

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.compose.runtime.snapshotFlow
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.harrypotter.data.source.local.model.CharacterEntity
import com.example.harrypotter.domain.usecase.home.FetchAndCacheCharactersUseCase
import com.example.harrypotter.domain.usecase.home.GetCachedCharactersUseCase
import com.example.harrypotter.domain.usecase.home.SearchCachedCharactersUseCase
import com.example.harrypotter.util.NetworkMonitor
import com.example.harrypotter.util.Results
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.collections.immutable.toPersistentList
import kotlinx.coroutines.FlowPreview
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.debounce
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class HomeViewModel @Inject constructor(
    private val getCachedCharactersUseCase: GetCachedCharactersUseCase,
    private val fetchAndCacheCharactersUseCase: FetchAndCacheCharactersUseCase,
    private val searchCachedCharactersUseCase: SearchCachedCharactersUseCase,
    private val networkMonitor: NetworkMonitor
) : ViewModel() {
    private val _uiState = MutableStateFlow(HomeUIState())
    val uiState = _uiState.asStateFlow()

    var searchQuery by mutableStateOf("")
        private set

    private var searchJob: Job? = null


    init {
        getCharacters()
        initializeSearch()
    }

    fun getCharacters() {
        _uiState.update { it.copy(isLoading = true) }
        viewModelScope.launch {
            val useCase = if (networkMonitor.isNetworkConnected()) {
                fetchAndCacheCharactersUseCase()
            } else {
                getCachedCharactersUseCase()
            }

            useCase.collect { result ->
                handleResult(result)
            }
        }
    }


    private fun handleResult(result: Results<List<CharacterEntity>>) {
        _uiState.update { currentState ->
            when (result) {
                is Results.Success -> currentState.copy(
                    characters = result.data.toPersistentList(),
                    isLoading = false,
                    errorMessage = null
                )

                is Results.Error -> currentState.copy(
                    isLoading = false,
                    errorMessage = result.message
                )
            }
        }
    }

    fun onSearchQueryChanged(newQuery: String) {
        searchQuery = newQuery
    }

    @OptIn(FlowPreview::class)
    fun initializeSearch() {
        snapshotFlow { searchQuery }
            .debounce(350L)
            .distinctUntilChanged()
            .onEach { query ->
                searchJob?.cancel()
                if (query.isNotBlank()) {
                    _uiState.update { it.copy(isLoading = true) }
                    searchJob = viewModelScope.launch {
                        searchCachedCharactersUseCase(query)
                            .onEach { result ->
                                handleResult(result)
                            }
                            .launchIn(this)
                    }
                } else {
                    // If query is empty, fetch all characters again
                    getCharacters()
                }
            }
            .launchIn(viewModelScope)
    }
}