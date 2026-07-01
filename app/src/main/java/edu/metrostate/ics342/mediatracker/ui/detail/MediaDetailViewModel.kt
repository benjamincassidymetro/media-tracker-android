package edu.metrostate.ics342.mediatracker.ui.detail

import androidx.lifecycle.ViewModel
import edu.metrostate.ics342.mediatracker.data.FakeMediaRepository
import edu.metrostate.ics342.mediatracker.data.model.Media
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow

class MediaDetailViewModel : ViewModel() {
    // TODO (Week 7): Accept mediaId, call GET /media/{id}, expose MediaDetail state.
    // Also call GET /library to load current status for this item.
    private val _media = MutableStateFlow<Media?>(null)
    val media = _media.asStateFlow()

    fun loadMedia(mediaId: Int) {
        _media.value = FakeMediaRepository.mediaList.firstOrNull { it.id == mediaId }
    }
}