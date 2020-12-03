package com.example.burnjams.networking

import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class Result(
        @Json(name = "tracks") val items: SpotifyTracks
)

@JsonClass(generateAdapter = true)
data class SpotifyTracks(
        @Json(name = "items") val items: List<SpotifyTrack>
)

@JsonClass(generateAdapter = true)
data class SpotifyTrack(
        @Json(name = "artists") val artists: List<SpotifyArtist>,
        @Json(name = "duration_ms") val duration: Int,
        @Json(name = "name") val name: String,
        @Json(name = "uri") val uri: String
)

@JsonClass(generateAdapter = true)
data class SpotifyArtist(
        @Json(name = "name") val name: String
)

@JsonClass(generateAdapter = true)
data class CreatePlaylistBody(
        @Json(name = "name") val name: String,
        @Json(name = "description") val description: String,
        @Json(name = "public") val public: Boolean = false,
)

@JsonClass(generateAdapter = true)
data class PlaylistCreatedResponse(
        @Json(name = "id") val playlistId: String
)

@JsonClass(generateAdapter = true)
data class UserProfile(
        @Json(name = "id") val userId: String
)

@JsonClass(generateAdapter = true)
data class TracksAddedResponse(
        @Json(name = "snapshot_id") val snapshotId: String
)


