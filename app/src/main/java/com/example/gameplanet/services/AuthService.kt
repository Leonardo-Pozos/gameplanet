package com.example.gameplanet.services

import com.example.gameplanet.models.Auth
import com.example.gameplanet.models.AuthResponse
import com.example.gameplanet.models.UserCreated
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.Field
import retrofit2.http.FormUrlEncoded
import retrofit2.http.Headers
import retrofit2.http.POST

interface AuthService {
    @FormUrlEncoded
    @POST("login")
    suspend fun login(
        @Field("grant_type") grantType: String,
        @Field("username") username: String,
        @Field("password") password: String,
        @Field("scope") scope: String = "",
        @Field("client_id") clientId: String = "",
        @Field("client_secret") clientSecret: String = ""
    ) : Response<AuthResponse>

    @POST("register")
    suspend fun createUser(@Body auth: Auth) : Response<UserCreated>
}