package rs.jovan.rickandmorty.di

import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import rs.jovan.rickandmorty.data.repository.CharacterRepositoryImpl
import rs.jovan.rickandmorty.domain.repository.CharacterRepository
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
abstract class RepositoryModule {

    @Binds
    @Singleton
    abstract fun bindRepository(repository: CharacterRepositoryImpl): CharacterRepository
}