package com.example.burnjams.fragments

import android.content.Intent
import android.os.AsyncTask
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.RecyclerView.ViewHolder
import com.example.burnjams.MainActivity
import com.example.burnjams.PlayWorkoutActivity
import com.example.burnjams.adapters.WorkoutsAdapter
import com.example.burnjams.databinding.FragmentWorkoutsBinding
import com.example.burnjams.entities.Workout
import com.example.burnjams.repository.BurnJamsRepository


class WorkoutsFragment : Fragment() {

    private lateinit var binding: FragmentWorkoutsBinding
    private var workouts = mutableListOf<Workout>()
    private val workoutsAdapter = WorkoutsAdapter() { workout ->
        BurnJamsRepository.setSelectedWorkout(workout)
        val intent = Intent(requireContext(), PlayWorkoutActivity::class.java)
        startActivity(intent)
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        super.onCreateView(inflater, container, savedInstanceState)
        binding = FragmentWorkoutsBinding.inflate(inflater, container, false)
        ItemTouchHelper(
                object : ItemTouchHelper.SimpleCallback(0, ItemTouchHelper.LEFT) {
                    override fun onMove(recyclerView: RecyclerView, viewHolder: ViewHolder, target: ViewHolder): Boolean {
                        return false
                    }

                    override fun onSwiped(viewHolder: ViewHolder, direction: Int) {
                        val position = viewHolder.adapterPosition
                        val workout = workouts[position]
                        deleteWorkout(workout)
                    }
                }).attachToRecyclerView(binding.workoutsList)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.addWorkout.setOnClickListener {
            (activity as MainActivity)
                    .swapFragments(CreateWorkoutFragment())
        }

        binding.workoutsList.apply {
            adapter = workoutsAdapter
            layoutManager = LinearLayoutManager(requireContext(), LinearLayoutManager.VERTICAL, false)
        }
    }

    override fun onResume() {
        super.onResume()
        refreshData()
    }

    private fun refreshData() {
        GetWorkouts() { dbWorkouts ->
            workouts = dbWorkouts as MutableList<Workout>
            workoutsAdapter.submitList(workouts)
            workoutsAdapter.notifyDataSetChanged()
        }.execute()
    }

    private fun deleteWorkout(workout: Workout) {
        BurnJamsRepository.GetToken { result ->
            unfollowPlaylist(result, workout)
        }.execute()
    }

    private fun unfollowPlaylist(result: String, workout: Workout) {

        BurnJamsRepository.deleteWorkout(result, workout) {
            refreshData()
        }
    }

    class GetWorkouts(private val onFinished: (List<Workout>) -> Unit) : AsyncTask<Void, Void, List<Workout>>() {
        override fun doInBackground(vararg params: Void?): List<Workout> {
            return BurnJamsRepository.getDb().burnJamsDao().getWorkouts()
        }

        override fun onPostExecute(result: List<Workout>?) {
            super.onPostExecute(result)
            if (result != null) {
                onFinished(result)
            }
        }
    }
}