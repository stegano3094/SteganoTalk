package com.stegano.steganotalk

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.inputmethod.InputMethodManager
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.google.android.gms.auth.api.signin.GoogleSignIn
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

    // 아이디, 비밀번호를 저장하는 객체
    val sharedPreferences by lazy { getSharedPreferences("UserIdPassword", Context.MODE_PRIVATE) }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)

        // FirebaseAuth 사용 방법 : https://firebase.google.com/docs/auth/android/firebaseui
        firebaseAuth = FirebaseAuth.getInstance()  // 파이어베이스 인증 인스턴스

        // alreadyLoginUser가 null이 아닐 때 바로 MainActivity로 바로 이동됨
        val alreadyLoginUser = firebaseAuth!!.currentUser
        Log.d(TAG, "onCreate: alreadyLoginUser: $alreadyLoginUser")
        alreadyLoginUser?.let {
            Log.d(TAG, "onCreate: user.email: ${it.email}")
            Log.d(TAG, "onCreate: user.uid: ${it.uid}")
            toActivity(it.email.toString(), it.uid)
        }

        // 앱 아이디로 로그인 버튼 클릭 시
        loginButton.setOnClickListener {
            appSignIn()
            //FirebaseAuth.getInstance().signOut()  // 파이어베이스 로그아웃
            //FirebaseAuth.getInstance().delete()  // 파이어베이스 계정 삭제
        }
        // 구글 통합 아이디로 로그인 시
        val gso = GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
            .requestIdToken(getString(R.string.default_web_client_id))
            .requestEmail()
            .build()
        googleSignInClient = GoogleSignIn.getClient(this, gso)
        googleLoginButton.setOnClickListener {
            googleSignIn()
        }

        // 회원가입 화면으로 이동
        signupButton.setOnClickListener {
            startActivity(Intent(this, SignupActivity::class.java))
        }

        // 비밀번호 재설정 화면으로 이동
        passwordResetButton.setOnClickListener {
            startActivity(Intent(this, PasswordResetActivity::class.java))
        }

        // 확인을 누르면 키보드가 닫히도록 할 때 아래 코드를 넣어준다. Soft가 붙은 것은 가상 키보드임
        val imm = getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
        view.setOnClickListener {
            imm.hideSoftInputFromWindow(view.windowToken, 0)
            email_input.clearFocus()  // 포커스 없애기
            password_input.clearFocus()
        }

        // 입력값 기억하기 클릭 시 값 불러오기
        rememberCheckBox.isChecked = sharedPreferences.getBoolean("rememberCheckBoxChecked", false)
        if(rememberCheckBox.isChecked) {
            val getEmail = sharedPreferences.getString("userEmail", "")
            val getPassword = sharedPreferences.getString("userPassword", "")
            email_input.setText(getEmail)
            password_input.setText(getPassword)
        }
        rememberCheckBox.setOnClickListener {  // 체크박스 클릭 리스너
            if(rememberCheckBox.isChecked)  // 체크 상태를 저장함
                sharedPreferences.edit().putBoolean("rememberCheckBoxChecked", true).apply()
            else
                sharedPreferences.edit().putBoolean("rememberCheckBoxChecked", false).apply()
        }
    }

    // app login
    private fun appSignIn() {
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
                // 로그인 성공 시 값을 저장함
                if(rememberCheckBox.isChecked) {  // 체크 박스가 체크된 상태면 useEmail을 저장함
                    sharedPreferences.edit().putString("userEmail", inputEmail).apply()
                    sharedPreferences.edit().putString("userPassword", inputPassword).apply()
                }
//                Toast.makeText(applicationContext, "로그인 성공", Toast.LENGTH_SHORT).show()
                val userEmail: String = inputEmail  // 앱에서 회원가입 후 로그인 시 이메일을 닉네임으로 함
                val user = firebaseAuth!!.currentUser

                if(user!!.isEmailVerified) {  // 이메일 인증되었는지 확인하는 코드
                    Toast.makeText(this, "로그인 성공", Toast.LENGTH_SHORT).show()
                    toActivity(userEmail, user.uid)
                    Log.d(TAG, "appSignIn: 인증되었습니다.")
                } else {
                    Toast.makeText(this, "인증 메일을 확인해주세요.", Toast.LENGTH_SHORT).show()
                    Log.d(TAG, "인증 메일을 확인해주세요.")
                }

            } else {
                Toast.makeText(applicationContext, "아이디 또는 비밀번호가 맞지 않거나 회원가입이 필요합니다.", Toast.LENGTH_SHORT).show()
            }
        }
    }

    // google login
    private fun googleSignIn() {
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
                    //Log.d(TAG, "firebaseAuthWithGoogle: user: $user")
                    Log.d(TAG, "firebaseAuthWithGoogle: user?.email.toString(): ${user?.email.toString()}")
                    Log.d(TAG, "firebaseAuthWithGoogle: user?.uid.toString(): ${user?.uid.toString()}")
                    toActivity(user?.email.toString(), user?.uid.toString())
                } else {
                    // If sign in fails, display a message to the user.
                    Log.w(TAG, "signInWithCredential:failure", task.exception)
                }
            }
    }

    // 메인 화면으로 이동하는 코드
    private fun toActivity(userName: String, uid: String) {
        MyProfileData(userName, uid)  // 데이터 클래스 객체에 저장함
        Log.d(TAG, "toActivity: userName: $userName, uid: $uid")

        val intent = Intent(this, MainActivity::class.java)
        intent.putExtra("userName", userName)  // 닉네임 (중복 가능)
        intent.putExtra("uid", uid)  // 메시지를 누가 썻는지 확인을 위해서 uid를 추가해서 보내줌 (중복 불가)
        startActivity(intent)
        finish()
    }
}