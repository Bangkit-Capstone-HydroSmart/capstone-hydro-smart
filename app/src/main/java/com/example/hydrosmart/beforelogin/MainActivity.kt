package com.example.hydrosmart.beforelogin

import android.content.Context
import android.content.Intent
import android.content.res.Configuration
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.activity.viewModels
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.preferencesDataStore
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager

import com.example.hydrosmart.R
import com.example.hydrosmart.ViewModelFactory
import com.example.hydrosmart.afterlogin.ui.detail.DetailActivity
import com.example.hydrosmart.afterlogin.ui.home.HomeViewModel
import com.example.hydrosmart.auth.login.LoginActivity
import com.example.hydrosmart.data.adapter.PlantAdapter
import com.example.hydrosmart.data.pref.UserPreference
import com.example.hydrosmart.databinding.ActivityMainBinding
import com.example.hydrosmart.utils.ShowLoading
import kotlinx.coroutines.launch

private val Context.dataStore: DataStore<Preferences> by preferencesDataStore(name = "settings")
class MainActivity : AppCompatActivity() {
    private var _binding : ActivityMainBinding? = null

    private val binding get() = _binding!!
    private lateinit var showLoading: ShowLoading
    private val mainActivityViewModel by viewModels<HomeViewModel> {
        ViewModelFactory(UserPreference.getInstance(dataStore), this)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        _binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        getPlants()
        setUpAction()
        showRecycleView()

    }

    private fun showRecycleView() {
        val layoutManager = LinearLayoutManager(this@MainActivity)
        if (resources?.configuration?.orientation == Configuration.ORIENTATION_LANDSCAPE) {
            binding.rvListPlant.layoutManager = GridLayoutManager(this@MainActivity, 2)
        } else {
            binding.rvListPlant.layoutManager = layoutManager
        }
        val itemDecoration = DividerItemDecoration(this@MainActivity, layoutManager.orientation)
        binding.rvListPlant.addItemDecoration(itemDecoration)
    }

    private fun getPlants(){
        lifecycleScope.launch {
            mainActivityViewModel.getPlants()
        }
    }

    private fun setUpAction(){
        mainActivityViewModel.apply {
            plant.observe(this@MainActivity){
                getListPlant(it)
            }
            isLoading.observe(this@MainActivity){
                showLoading.showLoading(it, binding.progressBar)
            }
        }

        binding.btJoin.setOnClickListener{
            val toLogin = Intent(this@MainActivity, LoginActivity::class.java)
            startActivity(toLogin)
        }
    }

    private fun getListPlant(plant : List<String>) {
        val listPlant = ArrayList(plant)
        binding.rvListPlant.adapter = PlantAdapter(listPlant){
            val toDetailPlant = Intent(this, DetailActivity::class.java)
            toDetailPlant.putExtra(DetailActivity.PLANTS, it)
            startActivity(toDetailPlant)
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }
}