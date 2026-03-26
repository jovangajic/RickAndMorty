package rs.jovan.rickandmorty.domain.model

data class Character(
    val id: Int,
    val name: String,
    val status: String,
    val species: String,
    val gender: String,
    val image: String,
    val locationName: String,
    val locationUrl: String,
    val locationImageUrl: String?,
    val isFavorite: Boolean
)