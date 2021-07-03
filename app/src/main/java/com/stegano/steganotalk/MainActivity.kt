package com.stegano.steganotalk

import android.os.Bundle
import android.view.WindowManager
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.database.FirebaseDatabase
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity() {
    val TAG: String = "MainActivity"

    companion object {
        val tab1 = FirstFragment()  // 내 프로필
        val tab2 = SecondFragment()  // 채팅방 리스트
        val tab3 = ThirdFragment()  // 설정 화면
    }

    private var userName: String = ""

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        window.addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON)  // 메인 화면에서는 화면 꺼짐 방지함

        userName = intent.getStringExtra("userName").toString()
        supportActionBar?.title = userName

        // 바텀네비뷰에 데이터를 전달할 번들 생성
        val bundle = Bundle()
        bundle.putString("userName", userName)
        bundle.putString("userName", userName)
        tab1.arguments = bundle

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
                R.id.tab3 -> {
                    with(supportFragmentManager.beginTransaction()) {
                        replace(R.id.frameLayout, tab3)
                    }.commit()
                }
            }
            true
        }
    }
}