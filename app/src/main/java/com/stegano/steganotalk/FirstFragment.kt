package com.stegano.steganotalk

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.firebase.auth.FirebaseAuth
import kotlinx.android.synthetic.main.fragment_first.*

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

    lateinit var firebaseAuth: FirebaseAuth

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)

        val resultData = arguments?.getString("userName") ?: "Anonymous"
        Log.e(TAG, "onCreateView: resultData : $resultData")

        val layoutManager = LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false)
        my_friend_recycler_view.layoutManager = layoutManager
        val friendsListAdapter = FriendsListAdapter()
        friendsListAdapter.friends.add(MyFriendListData("test@naver.com"))
        friendsListAdapter.friends.add(MyFriendListData("ste@naver.com"))
        friendsListAdapter.friends.add(MyFriendListData())
        my_friend_recycler_view.adapter = friendsListAdapter

        firebaseAuth = FirebaseAuth.getInstance()

        SetProfile()

        // 로그아웃 버튼 클릭 시
        signOutButton.setOnClickListener {
            firebaseAuth.signOut()
            Log.d(TAG, "onActivityCreated: signOutButton Clicked")
            val intentGoToLoginActivity = Intent(context, LoginActivity::class.java)
            startActivity(intentGoToLoginActivity)
            activity?.finish()
        }
    }

    private fun SetProfile() {  // 내 프로필 세팅
        val userId = MyProfileData().myEmail
        val userUid = MyProfileData().myUid
        textViewUserId.text = userId
        textViewUserUid.text = userUid
        Log.d(TAG, "onActivityCreated: userId: $userId, userUid: $userUid")
    }
}