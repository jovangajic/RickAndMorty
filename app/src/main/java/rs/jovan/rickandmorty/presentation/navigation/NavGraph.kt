package rs.jovan.rickandmorty.presentation.navigation

import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Snackbar
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.ui.Modifier
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.paging.compose.collectAsLazyPagingItems
import rs.jovan.rickandmorty.presentation.detail.CharacterDetailEvent
import rs.jovan.rickandmorty.presentation.detail.CharacterDetailScreen
import rs.jovan.rickandmorty.presentation.detail.CharacterDetailViewModel
import rs.jovan.rickandmorty.presentation.favorites.FavoritesEvent
import rs.jovan.rickandmorty.presentation.favorites.FavoritesScreen
import rs.jovan.rickandmorty.presentation.favorites.FavoritesViewModel
import rs.jovan.rickandmorty.presentation.list.CharacterListEvent
import rs.jovan.rickandmorty.presentation.list.CharacterListScreen
import rs.jovan.rickandmorty.presentation.list.CharacterListViewModel

@Composable
fun NavGraph(navController: NavHostController) {
    NavHost(
        navController = navController,
        startDestination = Routes.CharacterList
    ) {
        composable<Routes.CharacterList> {
            val vm: CharacterListViewModel = hiltViewModel()
            val characters = vm.characters.collectAsLazyPagingItems()

            val snackbarHostState = remember { SnackbarHostState() }
            LaunchedEffect(Unit) {
                vm.events.collect { event ->
                    when (event) {
                        is CharacterListEvent.NavigateToDetails -> {
                            navController.navigate(Routes.CharacterDetails(id = event.id))
                        }
                        is CharacterListEvent.ShowError -> {
                            snackbarHostState.showSnackbar(event.msg)
                        }
                    }
                }
            }

            CharacterListScreen(
                characters = characters,
                onCharacterClicked = vm::onCharacterClicked,
                onSearchQueryChanged = vm::onSearchQueryChanged,
                showError = vm::onError,
                onFavoritesClicked = { navController.navigate(Routes.Favorites) }
            )
        }

        composable<Routes.Favorites> {
            val vm: FavoritesViewModel = hiltViewModel()
            val uiState by vm.uiState.collectAsStateWithLifecycle()

            LaunchedEffect(Unit) {
                vm.events.collect { event ->
                    when (event) {
                        is FavoritesEvent.NavigateToDetails -> navController.navigate(Routes.CharacterDetails(id = event.id))
                        is FavoritesEvent.NavigateBack -> navController.navigateUp()
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

        composable<Routes.CharacterDetails> {
            val vm: CharacterDetailViewModel = hiltViewModel()
            val uiState by vm.uiState.collectAsStateWithLifecycle()
            val snackbarHostState = remember { SnackbarHostState() }

            LaunchedEffect(Unit) {
                vm.events.collect { event ->
                    when (event) {
                        is CharacterDetailEvent.NavigateBack -> navController.navigateUp()
                        is CharacterDetailEvent.ShowError -> snackbarHostState.showSnackbar(event.message)
                    }
                }
            }

            Scaffold(
                snackbarHost = {
                    SnackbarHost(hostState = snackbarHostState) {
                        Snackbar(
                            snackbarData = it,
                            containerColor = MaterialTheme.colorScheme.errorContainer,
                            contentColor = MaterialTheme.colorScheme.onErrorContainer
                        )
                    }
                }
            ) { padding ->
                CharacterDetailScreen(
                    uiState = uiState,
                    onBack = vm::onBack,
                    onToggleFavorite = vm::onToggleFavorite,
                    modifier = Modifier.padding(padding)
                )
            }
        }
    }
}
