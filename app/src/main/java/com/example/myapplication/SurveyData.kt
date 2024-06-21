package com.example.myapplication

data class SurveyData(
    var gender: String = "", // 성별
    var age: String = "", // 나이
    var themes: MutableList<String> = mutableListOf(), // 여행 테마
    var travelTime: String = "", // 여행 시간
    var cost: String = "" // 비용
)
