package rs.jovan.rickandmorty.presentation.navigation

import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.paging.compose.collectAsLazyPagingItems
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

           LaunchedEffect(Unit) {
               vm.events.collect { event ->
                   when(event) {
                       is CharacterListEvent.NavigateToDetails -> {
                           navController.navigate(Routes.CharacterDetails(id = event.id))
                       }
                       is CharacterListEvent.ShowError -> {
                           // error msg
                       }
                   }
               }
           }

           CharacterListScreen(
               characters = characters,
               onCharacterClicked = vm::onCharacterClicked,
               onSearchQueryChanged = vm::onSearchQueryChanged,
               showError = vm::onError
           )

       }
    }
}