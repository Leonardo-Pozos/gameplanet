package com.example.gameplanet.services

import com.example.gameplanet.models.Game
import com.example.gameplanet.models.ListaDeseo
import retrofit2.http.GET
import retrofit2.http.Path

interface GameService {
    @GET("videojuegos/")
    suspend fun getGames(): List<Game>

    @GET("videojuegos/{id}")
    suspend fun getGameById(@Path("id") id: Int) : Game

    @GET("videojuegos/{clasificacion}")
    suspend fun getGameByClasificacion(@Path("clasificacion") clasificacion: String): List<Game>

    @GET("videojuegos/{desarrollador}")
    suspend fun getGameByDesarrollador(@Path("desarrollador") desarrollador: String): List<Game>

    @GET("lista_de_deseados/{idUser}")
    suspend fun getListaDeseo(@Path("idUser") idUser: Int): List<ListaDeseo>
}