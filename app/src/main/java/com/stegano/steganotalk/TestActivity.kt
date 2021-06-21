package com.stegano.steganotalk

import android.os.Bundle
import android.text.TextUtils
import android.util.Log
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.database.*
import io.realm.*
import kotlinx.android.synthetic.main.activity_test.*

data class Post(var name:String = "anonymous", var message: String = "")

class TestActivity : AppCompatActivity() {
    var TAG: String = "TestActivity"
    lateinit var userName: String
    // firebase RealtimeDatabase
    var ref = FirebaseDatabase.getInstance().getReference("getTitle")  // 키값으로 읽어옴

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_test)

        // 리얼타임 데이터베이스 연동 방법 : https://hamzzibari.tistory.com/58
        // 파이어베이스에서 가져온 데이터로 타이틀 변경 (최상단 데이터를 가져옴)
        ref.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(dataSnapshot: DataSnapshot) {
                // 데이터 변경 감지시 호출됨

                // test 키를 가진 데이터 스냅샷에서 값을 읽고 문자열로 변경한다
                userName = dataSnapshot.value.toString()
                // 읽은 문자 로깅m
                Log.e(TAG, "onDataChange: " + userName)
                // 파이어베이스에서 전달받은 메세지로 제목을 변경한다
                supportActionBar?.title = userName
            }

            override fun onCancelled(error: DatabaseError) {
                // 데이터 읽기가 취소된 경우
                Log.e(TAG, "onCancelled")
                error.toException().printStackTrace()
            }
        })

        // 버튼 눌렀을 때 데이터베이스에 데이터 보내기
        button.setOnClickListener {
            if(TextUtils.isEmpty(edittext.text)) {
                Toast.makeText(this, "메세지를 입력하세요.", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            // 이렇게 클래스를 만들어도 되고 HashMap을 만들어서 그 안에 값을 넣고 setValue에 넣어도 됨
            val post = Post()
            val newRef = FirebaseDatabase.getInstance().getReference("room1").push()
            post.name = userName
            post.message = edittext.text.toString()
            newRef.setValue(post)
            Toast.makeText(this, "DB에 전달했습니다.", Toast.LENGTH_SHORT).show()
        }

        // 데이터베이스에서 데이터 가져오기
        FirebaseDatabase.getInstance().getReference("room1").addChildEventListener(object : ChildEventListener {
            override fun onChildAdded(snapshot: DataSnapshot, previousChildName: String?) {
                snapshot.let {
                    val post = it.getValue(Post::class.java)

                    post?.let {
                        Log.e(TAG, "onChildAdded: post : $it")
                        resultData.text = "name : ${it.name}, message : ${it.message} \n"
                    }
                }
            }

            override fun onChildChanged(snapshot: DataSnapshot, previousChildName: String?) {
                snapshot.let {
                    val post = it.getValue(Post::class.java)
                    post?.let {
                        Log.e(TAG, "onChildChanged: post : $it")
                        resultData.text = "name : ${it.name}, message : ${it.message} \n"
                    }
                }
            }

            override fun onChildRemoved(snapshot: DataSnapshot) {
                snapshot.let {
                    val post = it.getValue(Post::class.java)
                    post?.let {
                        Log.e(TAG, "onChildRemoved: post : $it")
                        resultData.text = "name : ${it.name}, message : ${it.message} \n"
                    }
                }
            }

            override fun onChildMoved(snapshot: DataSnapshot, previousChildName: String?) {
                snapshot.let {
                    val post = it.getValue(Post::class.java)
                    post?.let {
                        Log.e(TAG, "onChildMoved: post : $it")
                        resultData.text = "name : ${it.name}, message : ${it.message} \n"
                    }
                }
            }

            override fun onCancelled(error: DatabaseError) {
                // 데이터 읽기가 취소된 경우
                Log.e(TAG, "onCancelled")
                error.toException().printStackTrace()
            }
        })
    }
}