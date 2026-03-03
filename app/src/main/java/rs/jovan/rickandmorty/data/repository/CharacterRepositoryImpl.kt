package rs.jovan.rickandmorty.data.repository

import androidx.paging.ExperimentalPagingApi
import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import androidx.paging.map
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import rs.jovan.rickandmorty.data.local.AppDatabase
import rs.jovan.rickandmorty.data.local.dao.CharacterDao
import rs.jovan.rickandmorty.data.mapper.toDomain
import rs.jovan.rickandmorty.data.remote.CharacterRemoteMediator
import rs.jovan.rickandmorty.data.remote.RickAndMortyApi
import rs.jovan.rickandmorty.domain.model.Character
import rs.jovan.rickandmorty.domain.repository.CharacterRepository
import javax.inject.Inject

class CharacterRepositoryImpl @Inject constructor(
    private val api: RickAndMortyApi,
    private val dao: CharacterDao,
    private val database: AppDatabase
): CharacterRepository {
    @OptIn(ExperimentalPagingApi::class)
    override suspend fun getCharacters(query: String?): Flow<PagingData<Character>> {
        return Pager(
            config = PagingConfig(
                pageSize = 20,
                enablePlaceholders = false
            ),
            remoteMediator = CharacterRemoteMediator(
                api = api,
                database = database,
                query = query
            ),
            pagingSourceFactory = {
                dao.pagingSource(query)
            }
        ).flow.map { pagingData ->
            pagingData.map { entity ->
                entity.toDomain()
            }
        }
    }

    override suspend fun getCharacterDetails(id: Int): Flow<Character?> {
        return dao.getCharacter(id).map { it?.toDomain() }
    }

    override suspend fun toggleFavorite(id: Int) {
        dao.toggleFavorite(id)
    }
}