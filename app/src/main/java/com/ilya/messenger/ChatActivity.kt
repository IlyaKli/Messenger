package com.ilya.messenger

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.Menu
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import kotlinx.android.synthetic.main.activity_chat.*

class ChatActivity : AppCompatActivity() {

    private val currentUserId by lazy { intent.getStringExtra("user_id").toString() }
    private val otherUserId by lazy { intent.getStringExtra("other_id").toString() }
    private val chatViewModelFactory by lazy { ChatViewModelFactory(currentUserId, otherUserId) }
    private val chatViewModel by lazy {
        ViewModelProvider(this, chatViewModelFactory)[ChatViewModel::class.java]
    }

    private val chatsRAdapter by lazy { ChatsRAdapter(currentUserId) }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_chat)

        chatsRecyclerView.adapter = chatsRAdapter
        chatsRecyclerView.layoutManager = LinearLayoutManager(this, RecyclerView.VERTICAL, false)

        observeViewModel()
        sendImageView.setOnClickListener {
            val message =
                Message(messageEditText.text.toString().trim(), currentUserId, otherUserId)
            chatViewModel.sendMessage(message)
        }
    }

    override fun onResume() {
        chatViewModel.setUserOnline(true)
        super.onResume()
    }

    override fun onPause() {
        chatViewModel.setUserOnline(false)
        super.onPause()
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.menu_item, menu)
        return true
    }

    private fun observeViewModel() {
        chatViewModel.getMessagesLD().observe(this) {
            chatsRAdapter.setMessages(it)
            if (it.size > 14) chatsRecyclerView.smoothScrollToPosition(it.size - 1)
        }

        chatViewModel.getMessageSentLD().observe(this) {
            messageEditText.text.clear()
        }

        chatViewModel.getErrorLD().observe(this) {
            if (it != null) {
                Toast.makeText(this, it, Toast.LENGTH_SHORT).show()
            }
        }

        chatViewModel.getOtherUserLD().observe(this) {
            val userInfo = String.format("%s %s", it.name, it.lastName)
            nameUserTextView.text = userInfo
            val backgroundId = when (it.status) {
                true -> R.drawable.circle_green
                else -> R.drawable.circle_red
            }
            val background = ContextCompat.getDrawable(this, backgroundId)
            statusChatImageView.background = background
        }
    }

    companion object {
        fun newIntent(context: Context, currentUserId: String, otherUserId: String): Intent {
            val intent = Intent(context, ChatActivity::class.java)
            intent.putExtra("user_id", currentUserId)
            intent.putExtra("other_id", otherUserId)
            return intent
        }
    }
}