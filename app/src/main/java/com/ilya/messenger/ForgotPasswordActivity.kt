package com.ilya.messenger

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import kotlinx.android.synthetic.main.activity_forgot_password.*

class ForgotPasswordActivity : AppCompatActivity() {

    private val forgotPasswordViewModel by lazy { ViewModelProvider(this)[ForgotPasswordViewModel::class.java] }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_forgot_password)

        val email = intent.getStringExtra("email")

        emailForgotEditText.setText(email)

        clickListener()
        observerViewModel()
    }

    private fun clickListener() {
        forgotButton.setOnClickListener {
            val email = emailForgotEditText.text.toString()

            if (email.isEmpty()) {
                Toast.makeText(this, "Пожалуйста, введите email!", Toast.LENGTH_SHORT).show()
            } else {
                forgotPasswordViewModel.forgotPassword(email)
            }
        }
    }

    private fun observerViewModel() {
        forgotPasswordViewModel.forgot.observe(this) {
            if (it) {
                Toast.makeText(
                    this,
                    "Письмо для сброса пароля отправлено на ваш email",
                    Toast.LENGTH_SHORT
                ).show()
                finish()
            }
        }

        forgotPasswordViewModel.error.observe(this) {
            if (it != null) {
                Toast.makeText(this, it, Toast.LENGTH_SHORT).show()
            }
        }
    }

    companion object {
        fun newIntent(context: Context, email: String): Intent {
            val intent = Intent(context, ForgotPasswordActivity::class.java)
            intent.putExtra("email", email)
            return intent
        }
    }
}