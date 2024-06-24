package com.example.myapplication

import android.Manifest
import android.content.pm.PackageManager
import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.EditText
import android.widget.LinearLayout
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.Marker
import com.google.android.gms.maps.model.MarkerOptions
import com.google.android.libraries.places.api.Places
import com.google.android.libraries.places.api.model.Place
import com.google.android.libraries.places.api.net.PlacesClient
import com.google.android.gms.common.api.ApiException
import com.google.android.gms.maps.model.PolylineOptions
import com.google.android.libraries.places.api.net.FetchPlaceRequest
import com.google.android.libraries.places.api.net.FindAutocompletePredictionsRequest

class PlacesActivity : AppCompatActivity(), OnMapReadyCallback, GoogleMap.OnMarkerClickListener {

    private lateinit var mMap: GoogleMap
    private lateinit var placesClient: PlacesClient
    private lateinit var searchInput: EditText
    private lateinit var searchButton: Button
    private lateinit var infoWindow: LinearLayout
    private lateinit var infoName: TextView
    private lateinit var infoAddress: TextView
    private lateinit var infoPhone: TextView
    private lateinit var infoRating: TextView
    private lateinit var infoWebsite: TextView
    private val markerPlaceIdMap = mutableMapOf<Marker, String>()
    private val firestoreHelper = FirestoreHelper()

    companion object {
        private const val LOCATION_PERMISSION_REQUEST_CODE = 1
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_places)

        // Initialize the SDK
        Places.initialize(applicationContext, getString(R.string.places_api_key))

        // Create a new Places client instance
        placesClient = Places.createClient(this)


        // Initialize views
        searchInput = findViewById(R.id.search_input)
        searchButton = findViewById(R.id.search_button)
        infoWindow = findViewById(R.id.info_window)
        infoName = findViewById(R.id.info_name)
        infoAddress = findViewById(R.id.info_address)
        infoPhone = findViewById(R.id.info_phone)
        infoRating = findViewById(R.id.info_rating)
        infoWebsite = findViewById(R.id.info_website)

        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        val mapFragment = supportFragmentManager.findFragmentById(R.id.map) as SupportMapFragment
        mapFragment.getMapAsync(this)

        // Set search button click listener
        searchButton.setOnClickListener {
            val query = searchInput.text.toString()
            if (query.isNotEmpty()) {
                searchPlaces(query)
            } else {
                Toast.makeText(this, "Please enter a place to search", Toast.LENGTH_SHORT).show()
            }
        }
    }

    override fun onMapReady(googleMap: GoogleMap) {
        mMap = googleMap

        // Move the camera to a default location (e.g., Sydney)
        val defaultLocation = LatLng(37.3401906, 126.7335293)
        mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(defaultLocation, 10f))

        // Check location permission
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(
                this,
                arrayOf(Manifest.permission.ACCESS_FINE_LOCATION),
                LOCATION_PERMISSION_REQUEST_CODE
            )
        } else {
            mMap.isMyLocationEnabled = true
        }

        // Set a marker click listener
        // Load user courses and display on the map
        loadUserCourses()
        mMap.setOnMarkerClickListener(this)
    }

    override fun onMarkerClick(marker: Marker): Boolean {
        val placeId = markerPlaceIdMap[marker]
        placeId?.let {
            fetchPlaceDetails(it)
        }
        return true
    }

    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<String>, grantResults: IntArray) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        if (requestCode == LOCATION_PERMISSION_REQUEST_CODE) {
            if (grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                if (ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
                    mMap.isMyLocationEnabled = true
                }
            } else {
                Toast.makeText(this, "Location permission denied", Toast.LENGTH_SHORT).show()
            }
        }
    }

    private fun searchPlaces(query: String) {
        val request = FindAutocompletePredictionsRequest.builder()
            .setQuery(query)
            .build()

        placesClient.findAutocompletePredictions(request)
            .addOnSuccessListener { response ->
                mMap.clear()
                markerPlaceIdMap.clear()
                for (prediction in response.autocompletePredictions) {
                    val placeId = prediction.placeId
                    val placeFields = listOf(Place.Field.ID, Place.Field.NAME, Place.Field.LAT_LNG)
                    val fetchPlaceRequest = FetchPlaceRequest.newInstance(placeId, placeFields)

                    placesClient.fetchPlace(fetchPlaceRequest).addOnSuccessListener { fetchPlaceResponse ->
                        val place = fetchPlaceResponse.place
                        val latLng = place.latLng
                        if (latLng != null && place.id != null) {
                            val marker = mMap.addMarker(MarkerOptions().position(latLng).title(place.name))
                            if (marker != null) {
                                markerPlaceIdMap[marker] = place.id!!
                                mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(latLng, 10f))
                            }
                        }
                    }.addOnFailureListener { exception ->
                        if (exception is ApiException) {
                            Toast.makeText(this, "Place not found: ${exception.message}", Toast.LENGTH_LONG).show()
                        }
                    }
                }
            }
            .addOnFailureListener { exception ->
                if (exception is ApiException) {
                    Toast.makeText(this, "Place search failed: ${exception.message}", Toast.LENGTH_LONG).show()
                }
            }
    }

    private fun fetchPlaceDetails(placeId: String) {
        val placeFields = listOf(
            Place.Field.ID, Place.Field.NAME, Place.Field.ADDRESS, Place.Field.PHONE_NUMBER,
            Place.Field.OPENING_HOURS, Place.Field.RATING, Place.Field.USER_RATINGS_TOTAL, Place.Field.WEBSITE_URI
        )
        val request = FetchPlaceRequest.newInstance(placeId, placeFields)

        placesClient.fetchPlace(request).addOnSuccessListener { response ->
            val place = response.place
            showInfoWindow(place)
        }.addOnFailureListener { exception ->
            if (exception is ApiException) {
                Toast.makeText(this, "Place details not found: ${exception.message}", Toast.LENGTH_LONG).show()
            }
        }
    }

    private fun showInfoWindow(place: Place) {
        infoName.text = place.name
        infoAddress.text = place.address ?: "주소 없음"
        infoPhone.text = place.phoneNumber ?: "전화번호 없음"
        infoRating.text = "별점: ${place.rating ?: "N/A"} (${place.userRatingsTotal ?: 0} 리뷰)"
        infoWebsite.text = place.websiteUri?.toString() ?: "웹사이트 없음"

        infoWindow.visibility = View.VISIBLE
    }
    private fun loadUserCourses() {
        firestoreHelper.getUserCourses { courses ->
            for (course in courses) {
                val polylineOptions = PolylineOptions().clickable(true)
                for (point in course.points) {
                    polylineOptions.add(LatLng(point.latitude, point.longitude))
                }
                mMap.addPolyline(polylineOptions)
            }
        }
    }

    private fun loadPlaces() {
        firestoreHelper.getPlaces { places ->
            places.forEach { place ->
                val location = place.location
                if (location != null) {
                    val latLng = LatLng(location.latitude, location.longitude)
                    val marker = mMap.addMarker(MarkerOptions().position(latLng).title(place.name))
                    if (marker != null) {
                      //  markerPlaceIdMap[marker] = place
                    }
                }
            }
        }
    }
}
