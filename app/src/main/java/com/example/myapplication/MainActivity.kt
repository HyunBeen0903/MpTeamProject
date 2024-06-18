package com.example.myapplication

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity

//Kangjihun Branch
class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        var txt = findViewById<TextView>(R.id.회원가입)
        var home = findViewById<Button>(R.id.홈화면이동)
        txt.setOnClickListener{
            var intent = Intent(applicationContext, NewAccountPage::class.java)
            startActivity(intent)
        }
        home.setOnClickListener{
            var intent = Intent(applicationContext, HomeActivity::class.java)
            startActivity(intent)
        }

      //val database = Firebase.database
      //  val myRef = database.getReference("message")
      //  myRef.setValue("Hello, Firebase")

    }
}