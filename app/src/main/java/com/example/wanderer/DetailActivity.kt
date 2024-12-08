package com.example.wanderer

import android.os.Bundle
import android.util.Log
import android.widget.ImageView
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import com.bumptech.glide.Glide
import com.codepath.asynchttpclient.AsyncHttpClient
import com.codepath.asynchttpclient.RequestParams
import com.codepath.asynchttpclient.callback.JsonHttpResponseHandler
import kotlinx.serialization.json.Json
import okhttp3.Headers
import org.json.JSONException

private const val URL = "https://maps.googleapis.com/maps/api/place/details/json"

class DetailActivity: AppCompatActivity() {
    private lateinit var nameTv: TextView
    private lateinit var addressTv: TextView
    private lateinit var thumbnail: ImageView

    private lateinit var place: Place
    private lateinit var placeDetails: PlaceDetails

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.details_place)

        nameTv = findViewById(R.id.placeName)
        addressTv = findViewById(R.id.placeAddress)
        thumbnail = findViewById(R.id.placeThumbnail)

        place = intent.getSerializableExtra("PLACE_EXTRA") as Place

        val client = AsyncHttpClient()
        val params = RequestParams()
        params["key"] = PLACES_KEY
        params["place_id"] = place.apiId
        Log.e("","Calling API $PLACES_KEY ${place.apiId}")
        client[URL, params, object : JsonHttpResponseHandler() {
            override fun onFailure(
                statusCode: Int,
                headers: Headers?,
                response: String?,
                throwable: Throwable?
            ) {
                Log.e("", "Failed to fetch places: $statusCode")
            }

            override fun onSuccess(statusCode: Int, headers: Headers, json: JSON) {
                try {
                    Log.e("","Success API")
                    val parsed = Json { ignoreUnknownKeys = true }.decodeFromString(
                        Result.serializer(),
                        json.jsonObject.toString()
                    )
                    Log.e("","Success parsing API")
                    placeDetails = parsed.result

                    runOnUiThread {
                        updateView(place)
                    }
                } catch (e: JSONException) {
                    Log.e("", "Exception: ${e}")
                }
            }
        }]
    }

    private fun updateView(place: Place) {
        nameTv.text = placeDetails.name
        addressTv.text = if (place.vicinity != "") place.vicinity else place.address

        val photo_id = place.photo?.get(0)?.photoId
        if (photo_id != null) {
            val photoUrl = "https://maps.googleapis.com/maps/api/place/photo?maxheight=600&key=${PLACES_KEY}&photo_reference=${photo_id}"
            Glide.with(this)
                .load(photoUrl)
                .into(thumbnail)
        }
    }
}