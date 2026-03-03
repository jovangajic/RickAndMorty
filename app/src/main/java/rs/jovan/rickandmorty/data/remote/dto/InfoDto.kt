package rs.jovan.rickandmorty.data.remote.dto

import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class InfoDto(
    val count: Int,
    val pages: Int,
    val next: String?,
    val prev: String?
)