package com.example.wanderer

import BookmarksObj
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.codepath.asynchttpclient.AsyncHttpClient
import com.codepath.asynchttpclient.RequestParams
import com.codepath.asynchttpclient.callback.JsonHttpResponseHandler
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.DocumentReference
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.serialization.json.Json
import okhttp3.Headers
import org.json.JSONException

private const val URL = "https://maps.googleapis.com/maps/api/place/details/json"

class BookmarkFragment: Fragment() {
    private lateinit var adapter: PlaceAdapter
    private lateinit var emptyTv: TextView
    private val places = mutableListOf<Place>()
    private val placesDetails = mutableMapOf<String, PlaceDetails>()
    private val bookmarks = BookmarksObj.getBookmarks()

    private val firestore = FirebaseFirestore.getInstance()
    private val userId = FirebaseAuth.getInstance().currentUser?.uid

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val view = inflater.inflate(R.layout.bookmark_fragment, container, false)
        val recyclerView: RecyclerView = view.findViewById(R.id.recyclerView)
        adapter = PlaceAdapter(view.context, places, placesDetails)
        recyclerView.adapter = adapter
        recyclerView.layoutManager = LinearLayoutManager(view.context)
        emptyTv = view.findViewById(R.id.empty)

        lateinit var document: DocumentReference
        if (userId != null) {
            document = firestore.collection("users").document(userId)
            document.get().addOnSuccessListener { documentSnapshot ->
                val firestorebookmarks =
                    documentSnapshot.get("bookmarks") as? MutableList<String> ?: mutableListOf()
                BookmarksObj.setBookmarks(firestorebookmarks)
            }
        }
        loadPlaces()
        return view
    }

    private fun loadPlaces() {
        if (BookmarksObj.getBookmarks().isEmpty()) {
            emptyTv.visibility = View.VISIBLE
            return
        }
        for (id in BookmarksObj.getBookmarks()) {
            val client = AsyncHttpClient()
            val params = RequestParams()
            params["key"] = PLACES_KEY
            params["place_id"] = id
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
                        val placeDetails = parsed.result
                        val place: Place = parsed.result.toPlace(id)

                        placesDetails[id] = placeDetails
                        places.add(place)
                        adapter.notifyItemInserted(places.size - 1)
                    } catch (e: JSONException) {
                        Log.e("", "Exception: ${e}")
                    }
                }
            }]
        }
    }

    private fun PlaceDetails.toPlace(id: String): Place {
        return Place(
            apiId = id,
            name = this.name,
            rating = this.rating,
            photo = this.photos,
            vicinity = this.address,
            formattedAddress = this.address,
            types = this.types
        )
    }

    override fun onDestroyView() {
        super.onDestroyView()
        places.clear()
    }
}