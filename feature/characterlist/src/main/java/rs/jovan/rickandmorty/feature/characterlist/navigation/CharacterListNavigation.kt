package rs.jovan.rickandmorty.feature.characterlist.navigation

import androidx.compose.material3.SnackbarHostState
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.remember
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import androidx.paging.compose.collectAsLazyPagingItems
import kotlinx.serialization.Serializable
import rs.jovan.rickandmorty.feature.characterlist.CharacterListEvent
import rs.jovan.rickandmorty.feature.characterlist.CharacterListScreen
import rs.jovan.rickandmorty.feature.characterlist.CharacterListViewModel

@Serializable
object CharacterListRoute

fun NavGraphBuilder.characterListScreen(
    onCharacterClick: (Int) -> Unit,
    onFavoritesClick: () -> Unit
) {
    composable<CharacterListRoute> {
        val vm: CharacterListViewModel = hiltViewModel()
        val characters = vm.characters.collectAsLazyPagingItems()

        val snackbarHostState = remember { SnackbarHostState() }
        LaunchedEffect(Unit) {
            vm.events.collect { event ->
                when (event) {
                    is CharacterListEvent.NavigateToDetails -> onCharacterClick(event.id)
                    is CharacterListEvent.ShowError -> snackbarHostState.showSnackbar(event.msg)
                }
            }
        }

        CharacterListScreen(
            characters = characters,
            query = vm.searchQuery,
            snackbarHostState = snackbarHostState,
            onCharacterClicked = vm::onCharacterClicked,
            onSearchQueryChanged = vm::onSearchQueryChanged,
            showError = vm::onError,
            onFavoritesClicked = onFavoritesClick
        )
    }
}
