package com.example.wanderer

import android.os.Bundle
import android.widget.ImageView
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import com.bumptech.glide.Glide

class DetailPlace: AppCompatActivity() {
    private lateinit var nameTv: TextView
    private lateinit var addressTv: TextView
    private lateinit var thumbnail: ImageView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.details_place)

        nameTv = findViewById(R.id.placeName)
        addressTv = findViewById(R.id.placeAddress)
        thumbnail = findViewById(R.id.placeThumbnail)

        val place = intent.getSerializableExtra("PLACE_EXTRA") as Place

        nameTv.text = place.name
        addressTv.text = place.address

        val photo_id = place.photo?.get(0)?.photo_id
        if (photo_id != null) {
            val photoUrl = "https://maps.googleapis.com/maps/api/place/photo?maxheight=600&key=${PLACES_KEY}&photo_reference=${photo_id}"
            Glide.with(this)
                .load(photoUrl)
                .into(thumbnail)
        }
    }
}