package com.stegano.steganotalk

import android.os.Bundle
import android.text.TextUtils
import android.util.Log
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.*
import com.google.firebase.ktx.Firebase
import kotlinx.android.synthetic.main.activity_chatting.*
import java.text.SimpleDateFormat

data class FirebaseMessage(
    var name: String = "Anonymous",
    var message: String = "",
    var timeStamp: Long = 0L
)

class ChattingActivity : AppCompatActivity() {
    var TAG: String = "ChattingActivity"

    lateinit var userName: String
    lateinit var getRoomName: String

    private val chatMessageAdapter = ChatMessageAdapter()

    // firebase RealtimeDatabase
    //var ref = FirebaseDatabase.getInstance().getReference("getTitle")  // 키값으로 읽어옴

    private lateinit var auth: FirebaseAuth

    override fun onStart() {
        super.onStart()
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_chatting)

        userName = intent.getStringExtra("userName").toString()
        getRoomName = intent.getStringExtra("roomName").toString()
        supportActionBar?.title = getRoomName

        val linearLayoutManager = LinearLayoutManager(applicationContext, LinearLayoutManager.VERTICAL, false)
        recyclerView.layoutManager = linearLayoutManager
        chatMessageAdapter.myNickName = userName
        recyclerView.adapter = chatMessageAdapter

        // 리얼타임 데이터베이스 연동 방법 참고 : https://hamzzibari.tistory.com/58
        // 파이어베이스에서 가져온 데이터로 타이틀 변경 (최상단 데이터를 가져옴)
//        ref.addValueEventListener(object : ValueEventListener {
//            override fun onDataChange(dataSnapshot: DataSnapshot) {
//                // 데이터 변경 감지시 호출됨
//                // test 키를 가진 데이터 스냅샷에서 값을 읽고 문자열로 변경한다
//                userName = dataSnapshot.value.toString()
//                // 읽은 문자 로깅
//                Log.e(TAG, "onDataChange: " + userName)
//                // 파이어베이스에서 전달받은 메세지로 제목을 변경한다
//                supportActionBar?.title = userName
//            }
//
//            override fun onCancelled(error: DatabaseError) {
//                // 데이터 읽기가 취소된 경우
//                Log.e(TAG, "onCancelled")
//                error.toException().printStackTrace()
//            }
//        })
        // 데이터베이스에서 데이터 가져오기 ( 테스트용으로 String을 썼지만 RecyclerView를 사용하는 것이 좋음)
        FirebaseDatabase.getInstance().getReference(getRoomName).addChildEventListener(object : ChildEventListener {
            override fun onChildAdded(snapshot: DataSnapshot, previousChildName: String?) {
                snapshot.let {
                    val firebaseMessage = it.getValue(FirebaseMessage::class.java)
                    firebaseMessage?.let { it2 ->
                        Log.e(TAG, "onChildAdded: firebaseMessage : $it2")

                        // System.currentTimeMillis()으로 시간 가져올 경우 디바이스마다 시간이 상이함
                        if( (it2.timeStamp + 60000) >= System.currentTimeMillis() ) {  // 최근 1분 동안의 채팅만 보여줌
                            chatMessageAdapter.messageItems.add(MessageItem(it2.message, it2.name))
                            Log.e(TAG, "onChildAdded: 채팅 보여주기, ${getDateTime(it2.timeStamp)}")
                        }
                    }
                }
                chatMessageAdapter.notifyDataSetChanged()
                recyclerView.scrollToPosition(chatMessageAdapter.messageItems.size - 1)  // 리사이클러뷰의 스크롤을 맨 아래로 내림
            }

            override fun onChildChanged(snapshot: DataSnapshot, previousChildName: String?) {
                snapshot.let {
                    val firebaseMessage = it.getValue(FirebaseMessage::class.java)
                    firebaseMessage?.let {
                        Log.e(TAG, "onChildChanged: firebaseMessage : $it")
                    }
                }
            }

            override fun onChildRemoved(snapshot: DataSnapshot) {
                snapshot.let {
                    val firebaseMessage = it.getValue(FirebaseMessage::class.java)
                    firebaseMessage?.let {
                        Log.e(TAG, "onChildRemoved: firebaseMessage : $it")
                    }
                }
            }

            override fun onChildMoved(snapshot: DataSnapshot, previousChildName: String?) {
                snapshot.let {
                    val firebaseMessage = it.getValue(FirebaseMessage::class.java)
                    firebaseMessage?.let {
                        Log.e(TAG, "onChildMoved: firebaseMessage : $it")
                    }
                }
            }

            override fun onCancelled(error: DatabaseError) {
                // 데이터 읽기가 취소된 경우
                Log.e(TAG, "onCancelled")
                error.toException().printStackTrace()
            }
        })

        // 버튼 눌렀을 때 데이터베이스에 데이터 보내기
        sendButton.setOnClickListener {
            if(TextUtils.isEmpty(inputEditText.text)) {
                Toast.makeText(this, "메세지를 입력하세요.", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            // 이렇게 클래스를 만들어도 되고 HashMap을 만들어서 그 안에 값을 넣고 setValue에 넣어도 됨
            val firebaseMessage = FirebaseMessage()
            val newRef = FirebaseDatabase.getInstance().getReference(getRoomName).push()
            firebaseMessage.name = userName
            firebaseMessage.message = inputEditText.text.toString()
            firebaseMessage.timeStamp = System.currentTimeMillis()

            newRef.setValue(firebaseMessage)  // 이 한 줄이 파이어베이스에 데이터 쓰는 코드임
//            Toast.makeText(this, "메시지를 보냈습니다.", Toast.LENGTH_SHORT).show()
            inputEditText.text.clear()  // 메시지를 보낸 후 입력창 비우기
        }
    }

    fun getDateTime(milliTime: Long): String {
        return SimpleDateFormat("yyyy-MM-dd hh:mm:ss").format(milliTime)
    }
}