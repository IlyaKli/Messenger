package com.ilya.messenger

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase
import kotlinx.android.synthetic.main.activity_register.*

class RegisterActivity : AppCompatActivity() {

    private val auth by lazy { Firebase.auth }
    private val currentUser by lazy { auth.currentUser }
    private val registerActivityViewModel by lazy { ViewModelProvider(this)[RegisterActivityViewModel::class.java] }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_register)
        clickListener()
        observerViewModel()
    }

    private fun clickListener() {
        registrationButton.setOnClickListener {
            val email = emailRegistrationEditText.text.toString().trim()
            val password = passwordRegistrationEditText.text.toString().trim()
            val name = nameRegistrationEditText.text.toString().trim()
            val surname = lastNameRegistrationEditText.text.toString().trim()

            if (email.isEmpty() || password.isEmpty() || name.isEmpty() || surname.isEmpty()) {
                Toast.makeText(this, "Пожалуйста, заполните все поля!", Toast.LENGTH_SHORT).show()
            } else {
                registerActivityViewModel.registration(email, password, name, surname)
            }
        }
    }

    private fun observerViewModel() {
        registerActivityViewModel.registrationUser.observe(this) {
            if (it != null) {
                Toast.makeText(
                    this,
                    "Пользователь зарегистрирован",
                    Toast.LENGTH_SHORT
                ).show()
                startActivity(UsersActivity.newIntent(this, currentUser!!.uid))
                finish()
            }
        }

        registerActivityViewModel.error.observe(this) {
            if (it != null) Toast.makeText(this, it, Toast.LENGTH_SHORT).show()
        }
    }

    companion object {
        fun newIntent(context: Context): Intent {
            return Intent(context, RegisterActivity::class.java)
        }
    }
}