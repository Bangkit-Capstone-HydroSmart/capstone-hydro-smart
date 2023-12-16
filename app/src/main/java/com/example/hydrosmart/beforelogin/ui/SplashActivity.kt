package com.example.hydrosmart.beforelogin.ui

import android.content.Intent
import android.os.Bundle
import android.os.Handler
import androidx.appcompat.app.AppCompatActivity
import com.example.hydrosmart.R
import com.example.hydrosmart.afterlogin.MainActivityAfter

class SplashActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_splash)

        supportActionBar?.hide()

        @Suppress("DEPRECATION")
        Handler().postDelayed({
            // Intent untuk beralih ke aktivitas berikutnya
            val intent = Intent(this, MainActivityAfter::class.java)
            startActivity(intent)
            finish()
        }, SPLASH_TIME_OUT)

    }

    companion object {
        const val SPLASH_TIME_OUT: Long = 3000 // Tundaan 3 detik
    }
}