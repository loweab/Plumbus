package com.example.plumbus.network

import com.example.plumbus.network.response.GetCharacterByIdResponse
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Path

interface RickAndMortyService {

    @GET("character/{character-id}")
    suspend fun getCharacterById(@Path("character-id") characterId: Int):
            Response<GetCharacterByIdResponse>
}