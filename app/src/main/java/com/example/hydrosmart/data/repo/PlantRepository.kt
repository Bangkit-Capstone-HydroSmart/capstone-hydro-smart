package com.example.hydrosmart.data.repo

import android.content.Context
import android.util.Log
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.example.hydrosmart.R
import com.example.hydrosmart.data.CalculatorMass
import com.example.hydrosmart.data.CalculatorPPM
import com.example.hydrosmart.data.CalculatorVolume
import com.example.hydrosmart.data.MyRequest
import com.example.hydrosmart.data.networking.ApiService
import com.example.hydrosmart.data.networking.PlantResponse

class PlantRepository(private val apiService: ApiService) {

    private val _plant = MutableLiveData<List<String>>()
    val plant: LiveData<List<String>> = _plant

    private val _detailPlant = MutableLiveData<PlantResponse>()
    val detailPlant: LiveData<PlantResponse> = _detailPlant

    private val _predictPlant = MutableLiveData<List<String>>()
    val predictPlant: LiveData<List<String>> = _predictPlant

    private val _resultPPM = MutableLiveData<Any>()
    val resultPPM: LiveData<Any> = _resultPPM

    private val _resultMass = MutableLiveData<Any>()
    val resultMass: LiveData<Any> = _resultMass

    private val _resultVolume = MutableLiveData<Any>()
    val resultVolume: LiveData<Any> = _resultVolume

    private val _isLoading = MutableLiveData<Boolean>()
    val isLoading: LiveData<Boolean> = _isLoading

    suspend fun getPlants(onRefreshComplete: () -> Unit, context: Context) {
        _isLoading.value = true
        try {
            val response = apiService.getPlants()
            _isLoading.value = false
            if (response.isSuccessful) {
                _plant.value = response.body()
            }
        } catch (e: Exception) {
            _isLoading.value = false
            Log.e(TAG1, "onFailure: ${e.message}")
            showRefreshErrorDialog(onRefreshComplete, context)
        } finally {
            onRefreshComplete.invoke()
        }
    }

    suspend fun getPlantDetail(
        plant: String,
        context: Context
    ) {
        _isLoading.value = true
        try {
            val response = apiService.getPlantDetail(plant)
            _isLoading.value = false
            if (response.isSuccessful) {
                _detailPlant.value = response.body()
            } else {
                Toast.makeText(context,
                    context.getString(R.string.error_message_detail), Toast.LENGTH_LONG).show()
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

    suspend fun getResultPPM(result: CalculatorPPM) {
        try {
            val response = apiService.calculatorPPM(result)
            if (response.isSuccessful) {
                _resultPPM.value = response.body()?.result
            }
        } catch (e: Exception) {
            Log.e(TAG4, "onFailure: ${e.message}")
        }
    }

    suspend fun getResultMass(result: CalculatorMass) {
        try {
            val response = apiService.calculatorMass(result)
            if (response.isSuccessful) {
                _resultMass.value = response.body()?.result
            }
        } catch (e: Exception) {
            Log.e(TAG4, "onFailure: ${e.message}")
        }
    }

    suspend fun getResultVolume(result: CalculatorVolume) {
        try {
            val response = apiService.calculatorVolume(result)
            if (response.isSuccessful) {
                _resultVolume.value = response.body()?.result
            }
        } catch (e: Exception) {
            Log.e(TAG4, "onFailure: ${e.message}")
        }
    }

    private fun showRefreshErrorDialog(onRefreshComplete: () -> Unit, context: Context) {
        val builder = AlertDialog.Builder(context)
        builder.setTitle("Error")
            .setMessage("Failed to load plants. Do you want to try again?")
            .setPositiveButton("Refresh") { _, _ ->
                // Trigger refresh action
                onRefreshComplete.invoke()
            }
            .setNegativeButton("Cancel", null)
            .show()
    }

    companion object {
        private const val TAG1 = "HomeFragment"
        private const val TAG2 = "DetailActivity"
        private const val TAG3 = "RecommendActivity"
        private const val TAG4 = "CalculatorActivity"
    }
}