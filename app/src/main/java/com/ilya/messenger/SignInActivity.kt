package com.ilya.messenger

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase
import kotlinx.android.synthetic.main.activity_sign_in.*

class SignInActivity : AppCompatActivity() {

    private val auth by lazy { Firebase.auth }

    private val currentUser by lazy { auth.currentUser }

    private val signInViewModel by lazy { ViewModelProvider(this)[SignInViewModel::class.java] }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_sign_in)

        clickListener()
        observer()
    }

    private fun observer() {
        signInViewModel.user.observe(this) {
            if (it != null) {
                val intent = UsersActivity.newIntent(this, currentUser!!.uid)
                startActivity(intent)
                finish()
            }
        }

        signInViewModel.error.observe(this) {
            if (it != null) Toast.makeText(this, it, Toast.LENGTH_SHORT).show()
        }
    }

    private fun clickListener() {
        signInButton.setOnClickListener {
            val email = emailEditText.text.toString().trim()
            val password = passwordEditText.text.toString().trim()

            if (email.isEmpty() || password.isEmpty()) {
                Toast.makeText(
                    this,
                    "Пожалуйста, заполните все поля!",
                    Toast.LENGTH_SHORT
                ).show()
            } else {
                signInViewModel.login(email, password)
            }
        }

        registerTextView.setOnClickListener {
            val intent = RegisterActivity.newIntent(this)
            startActivity(intent)
        }

        forgotPasswordTextView.setOnClickListener {
            val intent = ForgotPasswordActivity.newIntent(
                this,
                emailEditText.text.toString().trim()
            )
            startActivity(intent)
        }
    }

    companion object {
        fun newIntent(context: Context): Intent {
            return Intent(context, SignInActivity::class.java)
        }
    }
}