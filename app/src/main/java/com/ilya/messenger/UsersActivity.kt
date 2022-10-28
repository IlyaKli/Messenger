package com.ilya.messenger

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import kotlinx.android.synthetic.main.activity_users.*

class UsersActivity : AppCompatActivity() {

    private val usersActivityViewModel by lazy { ViewModelProvider(this)[UsersActivityViewModel::class.java] }
    private val usersRAdapter by lazy { UsersRAdapter() }
    private val currentUserId by lazy { intent.getStringExtra("user_id") }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_users)
        observeViewModel()

        usersRecyclerView.layoutManager = LinearLayoutManager(this)
        usersRecyclerView.adapter = usersRAdapter



            usersRAdapter.setOnUserClickListener(object : UsersRAdapter.OnUserClickListener{
            override fun OnClick(user: User) {
                chatsScreen(user.id.toString())
            }
        })
    }

    override fun onResume() {
        usersActivityViewModel.setUserOnline(true)
        super.onResume()
    }

    override fun onPause() {
        usersActivityViewModel.setUserOnline(false)
        super.onPause()
    }

    fun chatsScreen(otherUserId: String) {
        val intent = ChatActivity.newIntent(this, currentUserId.toString(), otherUserId)
        startActivity(intent)
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.menu_item, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        if (item.itemId == R.id.itemSignOut) {
            usersActivityViewModel.logout()
        }

        if (item.itemId == R.id.itemDelete) {
            usersActivityViewModel.deleteUser()
        }

        return super.onOptionsItemSelected(item)
    }

    private fun observeViewModel() {
        usersActivityViewModel.userLD.observe(this) {
            if (it == null) {
                val intent = SignInActivity.newIntent(this)
                startActivity(intent)
                finish()
            }
        }

        usersActivityViewModel.users.observe(this) {
            usersRAdapter.setUsers(usersActivityViewModel.getUsers())
        }
    }

    companion object {
        fun newIntent(context: Context, currentUserId: String): Intent {
            val intent = Intent(context, UsersActivity::class.java)
            intent.putExtra("user_id", currentUserId)
            return intent
        }
    }
}