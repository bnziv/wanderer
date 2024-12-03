package com.example.wanderer

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide

class PlaceAdapter(private val context: Context, private val places: List<Place>) :
    RecyclerView.Adapter<PlaceAdapter.ViewHolder>() {

    inner class ViewHolder(itemView: View): RecyclerView.ViewHolder(itemView) {
        private val nameTv: TextView = itemView.findViewById(R.id.placeName)
        private val addressTv: TextView = itemView.findViewById(R.id.placeAddress)
        private val thumbnail: ImageView = itemView.findViewById(R.id.placeThumbnail)

        fun bind(place: Place) {
            nameTv.text = place.name
            addressTv.text = place.rating.toString()

            val photo_id = place.photo?.get(0)?.photo_id
            if (photo_id != null) {
                val photoUrl = "https://maps.googleapis.com/maps/api/place/photo?maxheight=200&key=${PLACES_KEY}&photo_reference=${photo_id}"
                Glide.with(context)
                    .load(photoUrl)
                    .into(thumbnail)
            }
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