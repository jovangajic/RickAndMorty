package rs.jovan.rickandmorty.feature.characterlist

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.compose.runtime.snapshotFlow
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.cachedIn
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.FlowPreview
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.debounce
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.launch
import rs.jovan.rickandmorty.core.domain.repository.CharacterRepository
import javax.inject.Inject

@HiltViewModel
class CharacterListViewModel @Inject constructor(
    private val repository: CharacterRepository
): ViewModel() {

    // Single source of truth for the search field, so the text survives navigating
    // to details and back and always matches the filtered list. Kept as synchronous
    // Compose state (not a StateFlow) so the TextField never renders a stale value
    // and drops keystrokes or IME composition.
    var searchQuery by mutableStateOf("")
        private set

    @OptIn(ExperimentalCoroutinesApi::class, FlowPreview::class)
    val characters = snapshotFlow { searchQuery }
        .map { it.ifBlank { null } }
        .debounce { if (it.isNullOrBlank()) 0L else 500L }
        .distinctUntilChanged()
        .flatMapLatest { repository.getCharacters(it) }
        .cachedIn(viewModelScope)

    private val _events = MutableSharedFlow<CharacterListEvent>(
        replay = 0,
        extraBufferCapacity = 1
    )
    val events = _events.asSharedFlow()

    fun onSearchQueryChanged(query: String) {
        searchQuery = query
    }

    fun onCharacterClicked(id: Int) {
        viewModelScope.launch {
            _events.emit(CharacterListEvent.NavigateToDetails(id))
        }
    }

    fun onError(msg: String) {
        viewModelScope.launch {
            _events.emit(CharacterListEvent.ShowError(msg))
        }
    }
}
