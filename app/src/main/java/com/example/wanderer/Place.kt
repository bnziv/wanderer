package com.example.wanderer

import androidx.annotation.Keep
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Keep
@Serializable
data class Results(
    @SerialName("results")
    val results: List<Place>?
)

@Keep
@Serializable
data class Place(
    @SerialName("place_id")
    val apiId: String?,
    @SerialName("name")
    val name: String?,
    @SerialName("rating")
    val rating: Double? = null,
    @SerialName("photos")
    val photo: List<Photo>? = null,
    @SerialName("vicinity")
    val address: String? = null
): java.io.Serializable

@Keep
@Serializable
data class Photo(
    @SerialName("photo_reference")
    val photo_id: String?
): java.io.Serializable