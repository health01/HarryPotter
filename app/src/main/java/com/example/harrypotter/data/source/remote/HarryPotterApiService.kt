package com.example.harrypotter.data.source.remote

import com.example.harrypotter.data.source.remote.model.CharacterApiModel
import retrofit2.Response
import retrofit2.http.GET

interface HarryPotterApiService {
    @GET("api/characters")
    suspend fun getCharacterList(): Response<CharacterApiModel>
}