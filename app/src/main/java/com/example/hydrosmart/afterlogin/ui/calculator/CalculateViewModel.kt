package com.example.hydrosmart.afterlogin.ui.calculator

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import com.example.hydrosmart.data.CalculatorMass
import com.example.hydrosmart.data.CalculatorPPM
import com.example.hydrosmart.data.CalculatorVolume
import com.example.hydrosmart.data.repo.PlantRepository

class CalculateViewModel(
    private val plantRepository: PlantRepository,
) : ViewModel() {
    val resultPPM: LiveData<Any> = plantRepository.resultPPM
    val resultMass: LiveData<Any> = plantRepository.resultMass
    val resultVolume: LiveData<Any> = plantRepository.resultVolume

    suspend fun getResultPPM(result: CalculatorPPM) = plantRepository.getResultPPM(result)
    suspend fun getResultMass(result: CalculatorMass) = plantRepository.getResultMass(result)
    suspend fun getResultVolume(result: CalculatorVolume) = plantRepository.getResultVolume(result)
}