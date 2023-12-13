package com.example.hydrosmart.afterlogin.ui.rekomendasi

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.preferencesDataStore
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.hydrosmart.R
import com.example.hydrosmart.ViewModelFactory
import com.example.hydrosmart.afterlogin.ui.detail.DetailActivity
import com.example.hydrosmart.data.MyRequest
import com.example.hydrosmart.data.adapter.PlantAdapter
import com.example.hydrosmart.data.pref.UserPreference
import com.example.hydrosmart.databinding.ActivityRecommendBinding
import com.example.hydrosmart.utils.ShowLoading
import kotlinx.coroutines.launch

private val Context.dataStore: DataStore<Preferences> by preferencesDataStore(name = "settings")

class RecommendActivity : AppCompatActivity() {
    private lateinit var binding: ActivityRecommendBinding
    private lateinit var showLoading: ShowLoading
    private val recommendViewModel by viewModels<RecommendViewModel> {
        ViewModelFactory(UserPreference.getInstance(dataStore), this)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityRecommendBinding.inflate(layoutInflater)
        setContentView(binding.root)

        showLoading = ShowLoading()

        showRecycleView()
        getPredictPlant()
        setUpAction()
    }

    private fun setUpAction() {
        recommendViewModel.apply {
            isLoading.observe(this@RecommendActivity) {
                showLoading.showLoading(it, binding.progressBar)
            }

            predictPlant.observe(this@RecommendActivity) {
                setDataAdapter(it)
            }
        }

        binding.fbBack.setOnClickListener {
            onBackPressedDispatcher.onBackPressed()
        }
    }

    private fun setDataAdapter(plant: List<String>) {
        val listPlant = ArrayList(plant)
        binding.rvResult.adapter = PlantAdapter(listPlant) {
            val toDetailPlant = Intent(this, DetailActivity::class.java)
            toDetailPlant.putExtra(DetailActivity.PLANTS, it)
            startActivity(toDetailPlant)
        }
    }

    private fun getPredictPlant() {
        binding.btSubmit.setOnClickListener {
            val suhu = binding.suhuEditText.text.toString()
            val luasLahan = binding.luasEditText.text.toString()
            val ph = binding.phEditText.text.toString()
            val kelembapan = binding.kelembapanEditText.text.toString()
            val penyinaran = binding.penyinaranEditText.text.toString()

            if (suhu.isNotEmpty() && luasLahan.isNotEmpty() && ph.isNotEmpty() && kelembapan.isNotEmpty() && penyinaran.isNotEmpty()) {
                try {
                    val request = MyRequest(
                        suhu_pengguna = suhu.toInt(),
                        luas_lahan_pengguna = luasLahan.toInt(),
                        ph_pengguna = ph.toInt(),
                        kelembapan_pengguna = kelembapan.toInt(),
                        penyinaran_pengguna = penyinaran.toInt()
                    )

                    lifecycleScope.launch {
                        recommendViewModel.getPredictPlant(request)
                    }

                } catch (e: NumberFormatException) {
                    Toast.makeText(this, getString(R.string.invalid_field), Toast.LENGTH_LONG)
                        .show()
                }
            } else {
                Toast.makeText(this, getString(R.string.empty_field), Toast.LENGTH_LONG).show()
            }
        }
    }

    private fun showRecycleView() {
        val layoutManager = LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false)
        binding.rvResult.layoutManager = layoutManager
    }
}