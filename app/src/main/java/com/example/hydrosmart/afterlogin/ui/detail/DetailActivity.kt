package com.example.hydrosmart.afterlogin.ui.detail

import android.content.Context
import android.content.Intent
import android.os.Bundle
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.preferencesDataStore
import androidx.lifecycle.lifecycleScope
import com.example.hydrosmart.ViewModelFactory
import com.example.hydrosmart.afterlogin.ui.market.MarketActivity
import com.example.hydrosmart.data.networking.PlantResponse
import com.example.hydrosmart.data.pref.UserPreference
import com.example.hydrosmart.databinding.ActivityDetailBinding
import com.example.hydrosmart.utils.ShowLoading
import kotlinx.coroutines.launch

private val Context.dataStore: DataStore<Preferences> by preferencesDataStore(name = "settings")

class DetailActivity : AppCompatActivity() {
    private lateinit var binding: ActivityDetailBinding
    private lateinit var showLoading: ShowLoading
    private val detailViewModel by viewModels<DetailViewModel> {
        ViewModelFactory(UserPreference.getInstance(dataStore), this)
    }


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityDetailBinding.inflate(layoutInflater)
        setContentView(binding.root)

        showLoading = ShowLoading()
        getDetailPlant()
        setUpAction()
    }


    private fun getDetailPlant() {
        val plantName = intent.getStringExtra(PLANTS)
        lifecycleScope.launch {
            plantName?.let { detailViewModel.getPlantDetail(it, this@DetailActivity) }
        }
    }


    private fun setUpAction() {
        detailViewModel.apply {
            detailPlant.observe(this@DetailActivity) {
                setDataPlant(it)
            }

            isLoading.observe(this@DetailActivity) {
                showLoading.showLoading(it, binding.progressBar)
            }
        }

        binding.apply {
            fbBack.setOnClickListener {
                onBackPressedDispatcher.onBackPressed()
            }

            fabStore.setOnClickListener {
                startActivity(Intent(this@DetailActivity, MarketActivity::class.java))
            }
        }
    }

    private fun setDataPlant(it: PlantResponse) {
        binding.apply {
            tvDetailTitle.text = it.tanaman.toString()
            tvAlatBahan.text = it.alatBahan.toString()
            tvLangkahBudidaya.text = it.sistemBudidaya.toString()
        }
    }

    companion object {
        const val PLANTS = "PLANTS"
    }
}