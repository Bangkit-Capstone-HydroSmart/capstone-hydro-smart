package com.example.hydrosmart.data.networking

import com.example.hydrosmart.data.CalculatorMass
import com.example.hydrosmart.data.CalculatorPPM
import com.example.hydrosmart.data.CalculatorVolume
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

    @POST("hydroponic_recommendations")
    suspend fun predictPlant(
        @Body request: MyRequest,
    ): Response<RecommendResponse>

    @POST("hydroponic_calculator_ppm")
    suspend fun calculatorPPM(
        @Body request: CalculatorPPM,
    ): Response<CalculatorResponse>

    @POST("hydroponic_calculator_mass")
    suspend fun calculatorMass(
        @Body request: CalculatorMass,
    ): Response<CalculatorResponse>

    @POST("hydroponic_calculator_volume")
    suspend fun calculatorVolume(
        @Body request: CalculatorVolume,
    ): Response<CalculatorResponse>
}