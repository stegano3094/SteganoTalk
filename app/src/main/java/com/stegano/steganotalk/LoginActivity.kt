package com.stegano.steganotalk

import android.content.Context
import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.util.Log
import android.view.inputmethod.InputMethodManager
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInAccount
import com.google.android.gms.auth.api.signin.GoogleSignInClient
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.android.gms.common.api.ApiException
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.GoogleAuthProvider
import kotlinx.android.synthetic.main.activity_login.*


class LoginActivity : AppCompatActivity() {
    val TAG: String = "LoginActivity"

    // firebase login
    var firebaseAuth: FirebaseAuth? = null  // app login (Firebase Auth)
    lateinit var googleSignInClient: GoogleSignInClient  // google login

    companion object {
        private const val RC_SIGN_IN = 722
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)

        // FirebaseAuth 사용 방법 : https://firebase.google.com/docs/auth/android/firebaseui
        firebaseAuth = FirebaseAuth.getInstance()  // 파이어베이스 인증 인스턴스

        loginButton.setOnClickListener {  // 앱 아이디로 로그인 버튼 클릭 시
            appLogin()
            //FirebaseAuth.getInstance().signOut()  // 파이어베이스 로그아웃
            //FirebaseAuth.getInstance().delete()  // 파이어베이스 계정 삭제
        }

        val gso = GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
            .requestIdToken(getString(R.string.default_web_client_id))
            .requestEmail()
            .build()
        googleSignInClient = GoogleSignIn.getClient(this, gso)
        googleLoginButton.setOnClickListener {  // 구글 통합 아이디로 로그인 시
            signIn()
        }

        // 회원가입 화면으로 이동
        signupButton.setOnClickListener {
            startActivity(Intent(this, SignupActivity::class.java))
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

    private fun appLogin() {  // app login
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
                val userEmail: String = inputEmail  // 앱에서 회원가입 후 로그인 시 이메일을 닉네임으로 함
                val user = firebaseAuth!!.currentUser
                toActivity(userEmail, user?.uid.toString())
            } else {
                Toast.makeText(applicationContext, "로그인 실패", Toast.LENGTH_SHORT).show()
            }
        }
    }

    private fun signIn() {  // google login
        val signInIntent = googleSignInClient.signInIntent
        startActivityForResult(signInIntent, RC_SIGN_IN)  // 구글 로그인을 위해 이동됨
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        // google login 후 반환받을 때
        super.onActivityResult(requestCode, resultCode, data)

        // Result returned from launching the Intent from GoogleSignInApi.getSignInIntent(...);
        if (requestCode == RC_SIGN_IN) {
            val task = GoogleSignIn.getSignedInAccountFromIntent(data)
            try {
                // Google Sign In was successful, authenticate with Firebase
                val account = task.getResult(ApiException::class.java)!!
                Log.d(TAG, "firebaseAuthWithGoogle:" + account.id)
                firebaseAuthWithGoogle(account.idToken!!)
            } catch (e: ApiException) {
                // Google Sign In failed, update UI appropriately
                Log.w(TAG, "Google sign in failed", e)
            }
        }
    }

    private fun firebaseAuthWithGoogle(idToken: String) {
        val credential = GoogleAuthProvider.getCredential(idToken, null)
        firebaseAuth!!.signInWithCredential(credential)
            .addOnCompleteListener(this) { task ->
                if (task.isSuccessful) {
                    // Sign in success, update UI with the signed-in user's information
                    Log.d(TAG, "signInWithCredential:success")
                    val user = firebaseAuth!!.currentUser
                    Log.d(TAG, "firebaseAuthWithGoogle: user: $user")
                    Log.d(TAG, "firebaseAuthWithGoogle: user?.displayName.toString(): ${user?.displayName.toString()}")
                    toActivity(user?.displayName.toString(), user?.uid.toString())
                } else {
                    // If sign in fails, display a message to the user.
                    Log.w(TAG, "signInWithCredential:failure", task.exception)
                }
            }
    }

    private fun toActivity(userName: String, uid: String) {
        val intent = Intent(this, MainActivity::class.java)
        intent.putExtra("userName", userName)  // 닉네임 (중복 가능)
        intent.putExtra("uid", uid)  // 메시지를 누가 썻는지 확인을 위해서 uid를 추가해서 보내줌 (중복 불가)
        startActivity(intent)
        finish()
    }
}