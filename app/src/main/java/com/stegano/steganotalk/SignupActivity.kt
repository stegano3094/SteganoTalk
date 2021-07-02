package com.stegano.steganotalk

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import com.google.firebase.auth.FirebaseAuth
import kotlinx.android.synthetic.main.activity_login.*
import kotlinx.android.synthetic.main.activity_signup.*
import kotlinx.android.synthetic.main.activity_signup.email_input
import kotlinx.android.synthetic.main.activity_signup.password_input

class SignupActivity : AppCompatActivity() {
    val TAG: String = "SignupActivity"

    // firebase login
    var firebaseAuth: FirebaseAuth? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_signup)

        sendSignupButton.setOnClickListener {
            createId()
        }
    }

    private fun createId() {
        val inputEmail = email_input.text.toString()
        val inputPassword = password_input.text.toString()

        if(inputEmail.isEmpty()) {
            Toast.makeText(this, "이메일을 입력해주세요.", Toast.LENGTH_SHORT).show()
            return
        }
        if(inputPassword.isEmpty()) {
            Toast.makeText(this, "비밀번호를 입력해주세요.", Toast.LENGTH_SHORT).show()
            return
        }

        firebaseAuth!!.createUserWithEmailAndPassword(inputEmail, inputPassword).addOnCompleteListener(this) {
            if(it.isSuccessful) {
                val user = firebaseAuth?.currentUser
                Toast.makeText(applicationContext, "회원가입 성공, $user", Toast.LENGTH_SHORT).show()
                finish()  // 회원가입 성공 시 회원가입 화면 닫기
            } else {
                Toast.makeText(applicationContext, "회원가입 실패", Toast.LENGTH_SHORT).show()
            }
        }
    }
}