package com.example.hydrosmart.beforelogin.ui.detailbefore

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.activity.viewModels
import com.example.hydrosmart.R
import com.example.hydrosmart.ViewModelFactory
import com.example.hydrosmart.afterlogin.ui.detail.DetailViewModel
import com.example.hydrosmart.databinding.ActivityDetailBinding
import com.example.hydrosmart.databinding.ActivityDetailitemBinding
import com.example.hydrosmart.utils.ShowLoading

class Detailitem : AppCompatActivity() {
    private lateinit var binding: ActivityDetailitemBinding
    private lateinit var showLoading: ShowLoading
    private val DetailbeforeViewModel by viewModels<DetailbeforeViewModel> {
        ViewModelFactory(this)
    }
    override fun onCreate(savedInstanceState: Bundle?) {

        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_detailitem)
        binding = ActivityDetailitemBinding.inflate(layoutInflater)
        setContentView(binding.root)

        showLoading = ShowLoading()

    }
}