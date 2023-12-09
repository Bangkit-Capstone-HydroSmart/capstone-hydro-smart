package com.example.hydrosmart.data.networking

import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Query

interface ApiService {
    @GET("list_tanaman")
    suspend fun getPlants(): Response<ArrayList<String>>

    @GET("panduan")
    suspend fun getPlantDetail(
        @Query("tanaman_name") tanamanName: String
    ): Response<PlantResponse>
}