package com.stegano.steganotalk

import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import android.widget.Toast
import androidx.recyclerview.widget.LinearLayoutManager
import kotlinx.android.synthetic.main.fragment_second.*

class SecondFragment : Fragment() {
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_second, container, false)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)

        val userName: String = arguments?.getString("userName") ?: "Anonymous"

        val layoutManager = LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false)
        recyclerview_chat_room_list.layoutManager = layoutManager

        val roomListAdapter = ChatRoomListAdapter()
        roomListAdapter.roomListData.add(RoomListItem("1번 방"))  // 전체 채팅방 2개 생성됌
        roomListAdapter.roomListData.add(RoomListItem("2번 방"))
        recyclerview_chat_room_list.adapter = roomListAdapter

        roomListAdapter.listener = object: OnRoomListClickListener {
            override fun onRoomListClick(holder: ItemViewHolder, view: View, position: Int) {
                val intentGoToChatRoom = Intent(context, ChattingActivity::class.java)
                intentGoToChatRoom.putExtra("roomName", roomListAdapter.roomListData[position].roomName)
                    .putExtra("userName", userName)
                startActivity(intentGoToChatRoom)
            }
        }
    }
}