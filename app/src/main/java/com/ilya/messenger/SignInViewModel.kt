package com.ilya.messenger

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase

class SignInViewModel : ViewModel() {

    private val auth by lazy { Firebase.auth }

    val error = MutableLiveData<String>()

    val user = MutableLiveData<FirebaseUser>()

    init {
        auth.addAuthStateListener {
            if (auth.currentUser != null) user.value = auth.currentUser
        }
    }


    fun login(email: String, password: String) {
        auth.signInWithEmailAndPassword(email, password).addOnFailureListener {
            error.value = it.message
        }
    }
}