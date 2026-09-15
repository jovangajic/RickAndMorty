package rs.jovan.rickandmorty.feature.characterlist

sealed interface CharacterListEvent {
    data class NavigateToDetails(val id: Int): CharacterListEvent
    data class ShowError(val msg: String): CharacterListEvent
}
