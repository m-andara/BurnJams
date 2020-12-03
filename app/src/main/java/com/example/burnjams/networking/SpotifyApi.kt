package com.example.burnjams.networking

import retrofit2.Call
import retrofit2.http.*

interface SpotifyApi {

    @GET("/v1/search")
    fun getTracks(
            @Header("Authorization") authorization: String,
            @Query("q") search: String,
            @Query("type") type: String = "track"
    ): Call<Result>

    @POST("/v1/users/{user_id}/playlists")
    fun createPlaylist(
            @Header("Authorization") authorization: String,
            @Path("user_id") user: String,
            @Body() body: CreatePlaylistBody
    ): Call<PlaylistCreatedResponse>

    @GET("/v1/me")
    fun getUserProfile(
            @Header("Authorization") authorization: String
    ): Call<UserProfile>

    @POST("/v1/playlists/{playlist_id}/tracks")
    fun addTracksToPlaylist(
            @Header("Authorization") authorization: String,
            @Path("playlist_id") playlist: String,
            @Query("uris") tracks: String
    ): Call<TracksAddedResponse>

    @DELETE("/v1/playlists/{playlist_id}/followers")
    fun deletePlaylist(
            @Header("Authorization") authorization: String,
            @Path("playlist_id") playlist: String
    ): Call<Void>
}

