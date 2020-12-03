package com.example.burnjams.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.fragment.app.Fragment
import com.example.burnjams.CreateWorkoutActivity
import com.example.burnjams.databinding.FragmentCreateWorkoutBinding
import com.example.burnjams.repository.BurnJamsRepository

class CreateWorkoutFragment : Fragment() {

    private lateinit var binding: FragmentCreateWorkoutBinding

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        super.onCreateView(inflater, container, savedInstanceState)
        binding = FragmentCreateWorkoutBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.workoutSavePlaylist.setOnClickListener {
            if (BurnJamsRepository.getCurrentCreateWorkout().durationInMilliseconds != 0) {
                BurnJamsRepository.createPlaylist() {
                    activity?.finish()
                }
            } else {
                AlertDialog.Builder(requireContext())
                        .setMessage("You must add songs to your playlist in order to save a workout.")
                        .setCancelable(true)
                        .create()
                        .show()
            }

        }

        binding.workoutCreatePlaylist.setOnClickListener {
            val name = binding.workoutName.editText?.text.toString()
            val description = binding.workoutDescription.editText?.text.toString()

            if (name.isEmpty() || description.isEmpty()) {
                Toast.makeText(requireContext(), "Must fill out all boxes", Toast.LENGTH_LONG).show()
            } else {
                BurnJamsRepository.setCurrentCreateWorkout(name, description)
                (activity as CreateWorkoutActivity).swapFragments(CreatePlaylistFragment())
            }
        }
    }

    override fun onResume() {
        super.onResume()
        if (BurnJamsRepository.getCurrentCreateWorkout().name.isNotEmpty()) {
            val workout = BurnJamsRepository.getCurrentCreateWorkout()
            binding.apply {
                workoutName.editText?.setText(workout.name)
                var duration = BurnJamsRepository.getCurrentCreateWorkout().durationInMinutes
                workoutDuration.text = "Workout Duration: $duration mins"
                workoutDescription.editText?.setText(workout.description)
            }
        }
    }
}