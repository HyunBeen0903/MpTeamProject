package com.example.myapplication

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.MarkerOptions

class CourseActivity : AppCompatActivity(), OnMapReadyCallback {

    private lateinit var mMap: GoogleMap
    private lateinit var courseNameTextView: TextView
    private lateinit var blogLinkTextView: TextView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_course)

        // Initialize UI components
        courseNameTextView = findViewById(R.id.course_name)
        blogLinkTextView = findViewById(R.id.blog_link)

        // Get the course name from intent
        val courseName = intent.getStringExtra("coursename")

        // Set course name
        courseNameTextView.text = courseName

        // Set blog link click listener
        blogLinkTextView.setOnClickListener {
            val blogIntent = Intent(Intent.ACTION_VIEW, Uri.parse("https://m.blog.naver.com/rambo3/222926330696"))
            startActivity(blogIntent)
        }

        // Initialize Google Map fragment
        val mapFragment = supportFragmentManager.findFragmentById(R.id.map_fragment) as SupportMapFragment
        mapFragment.getMapAsync(this)
    }

    override fun onMapReady(googleMap: GoogleMap) {
        mMap = googleMap

        // Add markers or other map customization as needed
        // For example, add a marker at a specific location
        val location = LatLng(37.526617, 126.936773) // Example coordinates
        mMap.addMarker(MarkerOptions().position(location).title("Marker Title"))
        mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(location, 15f))
    }
}
