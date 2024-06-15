package sszj.s.geminiapi.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import sszj.s.geminiapi.R
import sszj.s.geminiapi.data.model.Chat

class ChatAdapter : RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    private var chatList: List<Chat> = emptyList()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return when (viewType) {
            VIEW_TYPE_USER -> {
                val view = LayoutInflater.from(parent.context)
                    .inflate(R.layout.item_user_chat, parent, false)
                UserChatViewHolder(view)
            }

            VIEW_TYPE_MODEL -> {
                val view = LayoutInflater.from(parent.context)
                    .inflate(R.layout.item_model_chat, parent, false)
                ModelChatViewHolder(view)
            }

            else -> throw IllegalArgumentException("Invalid view type")
        }
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        val chatItem = chatList[position]
        when (holder.itemViewType) {
            VIEW_TYPE_USER -> {
                val userViewHolder = holder as UserChatViewHolder
                userViewHolder.bind(chatItem)
            }

            VIEW_TYPE_MODEL -> {
                val modelViewHolder = holder as ModelChatViewHolder
                modelViewHolder.bind(chatItem)
            }
        }
    }

    override fun getItemCount(): Int {
        return chatList.size
    }

    override fun getItemViewType(position: Int): Int {
        return if (chatList[position].isUser) VIEW_TYPE_USER else VIEW_TYPE_MODEL
    }

    fun submitList(newList: List<Chat>) {
        chatList = newList
        notifyDataSetChanged()
    }

    companion object {
        private const val VIEW_TYPE_USER = 1
        private const val VIEW_TYPE_MODEL = 2
    }

    class UserChatViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        private val chatText: TextView = itemView.findViewById(R.id.chat_text)
        private val chatImage: ImageView = itemView.findViewById(R.id.chat_image)

        fun bind(chatItem: Chat) {
            chatText.text = chatItem.prompt
            if (chatItem.imageBitmap != null) {
                chatImage.setImageBitmap(chatItem.imageBitmap)
                chatImage.visibility = View.VISIBLE
            } else {
                chatImage.visibility = View.GONE
            }
        }
    }

    class ModelChatViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        private val responseText: TextView = itemView.findViewById(R.id.response_text)

        fun bind(chatItem: Chat) {
            responseText.text = chatItem.prompt
        }
    }
}
