package com.ilya.messenger

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.auth.ktx.auth
import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase

class RegisterActivityViewModel : ViewModel() {

    private val auth by lazy { Firebase.auth }
    private val database by lazy { Firebase.database("https://messenger-225a1-default-rtdb.europe-west1.firebasedatabase.app/") }
    private val databaseReference = database.getReference("Users")


    val error = MutableLiveData<String>()

    val registrationUser = MutableLiveData<FirebaseUser>()

    init {
        auth.addAuthStateListener {
            if (auth.currentUser != null) registrationUser.value = auth.currentUser
        }
    }


    fun registration(email: String, password: String, name: String, surname: String) {
        auth.createUserWithEmailAndPassword(email, password).addOnSuccessListener {
            val user = User(auth.uid.toString(), name, surname, false)
            databaseReference.child(user.id.toString()).setValue(user)
        }.addOnFailureListener {
            error.value = it.message
        }
    }
}