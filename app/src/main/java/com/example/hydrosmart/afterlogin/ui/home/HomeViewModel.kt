package com.example.hydrosmart.afterlogin.ui.home

import android.content.Context
import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.asLiveData
import com.example.hydrosmart.data.pref.UserModel
import com.example.hydrosmart.data.pref.UserPreference
import com.example.hydrosmart.data.repo.PlantRepository

class HomeViewModel(
    private val pref: UserPreference,
    private val plantRepository: PlantRepository,
) : ViewModel() {

    val plant: LiveData<List<String>> = plantRepository.plant
    val isLoading: LiveData<Boolean> = plantRepository.isLoading

    suspend fun getPlants(
        context: Context,
        onRefreshComplete: () -> Unit,
    ) = plantRepository.getPlants(onRefreshComplete = onRefreshComplete, context = context)

    fun getUser(): LiveData<UserModel> {
        return pref.getUser().asLiveData()
    }
}