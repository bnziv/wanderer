package com.example.wanderer

import android.content.Context
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import java.text.DateFormat
import java.text.SimpleDateFormat
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter
import java.util.Locale

class WeatherAdapter(private val context: Context, private val days: List<Weather>) :
    RecyclerView.Adapter<WeatherAdapter.ViewHolder>() {

    inner class ViewHolder(itemView: View): RecyclerView.ViewHolder(itemView) {
        private var iconImg: ImageView = itemView.findViewById(R.id.weatherIcon)
        private var dateTv: TextView = itemView.findViewById(R.id.weatherDate)
        private var descriptionTv: TextView = itemView.findViewById(R.id.weatherDescription)
        private var temperatureTv: TextView = itemView.findViewById(R.id.weatherTemp)
        private var humidityTv: TextView = itemView.findViewById(R.id.weatherHumidity)
        private var windTv: TextView = itemView.findViewById(R.id.weatherWindSpeed)

        fun bind(day: Weather) {
            descriptionTv.text = day.weatherType[0].formatDesc
            temperatureTv.text = "${day.weatherComponents.temp} F"
            humidityTv.text = "Humidity: ${day.weatherComponents.humidity}%"
            windTv.text = "Wind: ${day.windComponents.speed} mp/h"

            val date = SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.getDefault()).parse(day.date)
            dateTv.text = SimpleDateFormat("MM/dd hh:mm a").format(date)
            //Using hosted image URLs since API URLs don't load with Glide
            val url = "https://rodrigokamada.github.io/openweathermap/images/${day.weatherType[0].imgIcon}_t@4x.png"
            Glide.with(context)
                .load(url)
                .into(iconImg)
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(context).inflate(R.layout.weather_item, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val day = days[position]
        holder.bind(day)
    }

    override fun getItemCount(): Int {
        return days.size
    }
}