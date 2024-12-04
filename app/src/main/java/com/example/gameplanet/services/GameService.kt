package com.example.gameplanet.services

import com.example.gameplanet.models.Game
import retrofit2.http.GET

interface GameService {
    @GET("videojuegos/")
    suspend fun getGames(): List<Game>
}