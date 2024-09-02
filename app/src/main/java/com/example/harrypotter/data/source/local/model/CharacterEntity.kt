package com.example.harrypotter.data.source.local.model

import androidx.room.Entity
import androidx.room.PrimaryKey
import kotlinx.serialization.Serializable

@Serializable
@Entity(tableName = "characters")
data class CharacterEntity(
    @PrimaryKey val name: String,
    val actor: String,
    val species: String,
    val house: String?,
    val houseColor: Int,
    val dateOfBirth: String?,
    val imageUrl: String?,
    val status: String // "Alive" or "Deceased"
) : java.io.Serializable
