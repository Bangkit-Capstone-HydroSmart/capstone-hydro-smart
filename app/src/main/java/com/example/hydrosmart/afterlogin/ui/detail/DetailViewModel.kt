package com.example.hydrosmart.afterlogin.ui.detail

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import com.example.hydrosmart.data.networking.PlantResponse
import com.example.hydrosmart.data.repo.PlantRepository

class DetailViewModel(
    private val plantRepository: PlantRepository,
) : ViewModel() {
    val detailPlant: LiveData<PlantResponse> = plantRepository.detailPlant
    val isLoading: LiveData<Boolean> = plantRepository.isLoading

    suspend fun getPlantDetail(plant: String) = plantRepository.getPlantDetail(plant)

}