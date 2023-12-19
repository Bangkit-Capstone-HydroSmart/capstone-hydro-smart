package com.example.hydrosmart.afterlogin.ui.home

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.preferencesDataStore
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.hydrosmart.R
import com.example.hydrosmart.ViewModelFactory
import com.example.hydrosmart.afterlogin.ui.calculator.CalculateActivity
import com.example.hydrosmart.afterlogin.ui.detail.DetailActivity
import com.example.hydrosmart.afterlogin.ui.rekomendasi.RecommendActivity
import com.example.hydrosmart.beforelogin.MainActivity
import com.example.hydrosmart.data.adapter.PlantAdapter
import com.example.hydrosmart.data.pref.UserPreference
import com.example.hydrosmart.databinding.FragmentHomeBinding
import com.example.hydrosmart.utils.ShowLoading
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.FirebaseDatabase
import kotlinx.coroutines.launch

private val Context.dataStore: DataStore<Preferences> by preferencesDataStore(name = "settings")

class HomeFragment : Fragment() {

    private lateinit var _binding: FragmentHomeBinding

    // This property is only valid between onCreateView and
    // onDestroyView.
    private val binding get() = _binding
    private lateinit var showLoading: ShowLoading
    private val homeViewModel by viewModels<HomeViewModel> {
        ViewModelFactory(UserPreference.getInstance(requireContext().dataStore), requireContext())
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View {
        _binding = FragmentHomeBinding.inflate(inflater, container, false)

        showLoading = ShowLoading()

        getPlants()
        setUpAction()
        showRecycleView()
        action()

        return binding.root
    }

    private fun showRecycleView() {
        val layoutManager = LinearLayoutManager(requireContext())
        binding.rvListPlant.layoutManager = layoutManager

    }

    private fun getPlants() {
        lifecycleScope.launch {
            homeViewModel.getPlants(requireContext()) {
                binding.swipeRefresh.isRefreshing = true
                lifecycleScope.launch {
                    homeViewModel.getPlants(requireContext()) {
                        binding.swipeRefresh.isRefreshing = false
                    }
                }
            }
        }
    }

    private fun setUpAction() {
        homeViewModel.apply {
            plant.observe(viewLifecycleOwner) {
                getListPlant(it)
            }
            isLoading.observe(viewLifecycleOwner) {
                showLoading.showLoading(it, binding.progressBar)
            }
        }

        binding.apply {
            btRekomendasi.setOnClickListener {
                val toRecomActivity = Intent(requireContext(), RecommendActivity::class.java)
                startActivity(toRecomActivity)
            }

            fbCalculate.setOnClickListener {
                val toCalculateActivity = Intent(requireContext(), CalculateActivity::class.java)
                startActivity(toCalculateActivity)
            }
        }
    }

    private fun getListPlant(plant: List<String>) {
        val listPlant = ArrayList(plant)
        binding.rvListPlant.adapter = PlantAdapter(listPlant) {
            val toDetailPlant = Intent(requireContext(), DetailActivity::class.java)
            toDetailPlant.putExtra(DetailActivity.PLANTS, it)
            startActivity(toDetailPlant)
        }
    }

    private fun action() {
        val uid: String? = FirebaseAuth.getInstance().currentUser?.uid

        homeViewModel.getUser().observe(viewLifecycleOwner) { user ->
            if (user.isLogin) {
                uid?.let { uid ->
                    FirebaseDatabase.getInstance().reference.child("users").child(uid).get()
                        .addOnCompleteListener {
                            if (isAdded) {
                                binding.tvWelcome.text = getString(
                                    R.string.welcome_message_home,
                                    it.result.child("name").value.toString()
                                )
                            }
                        }
                }
            } else {
                startActivity(Intent(requireContext(), MainActivity::class.java))
                requireActivity().finish()
            }
        }

    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = FragmentHomeBinding.inflate(layoutInflater)
    }
}