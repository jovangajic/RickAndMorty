package rs.jovan.rickandmorty.domain.repository

import androidx.paging.PagingData
import kotlinx.coroutines.flow.Flow
import rs.jovan.rickandmorty.domain.model.Character


interface CharacterRepository {
    suspend fun getCharacters(query: String? = null): Flow<PagingData<Character>>
    suspend fun getCharacterDetails(id: Int): Flow<Character?>
    suspend fun fetchAndCacheLocationImageUrl(characterId: Int, locationUrl: String)
    fun getFavorites(query: String? = null): Flow<List<Character>>
    suspend fun toggleFavorite(id: Int)
}