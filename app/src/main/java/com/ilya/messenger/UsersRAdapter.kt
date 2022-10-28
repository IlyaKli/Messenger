package com.ilya.messenger

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import kotlinx.android.synthetic.main.user_item.view.*

class UsersRAdapter : RecyclerView.Adapter<UsersRAdapter.ChatsViewHolder>() {

    private lateinit var setOnUserClickListener: OnUserClickListener

    fun setOnUserClickListener(onUserClickListener: OnUserClickListener) {
        this.setOnUserClickListener = onUserClickListener
    }

    private var users = mutableListOf<User>()

    fun setUsers(users: List<User>) {
        this.users.clear()
        this.users.addAll(users)
        notifyDataSetChanged()
    }

    class ChatsViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val textView = itemView.userTextView

        val statusView = itemView.statusImageView
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ChatsViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.user_item, parent, false)

        return ChatsViewHolder(view)
    }

    override fun onBindViewHolder(holder: ChatsViewHolder, position: Int) {
        val user = users[position]

        val userInfo = String.format("%s %s", user.name, user.lastName)

        holder.textView.text = userInfo

        val backgroundId = when (user.status) {
            true -> R.drawable.circle_green
            else -> R.drawable.circle_red
        }
        val background = ContextCompat.getDrawable(holder.itemView.context, backgroundId)
        holder.statusView.background = background

        holder.itemView.setOnClickListener {
            if (setOnUserClickListener != null) setOnUserClickListener.OnClick(user)
        }

        holder.itemView.setOnClickListener {
            setOnUserClickListener.OnClick(user)
        }
    }

    override fun getItemCount(): Int {
        return users.size
    }

    interface OnUserClickListener {
        fun OnClick(user: User)
    }
}