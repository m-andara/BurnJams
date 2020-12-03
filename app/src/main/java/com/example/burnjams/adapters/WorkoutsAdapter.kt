package com.example.burnjams.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.example.burnjams.databinding.ItemWorkoutBinding
import com.example.burnjams.entities.Workout

class WorkoutsAdapter(private val onClick: (Workout) -> Unit) : ListAdapter<Workout, WorkoutsAdapter.WorkoutsViewHolder>(diff) {

    companion object {
        val diff = object : DiffUtil.ItemCallback<Workout>() {
            override fun areItemsTheSame(oldItem: Workout, newItem: Workout): Boolean {
                return oldItem == newItem
            }

            override fun areContentsTheSame(oldItem: Workout, newItem: Workout): Boolean {
                return oldItem == newItem
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): WorkoutsViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        val binding = ItemWorkoutBinding.inflate(inflater, parent, false)
        return WorkoutsViewHolder(binding)
    }

    override fun onBindViewHolder(holder: WorkoutsViewHolder, position: Int) {
        holder.onBind(getItem(position), onClick)
    }

    class WorkoutsViewHolder(private val binding: ItemWorkoutBinding) : RecyclerView.ViewHolder(binding.root) {

        fun onBind(workout: Workout, onClick: (Workout) -> Unit) {
            binding.apply {
                itemView.setOnClickListener {
                    onClick(workout)
                }
                workoutName.text = workout.name
                workoutDuration.text = "${workout.durationInMinutes.toString()} mins"
            }
        }
    }
}