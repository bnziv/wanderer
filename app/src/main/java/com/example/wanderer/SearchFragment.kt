package com.example.wanderer

import android.animation.Animator
import android.animation.AnimatorListenerAdapter
import android.animation.ObjectAnimator
import android.content.pm.PackageManager
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.ViewTreeObserver
import android.widget.Button
import android.widget.CheckBox
import android.widget.EditText
import android.widget.LinearLayout
import android.widget.RadioButton
import android.widget.Toast
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.core.app.ActivityCompat
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.codepath.asynchttpclient.AsyncHttpClient
import com.codepath.asynchttpclient.RequestParams
import com.codepath.asynchttpclient.callback.JsonHttpResponseHandler
import com.google.android.gms.location.LocationServices
import com.google.android.material.bottomsheet.BottomSheetBehavior
import kotlinx.serialization.json.Json
import okhttp3.Headers
import org.json.JSONException

private const val URL = "https://maps.googleapis.com/maps/api/place/textsearch/json"

class SearchFragment: Fragment() {
    private lateinit var adapter: PlaceAdapter
    private val places = mutableListOf<Place>()
    private var selectedType: String? = null

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val view = inflater.inflate(R.layout.search_fragment, container, false)
        val recyclerView: RecyclerView = view.findViewById(R.id.recyclerView)
        adapter = PlaceAdapter(view.context, places)
        recyclerView.adapter = adapter
        recyclerView.layoutManager = LinearLayoutManager(view.context)

        val filterButton: Button = view.findViewById(R.id.filterButton)
        val filterScreen: ConstraintLayout = view.findViewById(R.id.filterScreen)

        filterButton.setOnClickListener {
            filterScreen.translationY = -filterScreen.height.toFloat()
            filterScreen.visibility = View.VISIBLE
            val preDrawListener = object : ViewTreeObserver.OnPreDrawListener {
                override fun onPreDraw(): Boolean {
                    filterScreen.viewTreeObserver.removeOnPreDrawListener(this)

                    val animator = ObjectAnimator.ofFloat(filterScreen, "translationY", -filterScreen.height.toFloat(), 0f)
                    animator.duration = 300
                    animator.start()

                    return true
                }
            }
            filterScreen.viewTreeObserver.addOnPreDrawListener(preDrawListener)
            filterButton.isEnabled = false
        }
        val locationText: EditText = view.findViewById(R.id.location_text)
        val applyButton: Button = view.findViewById(R.id.applyButton)
        applyButton.setOnClickListener {
            if (selectedType == null) {
                Toast.makeText(requireContext(), "Please select a type", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }
            if (locationText.text.isEmpty()) {
                Toast.makeText(requireContext(), "Please enter a location", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }
            hideFilters(filterScreen)
            filterButton.isEnabled = true
            loadPlaces(locationText.text.toString())
        }

        val closeButton: Button = view.findViewById(R.id.closeButton)
        closeButton.setOnClickListener {
            hideFilters(filterScreen)
            filterButton.isEnabled = true
        }

        val radioGroup = listOf<RadioButton>(
            view.findViewById(R.id.radio_mall),
            view.findViewById(R.id.radio_parks),
            view.findViewById(R.id.radio_tourist),
            view.findViewById(R.id.radio_amusement),
            view.findViewById(R.id.radio_restaurants),
            view.findViewById(R.id.radio_stores)
        )
        val typeMap = mapOf(
            R.id.radio_mall to "shopping_mall",
            R.id.radio_parks to "park",
            R.id.radio_tourist to "tourist_attraction",
            R.id.radio_amusement to "amusement_park",
            R.id.radio_restaurants to "restaurant",
            R.id.radio_stores to "store"
        )

        radioGroup.forEach { button ->
            button.setOnClickListener {
                radioGroup.forEach { it.isChecked = false }
                button.isChecked = true
                selectedType = typeMap[button.id]
            }
        }

        return view
    }

    private fun hideFilters(view: View) {
        val slideOut = ObjectAnimator.ofFloat(view, "translationY", 0f, -view.height.toFloat())
        slideOut.duration = 300
        slideOut.addListener(object: AnimatorListenerAdapter() {
            override fun onAnimationEnd(animation: Animator) {
                super.onAnimationEnd(animation)
                view.visibility = View.GONE
            }
        })
        slideOut.start()
    }

    private fun loadPlaces(location: String) {
        val client = AsyncHttpClient()
        val params = RequestParams()
        params["key"] = PLACES_KEY
        params["type"] = selectedType
        params["radius"] = "2500"
        params["query"] = location
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
                        places.clear()
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