package com.stegano.steganotalk

import android.content.Context
import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.util.Log
import android.view.View
import android.view.WindowManager
import android.view.inputmethod.InputMethodManager
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import kotlinx.android.synthetic.main.activity_login.*


class LoginActivity : AppCompatActivity() {
    val TAG: String = "LoginActivity"

    // firebase RealtimeDatabase
    var ref = FirebaseDatabase.getInstance().getReference("test")  // 키값으로 읽어옴

    // firebase login
    val LOGIN_REQUEST_CODE = 9001
    var firebaseAuth: FirebaseAuth? = null

    // firebase sign up
    var inflater: View? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)

        // FirebaseAuth 사용 방법 : https://firebase.google.com/docs/auth/android/firebaseui
        // 파이어베이스 로그인
        firebaseAuth = FirebaseAuth.getInstance()
        loginButton.setOnClickListener {
            login()
            //FirebaseAuth.getInstance().signOut()  // 파이어베이스 로그아웃
            //FirebaseAuth.getInstance().delete()  // 파이어베이스 계정 삭제
        }

        // 파이어베이스 회원가입
        createIdButton.setOnClickListener {
            createId()
        }

        // 확인을 누르면 키보드가 닫히도록 할 때 아래 코드를 넣어준다. Soft가 붙은 것은 가상 키보드임
        val imm = getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
        view.setOnClickListener {
            imm.hideSoftInputFromWindow(view.windowToken, 0)
            email_input.clearFocus()  // 포커스 없애기
            password_input.clearFocus()
        }

        // 입력값 기억하기 클릭 시 값 불러오기. 테스트용도로 편리를 위해 하드코딩함
        rememberButton.setOnClickListener {
            if(Build.VERSION.SDK_INT == Build.VERSION_CODES.Q) {
                email_input.setText("test@naver.com")  // note9
            } else {
                email_input.setText("ste@naver.com")  // s10 5g
            }
            password_input.setText("qwer1234")
        }
    }

    private fun login() {
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
        firebaseAuth!!.signInWithEmailAndPassword(inputEmail, inputPassword).addOnCompleteListener(this) {
            if(it.isSuccessful) {
                Toast.makeText(applicationContext, "로그인 성공", Toast.LENGTH_SHORT).show()
                val user: String = inputEmail
                toActivity(user)
            } else {
                Toast.makeText(applicationContext, "로그인 실패", Toast.LENGTH_SHORT).show()
            }
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
            } else {
                Toast.makeText(applicationContext, "회원가입 실패", Toast.LENGTH_SHORT).show()
            }
        }
    }

    private fun toActivity(userName: String) {
        val intent = Intent(this, MainActivity::class.java)
        intent.putExtra("userName", userName)
        startActivity(intent)
        finish()
    }
}