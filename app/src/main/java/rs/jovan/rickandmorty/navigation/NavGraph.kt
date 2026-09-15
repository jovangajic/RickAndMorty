package rs.jovan.rickandmorty.navigation

import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import rs.jovan.rickandmorty.feature.characterdetail.navigation.characterDetailScreen
import rs.jovan.rickandmorty.feature.characterdetail.navigation.navigateToCharacterDetail
import rs.jovan.rickandmorty.feature.characterlist.navigation.CharacterListRoute
import rs.jovan.rickandmorty.feature.characterlist.navigation.characterListScreen
import rs.jovan.rickandmorty.feature.favorites.navigation.favoritesScreen
import rs.jovan.rickandmorty.feature.favorites.navigation.navigateToFavorites

@Composable
fun NavGraph(navController: NavHostController) {
    NavHost(
        navController = navController,
        startDestination = CharacterListRoute
    ) {
        characterListScreen(
            onCharacterClick = { id -> navController.navigateToCharacterDetail(id) },
            onFavoritesClick = { navController.navigateToFavorites() }
        )

        favoritesScreen(
            onCharacterClick = { id -> navController.navigateToCharacterDetail(id) },
            onBack = { navController.navigateUp() }
        )

        characterDetailScreen(
            onBack = { navController.navigateUp() }
        )
    }
}
