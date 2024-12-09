package com.example.wanderer

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView

class ReviewAdapter(private val context: Context, private val reviews: List<Review>?) :
    RecyclerView.Adapter<ReviewAdapter.ViewHolder>() {

    inner class ViewHolder(itemView: View): RecyclerView.ViewHolder(itemView) {
        private val textTv: TextView = itemView.findViewById(R.id.reviewText)
        private val headerTv: TextView = itemView.findViewById(R.id.reviewHeader)

        fun bind(review: Review) {
            textTv.text = review.text
            val time: String? = review.time?.capitalize()
            headerTv.text = "${time} - ${review.rating}/5"
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(context).inflate(R.layout.review_item, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val review = reviews?.get(position)
        if (review != null) {
            holder.bind(review)
        }
    }

    override fun getItemCount(): Int {
        return reviews?.size ?: 0
    }
}