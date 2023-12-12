package com.example.hydrosmart.data.networking

import com.google.gson.annotations.SerializedName

data class PlantResponse(

	@field:SerializedName("Alat & Bahan")
	val alatBahan: String? = null,

	@field:SerializedName("Tanaman")
	val tanaman: String? = null,

	@field:SerializedName("Sistem Budidaya")
	val sistemBudidaya: String? = null
)
