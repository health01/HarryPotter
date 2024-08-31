package com.example.harrypotter.di

import android.content.Context
import androidx.room.Room
import com.example.harrypotter.data.source.local.CharacterDatabase
import com.example.harrypotter.data.source.local.dao.CharacterDao
import com.example.harrypotter.util.Constants
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object RoomModule {

    @Provides
    @Singleton
    fun provideCharacterDao(database: CharacterDatabase): CharacterDao {
        return database.characterDao()
    }

    @Provides
    @Singleton
    fun provideCharacterDatabase(@ApplicationContext context: Context): CharacterDatabase {
        return Room.databaseBuilder(
            context.applicationContext,
            CharacterDatabase::class.java,
            Constants.DB_NAME
        ).build()
    }
}