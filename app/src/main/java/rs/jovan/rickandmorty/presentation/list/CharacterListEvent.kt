package rs.jovan.rickandmorty.presentation.list

sealed interface CharacterListEvent {
    data class NavigateToDetails(val id: Int): CharacterListEvent
    data class ShowError(val msg: String): CharacterListEvent
}