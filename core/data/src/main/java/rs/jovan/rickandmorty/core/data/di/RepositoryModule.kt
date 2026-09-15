package rs.jovan.rickandmorty.core.data.di

import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import rs.jovan.rickandmorty.core.data.repository.CharacterRepositoryImpl
import rs.jovan.rickandmorty.core.domain.repository.CharacterRepository
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
abstract class RepositoryModule {

    @Binds
    @Singleton
    abstract fun bindRepository(repository: CharacterRepositoryImpl): CharacterRepository
}
