package rs.jovan.rickandmorty.core.data.remote.dto

import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class LocationReferenceDto(
    val name: String,
    val url: String
)
