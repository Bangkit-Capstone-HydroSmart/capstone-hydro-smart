package com.example.hydrosmart.beforelogin.ui.detailbefore

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.activity.viewModels
import androidx.lifecycle.lifecycleScope
import com.example.hydrosmart.R
import com.example.hydrosmart.ViewModelFactory
import com.example.hydrosmart.afterlogin.ui.detail.DetailViewModel
import com.example.hydrosmart.data.networking.PlantResponse
import com.example.hydrosmart.databinding.ActivityDetailBinding
import com.example.hydrosmart.databinding.ActivityDetailitemBinding
import com.example.hydrosmart.utils.ShowLoading
import kotlinx.coroutines.launch

class Detailitem : AppCompatActivity() {
    private lateinit var binding: ActivityDetailitemBinding
    private lateinit var showLoading: ShowLoading
    private val detailbeforeViewModel by viewModels<DetailbeforeViewModel> {
        ViewModelFactory(this)
    }
    override fun onCreate(savedInstanceState: Bundle?) {

        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_detailitem)
        binding = ActivityDetailitemBinding.inflate(layoutInflater)
        setContentView(binding.root)

        showLoading = ShowLoading()

    }

    private fun getDetailPlant(){
        val plantName = intent.getStringExtra(PLANTS)
        lifecycleScope.launch {
            plantName?.let { detailbeforeViewModel.getPlantDetail(it) }
        }
    }

    private fun setUpAction(){
        detailbeforeViewModel.apply {
            detailPlant.observe(this@Detailitem){
                setDataPlant(it)
            }

            isLoading.observe(this@Detailitem) {
                showLoading.showLoading(it, binding.progressBar )
            }
        }
        binding.fbBack.setOnClickListener{
            onBackPressedDispatcher.onBackPressed()
        }
    }

    private fun setDataPlant(it: PlantResponse{
        binding.apply {
            tvDetailTitle.text = it.tanaman.toString()
            tvAlat
        }
    }

    companion object{
        const val PLANTS = "PLANTS"
    }
}