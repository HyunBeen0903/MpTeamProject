package com.example.myapplication

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity

class HomeActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_home)

        // 상단의 설문조사 버튼
        val surveyButton = findViewById<Button>(R.id.survey_button)
        surveyButton.setOnClickListener {
            val intent = Intent(this, SurveyActivity::class.java)
            startActivity(intent)
        }

        // 중앙의 첫 번째 이미지 레이아웃 클릭 이벤트
        val firstImageLayout = findViewById<LinearLayout>(R.id.등대)
        firstImageLayout.setOnClickListener {
            val coursename = findViewById<TextView>(R.id.장소이름).text.toString()
            val intent = Intent(this, CourseActivity::class.java)
            intent.putExtra("coursename", coursename)
            startActivity(intent)
        }

        // 중앙의 두 번째 이미지 레이아웃 클릭 이벤트
        val secondImageLayout = findViewById<LinearLayout>(R.id.이등대)
        secondImageLayout.setOnClickListener {
            val coursename = findViewById<TextView>(R.id.이장소).text.toString()
            val intent = Intent(this, CourseActivity::class.java)
            intent.putExtra("coursename", coursename)
            startActivity(intent)
        }

        // 하단 네비게이션 버튼들
        val mapsButton = findViewById<ImageView>(R.id.maps_button)
        val homeButton = findViewById<ImageView>(R.id.home_button)
        val courseRecommendButton = findViewById<ImageView>(R.id.course_recommend_button)

        mapsButton.setOnClickListener {
            val intent = Intent(this, MapsActivity::class.java)
            startActivity(intent)
        }

        homeButton.setOnClickListener {
            // 현재 홈 화면이므로 특별한 동작 없음
        }

        courseRecommendButton.setOnClickListener {
            // 코스 추천 버튼 클릭 시의 동작 정의 (추후 구현 필요)
        }
    }
}
