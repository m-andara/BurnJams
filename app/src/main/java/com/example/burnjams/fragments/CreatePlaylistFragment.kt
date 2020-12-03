package com.example.burnjams.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.burnjams.adapters.SearchResultsAdapter
import com.example.burnjams.databinding.FragmentCreatePlaylistBinding
import com.example.burnjams.models.Track
import com.example.burnjams.networking.SpotifyApiNetworking
import com.example.burnjams.repository.BurnJamsRepository

class CreatePlaylistFragment : Fragment() {

    private lateinit var binding: FragmentCreatePlaylistBinding
    private val searchResultsAdapter = SearchResultsAdapter() { track ->
        addTrackToWorkout(track)
        Toast.makeText(requireContext(), "${track.name} was added to your workout playlist", Toast.LENGTH_LONG).show()
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        super.onCreateView(inflater, container, savedInstanceState)
        binding = FragmentCreatePlaylistBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.createPlaylistSearchButton.setOnClickListener {
            onSearchClicked()
        }
        binding.createPlaylistDoneButton.setOnClickListener {
            onDoneClicked()
        }
        binding.createPlaylistSearchResults.apply {
            adapter = searchResultsAdapter
            layoutManager = LinearLayoutManager(requireContext(), LinearLayoutManager.VERTICAL, false)
            addItemDecoration(DividerItemDecoration(requireContext(), DividerItemDecoration.VERTICAL))
        }
    }

    private fun onSearchClicked() {
        BurnJamsRepository.GetToken { result ->
            val search = binding.createPlaylistSearch.editText?.text.toString()
            if (!search.isNullOrEmpty()) {
                SpotifyApiNetworking.getTracks(search, result) { tracks ->
                    updateSearchResults(tracks)
                }
            }
            binding.createPlaylistSearch.editText?.setText("")
        }.execute()
    }

    private fun updateSearchResults(tracks: List<Track>) {
        searchResultsAdapter.submitList(tracks)
        searchResultsAdapter.notifyDataSetChanged()
    }

    private fun addTrackToWorkout(track: Track) {
        BurnJamsRepository.addSongToWorkout(
                track.name,
                track.artists.joinToString(", ") { it.name },
                track.duration,
                track.uri
        )
        updateSearchResults(emptyList())
    }

    private fun onDoneClicked() {
        activity?.onBackPressed()
    }
}