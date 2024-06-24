package com.example.myapplication

import com.google.android.gms.maps.model.LatLng
import com.google.firebase.firestore.GeoPoint

data class UserCourse(
    val name: String = "",
    val points: List<GeoPoint> = emptyList(),
    val placeIds: List<String> = emptyList() // 장소 ID 목록 추가
)
