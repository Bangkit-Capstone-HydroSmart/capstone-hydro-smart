package com.example.hydrosmart.auth.signup

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Patterns
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import com.basgeekball.awesomevalidation.AwesomeValidation
import com.basgeekball.awesomevalidation.ValidationStyle
import com.example.hydrosmart.R
import com.example.hydrosmart.data.pref.UserModel
import com.example.hydrosmart.databinding.ActivitySignupBinding
import com.example.hydrosmart.auth.login.LoginActivity
import com.example.hydrosmart.utils.LoadingDialog
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.FirebaseDatabase

class Signup : AppCompatActivity() {
    private lateinit var binding: ActivitySignupBinding
    private lateinit var auth: FirebaseAuth
    private lateinit var db: FirebaseDatabase
    private lateinit var showLoading: LoadingDialog

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySignupBinding.inflate(layoutInflater)
        setContentView(binding.root)

        auth = FirebaseAuth.getInstance()
        db = FirebaseDatabase.getInstance()
        showLoading = LoadingDialog(this)

        action()
        binding.tvLoginHere.setOnClickListener {
            startActivity(Intent(this, LoginActivity::class.java))
        }
    }

    private fun action() {
        val validation = AwesomeValidation(ValidationStyle.BASIC)
        validation.apply {
            addValidation(
                binding.emailEditText,
                Patterns.EMAIL_ADDRESS,
                getString(R.string.invalid_email_type))
            addValidation(
                binding.passwordEditText,
                ".{6,}",
                getString(R.string.valid_password))
            addValidation(
                binding.passwordConfirmEditText,
                binding.passwordEditText,
                getString(R.string.invalid_password_matches)
            )
        }

        binding.btSignup.setOnClickListener {
            val name = binding.nameEditText.text.toString()
            val email = binding.emailEditText.text.toString().trim()
            val pass = binding.passwordEditText.text.toString().trim()
            val passConfirm = binding.passwordConfirmEditText.text.toString()

            if (validation.validate()) {
                if (email.isNotEmpty() && pass.isNotEmpty() && passConfirm.isNotEmpty()) {
                    showLoading.startLoading()
                    auth.createUserWithEmailAndPassword(email, pass)
                        .addOnCompleteListener { result ->
                            if (result.isSuccessful) {
                                showLoading.dismissLoading()
                                val dbUser =
                                    auth.currentUser?.let { it1 ->
                                        db.reference.child("users").child(
                                            it1.uid)
                                    }
                                val user = UserModel(
                                    name = name,
                                    email = email
                                )
                                dbUser?.setValue(user)?.addOnCompleteListener {
                                    if (it.isSuccessful) {
                                        dialogAlert()
                                    } else {
                                        Toast.makeText(
                                            this,
                                            it.exception.toString(),
                                            Toast.LENGTH_SHORT
                                        ).show()
                                    }
                                }
                            } else {
                                showLoading.dismissLoading()
                                Toast.makeText(
                                    this,
                                    result.exception.toString(),
                                    Toast.LENGTH_SHORT).show()
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
                    Toast.LENGTH_SHORT
                ).show()
            }
        }
    }

    private fun dialogAlert() {
        AlertDialog.Builder(this@Signup).apply {
            setTitle(getString(R.string.title_alert_dialog))
            setMessage(getString(R.string.message_alert_dialog))
            setPositiveButton(getString(R.string.message_positive_button)) { _, _ ->
                val intent = Intent(this@Signup, LoginActivity::class.java)
                intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TASK or Intent.FLAG_ACTIVITY_NEW_TASK
                startActivity(intent)
                finish()
            }
            create()
            show()
        }
    }
}