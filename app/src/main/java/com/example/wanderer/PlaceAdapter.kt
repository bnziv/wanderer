package com.example.wanderer

import android.content.Context
import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide

class PlaceAdapter(private val context: Context, private val places: List<Place>) :
    RecyclerView.Adapter<PlaceAdapter.ViewHolder>() {

    inner class ViewHolder(itemView: View): RecyclerView.ViewHolder(itemView), View.OnClickListener {
        private val nameTv: TextView = itemView.findViewById(R.id.placeName)
        private val addressTv: TextView = itemView.findViewById(R.id.placeAddress)
        private val ratingTv: TextView = itemView.findViewById(R.id.placeRating)
        private val thumbnail: ImageView = itemView.findViewById(R.id.placeThumbnail)

        init {
            itemView.setOnClickListener(this)
        }

        fun bind(place: Place) {
            nameTv.text = place.name
            addressTv.text = place.address
            ratingTv.text = if (place.rating != null) place.rating.toString() else ""

            val photoId = place.photo?.get(0)?.photo_id
            if (photoId != null) {
                val photoUrl = "https://maps.googleapis.com/maps/api/place/photo?maxheight=200&key=${PLACES_KEY}&photo_reference=${photoId}"
                Glide.with(context)
                    .load(photoUrl)
                    .placeholder(R.drawable.ic_launcher_foreground)
                    .into(thumbnail)
            }
        }

        override fun onClick(v: View?) {
            val place = places[absoluteAdapterPosition]
            val intent = Intent(context, DetailPlace::class.java)
            intent.putExtra("PLACE_EXTRA", place)
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