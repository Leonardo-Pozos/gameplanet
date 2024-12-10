package com.example.gameplanet.services

import com.example.gameplanet.models.Game
import retrofit2.http.GET
import retrofit2.http.Path

interface GameService {
    @GET("videojuegos/")
    suspend fun getGames(): List<Game>

    @GET("videojuegos/{id}")
    suspend fun getGameById(@Path("id") id: Int) : Game

    @GET("videojuegos/{clasificacion}")
    suspend fun getGameByClasificacion(@Path("clasificacion") clasificacion: String): List<Game>

}