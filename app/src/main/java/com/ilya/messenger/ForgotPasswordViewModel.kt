package com.ilya.messenger

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase

class ForgotPasswordViewModel : ViewModel() {
    private val auth by lazy { Firebase.auth }

    val error = MutableLiveData<String>()

    val forgot = MutableLiveData(false)

    fun forgotPassword(email: String) {
        auth.sendPasswordResetEmail(email).addOnSuccessListener {
            forgot.value = true
        }.addOnFailureListener {
            error.value = it.message
        }
    }
}