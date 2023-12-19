package com.example.hydrosmart.beforelogin.ui.detailbefore

import android.content.Context
import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import com.example.hydrosmart.data.repo.PlantRepository

class MainActivityViewModel(
    private val plantRepository: PlantRepository,
) : ViewModel() {
    val plant: LiveData<List<String>> = plantRepository.plant
    val isLoading: LiveData<Boolean> = plantRepository.isLoading

    suspend fun getPlant(context: Context, onRefreshComplete: () -> Unit) =
        plantRepository.getPlants(context = context, onRefreshComplete = onRefreshComplete)
}
