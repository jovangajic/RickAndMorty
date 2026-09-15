package rs.jovan.rickandmorty.feature.characterdetail

sealed interface CharacterDetailEvent {
    data object NavigateBack : CharacterDetailEvent
    data class ShowError(val message: String) : CharacterDetailEvent
}
