package com.example.hydrosmart.afterlogin.ui.home

import android.content.Intent
import android.content.res.Configuration
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.hydrosmart.ViewModelFactory
import com.example.hydrosmart.afterlogin.ui.detail.DetailActivity
import com.example.hydrosmart.data.adapter.PlantAdapter
import com.example.hydrosmart.databinding.FragmentHomeBinding
import com.example.hydrosmart.utils.ShowLoading
import kotlinx.coroutines.launch

class HomeFragment : Fragment() {

    private var _binding: FragmentHomeBinding? = null

    // This property is only valid between onCreateView and
    // onDestroyView.
    private val binding get() = _binding!!
    private lateinit var showLoading: ShowLoading
    private val homeViewModel by viewModels<HomeViewModel> {
        ViewModelFactory(requireActivity())
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View {
        _binding = FragmentHomeBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        showLoading = ShowLoading()

        getPlants()
        setUpAction()
        showRecycleView()
    }

    private fun showRecycleView() {
        val layoutManager = LinearLayoutManager(requireActivity())
        if (context?.resources?.configuration?.orientation == Configuration.ORIENTATION_LANDSCAPE) {
            binding.rvListPlant.layoutManager = GridLayoutManager(requireActivity(), 2)
        } else {
            binding.rvListPlant.layoutManager = layoutManager
        }
        val itemDecoration = DividerItemDecoration(requireActivity(), layoutManager.orientation)
        binding.rvListPlant.addItemDecoration(itemDecoration)
    }

    private fun getPlants() {
        lifecycleScope.launch {
            homeViewModel.getPlants()
        }
    }

    private fun setUpAction() {
        homeViewModel.apply {
            plant.observe(requireActivity()) {
                getListPlant(it)
            }
            isLoading.observe(requireActivity()) {
                showLoading.showLoading(it, binding.progressBar)
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


    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}