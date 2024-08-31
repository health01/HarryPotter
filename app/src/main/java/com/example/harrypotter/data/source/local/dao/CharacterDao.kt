package com.example.harrypotter.data.source.local.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Transaction
import com.example.harrypotter.data.source.local.model.CharacterEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface CharacterDao {

    // Query to get all characters
    @Query("SELECT * FROM characters")
     fun getAllCharacters(): Flow<List<CharacterEntity>>

    // Query to get characters by house
    @Query("SELECT * FROM characters WHERE house = :house")
     fun getCharactersByHouse(house: String): Flow<List<CharacterEntity>>

    // Insert or update a list of characters
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAll(characters: List<CharacterEntity>)

    // Delete all characters
    @Query("DELETE FROM characters")
    suspend fun deleteAllCharacters()

    @Transaction
    suspend fun updateCoins(characterEntity: List<CharacterEntity>) {
        deleteAllCharacters()
        insertAll(characterEntity)
    }
}
