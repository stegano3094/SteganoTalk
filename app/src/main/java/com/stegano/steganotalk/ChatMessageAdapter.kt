package com.stegano.steganotalk

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import kotlinx.android.synthetic.main.chat_item_yourchat.view.*

data class MessageItem(
    val message: String,
    val nickName: String = "Anonymous"
)

class MessageViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
    fun setItem(item: MessageItem) {
        itemView.userNickName.text = item.nickName
        itemView.userMessage.text = item.message
    }
}

class ChatMessageAdapter : RecyclerView.Adapter<MessageViewHolder>() {
    var messageItems = ArrayList<MessageItem>()
    var myNickName = ""

    override fun getItemViewType(position: Int): Int {
//        return super.getItemViewType(position)
        if(messageItems[position].nickName == myNickName) {  // 내 닉네임과 같을 때 0을 반환함
            return 0
        }
        return 1
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MessageViewHolder {
        val inflate = if(viewType == 0) {  // 뷰타입이 0으로 오면 내 닉네임과 같으므로 오른쪽에 메시지를 배치함
            LayoutInflater.from(parent.context).inflate(R.layout.chat_item_mychat, parent, false)
        } else {
            LayoutInflater.from(parent.context).inflate(R.layout.chat_item_yourchat, parent, false)
        }
        return MessageViewHolder(inflate)
    }

    override fun onBindViewHolder(holder: MessageViewHolder, position: Int) {
        holder.setItem( messageItems[position])
    }

    override fun getItemCount(): Int {
        return messageItems.size
    }
}