package rs.jovan.rickandmorty.presentation.detail

sealed interface CharacterDetailEvent {
    data object NavigateBack : CharacterDetailEvent
    data class ShowError(val message: String) : CharacterDetailEvent
}
