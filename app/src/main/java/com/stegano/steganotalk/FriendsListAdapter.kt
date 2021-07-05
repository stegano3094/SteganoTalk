package com.stegano.steganotalk

import android.app.AlertDialog
import android.content.Context
import android.content.DialogInterface
import android.content.Intent
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import kotlinx.android.synthetic.main.my_friend_item.view.*

data class MyFriendListData(
    val friendEmail: String,
    val friendUid: String
)

class FriendsListViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
    fun setFriend(friend: MyFriendListData) {
        itemView.my_friend_nick_name.text = friend.friendEmail
    }
}

class FriendsListAdapter : RecyclerView.Adapter<FriendsListViewHolder>() {
    var friends = ArrayList<MyFriendListData>()
    var context: Context? = null

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): FriendsListViewHolder {
        context = parent.context
        val itemView = LayoutInflater.from(parent.context).inflate(R.layout.my_friend_item, parent, false)
        return FriendsListViewHolder(itemView)
    }

    override fun onBindViewHolder(holder: FriendsListViewHolder, position: Int) {
        val friend = friends[position]
        holder.setFriend(friend)
        holder.itemView.setOnClickListener {
            // 아이템 클릭 시 알림창 열림 -> 1:1 채팅창 생성
            val dialog = AlertDialog.Builder(context)
            dialog.setTitle("채팅하기")
                .setMessage("${friends[position].friendEmail}님과 채팅하시겠습니까?")
                .setPositiveButton("네", object:DialogInterface.OnClickListener{
                    override fun onClick(dialog: DialogInterface?, which: Int) {
                        val intentGoToChatRoom = Intent(context, ChattingActivity::class.java)
                        intentGoToChatRoom
                            .putExtra("userEmail", friend.friendEmail)
                            .putExtra("roomName", friend.friendEmail)
                            .putExtra("roomUid", friend.friendUid)

                        context!!.startActivity(intentGoToChatRoom)
                        Log.d("FriendsListAdapter", "onBindViewHolder, onClick, userEmail : ${friend.friendEmail}, roomName : ${friend.friendEmail}, roomUid : ${friend.friendUid}")
                    }
                })
                .setNegativeButton("아니요", object:DialogInterface.OnClickListener{
                    override fun onClick(dialog: DialogInterface?, which: Int) {
                        dialog?.dismiss()
                    }
                })
            dialog.show()
        }
    }

    override fun getItemCount(): Int {
        return friends.size
    }
}