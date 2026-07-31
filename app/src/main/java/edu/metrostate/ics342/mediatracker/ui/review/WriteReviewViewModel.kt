package edu.metrostate.ics342.mediatracker.ui.review

import androidx.lifecycle.ViewModel
import edu.metrostate.ics342.mediatracker.data.FakeMediaRepository
import edu.metrostate.ics342.mediatracker.data.model.Media
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow

class WriteReviewViewModel : ViewModel() {
    private val _media = MutableStateFlow<Media?>(null)
    val media: StateFlow<Media?> = _media.asStateFlow()

    private val _rating = MutableStateFlow(0)
    val rating: StateFlow<Int> = _rating.asStateFlow()

    private val _reviewText = MutableStateFlow("")
    val reviewText: StateFlow<String> = _reviewText.asStateFlow()

    private val _isSubmitting = MutableStateFlow(false)
    val isSubmitting: StateFlow<Boolean> = _isSubmitting.asStateFlow()

    fun loadMedia(mediaId: Int) {
        // Mocking finding the media from fake repo
        _media.value = FakeMediaRepository.mediaList.find { it.id == mediaId }
    }

    fun onRatingChange(value: Int) { 
        _rating.value = value 
    }

    fun onReviewTextChange(value: String) {
        _reviewText.value = value
    }

    fun submitReview(mediaId: Int, onSuccess: () -> Unit) {
        _isSubmitting.value = true
        // Simulating network delay
        onSuccess()
    }
}
