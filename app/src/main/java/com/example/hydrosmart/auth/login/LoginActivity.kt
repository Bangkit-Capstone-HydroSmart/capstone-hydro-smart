package com.example.hydrosmart.auth.login

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.util.Patterns
import android.widget.Toast
import androidx.activity.viewModels
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.preferencesDataStore
import com.basgeekball.awesomevalidation.AwesomeValidation
import com.basgeekball.awesomevalidation.ValidationStyle
import com.example.hydrosmart.R
import com.example.hydrosmart.ViewModelFactory
import com.example.hydrosmart.afterlogin.MainActivityAfter
import com.example.hydrosmart.data.pref.UserModel
import com.example.hydrosmart.data.pref.UserPreference
import com.example.hydrosmart.databinding.ActivityLoginBinding
import com.example.hydrosmart.utils.LoadingDialog
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.FirebaseDatabase

private val Context.dataStore: DataStore<Preferences> by preferencesDataStore(name = "settings")

class LoginActivity : AppCompatActivity() {
    private lateinit var binding: ActivityLoginBinding
    private lateinit var auth: FirebaseAuth
    private lateinit var db: FirebaseDatabase
    private lateinit var showLoading: LoadingDialog
    private val loginViewModel by viewModels<LoginViewModel> {
        ViewModelFactory(UserPreference.getInstance(dataStore), this)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityLoginBinding.inflate(layoutInflater)
        setContentView(binding.root)

        auth = FirebaseAuth.getInstance()
        db = FirebaseDatabase.getInstance()
        showLoading = LoadingDialog(this)

        action()
    }

    private fun action() {
        val validation = AwesomeValidation(ValidationStyle.BASIC)
        validation.apply {
            addValidation(
                binding.emailEditText,
                Patterns.EMAIL_ADDRESS,
                getString(R.string.invalid_email_type)
            )
            addValidation(
                binding.passwordEditText,
                ".{6,}",
                getString(R.string.valid_password)
            )
        }

        binding.btLogin.setOnClickListener {
            val email = binding.emailEditText.text.toString()
            val pass = binding.passwordEditText.text.toString()
            if (validation.validate()) {
                if (email.isNotEmpty() && pass.isNotEmpty()) {
                    showLoading.startLoading()
                    auth.signInWithEmailAndPassword(email, pass)
                        .addOnCompleteListener { result ->
                            if (result.isSuccessful) {
                                showLoading.dismissLoading()
                                loginViewModel.saveUser(
                                    UserModel(
                                        email = email,
                                        isLogin = true
                                    )
                                )
                                dialogAlert()
                            } else {
                                showLoading.dismissLoading()
                                Toast.makeText(
                                    this,
                                    result.exception.toString(),
                                    Toast.LENGTH_SHORT
                                ).show()
                            }
                        }
                } else {
                    showLoading.dismissLoading()
                    Toast.makeText(
                        this,
                        getString(R.string.empty_field),
                        Toast.LENGTH_SHORT
                    ).show()
                }
            } else {
                Toast.makeText(
                    this,
                    getString(R.string.format_validation),
                    Toast.LENGTH_LONG
                ).show()
            }
        }
    }

    private fun dialogAlert() {
        AlertDialog.Builder(this).apply {
            setTitle(getString(R.string.title_alert_dialog))
            setMessage(getString(R.string.message_alert_dialog_login))
            setPositiveButton(getString(R.string.message_positive_button)) { _, _ ->
                val intent = Intent(this@LoginActivity, MainActivityAfter::class.java)
                intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TASK or Intent.FLAG_ACTIVITY_NEW_TASK
                startActivity(intent)
                finish()
            }
            create()
            show()
        }

    }
}