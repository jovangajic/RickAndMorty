package rs.jovan.rickandmorty.data.remote

import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query
import rs.jovan.rickandmorty.data.remote.dto.CharacterDto

interface RickAndMortyApi {

    @GET("characters")
    suspend fun getCharacters(
        @Query("page")page: Int,
        @Query("name")name: String?
    ): List<CharacterDto>


    @GET("character/{id}")
    suspend fun getCharacter(
        @Path("id") id: Int
    ): CharacterDto
}