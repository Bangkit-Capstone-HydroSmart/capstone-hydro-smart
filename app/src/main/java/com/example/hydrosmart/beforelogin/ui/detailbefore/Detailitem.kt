package com.example.hydrosmart.beforelogin.ui.detailbefore

import android.content.Context
import android.os.Bundle
import android.widget.ImageView
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.preferencesDataStore
import androidx.lifecycle.lifecycleScope
import com.bumptech.glide.Glide
import com.example.hydrosmart.R
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

            val plantImageResource = when (it.tanaman.toString()) {
                "Sawi" -> R.drawable.sawi
                "Selada" -> R.drawable.selada
                "Kangkung" -> R.drawable.kangkung
                "Bayam" -> R.drawable.bayam
                "Tomat" -> R.drawable.tomat
                "Cabai" -> R.drawable.cabai
                "Mentimun" -> R.drawable.mentimun
                "Terong" -> R.drawable.terong
                "Daun Bawang" -> R.drawable.daun_bawang
                "Melon" -> R.drawable.melon
                "Stroberi" -> R.drawable.stroberi
                "Mawar" -> R.drawable.mawar
                "Anggrek" -> R.drawable.anggrek
                else -> R.drawable.default_img_plant
            }

            imgPlants.loadImage(plantImageResource)
        }
    }

    private fun ImageView.loadImage(img: Int) {
        Glide.with(this.context)
            .load(img)
            .into(this)
    }

    companion object {
        const val PLANTS = "PLANTS"
    }
}