package rs.jovan.rickandmorty.core.data.remote.dto

import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class LocationDto(
    val name: String,
    val residents: List<String>
)
