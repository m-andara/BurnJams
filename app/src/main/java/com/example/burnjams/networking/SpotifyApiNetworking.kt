package com.example.burnjams.networking

import android.util.Log
import com.example.burnjams.models.Artist
import com.example.burnjams.models.Track
import com.example.burnjams.repository.BurnJamsRepository
import okhttp3.OkHttpClient
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.moshi.MoshiConverterFactory

object SpotifyApiNetworking {

    private val client = OkHttpClient.Builder()
            .build()

    private val spotifyApi: SpotifyApi
        get() {
            return Retrofit.Builder()
                    .baseUrl(BurnJamsRepository.getBaseUrl())
                    .client(client)
                    .addConverterFactory(MoshiConverterFactory.create())
                    .build()
                    .create(SpotifyApi::class.java)
        }

    fun getTracks(search: String, result: String, onFinished: (List<Track>) -> Unit) {

        spotifyApi.getTracks(search = search, authorization = "Bearer $result").enqueue(object : Callback<Result> {
            override fun onResponse(call: Call<Result>, response: Response<Result>) {
                val tracks = response.body()?.items?.items?.map { it ->
                    it.toModel()
                } ?: emptyList()
                onFinished(tracks)
            }

            override fun onFailure(call: Call<Result>, t: Throwable) {
                Log.v("Networking", "Errors: $t")
            }
        })
    }

    fun createPlaylist(result: String, onFinished: (String) -> Unit) {
        val workout = BurnJamsRepository.getCurrentCreateWorkout()
        val postBody = CreatePlaylistBody(
                name = workout.name,
                description = workout.description,
        )
        spotifyApi.createPlaylist(
                user = BurnJamsRepository.getUser(),
                body = postBody,
                authorization = "Bearer $result").enqueue(object : Callback<PlaylistCreatedResponse> {
            override fun onResponse(call: Call<PlaylistCreatedResponse>, response: Response<PlaylistCreatedResponse>) {
                response.body()?.playlistId?.let { onFinished(it) }
            }

            override fun onFailure(call: Call<PlaylistCreatedResponse>, t: Throwable) {
                Log.v("Networking", "Errors: $t")
            }

        })
    }

    fun getUserProfile(result: String) {
        spotifyApi.getUserProfile(authorization = "Bearer $result").enqueue(object : Callback<UserProfile> {
            override fun onResponse(call: Call<UserProfile>, response: Response<UserProfile>) {
                response.body()?.userId?.let { BurnJamsRepository.setUser(it) }
            }

            override fun onFailure(call: Call<UserProfile>, t: Throwable) {
                Log.v("Networking", "Errors: $t")
            }
        })
    }

    fun addTracksToPlaylist(result: String, tracks: String, playlist: String) {
        spotifyApi.addTracksToPlaylist(
                authorization = "Bearer $result",
                tracks = tracks,
                playlist = playlist).enqueue(object : Callback<TracksAddedResponse> {
            override fun onResponse(call: Call<TracksAddedResponse>, response: Response<TracksAddedResponse>) {
                Log.v("Networking", "Response: ${response.body()?.snapshotId}")
            }

            override fun onFailure(call: Call<TracksAddedResponse>, t: Throwable) {
                Log.v("Networking", "Errors: $t")
            }

        })
    }

    fun unfollowPlaylist(result: String, playlist: String) {
        spotifyApi.deletePlaylist(
                authorization = "Bearer $result",
                playlist = playlist).enqueue(object : Callback<Void> {
            override fun onResponse(call: Call<Void>, response: Response<Void>) {

            }

            override fun onFailure(call: Call<Void>, t: Throwable) {
                Log.v("Networking", "Errors: $t")
            }

        })
    }

    private fun SpotifyTrack.toModel(): Track {

        return Track(
                artists = artists.map { it -> it.toModel() },
                duration = duration,
                name = name,
                uri = uri
        )
    }

    private fun SpotifyArtist.toModel(): Artist {

        return Artist(
                name = name
        )
    }
}