package com.stegano.steganotalk

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import kotlinx.android.synthetic.main.fragment_first.*
import kotlinx.android.synthetic.main.my_profile.*

class FirstFragment : Fragment() {
    companion object {
        val TAG: String = "FirstFragment"
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?): View?
    {
        return inflater.inflate(R.layout.fragment_first, container, false)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)

        val resultData = arguments?.getString("userName") ?: "Anonymous"
        my_id.text = resultData
        Log.e(TAG, "onCreateView: resultData : $resultData")

        val layoutManager = LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false)
        my_friend_recycler_view.layoutManager = layoutManager
        val friendsListAdapter = FriendsListAdapter()
        friendsListAdapter.friends.add(MyFriendListData("내 친구1"))
        friendsListAdapter.friends.add(MyFriendListData("내 친구2"))
        my_friend_recycler_view.adapter = friendsListAdapter
    }
}