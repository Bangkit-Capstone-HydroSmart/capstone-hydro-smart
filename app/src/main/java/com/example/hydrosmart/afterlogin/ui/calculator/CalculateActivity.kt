package com.example.hydrosmart.afterlogin.ui.calculator

import android.content.Context
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import androidx.activity.viewModels
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.preferencesDataStore
import androidx.lifecycle.lifecycleScope
import com.example.hydrosmart.R
import com.example.hydrosmart.ViewModelFactory
import com.example.hydrosmart.data.CalculatorMass
import com.example.hydrosmart.data.CalculatorPPM
import com.example.hydrosmart.data.CalculatorVolume
import com.example.hydrosmart.data.pref.UserPreference
import com.example.hydrosmart.databinding.ActivityCalculateBinding
import kotlinx.coroutines.launch

private val Context.dataStore: DataStore<Preferences> by preferencesDataStore(name = "settings")

class CalculateActivity : AppCompatActivity() {
    private lateinit var binding: ActivityCalculateBinding
    private val calculateViewModel by viewModels<CalculateViewModel> {
        ViewModelFactory(UserPreference.getInstance(dataStore), this)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityCalculateBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setUpAction()
        getInput()
    }

    private fun setUpAction() {
        calculateViewModel.apply {
            resultPPM.observe(this@CalculateActivity) {
                binding.tvResultPpmValue.text = it.toString()
            }

            resultMass.observe(this@CalculateActivity) {
                binding.tvResultMassValue.text = it.toString()
            }

            resultVolume.observe(this@CalculateActivity) {
                binding.tvResultVolumeValue.text = it.toString()
            }
        }

        binding.fbBack.setOnClickListener {
            onBackPressedDispatcher.onBackPressed()
        }
    }

    private fun getInput() {
        binding.apply {
            btSubmitPpm.setOnClickListener {
                val mass = binding.massEditText.text.toString()
                val volume = binding.volumeEditText.text.toString()

                if (mass.isNotEmpty() && volume.isNotEmpty()) {
                    try {
                        val input = CalculatorPPM(
                            mass_solute_mg = mass.toDouble(),
                            volume_solution_liters = volume.toDouble()
                        )

                        lifecycleScope.launch {
                            calculateViewModel.getResultPPM(input)
                        }

                    } catch (e: NumberFormatException) {
                        Toast.makeText(this@CalculateActivity, getString(R.string.invalid_field), Toast.LENGTH_LONG)
                            .show()
                    }
                } else {
                    Toast.makeText(this@CalculateActivity, getString(R.string.empty_field), Toast.LENGTH_LONG).show()
                }
            }

            btSubmitMass.setOnClickListener {
                val ppm = binding.ppmEditText.text.toString()
                val volume = binding.volumeMassEditText.text.toString()

                if (ppm.isNotEmpty() && volume.isNotEmpty()) {
                    try {
                        val input = CalculatorMass(
                            ppm = ppm.toDouble(),
                            volume_solution_liters = volume.toDouble()
                        )

                        lifecycleScope.launch {
                            calculateViewModel.getResultMass(input)
                        }

                    } catch (e: NumberFormatException) {
                        Toast.makeText(this@CalculateActivity, getString(R.string.invalid_field), Toast.LENGTH_LONG)
                            .show()
                    }
                } else {
                    Toast.makeText(this@CalculateActivity, getString(R.string.empty_field), Toast.LENGTH_LONG).show()
                }
            }

            btSubmitVolume.setOnClickListener {
                val mass = binding.massVolumeEditText.text.toString()
                val ppm = binding.ppmVolumeEditText.text.toString()

                if (mass.isNotEmpty() && ppm.isNotEmpty()) {
                    try {
                        val input = CalculatorVolume(
                            mass_solute_mg = mass.toDouble(),
                            ppm = ppm.toDouble()
                        )

                        lifecycleScope.launch {
                            calculateViewModel.getResultVolume(input)
                        }

                    } catch (e: NumberFormatException) {
                        Toast.makeText(this@CalculateActivity, getString(R.string.invalid_field), Toast.LENGTH_LONG)
                            .show()
                    }
                } else {
                    Toast.makeText(this@CalculateActivity, getString(R.string.empty_field), Toast.LENGTH_LONG).show()
                }
            }
        }
    }

}
