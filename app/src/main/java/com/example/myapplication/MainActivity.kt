package com.example.myapplication

import android.content.Intent
import android.os.Bundle
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.Firebase
import com.google.firebase.database.database
import com.google.firebase.messaging.FirebaseMessaging
import com.google.firebase.messaging.FirebaseMessagingService
import com.google.firebase.messaging.messaging
class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        var txt = findViewById<TextView>(R.id.회원가입)
        txt.setOnClickListener{
            var intent = Intent(applicationContext, NewAccountPage::class.java)
            startActivity(intent)
        }
      //val database = Firebase.database
      //  val myRef = database.getReference("message")
      //  myRef.setValue("Hello, Firebase")

    }
}