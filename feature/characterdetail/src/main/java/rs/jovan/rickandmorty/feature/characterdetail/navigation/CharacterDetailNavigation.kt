package rs.jovan.rickandmorty.feature.characterdetail.navigation

import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Snackbar
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import kotlinx.serialization.Serializable
import rs.jovan.rickandmorty.feature.characterdetail.CharacterDetailEvent
import rs.jovan.rickandmorty.feature.characterdetail.CharacterDetailScreen
import rs.jovan.rickandmorty.feature.characterdetail.CharacterDetailViewModel

@Serializable
data class CharacterDetailRoute(val id: Int)

fun NavController.navigateToCharacterDetail(id: Int) {
    navigate(CharacterDetailRoute(id = id))
}

fun NavGraphBuilder.characterDetailScreen(
    onBack: () -> Unit
) {
    composable<CharacterDetailRoute> {
        val vm: CharacterDetailViewModel = hiltViewModel()
        val uiState by vm.uiState.collectAsStateWithLifecycle()
        val snackbarHostState = remember { SnackbarHostState() }

        LaunchedEffect(Unit) {
            vm.events.collect { event ->
                when (event) {
                    is CharacterDetailEvent.NavigateBack -> onBack()
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
