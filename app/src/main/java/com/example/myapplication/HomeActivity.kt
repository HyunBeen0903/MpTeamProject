package com.example.myapplication

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.LinearLayout
import android.widget.TextView
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity

class HomeActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_home)
        var hobit = findViewById<Button>(R.id.취향)
        var course = findViewById<LinearLayout>(R.id.등대)
        var coursename = findViewById<TextView>(R.id.장소이름)
        course.setOnClickListener{
            var intent = Intent(applicationContext, CourseActivity::class.java)
            intent.putExtra("coursename", coursename.text.toString() )
            startActivity(intent)
        }
        hobit.setOnClickListener{
            var intent = Intent(applicationContext, SurveyActivity::class.java)
            startActivity(intent)
        }

    }
}