package rs.jovan.rickandmorty.feature.favorites

sealed interface FavoritesEvent {
    data class NavigateToDetails(val id: Int) : FavoritesEvent
    data object NavigateBack : FavoritesEvent
}
