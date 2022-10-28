package com.ilya.messenger

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.auth.ktx.auth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.ValueEventListener
import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase

class UsersActivityViewModel : ViewModel() {

    val auth by lazy { Firebase.auth }
    private val database by lazy { Firebase.database("https://messenger-225a1-default-rtdb.europe-west1.firebasedatabase.app/") }
    private val databaseReference = database.getReference("Users")

    val userLD = MutableLiveData<FirebaseUser>()
    val users = MutableLiveData<MutableList<User>>()

    fun getUsers(): List<User> {
        return users.value!!.toList()
    }

    init {
        auth.addAuthStateListener {
            userLD.value = auth.currentUser
        }

        databaseReference.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(dataSnapshot: DataSnapshot) {
                val usersList = mutableListOf<User>()
                for (postSnapshot in dataSnapshot.children) {
                    val user = postSnapshot.getValue(User::class.java)
                    if (user == null) {
                        return
                    }
                    else if (!user.id.equals(auth.currentUser?.uid)) {
                        usersList.add(user)
                    }
                }
                users.value = usersList
            }

            override fun onCancelled(databaseError: DatabaseError) {
            }
        })
    }

    fun logout() {
        setUserOnline(false)
        auth.signOut()
    }

    fun deleteUser() {
        databaseReference.child(auth.currentUser!!.uid).removeValue()
        auth.currentUser!!.delete()
        auth.signOut()
    }

    fun setUserOnline(isOnline: Boolean) {
        if (auth.currentUser == null) return
        databaseReference.child(auth.currentUser!!.uid).child("status").setValue(isOnline)
    }
}