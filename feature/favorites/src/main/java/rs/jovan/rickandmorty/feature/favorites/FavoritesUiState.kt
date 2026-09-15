package rs.jovan.rickandmorty.feature.favorites

import rs.jovan.rickandmorty.core.domain.model.Character

sealed interface FavoritesUiState {
    data object Loading : FavoritesUiState
    data class Success(val favorites: List<Character>) : FavoritesUiState
    data object Error : FavoritesUiState
}
