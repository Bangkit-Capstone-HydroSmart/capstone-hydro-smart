package com.example.hydrosmart.utils.di

import android.content.Context
import com.example.hydrosmart.data.networking.ApiConfig
import com.example.hydrosmart.data.repo.PlantRepository

object Injection {
    fun provideRepository(context: Context): PlantRepository {
        val apiService = ApiConfig.getApiService()
        return PlantRepository(apiService)
    }
}