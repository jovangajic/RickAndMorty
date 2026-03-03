package rs.jovan.rickandmorty.data.repository

import rs.jovan.rickandmorty.data.remote.RickAndMortyApi
import rs.jovan.rickandmorty.domain.model.Character
import rs.jovan.rickandmorty.domain.repository.CharacterRepository
import javax.inject.Inject

class CharacterRepositoryImpl @Inject constructor(
    private val api: RickAndMortyApi
): CharacterRepository {
    override suspend fun fetchCharacters(
        page: Int,
        query: String?
    ): List<Character> {
        TODO("Not yet implemented")
    }

    override suspend fun fetchCharacterDetail(id: Int): Character {
        TODO("Not yet implemented")
    }
}