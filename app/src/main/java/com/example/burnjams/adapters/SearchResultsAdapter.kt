package com.example.burnjams.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.example.burnjams.databinding.ItemSearchResultsBinding
import com.example.burnjams.models.Track

class SearchResultsAdapter(private val onClick: (Track) -> Unit) : ListAdapter<Track, SearchResultsAdapter.SearchResultsViewHolder>(diff) {
    companion object {
        val diff = object : DiffUtil.ItemCallback<Track>() {
            override fun areItemsTheSame(oldItem: Track, newItem: Track): Boolean {
                return oldItem == newItem
            }

            override fun areContentsTheSame(oldItem: Track, newItem: Track): Boolean {
                return oldItem == newItem
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): SearchResultsViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        val binding = ItemSearchResultsBinding.inflate(inflater, parent, false)
        return SearchResultsViewHolder(binding)
    }

    override fun onBindViewHolder(holder: SearchResultsViewHolder, position: Int) {
        holder.onBind(getItem(position), position, onClick)
    }

    class SearchResultsViewHolder(private val binding: ItemSearchResultsBinding) : RecyclerView.ViewHolder(binding.root) {

        fun onBind(track: Track, position: Int, onClick: (Track) -> Unit) {
            val artists = track.artists.joinToString(", ") { artist ->
                artist.name
            }
            binding.apply {
                searchResultTrack.apply {
                    text = "$artists - ${track.name}"
                    setOnClickListener {
                        onClick(track)
                    }
                }
            }
        }
    }
}