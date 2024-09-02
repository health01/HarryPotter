package com.example.harrypotter.di


import com.example.harrypotter.data.mapper.CharacterMapper
import com.example.harrypotter.data.repository.CharacterRepository
import com.example.harrypotter.data.repository.CharacterRepositoryImpl
import com.example.harrypotter.data.source.local.dao.CharacterDao
import com.example.harrypotter.data.source.remote.HarryPotterApiService
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import kotlinx.coroutines.CoroutineDispatcher
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object RepositoryModule {
    @Provides
    @Singleton
    fun provideCharacterRepository(
        harryPotterApiService: HarryPotterApiService,
        characterDao: CharacterDao,
        characterMapper: CharacterMapper,
        @IoDispatcher ioDispatcher: CoroutineDispatcher,
        @MainDispatcher mainDispatcher: CoroutineDispatcher,
    ): CharacterRepository {
        return CharacterRepositoryImpl(
            harryPotterApiService = harryPotterApiService,
            characterDao = characterDao,
            characterMapper = characterMapper,
            ioDispatcher = ioDispatcher,
            mainDispatcher = mainDispatcher
        )
    }
}
