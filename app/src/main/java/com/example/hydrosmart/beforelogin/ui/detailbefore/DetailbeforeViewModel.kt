package com.example.hydrosmart.beforelogin.ui.detailbefore

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import com.example.hydrosmart.data.networking.PlantResponse
import com.example.hydrosmart.data.repo.PlantRepository

class DetailbeforeViewModel(private val plantRepository: PlantRepository) : ViewModel() {
    val detailPlant : LiveData<PlantResponse> = plantRepository.detailPlant
    val isLoading: LiveData<Boolean> = plantRepository.isLoading

    suspend fun getPlantDetail(plant: String) = plantRepository.getPlantDetail(plant)
}