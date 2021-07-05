package com.stegano.steganotalk

import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import android.widget.Toast
import androidx.recyclerview.widget.LinearLayoutManager
import kotlinx.android.synthetic.main.fragment_second.*

class SecondFragment : Fragment() {
    private val TAG = "SecondFragment"

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_second, container, false)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)

        val userEmail: String = arguments?.getString("userEmail") ?: "Anonymous"
        val userUid: String = arguments?.getString("userUid") ?: "Anonymous"
        Log.d(TAG, "onActivityCreated: userEmail : $userEmail, userUid : $userUid")

        val layoutManager = LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false)
        recyclerview_chat_room_list.layoutManager = layoutManager

        val chatRoomListAdapter = ChatRoomListAdapter()
        chatRoomListAdapter.roomListData.add(RoomListItem(userEmail, userUid))  // 자기 자신의 채팅방 생성, 채팅방에서 자신의 Uid로 채팅방 생성함 (userUid -> roomUid)
        for( i in 1..20) {  // 전체 채팅방의 Uid는 1~20 으로 지정됨
            chatRoomListAdapter.roomListData.add(RoomListItem("전체 채팅방 $i", "전체 채팅방 $i"))  // 기본적인 전체 채팅방 생성
        }
        recyclerview_chat_room_list.adapter = chatRoomListAdapter

        chatRoomListAdapter.listener = object: OnRoomListClickListener {  // 채팅방 클릭 시 리스너 이벤트 발생
            override fun onRoomListClick(holder: ItemViewHolder, view: View, position: Int) {
                val intentGoToChatRoom = Intent(context, ChattingActivity::class.java)
                intentGoToChatRoom.putExtra("roomName", chatRoomListAdapter.roomListData[position].roomName)
                    .putExtra("roomUid", chatRoomListAdapter.roomListData[position].roomUid)
                    .putExtra("userEmail", userEmail)

                startActivity(intentGoToChatRoom)  // 채팅 화면으로 이동
            }
        }
    }
}