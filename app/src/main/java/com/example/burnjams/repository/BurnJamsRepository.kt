package com.example.burnjams.repository

import android.content.Context
import android.os.AsyncTask
import androidx.room.Room
import com.example.burnjams.entities.Track
import com.example.burnjams.entities.Workout
import com.example.burnjams.networking.SpotifyApiNetworking
import com.example.burnjams.utils.TimeConversion
import com.spotify.android.appremote.api.ConnectionParams
import com.spotify.sdk.android.auth.AuthorizationRequest
import com.spotify.sdk.android.auth.AuthorizationResponse

object BurnJamsRepository {

    private val redirectUri = "http://com.example.burnjams/callback"
    private const val DATABASE_NAME = "BurnJams-Database"
    private lateinit var db: BurnJamsDatabase
    private var currentUser = ""
    private val baseUrl = "https://api.spotify.com/"
    private var selectedWorkout = Workout(
            name = "",
            description = "",
            playlistId = ""
    )
    private var currentCreateSongs = mutableListOf<Track>()
    private var currentCreateWorkout = Workout(
            name = "",
            description = "",
            playlistId = ""
    )

    fun setSelectedWorkout(workout: Workout) {
        selectedWorkout.name = workout.name
        selectedWorkout.durationInMinutes = workout.durationInMinutes
        selectedWorkout.durationInMilliseconds = workout.durationInMilliseconds
        selectedWorkout.description = workout.description
        selectedWorkout.playlistId = workout.playlistId
    }

    fun getSelectedWorkout(): Workout {
        return selectedWorkout
    }


    fun setUser(user: String) {
        currentUser = user
    }

    fun getUser(): String {
        return currentUser
    }

    fun addSongToWorkout(name: String, artists: String, duration: Int, uri: String) {
        val track = Track(
                name = name,
                artists = artists,
                duration = duration,
                uri = uri
        )
        currentCreateSongs.add(track)
        currentCreateWorkout.durationInMilliseconds += duration
        currentCreateWorkout.durationInMinutes = TimeConversion.milToMin(
                currentCreateWorkout.durationInMilliseconds
        )
    }

    fun getCurrentCreateWorkout(): Workout {
        return currentCreateWorkout
    }

    fun setCurrentCreateWorkout(name: String, description: String) {
        currentCreateWorkout.name = name
        currentCreateWorkout.description = description
    }

    fun getBaseUrl(): String {
        return baseUrl
    }

    fun getConnParams(client: String): ConnectionParams {
        return ConnectionParams.Builder(client)
                .setRedirectUri(redirectUri)
                .showAuthView(true)
                .build()
    }

    fun inItDb(context: Context): BurnJamsDatabase {
        db = Room.databaseBuilder(
                context,
                BurnJamsDatabase::class.java,
                DATABASE_NAME
        )
                .fallbackToDestructiveMigration()
                .build()

        return db
    }

    fun getDb(): BurnJamsDatabase {
        return db
    }

    fun getAuthenticationRequest(type: AuthorizationResponse.Type, clientId: String): AuthorizationRequest {
        return AuthorizationRequest.Builder(clientId, type, redirectUri)
                .setShowDialog(false)
                .setScopes(arrayOf(
                        "user-read-private",
                        "playlist-read",
                        "playlist-read-private",
                        "streaming",
                        "user-read-playback-state",
                        "app-remote-control",
                        "playlist-modify-public",
                        "user-modify-playback-state",
                        "playlist-modify-private",
                        "user-read-currently-playing",
                        "playlist-read-private"
                ))
                .build()
    }

    fun createPlaylist(onFinished: () -> Unit) {
        AsyncTask.execute {
            val result = db.burnJamsDao().getToken()
            SpotifyApiNetworking.createPlaylist(result) { playlist ->
                val tracks = currentCreateSongs.joinToString(",") { it.uri }
                SpotifyApiNetworking.addTracksToPlaylist(result = result, tracks = tracks, playlist = playlist)
                currentCreateWorkout.playlistId = playlist
                var playlistTime = 0
                for (track in currentCreateSongs) playlistTime += track.duration
                saveCurrentWorkout()
                onFinished()
            }
        }
    }

    fun saveCurrentWorkout() {
        SaveWorkout() {
            clearCurrentCreate()
        }.execute()
    }

    fun deleteWorkout(result: String, workout: Workout, onFinished: () -> Unit) {
        DeleteWorkout(workout) {
            onFinished()
        }.execute()
        SpotifyApiNetworking.unfollowPlaylist(result, workout.playlistId)
    }

    private fun clearCurrentCreate() {
        currentCreateSongs.clear()
        currentCreateWorkout = Workout(
                name = "",
                description = "",
                playlistId = ""
        )
    }

    class SaveWorkout(private val onFinished: () -> Unit) : AsyncTask<Void, Void, Long>() {
        override fun doInBackground(vararg params: Void?): Long? {
            return db.burnJamsDao().add(getCurrentCreateWorkout())
        }

        override fun onPostExecute(result: Long?) {
            super.onPostExecute(result)
            if (result != null) {
                onFinished()
            }
        }
    }

    class DeleteWorkout(private val workout: Workout, private val onFinished: () -> Unit) : AsyncTask<Void, Void, Int>() {
        override fun doInBackground(vararg params: Void?): Int? {
            db.burnJamsDao().delete(workout)
            return 0
        }

        override fun onPostExecute(result: Int?) {
            super.onPostExecute(result)
            if (result != null) {
                onFinished()
            }
        }
    }

    class GetToken(private val onFinished: (String) -> Unit) : AsyncTask<Void, Void, String>() {

        private val db: BurnJamsDatabase = BurnJamsRepository.getDb()

        override fun doInBackground(vararg params: Void?): String {
            return db.burnJamsDao().getToken()
        }

        override fun onPostExecute(result: String?) {
            if (result != null) {
                onFinished(result)
            }
        }
    }
}