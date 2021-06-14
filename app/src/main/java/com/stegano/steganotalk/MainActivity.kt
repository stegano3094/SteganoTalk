package com.stegano.steganotalk

import android.content.Context
import android.os.Bundle
import android.util.Log
import android.view.WindowManager
import android.view.inputmethod.InputMethodManager
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import kotlinx.android.synthetic.main.activity_main.*


class MainActivity : AppCompatActivity() {
    val TAG: String = "MainActivity"

    // firebase RealtimeDatabase
    var ref = FirebaseDatabase.getInstance().getReference("test")  // 키값으로 읽어옴

    // firebase login
    val LOGINK_REQUEST_CODE = 9001
    var firebaseAuth: FirebaseAuth? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        window.addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON)

        // 파이어베이스 로그인
        firebaseAuth = FirebaseAuth.getInstance()
        loginButton.setOnClickListener {
            val inputEmail = email.text.toString()
            val inputPassword = password.text.toString()
            firebaseAuth!!.signInWithEmailAndPassword(inputEmail, inputPassword).addOnCompleteListener(this) {
                if(it.isSuccessful) {
                    Toast.makeText(this, "성공", Toast.LENGTH_SHORT).show()
                } else {
                    Toast.makeText(this, "실패", Toast.LENGTH_SHORT).show()
                }
            }
        }

        // 파이어베이스 연동 방법 : https://hamzzibari.tistory.com/58
        // 파이어베이스에서 가져온 데이터로 타이틀 변경
        ref.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(dataSnapshot: DataSnapshot) {
                // 데이터 변경 감지시 호출됨

                // test 키를 가진 데이터 스냅샷에서 값을 읽고 문자열로 변경한다
                val message = dataSnapshot.value.toString()
                // 읽은 문자 로깅
                Log.e(TAG, "onDataChange: " + message)
                // 파이어베이스에서 전달받은 메세지로 제목을 변경한다
                supportActionBar?.title = message
            }

            override fun onCancelled(error: DatabaseError) {
                // 데이터 읽기가 취소된 경우
                Log.e(TAG, "onCancelled")
                error.toException().printStackTrace()
            }
        })

        // 확인을 누르면 키보드가 닫히도록 할 때 아래 코드를 넣어준다. Soft가 붙은 것은 가상 키보드임
        val imm = getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
        view.setOnClickListener {
            imm.hideSoftInputFromWindow(view.windowToken, 0)
        }
    }
}