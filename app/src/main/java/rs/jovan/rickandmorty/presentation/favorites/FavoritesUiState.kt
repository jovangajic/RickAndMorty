package rs.jovan.rickandmorty.presentation.favorites

import rs.jovan.rickandmorty.domain.model.Character

sealed interface FavoritesUiState {
    data object Loading : FavoritesUiState
    data class Success(val favorites: List<Character>) : FavoritesUiState
    data object Error : FavoritesUiState
}
