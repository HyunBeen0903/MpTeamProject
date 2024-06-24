package com.example.myapplication

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.widget.TextView
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.model.Marker

class CustomInfoWindowAdapter(private val context: Context) : GoogleMap.InfoWindowAdapter {

    private val window: View = LayoutInflater.from(context).inflate(R.layout.custom_info_window, null)

    private fun render(marker: Marker, view: View) {
        val title: TextView = view.findViewById(R.id.title)
        val address: TextView = view.findViewById(R.id.address)
        val phone: TextView = view.findViewById(R.id.phone)
        val rating: TextView = view.findViewById(R.id.rating)
        val website: TextView = view.findViewById(R.id.website)

        title.text = marker.title
        val info = marker.snippet!!.split("\n")
        if (info.size >= 4) {
            address.text = info[0]
            phone.text = info[1]
            rating.text = info[2]
            website.text = info[3]
        }
    }

    override fun getInfoWindow(marker: Marker): View {
        render(marker, window)
        return window
    }

    override fun getInfoContents(marker: Marker): View {
        render(marker, window)
        return window
    }
}
