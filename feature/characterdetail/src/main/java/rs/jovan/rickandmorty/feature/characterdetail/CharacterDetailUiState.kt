package rs.jovan.rickandmorty.feature.characterdetail

import rs.jovan.rickandmorty.core.domain.model.Character

sealed interface CharacterDetailUiState {
    data object Loading : CharacterDetailUiState
    data class Success(val character: Character) : CharacterDetailUiState
    data object Error : CharacterDetailUiState
}
