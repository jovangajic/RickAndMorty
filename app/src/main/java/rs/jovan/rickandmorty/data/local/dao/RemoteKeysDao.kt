package rs.jovan.rickandmorty.data.local.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import rs.jovan.rickandmorty.data.local.entity.RemoteKeysEntity

@Dao
interface RemoteKeysDao {

    @Query("SELECT * FROM remote_keys WHERE characterId = :id AND `query` = :query")
    suspend fun getRemoteKeys(id: Int, query: String): RemoteKeysEntity?

    @Query("SELECT COUNT(*) > 0 FROM remote_keys WHERE `query` = :query")
    suspend fun hasKeysForQuery(query: String): Boolean

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAll(remoteKeys: List<RemoteKeysEntity>)

    @Query("DELETE FROM remote_keys WHERE `query` = :query")
    suspend fun clearRemoteKeysForQuery(query: String)
}
