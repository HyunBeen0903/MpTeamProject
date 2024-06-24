package com.example.myapplication
import com.google.gson.annotations.SerializedName
data class DirectionsResponse(
    @SerializedName("routes") val routes: List<Route>
)

data class Route(
    @SerializedName("legs") val legs: List<Leg>
)

data class Leg(
    @SerializedName("steps") val steps: List<Step>
)

data class Step(
    @SerializedName("start_location") val startLocation: Location,
    @SerializedName("end_location") val endLocation: Location,
    @SerializedName("polyline") val polyline: Polyline
)

data class Location(
    @SerializedName("lat") val lat: Double,
    @SerializedName("lng") val lng: Double
)

data class Polyline(
    @SerializedName("points") val points: String
)