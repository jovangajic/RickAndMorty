package rs.jovan.rickandmorty.presentation.detail

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.launch
import rs.jovan.rickandmorty.domain.repository.CharacterRepository
import javax.inject.Inject

@HiltViewModel
class CharacterDetailViewModel @Inject constructor(
    private val repository: CharacterRepository,
    savedStateHandle: SavedStateHandle
) : ViewModel() {

    private val characterId: Int = checkNotNull(savedStateHandle["id"])

    private val _uiState = MutableStateFlow<CharacterDetailUiState>(CharacterDetailUiState.Loading)
    val uiState = _uiState.asStateFlow()

    private val _events = MutableSharedFlow<CharacterDetailEvent>(extraBufferCapacity = 1)
    val events = _events.asSharedFlow()

    init {
        viewModelScope.launch {
            try {
                repository.getCharacterDetails(characterId)
                    .catch {
                        val message = it.message ?: "Unknown error"
                        _uiState.value = CharacterDetailUiState.Error
                        _events.emit(CharacterDetailEvent.ShowError(message))
                    }
                    .collect { character ->
                        if (character != null) {
                            _uiState.value = CharacterDetailUiState.Success(character)
                            if (character.locationImageUrl == null && character.locationUrl.isNotBlank()) {
                                launch {
                                    try {
                                        repository.fetchAndCacheLocationImageUrl(characterId, character.locationUrl)
                                    } catch (e: Exception) {
                                        // location image is optional, ignore failure
                                    }
                                }
                            }
                        } else {
                            _uiState.value = CharacterDetailUiState.Error
                            _events.emit(CharacterDetailEvent.ShowError("Character not found"))
                        }
                    }
            } catch (e: Exception) {
                val message = e.message ?: "Unknown error"
                _uiState.value = CharacterDetailUiState.Error
                _events.emit(CharacterDetailEvent.ShowError(message))
            }
        }
    }

    fun onBack() {
        viewModelScope.launch {
            _events.emit(CharacterDetailEvent.NavigateBack)
        }
    }

    fun onToggleFavorite() {
        viewModelScope.launch {
            repository.toggleFavorite(characterId)
        }
    }
}
