package edu.metrostate.ics342.mediatracker.ui.search

import androidx.lifecycle.ViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow

data class MediaItem(
    val id: Int,
    val title: String,
    val subtitle: String
)

class SearchViewModel : ViewModel() {

    private val _results = MutableStateFlow(
        listOf(
            MediaItem(1, "Dune", "Frank Herbert"),
            MediaItem(2, "Inception", "Christopher Nolan"),
            MediaItem(3, "Severance", "Dan Erickson"),
            MediaItem(4, "Last of the Mohicans", "Michael Mann")
        )
    )

    val results = _results.asStateFlow()
}