package com.stegano.steganotalk

import android.app.AlertDialog
import android.content.DialogInterface
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.firebase.auth.FirebaseAuth
import kotlinx.android.synthetic.main.fragment_first.*

class FirstFragment : Fragment() {
    companion object {
        val TAG: String = "FirstFragment"
    }

    lateinit var firebaseAuth: FirebaseAuth

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?): View?
    {
        return inflater.inflate(R.layout.fragment_first, container, false)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)

        val userEmail = arguments?.getString("userEmail") ?: "Anonymous"
        val userUid = arguments?.getString("userUid") ?: "Anonymous"
        Log.e(TAG, "onCreateView: userEmail : $userEmail, userUid : $userUid")

        firebaseAuth = FirebaseAuth.getInstance()

        SetProfile()  // 내 프로필 정보를 보여줌

        // 친구 목록을 리사이클러뷰로 보여줌
        val layoutManager = LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false)
        my_friend_recycler_view.layoutManager = layoutManager
        val friendsListAdapter = FriendsListAdapter()
        friendsListAdapter.friends.add(MyFriendListData(userEmail, userUid))  // 자기자신 추가
//        for(i in 1..20) {  // 테스트용으로 20명의 동일한 친구를 미리 생성함
//            friendsListAdapter.friends.add(MyFriendListData("test@naver.com"))
//        }
        my_friend_recycler_view.adapter = friendsListAdapter

        // 로그아웃 버튼 클릭 시
        signOutButton.setOnClickListener {
            firebaseAuth.signOut()
            Log.d(TAG, "onActivityCreated: signOutButton Clicked")

            val intentGoToLoginActivity = Intent(context, LoginActivity::class.java)
            startActivity(intentGoToLoginActivity)
            activity?.finish()
        }

        // 탈퇴하기 버튼 클릭 시
        deleteButton.setOnClickListener {
            val user = firebaseAuth.currentUser!!
            Log.d(TAG, "email : ${user.email}, uid : ${user.uid}")

            // 탈퇴 시 다이얼로그로 한번 더 확인시킴
            val dialog = AlertDialog.Builder(context)
            dialog.setTitle("탈퇴하시겠습니까?")
                .setMessage("회원탈퇴 시 즉시 탈퇴됩니다.")
                .setPositiveButton("네", object:DialogInterface.OnClickListener{
                    override fun onClick(dialog: DialogInterface?, which: Int) {
                        Log.d(TAG, "onClick: PositiveButton 클릭됨")
                        dialog?.dismiss()
                        user.delete()
                            .addOnCompleteListener { task ->
                                if (task.isSuccessful) {
                                    Log.d(TAG, "탈퇴되었습니다")
                                    Toast.makeText(context, "탈퇴되었습니다.", Toast.LENGTH_SHORT).show()
                                    startActivity(Intent(activity?.applicationContext, LoginActivity::class.java))
                                    activity?.finish()
                                } else {
                                    Log.d(TAG, "다시 시도해주세요 message: ${task.exception?.message.toString()}.")
                                    Toast.makeText(context, "다시 로그인 후 시도해주세요.", Toast.LENGTH_SHORT).show()
                                }
                            }
                    }
                })
                .setNegativeButton("아니요", object:DialogInterface.OnClickListener{
                    override fun onClick(dialog: DialogInterface?, which: Int) {
                        Log.d(TAG, "onClick: NegativeButton 클릭됨")
                        dialog?.dismiss()
                    }
                })
            dialog.show()

        }
    }

    private fun SetProfile() {  // 내 프로필 세팅
        val user = firebaseAuth.currentUser!!
        val userEmail = user.email
        val userUid = user.uid
        textViewUserEmail.text = userEmail
        //textViewUserUid.text = userUid
        Log.d(TAG, "onActivityCreated: userEmail: $userEmail, userUid: $userUid")
    }
}