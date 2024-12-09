package com.example.wanderer

import androidx.annotation.Keep
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Keep
@Serializable
data class Result(
    @SerialName("result")
    val result: PlaceDetails
)

@Keep
@Serializable
data class PlaceDetails (
    @SerialName("name")
    val name: String? = null,
    @SerialName("formatted_address")
    val address: String? = null,
    @SerialName("formatted_phone_number")
    val phoneNum: String? = null,
    @SerialName("reviews")
    val reviews: List<Review>? = null,
    @SerialName("url")
    val googleUrl: String? = null,
    @SerialName("opening_hours")
    val opening: Opening? = null,
    @SerialName("editorial_summary")
    val summary: Summary? = null,
    @SerialName("photos")
    val photos: List<Photo>? = null,
    @SerialName("types")
    val types: List<String>? = null,
    @SerialName("rating")
    val rating: Double? = null
): java.io.Serializable {
    val firstPhoto: Photo?
        get() = photos?.firstOrNull()
}

@Keep
@Serializable
data class Review (
    @SerialName("text")
    val text: String?,
    @SerialName("relative_time_description")
    val time: String?,
    @SerialName("rating")
    val rating: Int
): java.io.Serializable

@Keep
@Serializable
data class Opening (
    @SerialName("open_now")
    val openNow: Boolean,
    @SerialName("weekday_text")
    val hours: List<String>?
): java.io.Serializable

@Keep
@Serializable
data class Summary (
    @SerialName("overview")
    val overview: String?
): java.io.Serializable