package rs.jovan.rickandmorty.domain.repository

import androidx.paging.PagingData
import kotlinx.coroutines.flow.Flow
import rs.jovan.rickandmorty.domain.model.Character

interface CharacterRepository {
    suspend fun fetchCharacters(page: Int, query: String? = null): List<Character>
    suspend fun fetchCharacterDetail(id: Int): Character
}