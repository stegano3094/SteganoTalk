package com.stegano.steganotalk

import android.os.Bundle
import android.util.Log
import android.view.WindowManager
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener


class MainActivity : AppCompatActivity() {
    val TAG: String = "MainActivity"

    // firebase RealtimeDatabase
    var ref = FirebaseDatabase.getInstance().getReference("test")  // 키값으로 읽어옴

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val userName = intent.getStringExtra("userName")

        supportActionBar?.title = userName

        // 리얼타임 데이터베이스 연동 방법 : https://hamzzibari.tistory.com/58
        // 파이어베이스에서 가져온 데이터로 타이틀 변경
//        ref.addValueEventListener(object : ValueEventListener {
//            override fun onDataChange(dataSnapshot: DataSnapshot) {
//                // 데이터 변경 감지시 호출됨
//
//                // test 키를 가진 데이터 스냅샷에서 값을 읽고 문자열로 변경한다
//                val message = dataSnapshot.value.toString()
//                // 읽은 문자 로깅
//                Log.e(TAG, "onDataChange: " + message)
//                // 파이어베이스에서 전달받은 메세지로 제목을 변경한다
//                supportActionBar?.title = message
//            }
//
//            override fun onCancelled(error: DatabaseError) {
//                // 데이터 읽기가 취소된 경우
//                Log.e(TAG, "onCancelled")
//                error.toException().printStackTrace()
//            }
//        })
    }
}