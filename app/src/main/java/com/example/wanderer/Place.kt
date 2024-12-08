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
    val vicinity: String? = "",
    @SerialName("formatted_address")
    val formattedAddress: String? = "",
    @SerialName("types")
    val types: List<String>?
): java.io.Serializable {
    val address: String
        get() = formattedAddress?.let {
            val regex = Regex("^(.*),\\s[A-Za-z]{2}\\s[0-9]{5},\\s[A-Za-z\\s]+$")
            val matchResult = regex.find(it)
            matchResult?.groups?.get(1)?.value ?: it
        } ?: ""
}

@Keep
@Serializable
data class Photo(
    @SerialName("photo_reference")
    val photoId: String?
): java.io.Serializable