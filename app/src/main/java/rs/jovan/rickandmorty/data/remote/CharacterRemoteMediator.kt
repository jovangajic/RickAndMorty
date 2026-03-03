package rs.jovan.rickandmorty.data.remote

import androidx.paging.ExperimentalPagingApi
import androidx.paging.LoadType
import androidx.paging.PagingState
import androidx.paging.RemoteMediator
import androidx.room.withTransaction
import kotlinx.coroutines.CancellationException
import rs.jovan.rickandmorty.data.local.AppDatabase
import rs.jovan.rickandmorty.data.local.entity.CharacterEntity
import rs.jovan.rickandmorty.data.local.entity.RemoteKeysEntity
import rs.jovan.rickandmorty.data.mapper.toEntity


@OptIn(ExperimentalPagingApi::class)
class CharacterRemoteMediator(
    private val api: RickAndMortyApi,
    private val database: AppDatabase,
    private val query: String?
) : RemoteMediator<Int, CharacterEntity>() {

    private val dao = database.characterDao()
    private val remoteKeysDao = database.remoteKeyDao()

    override suspend fun load(
        loadType: LoadType,
        state: PagingState<Int, CharacterEntity>
    ): MediatorResult {

        try {

            val page = when (loadType) {

                LoadType.REFRESH -> {
                    1
                }

                LoadType.PREPEND -> {
                    return MediatorResult.Success(endOfPaginationReached = true)
                }

                LoadType.APPEND -> {
                    val lastItem = state.lastItemOrNull()
                        ?: return MediatorResult.Success(endOfPaginationReached = true)

                    val remoteKeys = remoteKeysDao.getRemoteKeys(lastItem.id)
                        ?: return MediatorResult.Success(endOfPaginationReached = false)

                    remoteKeys.nextKey
                        ?: return MediatorResult.Success(endOfPaginationReached = true)
                }
            }

            val response = api.getCharacters(page = page, name = query)

            val endOfPaginationReached = response.info.next == null

            database.withTransaction {

                if (loadType == LoadType.REFRESH) {
                    dao.clearAll()
                    remoteKeysDao.clearRemoteKeys()
                }

                val entities = response.results.map { it.toEntity() }

                dao.insertAll(entities)

                val keys = entities.map {
                    RemoteKeysEntity(
                        characterId = it.id,
                        prevKey = if (page == 1) null else page - 1,
                        nextKey = if (endOfPaginationReached) null else page + 1
                    )
                }

                remoteKeysDao.insertAll(keys)
            }

            return MediatorResult.Success(
                endOfPaginationReached = endOfPaginationReached
            )

        } catch (e: CancellationException) {
            throw e
        } catch (e: Exception) {
            return MediatorResult.Error(e)
        }
    }
}