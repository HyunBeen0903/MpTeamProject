package com.example.myapplication

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.auth.FirebaseAuth

//Kangjihun Branch
class MainActivity : AppCompatActivity() {

    private lateinit var mAuth: FirebaseAuth
    override fun onCreate(savedInstanceState: Bundle?) {

        super.onCreate(savedInstanceState)

        setContentView(R.layout.activity_main)

        mAuth = FirebaseAuth.getInstance()      //로그인


        var txt = findViewById<TextView>(R.id.회원가입)
        var home = findViewById<Button>(R.id.홈화면이동)
        val mapButton = findViewById<Button>(R.id.지도)
        val loginButton = findViewById<Button>(R.id.login)
        val id = findViewById<EditText>(R.id.idEditText)
        val password = findViewById<EditText>(R.id.passwordEditTest)
        txt.setOnClickListener {
            var intent = Intent(applicationContext, NewAccountPage::class.java)
            startActivity(intent)
        }
        home.setOnClickListener {
            var intent = Intent(applicationContext, HomeActivity::class.java)
            startActivity(intent)
        }
        mapButton.setOnClickListener {
            val intent = Intent(applicationContext, MapsActivity::class.java)
            startActivity(intent)
        }

        loginButton.setOnClickListener {
            val id = id.text.toString()
            val password = password.text.toString()

            loginUser(id, password)
        }
        //val database = Firebase.database
        //  val myRef = database.getReference("message")
        //  myRef.setValue("Hello, Firebase")
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