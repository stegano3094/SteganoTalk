package com.stegano.steganotalk

import android.content.Context
import android.content.Intent
import android.net.ConnectivityManager
import android.os.Bundle
import android.os.Handler
import android.view.WindowManager
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.FirebaseApp
import com.google.firebase.appcheck.FirebaseAppCheck
import com.google.firebase.appcheck.safetynet.SafetyNetAppCheckProviderFactory


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

                    FirebaseApp.initializeApp( /*context=*/this)
                    val firebaseAppCheck = FirebaseAppCheck.getInstance()
                    firebaseAppCheck.installAppCheckProviderFactory(
                        SafetyNetAppCheckProviderFactory.getInstance()
                    )

                    Thread.sleep(300)  // 너무 빨라서 지연시킴
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