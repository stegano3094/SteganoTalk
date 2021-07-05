package com.stegano.steganotalk

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import com.google.firebase.auth.FirebaseAuth
import kotlinx.android.synthetic.main.activity_password_reset.*

class PasswordResetActivity : AppCompatActivity() {
    val TAG = "PasswordResetActivity"
    var firebaseAuth: FirebaseAuth? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_password_reset)

        firebaseAuth = FirebaseAuth.getInstance()

        // 비밀번호 재설정 버튼 클릭
        passwordResetButton.setOnClickListener {
            val emailAddress = reset_email_input.text.toString()

            // 이메일 유효성 검사 코드
            // https://chicken-salad-sandwich.tistory.com/5
            val pattern = Regex("^[0-9a-zA-Z]([-_.]?[0-9a-zA-Z])*@[0-9a-zA-Z]([-_.]?[0-9a-zA-Z])*\\.[a-zA-Z]{2,3}")
            if(emailAddress.matches(pattern)) {
                Log.d(TAG, "createId: matched email")
            } else {
                Toast.makeText(applicationContext, "이메일 형식이 정확하지 않습니다.", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            firebaseAuth!!.sendPasswordResetEmail(emailAddress)
                .addOnCompleteListener { task ->
                    if (task.isSuccessful) {
                        Log.d(TAG, "passwordResetButton(): Email sent")
                        Toast.makeText(applicationContext, "해당 이메일로 비밀번호 재설정 메일을 보냈습니다.", Toast.LENGTH_SHORT).show()
                        finish()
                    } else {
                        Log.d(TAG, "passwordResetButton(): Email sent failed")
                        Toast.makeText(applicationContext, "해당 이메일이 정확한지 확인해주세요.", Toast.LENGTH_SHORT).show()
                    }
                }
        }
    }
}