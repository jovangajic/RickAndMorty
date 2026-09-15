package rs.jovan.rickandmorty.core.data.remote.dto

import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class CharacterResponseDto(
    val info: InfoDto,
    val results: List<CharacterDto>
)
