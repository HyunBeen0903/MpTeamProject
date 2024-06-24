package com.example.myapplication

import android.Manifest
import android.content.pm.PackageManager
import android.os.Bundle
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
import com.google.android.libraries.places.api.net.FindCurrentPlaceRequest
import com.google.android.libraries.places.api.net.FindCurrentPlaceResponse
import com.google.android.libraries.places.api.net.PlacesClient
import com.google.android.gms.common.api.ApiException

class MapsActivity : AppCompatActivity(), OnMapReadyCallback, GoogleMap.OnMarkerClickListener {

    private lateinit var mMap: GoogleMap
    private lateinit var placesClient: PlacesClient

    companion object {
        private const val LOCATION_PERMISSION_REQUEST_CODE = 1
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_maps)

        // Initialize the SDK
        Places.initialize(applicationContext, getString(R.string.MAPS_API_KEY))

        // Create a new Places client instance
        placesClient = Places.createClient(this)

        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        val mapFragment = supportFragmentManager
            .findFragmentById(R.id.map) as SupportMapFragment
        mapFragment.getMapAsync(this)
    }

    override fun onMapReady(googleMap: GoogleMap) {
        mMap = googleMap

        // Add a marker in Sydney and move the camera
        val sydney = LatLng(37.3401906, 126.7335293)
        mMap.addMarker(MarkerOptions().position(sydney).title("한국공대"))

        // Move the camera to the default location
        mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(sydney, 10.0F))

        // Check location permission
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(
                this,
                arrayOf(Manifest.permission.ACCESS_FINE_LOCATION),
                LOCATION_PERMISSION_REQUEST_CODE
            )
        } else {
            findCurrentPlace()
        }
    }

    override fun onMarkerClick(marker: Marker): Boolean {
        // Display the information associated with the marker
        marker.showInfoWindow()
        return true
    }

    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<String>, grantResults: IntArray) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        if (requestCode == LOCATION_PERMISSION_REQUEST_CODE) {
            if ((grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED)) {
                // Permission granted, proceed with finding current place
                findCurrentPlace()
            } else {
                // Permission denied, show a message to the user
                Toast.makeText(this, "Location permission denied", Toast.LENGTH_SHORT).show()
            }
        }
    }

    private fun findCurrentPlace() {
        // Use fields to define the data types to return.
        val placeFields: List<Place.Field> = listOf(Place.Field.ID, Place.Field.NAME, Place.Field.LAT_LNG)

        // Use the builder to create a FindCurrentPlaceRequest.
        val request: FindCurrentPlaceRequest = FindCurrentPlaceRequest.newInstance(placeFields)

        // Check for location permission before making the request
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(
                this,
                arrayOf(Manifest.permission.ACCESS_FINE_LOCATION),
                LOCATION_PERMISSION_REQUEST_CODE
            )
            return
        }

        // Call findCurrentPlace and handle the response.
        placesClient.findCurrentPlace(request)
            .addOnSuccessListener { response: FindCurrentPlaceResponse ->
                for (placeLikelihood in response.placeLikelihoods) {
                    val place = placeLikelihood.place
                    place.latLng?.let {
                        mMap.addMarker(MarkerOptions().position(it).title(place.name))
                    }
                }
            }.addOnFailureListener { exception: Exception ->
                if (exception is ApiException) {
                    val statusCode = exception.statusCode
                    // Handle error with given status code.
                    Toast.makeText(this, "Place not found: ${exception.message}", Toast.LENGTH_LONG).show()
                }
            }
    }
}