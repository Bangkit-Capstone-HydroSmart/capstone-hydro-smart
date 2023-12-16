package com.example.hydrosmart.beforelogin

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.example.hydrosmart.auth.login.LoginActivity
import com.example.hydrosmart.auth.signup.Signup
import com.example.hydrosmart.databinding.ActivityWelcomeBinding

class Welcome : AppCompatActivity() {
    private lateinit var binding: ActivityWelcomeBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityWelcomeBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setUpAction()
    }

    private fun setUpAction() {
        binding.apply {
            btLogin.setOnClickListener {
                startActivity(Intent(this@Welcome, LoginActivity::class.java))
            }

            btSignup.setOnClickListener {
                startActivity(Intent(this@Welcome, Signup::class.java))
            }
        }
    }
}