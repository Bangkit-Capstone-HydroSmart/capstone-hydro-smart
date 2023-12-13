package com.example.hydrosmart

import android.content.Context
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.hydrosmart.afterlogin.ui.detail.DetailViewModel
import com.example.hydrosmart.afterlogin.ui.home.HomeViewModel
import com.example.hydrosmart.afterlogin.ui.profile.ProfileViewModel
import com.example.hydrosmart.afterlogin.ui.rekomendasi.RecommendViewModel
import com.example.hydrosmart.auth.login.LoginViewModel
import com.example.hydrosmart.beforelogin.ui.detailbefore.DetailbeforeViewModel
import com.example.hydrosmart.data.pref.UserPreference
import com.example.hydrosmart.utils.di.Injection

class ViewModelFactory(
    private val pref: UserPreference,
    private val context: Context
): ViewModelProvider.NewInstanceFactory() {

    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        return when {
            modelClass.isAssignableFrom(HomeViewModel::class.java) -> {
                HomeViewModel(pref, Injection.provideRepository(context)) as T
            }

            modelClass.isAssignableFrom(DetailViewModel::class.java) -> {
                DetailViewModel(Injection.provideRepository(context)) as T
            }

            modelClass.isAssignableFrom(RecommendViewModel::class.java) -> {
                RecommendViewModel(Injection.provideRepository(context)) as T
            }

            modelClass.isAssignableFrom(LoginViewModel::class.java) -> {
                LoginViewModel(pref) as T
            }

            modelClass.isAssignableFrom(ProfileViewModel::class.java) -> {
                ProfileViewModel(pref) as T
            }

            modelClass.isAssignableFrom(DetailbeforeViewModel::class.java) -> {
                DetailbeforeViewModel(Injection.provideRepository(context)) as T
            }

            else -> throw IllegalArgumentException("Unknown ViewModel class: " + modelClass.name)
        }
    }
}