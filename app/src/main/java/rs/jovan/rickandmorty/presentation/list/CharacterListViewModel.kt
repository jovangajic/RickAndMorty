package rs.jovan.rickandmorty.presentation.list

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.cachedIn
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.debounce
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.launch
import rs.jovan.rickandmorty.domain.repository.CharacterRepository
import javax.inject.Inject

@HiltViewModel
class CharacterListViewModel @Inject constructor(
    private val repository: CharacterRepository
): ViewModel() {

    private val searchQuery = MutableStateFlow<String?>(null)

    @OptIn(ExperimentalCoroutinesApi::class)
    val characters = searchQuery
        .debounce { if (it.isNullOrBlank()) 0L else 500L }
        .flatMapLatest { repository.getCharacters(it) }
        .cachedIn(viewModelScope)

    private val _events = MutableSharedFlow<CharacterListEvent>(
        replay = 0,
        extraBufferCapacity = 1
    )
    val events = _events.asSharedFlow()

    fun onSearchQueryChanged(query: String?) {
        searchQuery.value = query
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