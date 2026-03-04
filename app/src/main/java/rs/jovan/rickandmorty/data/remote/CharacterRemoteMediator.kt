package rs.jovan.rickandmorty.data.remote

import androidx.paging.ExperimentalPagingApi
import androidx.paging.LoadType
import androidx.paging.PagingState
import androidx.paging.RemoteMediator
import androidx.room.withTransaction
import kotlinx.coroutines.CancellationException
import retrofit2.HttpException
import rs.jovan.rickandmorty.data.local.AppDatabase
import rs.jovan.rickandmorty.data.local.entity.CharacterEntity
import rs.jovan.rickandmorty.data.local.entity.RemoteKeysEntity
import rs.jovan.rickandmorty.data.mapper.toEntity
import java.io.IOException

@OptIn(ExperimentalPagingApi::class)
class CharacterRemoteMediator(
    private val api: RickAndMortyApi,
    private val database: AppDatabase,
    private val query: String?
) : RemoteMediator<Int, CharacterEntity>() {

    private val dao = database.characterDao()
    private val remoteKeysDao = database.remoteKeyDao()
    private val queryTag = query ?: ""

    override suspend fun initialize(): InitializeAction {
        val hasData = remoteKeysDao.hasKeysForQuery(queryTag)
        return if (hasData) InitializeAction.SKIP_INITIAL_REFRESH
        else InitializeAction.LAUNCH_INITIAL_REFRESH
    }

    override suspend fun load(
        loadType: LoadType,
        state: PagingState<Int, CharacterEntity>
    ): MediatorResult {

        try {
            val page = when (loadType) {

                LoadType.REFRESH -> 1

                LoadType.PREPEND -> {
                    return MediatorResult.Success(endOfPaginationReached = true)
                }

                LoadType.APPEND -> {
                    val lastItem = state.lastItemOrNull()
                        ?: return MediatorResult.Success(endOfPaginationReached = true)

                    val remoteKeys = remoteKeysDao.getRemoteKeys(lastItem.id, queryTag)
                        ?: return MediatorResult.Success(endOfPaginationReached = false)

                    remoteKeys.nextKey
                        ?: return MediatorResult.Success(endOfPaginationReached = true)
                }
            }

            val response = api.getCharacters(page = page, name = query)
            val endOfPaginationReached = response.info.next == null

            database.withTransaction {
                if (loadType == LoadType.REFRESH) {
                    remoteKeysDao.clearRemoteKeysForQuery(queryTag)
                }

                val entities = response.results.map { it.toEntity() }
                dao.insertAll(entities)

                val keys = entities.map {
                    RemoteKeysEntity(
                        characterId = it.id,
                        query = queryTag,
                        prevKey = if (page == 1) null else page - 1,
                        nextKey = if (endOfPaginationReached) null else page + 1
                    )
                }
                remoteKeysDao.insertAll(keys)
            }

            return MediatorResult.Success(endOfPaginationReached = endOfPaginationReached)

        } catch (e: CancellationException) {
            throw e
        } catch (e: HttpException) {
            if (e.code() == 404) {
                return MediatorResult.Success(endOfPaginationReached = true)
            }
            return MediatorResult.Error(e)
        } catch (e: IOException) {
            return MediatorResult.Error(e)
        }
    }
}
