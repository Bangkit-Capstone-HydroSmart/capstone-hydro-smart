package com.example.hydrosmart.afterlogin.ui.home

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.asLiveData
import com.example.hydrosmart.data.pref.UserModel
import com.example.hydrosmart.data.pref.UserPreference
import com.example.hydrosmart.data.repo.PlantRepository
import kotlinx.coroutines.flow.Flow

class HomeViewModel(
    private val pref: UserPreference,
    private val plantRepository: PlantRepository
) : ViewModel() {

    val plant: LiveData<List<String>> = plantRepository.plant
    val isLoading: LiveData<Boolean> = plantRepository.isLoading

    suspend fun getPlants() = plantRepository.getPlants()

    fun getUser(): LiveData<UserModel> {
        return pref.getUser().asLiveData()
    }
}