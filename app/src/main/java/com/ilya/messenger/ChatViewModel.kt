package com.ilya.messenger

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.ValueEventListener
import com.google.firebase.database.ktx.database
import com.google.firebase.database.ktx.getValue
import com.google.firebase.ktx.Firebase

class ChatViewModel(private val currentUserId: String, private val otherUserId: String) : ViewModel() {

    private val database = Firebase.database("https://messenger-225a1-default-rtdb.europe-west1.firebasedatabase.app/")
    private val referenceUser = database.getReference("Users")
    private val referenceMessages = database.getReference("Messages")

    val messages = MutableLiveData<List<Message>>()
    val otherUser = MutableLiveData<User>()
    private val messageSent = MutableLiveData<Boolean>()
    private val error = MutableLiveData<String>()

    fun getMessagesLD(): LiveData<List<Message>> {
        return messages
    }

    fun getOtherUserLD(): LiveData<User> {
        return otherUser
    }

    fun getMessageSentLD(): LiveData<Boolean> {
        return messageSent
    }

    fun getErrorLD(): LiveData<String> {
        return error
    }

    init {
        referenceUser.child(otherUserId).addValueEventListener(object : ValueEventListener {
            override fun onDataChange(dataSnapshot: DataSnapshot) {
                    val user = dataSnapshot.getValue(User::class.java)
                    otherUser.value = user
            }

            override fun onCancelled(databaseError: DatabaseError) {
            }
        })

        referenceMessages.child(this.currentUserId).child(this.otherUserId).addValueEventListener(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                val messagesList = mutableListOf<Message>()
                for (postSnapshot in snapshot.children) {
                    val message = postSnapshot.getValue<Message>()
                    if (message != null) {
                        messagesList.add(message)
                    }
                }
                messages.value = messagesList
            }

            override fun onCancelled(error: DatabaseError) {
                TODO("Not yet implemented")
            }
        })
    }

    fun sendMessage(message: Message) {
        referenceMessages
            .child(message.userId)
            .child(message.receiverId)
            .push()
            .setValue(message)
            .addOnSuccessListener {
                referenceMessages
                    .child(message.receiverId)
                    .child(message.userId)
                    .push()
                    .setValue(message)
                    .addOnSuccessListener {
                        messageSent.value = true
                    }.addOnFailureListener {
                        error.value = it.message.toString()
                    }
            }
    }

    fun setUserOnline(isOnline: Boolean) {
        referenceUser.child(currentUserId).child("status").setValue(isOnline)
    }


}