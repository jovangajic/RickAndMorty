package rs.jovan.rickandmorty.data.local.dao

import androidx.paging.PagingSource
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import kotlinx.coroutines.flow.Flow
import rs.jovan.rickandmorty.data.local.entity.CharacterEntity

@Dao
interface CharacterDao {

    @Query(
        """
        SELECT * FROM characters
        WHERE (:query IS NULL OR name LIKE '%' || :query || '%')
        ORDER BY id ASC
    """
    )
    fun pagingSource(query: String?): PagingSource<Int, CharacterEntity>

    @Query("SELECT * FROM characters WHERE id = :id")
    fun getCharacter(id: Int): Flow<CharacterEntity?>

    @Query("SELECT * FROM characters WHERE id = :id")
    suspend fun getCharacterOnce(id: Int): CharacterEntity?

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAll(characters: List<CharacterEntity>)

    @Update
    suspend fun update(character: CharacterEntity)

    @Query("DELETE FROM characters")
    suspend fun clearAll()

    @Query("UPDATE characters SET isFavorite = NOT isFavorite WHERE id = :id")
    suspend fun toggleFavorite(id: Int)

}