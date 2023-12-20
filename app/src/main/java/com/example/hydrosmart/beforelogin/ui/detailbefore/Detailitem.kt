package com.example.hydrosmart.beforelogin.ui.detailbefore

import android.content.Context
import android.os.Bundle
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.preferencesDataStore
import androidx.lifecycle.lifecycleScope
import com.example.hydrosmart.ViewModelFactory
import com.example.hydrosmart.data.networking.PlantResponse
import com.example.hydrosmart.data.pref.UserPreference
import com.example.hydrosmart.databinding.ActivityDetailitemBinding
import com.example.hydrosmart.utils.ShowLoading
import kotlinx.coroutines.launch

private val Context.dataStore: DataStore<Preferences> by preferencesDataStore(name = "settings")

class Detailitem : AppCompatActivity() {
    private lateinit var binding: ActivityDetailitemBinding
    private lateinit var showLoading: ShowLoading
    private val detailbeforeViewModel by viewModels<DetailbeforeViewModel> {
        ViewModelFactory(UserPreference.getInstance(dataStore), this)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityDetailitemBinding.inflate(layoutInflater)
        setContentView(binding.root)

        showLoading = ShowLoading()
        getDetailPlant()
        setUpAction()

    }

    private fun getDetailPlant() {
        val plantName = intent.getStringExtra(PLANTS)
        lifecycleScope.launch {
            plantName?.let { detailbeforeViewModel.getPlantDetail(it, this@Detailitem) }
        }
    }

    private fun setUpAction() {
        detailbeforeViewModel.apply {
            detailPlant.observe(this@Detailitem) {
                setDataPlant(it)
            }

            isLoading.observe(this@Detailitem) {
                showLoading.showLoading(it, binding.progressBar)
            }
        }
        binding.fbBack.setOnClickListener {
            onBackPressedDispatcher.onBackPressed()
        }
    }

    private fun setDataPlant(it: PlantResponse) {
        binding.apply {
            tvDetailTitle.text = it.tanaman.toString()
            tvAlatBahan
        }
    }

    companion object {
        const val PLANTS = "PLANTS"
    }
}