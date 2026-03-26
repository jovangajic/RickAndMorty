package rs.jovan.rickandmorty.presentation.favorites

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.launch
import rs.jovan.rickandmorty.domain.repository.CharacterRepository
import javax.inject.Inject

@HiltViewModel
class FavoritesViewModel @Inject constructor(
    private val repository: CharacterRepository
) : ViewModel() {

    private val _uiState = MutableStateFlow<FavoritesUiState>(FavoritesUiState.Loading)
    val uiState = _uiState.asStateFlow()

    private val _events = MutableSharedFlow<FavoritesEvent>(extraBufferCapacity = 1)
    val events = _events.asSharedFlow()

    private val _query = MutableStateFlow<String?>(null)

    init {
        viewModelScope.launch {
            @OptIn(ExperimentalCoroutinesApi::class)
            _query.flatMapLatest { query -> repository.getFavorites(query) }
                .catch { _uiState.value = FavoritesUiState.Error }
                .collect { favorites ->
                    _uiState.value = FavoritesUiState.Success(favorites)
                }
        }
    }

    fun onSearchQueryChanged(query: String?) {
        _query.value = query
    }

    fun onCharacterClicked(id: Int) {
        viewModelScope.launch {
            _events.emit(FavoritesEvent.NavigateToDetails(id))
        }
    }

    fun onBack() {
        viewModelScope.launch {
            _events.emit(FavoritesEvent.NavigateBack)
        }
    }
}
