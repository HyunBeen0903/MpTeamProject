package com.example.myapplication

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.Marker
import com.google.android.gms.maps.model.MarkerOptions
import com.google.android.gms.maps.model.PolylineOptions
import com.google.android.libraries.places.api.Places
import com.google.android.libraries.places.api.model.Place
import com.google.android.libraries.places.api.net.FetchPhotoRequest
import com.google.android.libraries.places.api.net.FetchPlaceRequest
import com.google.android.libraries.places.api.net.PlacesClient
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.GeoPoint
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import okhttp3.OkHttpClient
import okhttp3.Request
import okhttp3.Response
import org.json.JSONObject
import java.io.IOException

class MapsActivity : AppCompatActivity(), OnMapReadyCallback, GoogleMap.OnMarkerClickListener {

    private lateinit var mMap: GoogleMap
    private lateinit var firestore: FirebaseFirestore
    private lateinit var placesClient: PlacesClient

    private lateinit var placeInfoContainer: LinearLayout
    private lateinit var placeImage: ImageView
    private lateinit var placeName: TextView
    private lateinit var placeAddress: TextView
    private lateinit var placePhone: TextView
    private lateinit var placeRating: TextView
    private lateinit var placeWebsite: TextView
    private lateinit var blogLinkTextView: TextView


