package com.example.wanderer

import android.content.Context
import android.content.Intent
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.bitmap.RoundedCorners

class PlaceAdapter(private val context: Context, private val places: List<Place>, private val placeDetailsMap: Map<String, PlaceDetails>?) :
    RecyclerView.Adapter<PlaceAdapter.ViewHolder>() {

    private val notableTypes = mapOf(
        "restaurant" to "Restaurant",
        "tourist_attraction" to "Tourist Attraction",
        "amusement_park" to "Amusement Park",
        "shopping_mall" to "Mall",
        "park" to "Park",
        "bar" to "Bar",
        "university" to "University",
        "bank" to "Bank",
        "store" to "Store",
        "movie_theater" to "Movie Theater"
    )

    inner class ViewHolder(itemView: View): RecyclerView.ViewHolder(itemView), View.OnClickListener {
        private val nameTv: TextView = itemView.findViewById(R.id.placeName)
        private val addressTv: TextView = itemView.findViewById(R.id.placeAddress)
        private val ratingTv: TextView = itemView.findViewById(R.id.placeRating)
        private val thumbnail: ImageView = itemView.findViewById(R.id.placeThumbnail)
        private val typesTv: TextView = itemView.findViewById(R.id.placeType)

        init {
            itemView.setOnClickListener(this)
        }

        fun bind(place: Place) {
            nameTv.text = place.name
            addressTv.text = place.address
            ratingTv.text = if (place.rating != null) "${place.rating}/5"  else ""

            val typeString = place.types?.filter { it in notableTypes.keys }?.joinToString(" - ") { notableTypes[it].toString() }
            typesTv.text = typeString

            val photoId = place.photo?.get(0)?.photoId
            if (photoId != null) {
                val photoUrl = "https://maps.googleapis.com/maps/api/place/photo?maxheight=170&key=${PLACES_KEY}&photo_reference=${photoId}"
                Glide.with(context)
                    .load(photoUrl)
                    .placeholder(R.drawable.ic_launcher_foreground)
                    .transform(RoundedCorners(20))
                    .into(thumbnail)
            }
        }

        override fun onClick(v: View?) {
            val place = places[absoluteAdapterPosition]
            val placeDetails = placeDetailsMap?.get(place.apiId)
            val intent = Intent(context, DetailActivity::class.java). apply {
                putExtra("PLACE_EXTRA", place)
                placeDetails?.let {
                    putExtra("PLACE_DETAILS_EXTRA", it)
                }
            }
            context.startActivity(intent)
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(context).inflate(R.layout.place_item, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val place = places[position]
        holder.bind(place)
    }

    override fun getItemCount() = places.size
}