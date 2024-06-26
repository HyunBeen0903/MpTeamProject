package com.example.myapplication

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.auth.FirebaseAuth
import com.kakao.sdk.user.Constants.TAG
import com.kakao.sdk.user.UserApiClient

// Kangjihun Branch
class MainActivity : AppCompatActivity() {

    private lateinit var mAuth: FirebaseAuth
    override fun onCreate(savedInstanceState: Bundle?) {

        super.onCreate(savedInstanceState)

        setContentView(R.layout.activity_main)

        mAuth = FirebaseAuth.getInstance()      // 로그인

        val txt = findViewById<TextView>(R.id.회원가입)
        val loginButton = findViewById<Button>(R.id.login)
        val kakaologin = findViewById<Button>(R.id.카카오톡로그인)
        val id = findViewById<EditText>(R.id.idEditText)
        val password = findViewById<EditText>(R.id.passwordEditTest)

        kakaologin.setOnClickListener{
            UserApiClient.instance.loginWithKakaoTalk(this) { token, error ->
                if (error != null) {
                    Log.e(TAG, "로그인 실패", error)
                }
                else if (token != null) {
                    Log.i(TAG, "로그인 성공 ${token.accessToken}")
                    val intent = Intent(applicationContext, HomeActivity::class.java)
                    startActivity(intent)
                }
            }
        }

        txt.setOnClickListener {
            val intent = Intent(applicationContext, NewAccountPage::class.java)
            startActivity(intent)
        }

        loginButton.setOnClickListener {
            val userId = id.text.toString()
            val userPassword = password.text.toString()

            loginUser(userId, userPassword)
        }
    }

    private fun loginUser(email: String, password: String) {
        mAuth.signInWithEmailAndPassword(email, password)
            .addOnCompleteListener(this) { task ->
                if (task.isSuccessful) {
                    // 로그인 성공
                    Toast.makeText(this, "Login Successful", Toast.LENGTH_SHORT).show()
                    val intent = Intent(applicationContext, HomeActivity::class.java)
                    startActivity(intent)
                    finish() // 로그인 액티비티를 종료하여 뒤로 가기 버튼을 누르면 다시 돌아오지 않도록 함
                } else {
                    // 로그인 실패
                    Toast.makeText(
                        this,
                        "Login Failed: ${task.exception?.message}",
                        Toast.LENGTH_SHORT
                    ).show()
                }
            }
    }
}
