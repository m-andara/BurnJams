package com.example.burnjams

import android.os.Bundle
import android.os.CountDownTimer
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import com.example.burnjams.databinding.ActivityPlayWorkoutBinding
import com.example.burnjams.repository.BurnJamsRepository
import com.example.burnjams.utils.TimeConversion
import com.spotify.android.appremote.api.Connector
import com.spotify.android.appremote.api.SpotifyAppRemote
import java.util.*

class PlayWorkoutActivity : AppCompatActivity() {

    private lateinit var binding: ActivityPlayWorkoutBinding
    private var spotifyAppRemote: SpotifyAppRemote? = null
    private val playlist = BurnJamsRepository.getSelectedWorkout().playlistId
    private var count = BurnJamsRepository.getSelectedWorkout().durationInMilliseconds.toLong()
    private lateinit var countDownTimer: CountDownTimer
    private val pause = "PAUSE"
    private val start = "START"

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityPlayWorkoutBinding.inflate(layoutInflater)
        setContentView(binding.root)
        binding.controlButton.apply {
            setOnClickListener {
                controlWorkout()
            }
            text = pause
        }

        binding.time.text = TimeConversion.milToMin(count)

        countDownTimer = createTimer(count)
    }

    private fun createTimer(countVal: Long): CountDownTimer {

        return object : CountDownTimer(countVal, 1000) {
            override fun onTick(millisUntilFinished: Long) {
                count -= 1000
                val time = TimeConversion.milToMin(millisUntilFinished)
                binding.time.text = time
            }

            override fun onFinish() {
                binding.time.text = "Finished"
                alertWorkoutFinished().show()
            }
        }
    }

    override fun onStart() {
        super.onStart()
        SpotifyAppRemote.connect(this, BurnJamsRepository.getConnParams(getString(R.string.spotify_client_id)), object : Connector.ConnectionListener {
            override fun onConnected(appRemote: SpotifyAppRemote) {
                spotifyAppRemote = appRemote
                startWorkout()
                connected()
            }

            override fun onFailure(throwable: Throwable) {
            }
        })
    }

    private fun connected() {
        spotifyAppRemote?.playerApi?.subscribeToPlayerState()?.setEventCallback { playerState ->
            val songArtists = playerState.track.artists.joinToString(", ") { artist ->
                artist.name
            }
            val currentSong = "${playerState.track.name}: $songArtists"
            binding.currentSong.text = currentSong
        }
    }

    override fun onStop() {
        super.onStop()
        spotifyAppRemote?.playerApi?.pause()
        spotifyAppRemote?.let {
            SpotifyAppRemote.disconnect(it)
        }
    }

    private fun controlWorkout() {
        if (binding.controlButton.text.toString().toUpperCase(Locale.ROOT) == "START") {

            spotifyAppRemote?.playerApi?.resume()
            binding.controlButton.text = pause
            countDownTimer = createTimer(count)
            countDownTimer.start()
        } else {

            binding.controlButton.text = start
            spotifyAppRemote?.playerApi?.pause()
            countDownTimer.cancel()
        }
    }

    private fun alertWorkoutFinished(): AlertDialog {
        spotifyAppRemote?.playerApi?.pause()
        return AlertDialog.Builder(this)
                .setMessage("Great job on finishing your workout!")
                .setCancelable(false)
                .setPositiveButton("Finish") { _, _ ->
                    stopPlayer()
                }
                .create()
    }

    private fun stopPlayer() {
        spotifyAppRemote?.playerApi?.pause()
        finish()
    }

    private fun startWorkout() {

        spotifyAppRemote
                ?.playerApi
                ?.play("spotify:playlist:$playlist")
        countDownTimer.start()
    }

    override fun onBackPressed() {
        stopPlayer()
    }

}