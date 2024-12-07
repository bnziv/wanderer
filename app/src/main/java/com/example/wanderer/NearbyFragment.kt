package com.example.wanderer

import android.content.pm.PackageManager
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.app.ActivityCompat
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.codepath.asynchttpclient.AsyncHttpClient
import com.codepath.asynchttpclient.RequestParams
import com.codepath.asynchttpclient.callback.JsonHttpResponseHandler
import com.google.android.gms.location.LocationServices
import kotlinx.serialization.json.Json
import okhttp3.Headers
import org.json.JSONException

class NearbyFragment: Fragment() {
    private lateinit var adapter: PlaceAdapter
    private val places = mutableListOf<Place>()
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val view = inflater.inflate(R.layout.nearby_fragment, container, false)
        val recyclerView: RecyclerView = view.findViewById(R.id.recyclerView)
        adapter = PlaceAdapter(view.context, places)
        recyclerView.adapter = adapter
        recyclerView.layoutManager = LinearLayoutManager(view.context)

        fetchLocation()

        return view
    }

    private fun fetchLocation() {
        val locationClient = LocationServices.getFusedLocationProviderClient(requireContext())
        if (ActivityCompat.checkSelfPermission(
                requireContext(),
                android.Manifest.permission.ACCESS_FINE_LOCATION
            ) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(
                requireContext(),
                android.Manifest.permission.ACCESS_COARSE_LOCATION
            ) != PackageManager.PERMISSION_GRANTED
        ) {
            requestPermissions(arrayOf(android.Manifest.permission.ACCESS_FINE_LOCATION),
                100
            )
            return
        }

        locationClient.lastLocation.addOnSuccessListener { location ->
            if (location != null) {
                val latitude = location.latitude
                val longitude = location.longitude
                Log.d("LOCATION", "$latitude, $longitude")

                loadPlaces(latitude, longitude)
            } else {
                Toast.makeText(requireContext(), "Unable to get location", Toast.LENGTH_SHORT).show()
            }
        }
    }

    private fun loadPlaces(latitude: Double, longitude: Double) {
        val client = AsyncHttpClient()
        val params = RequestParams()
        params["key"] = PLACES_KEY
        params["location"] = "$latitude,$longitude"
        params["radius"] = "1500"
        client[URL, params, object : JsonHttpResponseHandler() {
            override fun onFailure(
                statusCode: Int,
                headers: Headers?,
                response: String?,
                throwable: Throwable?
            ) {
                Log.e("", "Failed to fetch articles: $statusCode")
            }

            override fun onSuccess(statusCode: Int, headers: Headers, json: JSON) {
                try {
                    val parsed = Json { ignoreUnknownKeys = true }.decodeFromString(
                        Results.serializer(),
                        json.jsonObject.toString()
                    )
                    parsed.results?.let { list ->
                        places.addAll(list)
                        adapter.notifyDataSetChanged()
                    }
                } catch (e: JSONException) {
                    Log.e("", "Exception: ${e}")
                }
            }
        }]
    }

    override fun onRequestPermissionsResult(requestCode: Int, permissions:Array<out String>, grantResults: IntArray) {
        if (requestCode == 100) {
            if (grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                fetchLocation()
            } else {
                Toast.makeText(requireContext(), "Location permission is required to show nearby places", Toast.LENGTH_SHORT).show()
            }
        }
    }
}