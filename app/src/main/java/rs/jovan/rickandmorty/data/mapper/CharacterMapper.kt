package rs.jovan.rickandmorty.data.mapper

import rs.jovan.rickandmorty.data.local.entity.CharacterEntity
import rs.jovan.rickandmorty.data.remote.dto.CharacterDto
import rs.jovan.rickandmorty.domain.model.Character

fun CharacterDto.toEntity(): CharacterEntity {
    return CharacterEntity(
        id = id,
        name = name,
        status = status,
        species = species,
        gender = gender,
        image = image,
        locationName = location.name,
        locationUrl = location.url,
        locationImageUrl = null,
        isFavorite = false
    )
}

fun CharacterEntity.toDomain(): Character {
    return Character(
        id = id,
        name = name,
        status = status,
        species = species,
        gender = gender,
        image = image,
        locationName = locationName,
        locationUrl = locationUrl,
        locationImageUrl = locationImageUrl,
        isFavorite = isFavorite
    )
}
