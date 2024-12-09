package com.example.wanderer

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.util.Log
import android.widget.ImageButton
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.bitmap.CenterCrop
import com.bumptech.glide.load.resource.bitmap.CenterInside
import com.bumptech.glide.load.resource.bitmap.RoundedCorners
import com.codepath.asynchttpclient.AsyncHttpClient
import com.codepath.asynchttpclient.RequestParams
import com.codepath.asynchttpclient.callback.JsonHttpResponseHandler
import kotlinx.serialization.json.Json
import okhttp3.Headers
import org.json.JSONException
import org.w3c.dom.Text

private const val URL = "https://maps.googleapis.com/maps/api/place/details/json"

class DetailActivity: AppCompatActivity() {
    private lateinit var nameTv: TextView
    private lateinit var addressTv: TextView
    private lateinit var hoursTv: TextView
    private lateinit var overviewTv: TextView
    private lateinit var phoneTv: TextView
    private lateinit var thumbnail: ImageView
    private lateinit var googleButton: ImageButton
    private lateinit var recyclerView: RecyclerView
    private lateinit var adapter: ReviewAdapter

    private lateinit var place: Place
    private var placeDetails: PlaceDetails? = null
    private var reviews = mutableListOf<Review>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.details_place)

        nameTv = findViewById(R.id.placeName)
        addressTv = findViewById(R.id.placeAddress)
        hoursTv = findViewById(R.id.placeHours)
        overviewTv = findViewById(R.id.placeOverview)
        phoneTv = findViewById(R.id.placePhone)
        googleButton = findViewById(R.id.googleButton)
        thumbnail = findViewById(R.id.placeThumbnail)
        recyclerView = findViewById(R.id.reviews)
        adapter = ReviewAdapter(this, reviews)
        recyclerView.adapter = adapter
        recyclerView.layoutManager = LinearLayoutManager(this)

        place = intent.getSerializableExtra("PLACE_EXTRA") as Place
        placeDetails = intent.getSerializableExtra("PLACE_DETAILS_EXTRA") as? PlaceDetails

        if (placeDetails != null) {
            updateView()
        } else {
            fetchDetails()
        }
    }

    private fun fetchDetails() {
        val client = AsyncHttpClient()
        val params = RequestParams()
        params["key"] = PLACES_KEY
        params["place_id"] = place.apiId
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
                    val parsed = Json { ignoreUnknownKeys = true }.decodeFromString(
                        Result.serializer(),
                        json.jsonObject.toString()
                    )
                    placeDetails = parsed.result

                    runOnUiThread {
                        updateView()
                    }
                } catch (e: JSONException) {
                    Log.e("", "Exception: ${e}")
                }
            }
        }]
    }

    private fun updateView() {
        placeDetails?.let { details ->
            nameTv.text = details.name
            addressTv.text = details.address
            hoursTv.text = details.opening?.hours?.joinToString("\n")
            overviewTv.text = details.summary?.overview
            phoneTv.text = details.phoneNum

            details.reviews?.let { reviews.addAll(it) }
            adapter.notifyDataSetChanged()

            val photoId = details.photos?.get(0)?.photoId
            if (photoId != null) {
                val photoUrl = "https://maps.googleapis.com/maps/api/place/photo?maxheight=600&key=${PLACES_KEY}&photo_reference=${photoId}"
                Glide.with(this)
                    .load(photoUrl)
                    .transform(CenterInside(), RoundedCorners(20))
                    .into(thumbnail)
            }
            googleButton.setOnClickListener {
                try {
                    val browserIntent = Intent(Intent.ACTION_VIEW, Uri.parse(details.googleUrl))
                    startActivity(browserIntent)
                } catch (e: Exception) {
                    Toast.makeText(this, "Error occurred while opening Google URL", Toast.LENGTH_SHORT).show()
                }
            }
        }
    }
}