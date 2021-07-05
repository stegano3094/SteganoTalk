package com.stegano.steganotalk

import android.os.Bundle
import android.view.WindowManager
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.FirebaseDatabase
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity() {
    val TAG: String = "MainActivity"

    companion object {
        val tab1 = FirstFragment()  // 내 프로필
        val tab2 = SecondFragment()  // 채팅방 리스트
    }

    var firebaseAuth: FirebaseAuth? = null  // app login (Firebase Auth)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        window.addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON)  // 메인 화면에서는 화면 꺼짐 방지함

        val userName = intent.getStringExtra("userName").toString()
        supportActionBar?.title = userName

        firebaseAuth = FirebaseAuth.getInstance()  // 파이어베이스 인증 인스턴스
        val loginUser = firebaseAuth!!.currentUser

        // 바텀네비뷰에 데이터를 전달할 번들 생성
        val bundle = Bundle()
        bundle.putString("userEmail", loginUser?.email)
        bundle.putString("userUid", loginUser?.uid)
        tab1.arguments = bundle
        tab2.arguments = bundle

        // 디폴트로 FirstFragment 선택됨
        supportFragmentManager.beginTransaction().replace(R.id.frameLayout, tab1).commit()

        // 바텀네비뷰 리스너 설정
        bottomNavigationView.setOnNavigationItemSelectedListener {
            when(it.itemId) {
                R.id.tab1 -> {
                    tab1.arguments = bundle
                    supportFragmentManager.beginTransaction()
                        .replace(R.id.frameLayout, tab1)
                        .commit()
                }
                R.id.tab2 -> {
                    tab2.arguments = bundle
                    with(supportFragmentManager.beginTransaction()) {
                        replace(R.id.frameLayout, tab2)
                    }.commit()
                }
            }
            true
        }
    }
}