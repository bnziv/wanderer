package com.example.wanderer

import android.Manifest
import android.content.pm.PackageManager
import android.os.Bundle
import android.util.Log
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.codepath.asynchttpclient.AsyncHttpClient
import com.codepath.asynchttpclient.RequestParams
import com.codepath.asynchttpclient.callback.JsonHttpResponseHandler
import com.example.wanderer.databinding.ActivityMainBinding
import com.google.android.gms.location.LocationServices
import kotlinx.serialization.json.Json
import okhttp3.Headers
import org.json.JSONException

const val PLACES_KEY = BuildConfig.GOOGLE_PLACES_API_KEY
const val URL = "https://maps.googleapis.com/maps/api/place/nearbysearch/json"

class MainActivity : AppCompatActivity() {
    private val places = mutableListOf<Place>()
    private lateinit var recyclerView: RecyclerView
    private lateinit var binding: ActivityMainBinding


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityMainBinding.inflate(layoutInflater)
        val view = binding.root
        setContentView(view)

        recyclerView = findViewById(R.id.recyclerView)
        val adapter = PlaceAdapter(this, places)
        recyclerView.adapter = adapter
        recyclerView.layoutManager = LinearLayoutManager(this)

        val locationClient = LocationServices.getFusedLocationProviderClient(this)
        if (ActivityCompat.checkSelfPermission(
                this,
                Manifest.permission.ACCESS_FINE_LOCATION
            ) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(
                this,
                Manifest.permission.ACCESS_COARSE_LOCATION
            ) != PackageManager.PERMISSION_GRANTED
        ) {
            return
        }
        var latitude: Double? = 0.0
        var longitude: Double? = 0.0
        locationClient.lastLocation.addOnSuccessListener { location ->
            if (location != null) {
                latitude = location.latitude
                longitude = location.longitude
                Log.d("LOCATION", "$latitude, $longitude")
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
        }
    }
}