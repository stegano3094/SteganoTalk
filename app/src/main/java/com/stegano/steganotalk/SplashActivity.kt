package com.stegano.steganotalk

import android.content.Context
import android.content.Intent
import android.net.ConnectivityManager
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import android.view.WindowManager
import android.widget.Toast

class SplashActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_splash)

        supportActionBar?.hide()  // 액션바 숨김
        window.addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON)  // 화면꺼짐 방지

        // 핸들러를 이용한 방법
        Handler().postDelayed({
            var count = 1
            while(count <= 3) {  // N번 시도함
                count++
                if (networkCheck()) {  // 네트워크 연결되면 루프를 종료하고 startActivity 실행함
                    Thread.sleep(1000)  // 너무 빨라서 1초 지연시킴
                    startActivity(Intent(this, LoginActivity::class.java))
                    break
                } else {
                    Thread.sleep(2000)
                }
            }
            finish()
        }, 100)
    }

    private fun networkCheck() : Boolean {
        val connectivityManager = getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager

        if(connectivityManager.activeNetwork.toString() != "null") {
            return true
        }
        Toast.makeText(applicationContext, "인터넷에 연결되어 있지 않습니다.", Toast.LENGTH_SHORT).show()
        return false
    }
}