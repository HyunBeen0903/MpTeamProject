package com.example.myapplication

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.CheckBox
import android.widget.EditText
import android.widget.Spinner
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity

class SurveyActivity : AppCompatActivity() {

    private val surveyData = SurveyData() // 설문조사 데이터 모델

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_survey)

        // 성별 체크박스
        val genderMale = findViewById<CheckBox>(R.id.gender_male)
        val genderFemale = findViewById<CheckBox>(R.id.gender_female)

        // 나이 입력란
        val ageInput = findViewById<EditText>(R.id.age_input)

        // 여행 테마 체크박스
        val themeFood = findViewById<CheckBox>(R.id.theme_food)
        val themeCulture = findViewById<CheckBox>(R.id.theme_culture)
        val themeSports = findViewById<CheckBox>(R.id.theme_sports)
        val themeNature = findViewById<CheckBox>(R.id.theme_nature)
        val themeHistory = findViewById<CheckBox>(R.id.theme_history)

        // 여행 시간 스피너
        val timeSpinner = findViewById<Spinner>(R.id.time_spinner)

        // 비용 스피너
        val costSpinner = findViewById<Spinner>(R.id.cost_spinner)

        // 제출 버튼
        val submitButton = findViewById<Button>(R.id.submit_button)
        submitButton.setOnClickListener {
            // 성별 처리
            surveyData.gender = when {
                genderMale.isChecked -> "남성"
                genderFemale.isChecked -> "여성"
                else -> "선택 안 함"
            }

            // 나이 처리
            surveyData.age = ageInput.text.toString()

            // 여행 테마 처리
            if (themeFood.isChecked) surveyData.themes.add("식도락")
            if (themeCulture.isChecked) surveyData.themes.add("문화체험")
            if (themeSports.isChecked) surveyData.themes.add("스포츠")
            if (themeNature.isChecked) surveyData.themes.add("자연")
            if (themeHistory.isChecked) surveyData.themes.add("역사")

            // 여행 시간 처리
            surveyData.travelTime = timeSpinner.selectedItem.toString()

            // 비용 처리
            surveyData.cost = costSpinner.selectedItem.toString()

            // 설문조사 결과 출력 (테스트용)
            println("설문조사 결과:")
            println("성별: ${surveyData.gender}")
            println("나이: ${surveyData.age}")
            println("여행 테마: ${surveyData.themes}")
            println("여행 시간: ${surveyData.travelTime}")
            println("비용: ${surveyData.cost}")

            // 설문조사 완료 메시지 출력
            Toast.makeText(this, "설문조사 완료", Toast.LENGTH_SHORT).show()

            // 홈 화면(HomeActivity)으로 이동
            val intent = Intent(this, HomeActivity::class.java)
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_NEW_TASK)
            startActivity(intent)
            finish()
        }
    }
}