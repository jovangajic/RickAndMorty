package rs.jovan.rickandmorty.presentation.detail

import rs.jovan.rickandmorty.domain.model.Character

sealed interface CharacterDetailUiState {
    data object Loading : CharacterDetailUiState
    data class Success(val character: Character) : CharacterDetailUiState
    data object Error : CharacterDetailUiState
}
