package com.example.hydrosmart

import android.content.Context
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.hydrosmart.afterlogin.ui.detail.DetailViewModel
import com.example.hydrosmart.afterlogin.ui.home.HomeViewModel
import com.example.hydrosmart.afterlogin.ui.rekomendasi.RecommendViewModel
import com.example.hydrosmart.utils.di.Injection

class ViewModelFactory(
    private val context: Context
): ViewModelProvider.NewInstanceFactory() {

    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        return when {
            modelClass.isAssignableFrom(HomeViewModel::class.java) -> {
                HomeViewModel(Injection.provideRepository(context)) as T
            }

            modelClass.isAssignableFrom(DetailViewModel::class.java) -> {
                DetailViewModel(Injection.provideRepository(context)) as T
            }

            modelClass.isAssignableFrom(RecommendViewModel::class.java) -> {
                RecommendViewModel(Injection.provideRepository(context)) as T
            }

            else -> throw IllegalArgumentException("Unknown ViewModel class: " + modelClass.name)
        }
    }
}