package com.example.hydrosmart.afterlogin.ui.detail

import android.os.Bundle
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import com.example.hydrosmart.ViewModelFactory
import com.example.hydrosmart.data.networking.PlantResponse
import com.example.hydrosmart.databinding.ActivityDetailBinding
import com.example.hydrosmart.utils.ShowLoading
import kotlinx.coroutines.launch

class DetailActivity : AppCompatActivity() {
    private lateinit var binding: ActivityDetailBinding
    private lateinit var showLoading: ShowLoading
    private val detailViewModel by viewModels<DetailViewModel> {
        ViewModelFactory(this)
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
            plantName?.let { detailViewModel.getPlantDetail(it) }
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

        binding.fbBack.setOnClickListener {
            onBackPressedDispatcher.onBackPressed()
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