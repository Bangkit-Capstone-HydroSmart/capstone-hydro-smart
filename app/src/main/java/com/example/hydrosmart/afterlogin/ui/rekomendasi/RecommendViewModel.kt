package com.example.hydrosmart.afterlogin.ui.rekomendasi

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import com.example.hydrosmart.data.MyRequest
import com.example.hydrosmart.data.repo.PlantRepository

class RecommendViewModel(
    private val plantRepository: PlantRepository
) : ViewModel() {
    val predictPlant: LiveData<List<String>> = plantRepository.predictPlant
    val isLoading: LiveData<Boolean> = plantRepository.isLoading

    suspend fun getPredictPlant(plant: MyRequest) = plantRepository.getPredictPlant(plant)
}