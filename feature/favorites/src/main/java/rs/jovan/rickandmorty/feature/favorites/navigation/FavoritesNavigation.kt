package rs.jovan.rickandmorty.feature.favorites.navigation

import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import kotlinx.serialization.Serializable
import rs.jovan.rickandmorty.feature.favorites.FavoritesEvent
import rs.jovan.rickandmorty.feature.favorites.FavoritesScreen
import rs.jovan.rickandmorty.feature.favorites.FavoritesViewModel

@Serializable
object FavoritesRoute

fun NavController.navigateToFavorites() {
    navigate(FavoritesRoute)
}

fun NavGraphBuilder.favoritesScreen(
    onCharacterClick: (Int) -> Unit,
    onBack: () -> Unit
) {
    composable<FavoritesRoute> {
        val vm: FavoritesViewModel = hiltViewModel()
        val uiState by vm.uiState.collectAsStateWithLifecycle()

        LaunchedEffect(Unit) {
            vm.events.collect { event ->
                when (event) {
                    is FavoritesEvent.NavigateToDetails -> onCharacterClick(event.id)
                    is FavoritesEvent.NavigateBack -> onBack()
                }
            }
        }

        FavoritesScreen(
            uiState = uiState,
            onCharacterClicked = vm::onCharacterClicked,
            onSearchQueryChanged = vm::onSearchQueryChanged,
            onBack = vm::onBack
        )
    }
}