    private val markerPlaceIdMap = mutableMapOf<Marker, String>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_maps)

        // Initialize the SDK
        Places.initialize(applicationContext, getString(R.string.MAPS_API_KEY))
        placesClient = Places.createClient(this)

        // Firestore 초기화
        firestore = Firebase.firestore

        // 지도 프래그먼트 가져오기
        val mapFragment = supportFragmentManager
            .findFragmentById(R.id.map) as SupportMapFragment
        mapFragment.getMapAsync(this)

        // 하단 창 UI 요소 초기화
        placeInfoContainer = findViewById(R.id.place_info)
        placeImage = findViewById(R.id.place_image)
        placeName = findViewById(R.id.place_name)
        placeAddress = findViewById(R.id.place_address)
        placePhone = findViewById(R.id.place_phone)
        placeRating = findViewById(R.id.place_rating)
        placeWebsite = findViewById(R.id.place_website)
        blogLinkTextView = findViewById(R.id.blog_website)



    }

    override fun onMapReady(googleMap: GoogleMap) {
        mMap = googleMap

        // 마커 클릭 리스너 설정
        mMap.setOnMarkerClickListener(this)

        // 경로 데이터 로드
        loadRouteData()
    }

    override fun onMarkerClick(marker: Marker): Boolean {
        val tagData = marker.tag as? Pair<String, String>
        val placeId = tagData?.first
        val blog = tagData?.second
        if (placeId != null) {
            Log.d("MapsActivity", "Fetching place info for ID: $placeId")
            fetchPlaceInfo(placeId, blog)
        } else {
            marker.showInfoWindow()
        }
        return true
    }

    private fun loadRouteData() {
        firestore.collection("코스1").document("코스1")
            .get()
            .addOnSuccessListener { document ->
                if (document != null) {
                    val locations = document.get("locations") as? List<Map<String, Any>>

                    if (locations != null) {
                        val polylineOptions = PolylineOptions()
                        val apiKey = getString(R.string.MAPS_API_KEY) // Your Google API Key

                        locations.forEach { point ->
                            val geoPoint = point["location"] as? GeoPoint
                            val pointName = point["name"] as? String
                            val blog = point["blog"]as? String
                            if (geoPoint != null && pointName != null) {
                                val position = LatLng(geoPoint.latitude, geoPoint.longitude)
                                val marker = mMap.addMarker(MarkerOptions().position(position).title(pointName))
                                Thread {
                                    val placeId = getPlaceId(apiKey, pointName)
                                    runOnUiThread {
                                        marker?.tag = Pair(placeId, blog)
                                    }
                                }.start()
                                polylineOptions.add(position)

                            }
                        }

                        mMap.addPolyline(polylineOptions.width(10f).color(getColor(R.color.teal_200)))
                        if (polylineOptions.points.isNotEmpty()) {
                            val firstLatLng = polylineOptions.points.first()
                            mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(firstLatLng, 15f))
                        }
                    } else {
                        Log.d("MapsActivity", "No locations found")
                    }
                } else {
                    Log.d("MapsActivity", "No such document")
                }
            }
            .addOnFailureListener { exception ->
                Log.e("MapsActivity", "Error getting documents: ", exception)
            }
    }

    private fun fetchPlaceInfo(placeId: String, blog: String?) {
        val placeFields = listOf(
            Place.Field.ID,
            Place.Field.NAME,
            Place.Field.ADDRESS,
            Place.Field.PHONE_NUMBER,
            Place.Field.RATING,
            Place.Field.PHOTO_METADATAS,
            Place.Field.OPENING_HOURS,
            Place.Field.USER_RATINGS_TOTAL,
            Place.Field.WEBSITE_URI
        )

        val request = FetchPlaceRequest.newInstance(placeId, placeFields)

        placesClient.fetchPlace(request).addOnSuccessListener { response ->
            val place = response.place

            Log.d("MapsActivity", "Place found: ${place.name}")
            showPlaceInfo(place,blog)

            val photoMetadata = place.photoMetadatas?.firstOrNull()
            if (photoMetadata != null) {
                val photoRequest = FetchPhotoRequest.builder(photoMetadata)
                    .setMaxWidth(placeImage.width) // Optional: Specify the width of the image
                    .setMaxHeight(placeImage.height) // Optional: Specify the height of the image
                    .build()

                placesClient.fetchPhoto(photoRequest).addOnSuccessListener { fetchPhotoResponse ->
                    val bitmap = fetchPhotoResponse.bitmap
                    placeImage.setImageBitmap(bitmap)
                }.addOnFailureListener { exception ->
                    Log.e("MapsActivity", "Photo not found: ", exception)
                    placeImage.setImageResource(R.drawable.placeholder_image)
                }
            } else {
                placeImage.setImageResource(R.drawable.placeholder_image)
            }

            placeInfoContainer.visibility = View.VISIBLE
        }.addOnFailureListener { exception ->
            Log.e("MapsActivity", "Place not found: ", exception)
        }
    }

    private fun showPlaceInfo(place: Place,blog:String?) {
        placeName.text = place.name
        placeAddress.text = place.address ?: "주소 없음"
        placePhone.text = place.phoneNumber ?: "전화번호 없음"
        placeRating.text = "별점: ${place.rating ?: "N/A"} (${place.userRatingsTotal ?: 0} 리뷰)"
        placeWebsite.text = place.websiteUri?.toString() ?: "웹사이트 없음"

        if (!blog.isNullOrEmpty()) {
            blogLinkTextView.setOnClickListener {
                val blogIntent = Intent(Intent.ACTION_VIEW, Uri.parse(blog))
                startActivity(blogIntent)
            }
        }else{
            blogLinkTextView.visibility = View.GONE
        }
        placeInfoContainer.visibility = View.VISIBLE
    }
    private fun getPlaceId(apiKey: String, placeName: String): String? {
        val client = OkHttpClient()

        val url = "https://maps.googleapis.com/maps/api/place/findplacefromtext/json" +
                "?input=${placeName.replace(" ", "%20")}" +
                "&inputtype=textquery" +
                "&fields=place_id" +
                "&key=$apiKey"

        val request = Request.Builder()
            .url(url)
            .build()

        return try {
            client.newCall(request).execute().use { response ->
                if (!response.isSuccessful) throw IOException("Unexpected code $response")

                val responseData = response.body?.string()
                val json = JSONObject(responseData)
                val candidates = json.getJSONArray("candidates")
                if (candidates.length() > 0) {
                    return candidates.getJSONObject(0).getString("place_id")
                }
            }
            null
        } catch (e: IOException) {
            e.printStackTrace()
            null
        }
    }
}
