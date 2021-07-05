package com.stegano.steganotalk

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.snackbar.Snackbar
import kotlinx.android.synthetic.main.chat_room_list_item.view.*

data class RoomListItem(
    val roomName: String,
    val roomUid: String
)

class ItemViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
    fun setItem(item: RoomListItem) {  // 리사이클러뷰에 roomName으로 채팅방 이름만 세팅
        itemView.chat_room_textview.text = item.roomName
    }
}

class ChatRoomListAdapter : RecyclerView.Adapter<ItemViewHolder>() {
    var roomListData = ArrayList<RoomListItem>()
    lateinit var listener: OnRoomListClickListener

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ItemViewHolder {
        val inflater =  LayoutInflater.from(parent.context).inflate(R.layout.chat_room_list_item, parent, false)
        return ItemViewHolder(inflater)
    }

    override fun onBindViewHolder(holder: ItemViewHolder, position: Int) {
        holder.setItem( roomListData[position] )
        holder.itemView.setOnClickListener {
            listener.onRoomListClick(holder, holder.itemView, position)  // 리스너에 인터페이스를 붙여줌, SecondFragment에 확인
            //Snackbar.make(holder.itemView, "${roomListData[position]}", 1000).show()
        }
    }

    override fun getItemCount(): Int {
        return roomListData.size
    }
}

interface OnRoomListClickListener {
    fun onRoomListClick(holder: ItemViewHolder, view: View, position: Int)
}