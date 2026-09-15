package rs.jovan.rickandmorty.core.data.local.entity

import androidx.room.Entity

@Entity(
    tableName = "remote_keys",
    primaryKeys = ["characterId", "query"]
)
data class RemoteKeysEntity(
    val characterId: Int,
    val query: String,
    val prevKey: Int?,
    val nextKey: Int?
)
