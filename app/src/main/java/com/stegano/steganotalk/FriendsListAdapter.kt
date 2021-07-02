package com.stegano.steganotalk

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.recyclerview.widget.RecyclerView
import kotlinx.android.synthetic.main.my_friend_item.view.*

data class MyFriendListData(
    val nickName: String = "Anonymous"
)

class FriendsListViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
    fun setFriend(friend: MyFriendListData) {
        itemView.my_friend_nick_name.text = friend.nickName
    }
}

class FriendsListAdapter : RecyclerView.Adapter<FriendsListViewHolder>() {
    var friends = ArrayList<MyFriendListData>()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): FriendsListViewHolder {
        val itemView = LayoutInflater.from(parent.context).inflate(R.layout.my_friend_item, parent, false)
        return FriendsListViewHolder(itemView)
    }

    override fun onBindViewHolder(holder: FriendsListViewHolder, position: Int) {
        val friend = friends[position]
        holder.setFriend(friend)
        holder.itemView.setOnClickListener {
            Toast.makeText(it.context, "${friend.nickName}", Toast.LENGTH_SHORT).show()
        }
    }

    override fun getItemCount(): Int {
        return friends.size
    }
}