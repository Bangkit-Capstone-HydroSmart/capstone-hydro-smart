package com.example.hydrosmart.afterlogin.ui.home

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.hydrosmart.data.repo.PlantRepository

class HomeViewModel(
    private val plantRepository: PlantRepository
) : ViewModel() {
    val plant: LiveData<List<String>> = plantRepository.plant
    val isLoading: LiveData<Boolean> = plantRepository.isLoading

    suspend fun getPlants() = plantRepository.getPlants()
}