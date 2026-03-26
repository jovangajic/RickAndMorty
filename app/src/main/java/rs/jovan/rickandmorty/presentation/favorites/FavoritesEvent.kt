package rs.jovan.rickandmorty.presentation.favorites

sealed interface FavoritesEvent {
    data class NavigateToDetails(val id: Int) : FavoritesEvent
    data object NavigateBack : FavoritesEvent
}
