package com.example.myapplication

import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.GeoPoint
import com.google.type.LatLng

class FirestoreHelper {
    private val db = FirebaseFirestore.getInstance()

    fun saveUserCourse(name: String, points: List<LatLng>, onComplete: (Boolean) -> Unit) {
        val geoPoints = points.map { GeoPoint(it.latitude, it.longitude) }
        val course = UserCourse(name, geoPoints)

        db.collection("user_courses")
            .add(course)
            .addOnSuccessListener {
                onComplete(true)
            }
            .addOnFailureListener {
                onComplete(false)
            }
    }

    fun getUserCourses(onComplete: (List<UserCourse>) -> Unit) {
        db.collection("user_courses")
            .get()
            .addOnSuccessListener { result ->
                val courses = result.map { document ->
                    document.toObject(UserCourse::class.java)
                }
                onComplete(courses)
            }
            .addOnFailureListener {
                onComplete(emptyList())
            }
    }
}