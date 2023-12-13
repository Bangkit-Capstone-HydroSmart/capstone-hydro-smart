package com.example.hydrosmart.beforelogin

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.example.hydrosmart.R
import com.example.hydrosmart.auth.signup.Signup
import com.example.hydrosmart.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.btWelcome.setOnClickListener {
            startActivity(Intent(this, Signup::class.java))
        }
    }
}