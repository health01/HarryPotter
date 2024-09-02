package com.example.harrypotter

import com.example.harrypotter.data.source.local.model.CharacterEntity
import com.example.harrypotter.data.source.remote.model.CharacterApiModel
import com.example.harrypotter.data.source.remote.model.CharacterItemAPIModel
import com.example.harrypotter.data.source.remote.model.Wand

open class BasicTesting {
    val mockApiModels = CharacterApiModel().apply {
        add(
            CharacterItemAPIModel(
                actor = "Daniel Radcliffe",
                alive = true,
                alternateActors = listOf(),
                alternateNames = listOf(),
                ancestry = "Half-blood",
                dateOfBirth = "31-07-1980",
                eyeColour = "Green",
                gender = "Male",
                hairColour = "Black",
                hogwartsStaff = false,
                hogwartsStudent = true,
                house = "Gryffindor",
                id = "harry_potter",
                image = "https://hp-api.herokuapp.com/images/harry.jpg",
                name = "Harry Potter",
                patronus = "Stag",
                species = "Human",
                wand = Wand("Holly", 2.0, "11"),
                wizard = true,
                yearOfBirth = 1980
            )
        )
    }
    val mockEntities = listOf(
        CharacterEntity(
            name = "Harry Potter",
            actor = "Daniel Radcliffe",
            species = "Human",
            house = "Gryffindor",
            houseColor = 0xFF740001.toInt(),
            dateOfBirth = "31 Jul 1980",
            imageUrl = "https://hp-api.herokuapp.com/images/harry.jpg",
            status = "Alive"
        )
    )
}