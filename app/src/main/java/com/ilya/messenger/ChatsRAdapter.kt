package com.ilya.messenger

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import kotlinx.android.synthetic.main.user_massage_item.view.*

class ChatsRAdapter(private var currentUserId: String) : RecyclerView.Adapter<ChatsRAdapter.ChatsViewHolder>() {

    private val VIEW_TYPE_MY_MESSAGE = 100
    private val VIEW_TYPE_OTHER_MESSAGE = 101

    private var messages = mutableListOf<Message>()

    @JvmName("setMessages1")
    fun setMessages(messages: List<Message>) {
        this.messages = messages.toMutableList()
        notifyDataSetChanged()
    }

    class ChatsViewHolder(val itemView: View) : RecyclerView.ViewHolder(itemView) {
        val messageTextView = itemView.messageTextView
    }

    override fun getItemViewType(position: Int): Int {
        val message = messages[position]
        if (message.userId.equals(currentUserId)) return VIEW_TYPE_MY_MESSAGE
        else return VIEW_TYPE_OTHER_MESSAGE
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ChatsViewHolder {
        val layoutResId = when(viewType) {
            100 -> R.layout.user_massage_item
            else -> R.layout.other_massage_item
        }
        val view = LayoutInflater.from(parent.context).inflate(layoutResId, parent, false)
        return ChatsViewHolder(view)
    }

    override fun onBindViewHolder(holder: ChatsViewHolder, position: Int) {
        val message = messages[position]

        holder.messageTextView.text = message.text
    }

    override fun getItemCount(): Int {
        return messages.size
    }
}