package rs.jovan.rickandmorty.data.local

import androidx.room.Database
import androidx.room.RoomDatabase
import rs.jovan.rickandmorty.data.local.dao.CharacterDao
import rs.jovan.rickandmorty.data.local.dao.RemoteKeysDao
import rs.jovan.rickandmorty.data.local.entity.CharacterEntity
import rs.jovan.rickandmorty.data.local.entity.RemoteKeysEntity

@Database(
    entities = [
        CharacterEntity::class,
        RemoteKeysEntity::class
    ],
    version = 1,
    exportSchema = false
)
abstract class AppDatabase: RoomDatabase() {
    abstract fun characterDao(): CharacterDao
    abstract fun remoteKeyDao(): RemoteKeysDao
}