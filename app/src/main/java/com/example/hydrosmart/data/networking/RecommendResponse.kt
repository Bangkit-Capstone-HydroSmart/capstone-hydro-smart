package com.example.hydrosmart.data.networking

import com.google.gson.annotations.SerializedName

data class RecommendResponse(

	@field:SerializedName("api_output")
	val apiOutput: List<String>
)
