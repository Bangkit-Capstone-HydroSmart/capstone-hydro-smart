package com.example.hydrosmart.data.networking

import com.google.gson.annotations.SerializedName

data class CalculatorResponse(

	@field:SerializedName("result")
	val result: Any
)
