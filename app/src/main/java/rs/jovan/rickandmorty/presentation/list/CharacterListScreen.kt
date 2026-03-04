package rs.jovan.rickandmorty.presentation.list

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.paging.PagingData
import androidx.paging.compose.LazyPagingItems
import androidx.paging.compose.collectAsLazyPagingItems
import androidx.paging.compose.itemKey
import kotlinx.coroutines.flow.flowOf
import rs.jovan.rickandmorty.domain.model.Character
import rs.jovan.rickandmorty.presentation.theme.RickAndMortyTheme
import androidx.paging.LoadState

@Composable
fun CharacterListScreen(
    characters: LazyPagingItems<Character>,
    onCharacterClicked: (Int) -> Unit,
    onSearchQueryChanged: (String?) -> Unit,
    showError: (String) -> Unit
) {
    var query by remember { mutableStateOf("") }

    Column(modifier = Modifier.fillMaxSize()) {
        OutlinedTextField(
            value = query,
            onValueChange = {
                query = it
                onSearchQueryChanged(it.ifBlank { null })
            },
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp, vertical = 8.dp),
            placeholder = { Text("Search characters") },
            leadingIcon = { Icon(Icons.Default.Search, contentDescription = null) },
            trailingIcon = {
                if (query.isNotEmpty()) {
                    IconButton(onClick = {
                        query = ""
                        onSearchQueryChanged(null)
                    }) {
                        Icon(Icons.Default.Close, contentDescription = "Clear search")
                    }
                }
            },
            singleLine = true,
            shape = RoundedCornerShape(50)
        )

        when (characters.loadState.refresh) {

            is LoadState.Loading -> {
                Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                    CircularProgressIndicator()
                }
            }

            is LoadState.Error -> {
                val error = characters.loadState.refresh as LoadState.Error
                showError(error.error.message ?: "Unknown error")
                Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                    Text(text = error.error.message ?: "Unknown error")
                }
            }

            else -> {
                LazyColumn(modifier = Modifier.fillMaxSize()) {
                    items(
                        count = characters.itemCount,
                        key = characters.itemKey { it.id }
                    ) { index ->
                        characters[index]?.let { character ->
                            CharacterItem(
                                character = character,
                                onClick = { onCharacterClicked(character.id) }
                            )
                        }
                    }
                }
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
private fun CharacterListScreenPreview() {
    val fakeCharacters = listOf(
        Character(1, "Rick Sanchez", "Alive", "Human", "Male", "", false),
        Character(2, "Morty Smith", "Alive", "Human", "Male", "", false),
        Character(3, "Summer Smith", "Alive", "Human", "Female", "", false),
        Character(4, "Beth Smith", "Alive", "Human", "Female", "", false),
        Character(5, "Jerry Smith", "Alive", "Human", "Male", "", false),
    )
    val characters = flowOf(PagingData.from(fakeCharacters)).collectAsLazyPagingItems()

    RickAndMortyTheme {
        CharacterListScreen(
            characters = characters,
            onCharacterClicked = { },
            onSearchQueryChanged = { },
            showError = { }
        )
    }
}
