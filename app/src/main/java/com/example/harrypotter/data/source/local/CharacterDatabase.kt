package com.example.harrypotter.data.source.local

import androidx.room.Database
import androidx.room.RoomDatabase
import com.example.harrypotter.data.source.local.dao.CharacterDao
import com.example.harrypotter.data.source.local.model.CharacterEntity

@Database(entities = [CharacterEntity::class], version = 1)

abstract class CharacterDatabase : RoomDatabase() {
    abstract fun characterDao(): CharacterDao
}