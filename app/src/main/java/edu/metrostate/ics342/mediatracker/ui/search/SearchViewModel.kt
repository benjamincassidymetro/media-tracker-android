package edu.metrostate.ics342.mediatracker.ui.search

import androidx.lifecycle.ViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow

data class MediaItem(
    val id: Int,
    val title: String,
    val subtitle: String,
    val type: String,
    val year: Int,
    val rating: Double
)

class SearchViewModel : ViewModel() {

    private val _results = MutableStateFlow(
        listOf(
            MediaItem(
                id = 1,
                title = "Dune",
                subtitle = "Frank Herbert",
                type = "Book",
                year = 1965,
                rating = 4.8
            ),
            MediaItem(
                id = 2,
                title = "Inception",
                subtitle = "Christopher Nolan",
                type = "Movie",
                year = 2010,
                rating = 4.7
            ),
            MediaItem(
                id = 3,
                title = "Severance",
                subtitle = "Dan Erickson",
                type = "Show",
                year = 2022,
                rating = 4.9
            ),
            MediaItem(
                id = 4,
                title = "Last of the Mohicans",
                subtitle = "Michael Mann",
                type = "Movie",
                year = 1992,
                rating = 4.5
            )
        )
    )

    val results = _results.asStateFlow()
}