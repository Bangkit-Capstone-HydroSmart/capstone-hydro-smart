package com.example.hydrosmart.data.networking

import com.example.hydrosmart.data.MyRequest
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.Query

interface ApiService {
    @GET("list_tanaman")
    suspend fun getPlants(): Response<ArrayList<String>>

    @GET("panduan")
    suspend fun getPlantDetail(
        @Query("tanaman_name") tanamanName: String,
    ): Response<PlantResponse>

    @POST("predict_text")
    suspend fun predictPlant(
        @Body request: MyRequest,
    ): Response<RecommendResponse>
}