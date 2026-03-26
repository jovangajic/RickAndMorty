package rs.jovan.rickandmorty.presentation.navigation

import kotlinx.serialization.Serializable

@Serializable
sealed interface Routes {

    @Serializable
    object CharacterList : Routes

    @Serializable
    data class CharacterDetails(val id: Int): Routes

    @Serializable
    object Favorites : Routes
}