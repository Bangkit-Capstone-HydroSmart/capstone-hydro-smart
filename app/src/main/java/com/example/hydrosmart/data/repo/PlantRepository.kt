package com.example.hydrosmart.data.repo

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.example.hydrosmart.data.MyRequest
import com.example.hydrosmart.data.networking.ApiService
import com.example.hydrosmart.data.networking.PlantResponse
import com.example.hydrosmart.data.networking.RecommendResponse

class PlantRepository(private val apiService: ApiService) {

    private val _plant = MutableLiveData<List<String>>()
    val plant: LiveData<List<String>> = _plant

    private val _detailPlant = MutableLiveData<PlantResponse>()
    val detailPlant: LiveData<PlantResponse> = _detailPlant

    private val _predictPlant = MutableLiveData<List<String>>()
    val predictPlant: LiveData<List<String>> = _predictPlant

    private val _isLoading = MutableLiveData<Boolean>()
    val isLoading: LiveData<Boolean> = _isLoading

    suspend fun getPlants() {
        _isLoading.value = true
        try {
            val response = apiService.getPlants()
            _isLoading.value = false
            if (response.isSuccessful) {
                _plant.value = response.body()
            }
        } catch (e: Exception) {
            Log.e(TAG1, "onFailure: ${e.message}")
        }
    }

    suspend fun getPlantDetail(plant: String) {
        _isLoading.value = true
        try {
            val response = apiService.getPlantDetail(plant)
            _isLoading.value = false
            if (response.isSuccessful) {
                _detailPlant.value = response.body()
            }
        } catch (e: Exception) {
            Log.e(TAG2, "onFailure: ${e.message}")
        }
    }

    suspend fun getPredictPlant(plant: MyRequest) {
        _isLoading.value = true
        try {
            val response = apiService.predictPlant(plant)
            _isLoading.value = false
            if (response.isSuccessful) {
                _predictPlant.value = response.body()?.apiOutput
            }
        } catch (e: Exception) {
            Log.e(TAG3, "onFailure: ${e.message}")
        }
    }
    companion object {
        private const val TAG1 = "HomeFragment"
        private const val TAG2 = "DetailActivity"
        private const val TAG3 = "RecommendActivity"
    }
}