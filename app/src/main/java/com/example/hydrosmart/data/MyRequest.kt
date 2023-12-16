package com.example.hydrosmart.data

data class MyRequest(
    val luas_lahan_pengguna: Double,
    val suhu_pengguna: Int,
    val ph_pengguna: Int,
    val kelembapan_pengguna: Int,
    val penyinaran_pengguna: Int
)

data class CalculatorPPM(
    val mass_solute_mg: Double,
    val volume_solution_liters: Double
)

data class CalculatorMass(
    val ppm: Double,
    val volume_solution_liters: Double
)

data class CalculatorVolume(
    val mass_solute_mg: Double,
    val ppm: Double
)