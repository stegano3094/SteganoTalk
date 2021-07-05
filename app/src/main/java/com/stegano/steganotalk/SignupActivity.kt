package com.stegano.steganotalk

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.actionCodeSettings
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase
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

        firebaseAuth = FirebaseAuth.getInstance()  // 파이어베이스 인증 인스턴스

        sendSignupButton.setOnClickListener {  // 가입 버튼 클릭 시
            createId()
        }
    }

    private fun createId() {  // 아이디를 생성하는 함수 (회원가입 시도)
        val inputEmail = email_input.text.toString()
        val inputPassword = password_input.text.toString()

        // 입력란의 값이 비어있는지 확인하는 코드
        if(inputEmail.isEmpty()) {
            Toast.makeText(this, "이메일을 입력해주세요.", Toast.LENGTH_SHORT).show()
            return
        }
        if(inputPassword.isEmpty() || inputPassword.length < 8) {
            Toast.makeText(this, "비밀번호를 8자리 이상 입력해주세요.", Toast.LENGTH_SHORT).show()
            return
        }

        // 이메일 유효성 검사 코드
        // https://chicken-salad-sandwich.tistory.com/5
        val pattern = Regex("^[0-9a-zA-Z]([-_.]?[0-9a-zA-Z])*@[0-9a-zA-Z]([-_.]?[0-9a-zA-Z])*\\.[a-zA-Z]{2,3}")
        if(inputEmail.matches(pattern)) {
            Log.d(TAG, "createId: matched email")
        } else {
            Toast.makeText(applicationContext, "이메일 형식이 정확하지 않습니다.", Toast.LENGTH_SHORT).show()
            return
        }

        // 가입 완료 전 이메일로 인증하는 코드 작성하기
        // https://firebase.google.com/docs/auth/android/email-link-auth?hl=ko
        // 파이어베이스에 회원가입을 인증하는 코드 (아이디, 비밀번호를 통해서 이메일 인증 후 회원가입하는 코드, 로그인 시 코드 다름)
        firebaseAuth!!.createUserWithEmailAndPassword(inputEmail, inputPassword).addOnCompleteListener(this) {
            if(it.isSuccessful) {
                val user = firebaseAuth?.currentUser
                user!!.sendEmailVerification()
                    .addOnCompleteListener { task ->
                        if (task.isSuccessful) {
                            Toast.makeText(applicationContext, "해당 메일로 인증 메일을 보냈습니다. 확인해주세요.", Toast.LENGTH_LONG).show()
                            finish()  // 회원가입 성공 시 회원가입 화면 닫기
                        } else {
                            Toast.makeText(applicationContext, "인증 메일을 보내지 못했습니다. 잠시 후 다시 시도해주세요", Toast.LENGTH_LONG).show()
                        }
                    }
            } else {
                Toast.makeText(applicationContext, "해당 이메일 또는 비밀번호를 사용할 수 없습니다.", Toast.LENGTH_SHORT).show()
            }
        }
        // 로그인 시 추가로 if문을 써서 이메일로 인증 되었는지 확인해주면 됨 (로그인 화면에서)
//        val user = firebaseAuth!!.currentUse
//        if(user!!.isEmailVerified) { }


        // 파이어베이스에 회원가입을 인증하는 코드 (가장 기본적인 아이디, 비밀번호로 회원가입)
//        firebaseAuth!!.createUserWithEmailAndPassword(inputEmail, inputPassword).addOnCompleteListener(this) {
//            if(it.isSuccessful) {
//                val user = firebaseAuth?.currentUser
//                Toast.makeText(applicationContext, "회원가입 성공, $user", Toast.LENGTH_SHORT).show()
//                finish()  // 회원가입 성공 시 회원가입 화면 닫기
//            } else {
//                Toast.makeText(applicationContext, "회원가입 실패", Toast.LENGTH_SHORT).show()
//            }
//        }
        // 로그인 시 아래처럼 하면 됨 (로그인 화면에서)
//        firebaseAuth!!.signInWithEmailAndPassword(inputEmail, inputPassword).addOnCompleteListener(this) {
//            if(it.isSuccessful) {
//                Toast.makeText(applicationContext, "로그인 성공", Toast.LENGTH_SHORT).show()
//                val userEmail: String = inputEmail  // 앱에서 회원가입 후 로그인 시 이메일을 닉네임으로 함
//                val user = firebaseAuth!!.currentUser
//                toActivity(userEmail, user?.uid.toString())
//            } else {
//                Toast.makeText(applicationContext, "아이디 또는 비밀번호가 맞지 않습니다.", Toast.LENGTH_SHORT).show()
//            }
//        }
    }
}