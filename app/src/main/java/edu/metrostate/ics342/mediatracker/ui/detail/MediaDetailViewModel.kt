package edu.metrostate.ics342.mediatracker.ui.detail

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import edu.metrostate.ics342.mediatracker.data.model.LibraryStatus
import edu.metrostate.ics342.mediatracker.data.model.Media
import edu.metrostate.ics342.mediatracker.data.repository.ApiException
import edu.metrostate.ics342.mediatracker.data.repository.DefaultMediaRepository
import edu.metrostate.ics342.mediatracker.data.repository.MediaRepository
import kotlinx.coroutines.async
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

sealed class MediaDetailUiState {
    data object Loading : MediaDetailUiState()

    data class Success(
        val media: Media,
        val libraryStatus: LibraryStatus?,
        val isFavorite: Boolean
    ) : MediaDetailUiState()

    data object NotFound : MediaDetailUiState()

    data class Error(
        val message: String
    ) : MediaDetailUiState()
}

class MediaDetailViewModel(
    private val repository: MediaRepository = DefaultMediaRepository()
) : ViewModel() {

    private val _uiState =
        MutableStateFlow<MediaDetailUiState>(
            MediaDetailUiState.Loading
        )

    val uiState: StateFlow<MediaDetailUiState> =
        _uiState.asStateFlow()

    private val _actionError =
        MutableStateFlow<String?>(null)

    val actionError: StateFlow<String?> =
        _actionError.asStateFlow()

    private var currentMediaId: Int? = null

    fun load(mediaId: Int) {
        currentMediaId = mediaId

        viewModelScope.launch {
            _uiState.value = MediaDetailUiState.Loading

            try {
                val mediaDeferred = async {
                    repository.getMediaDetail(mediaId)
                }

                val libraryDeferred = async {
                    repository.getLibraryItem(mediaId)
                }

                val favoriteDeferred = async {
                    repository.getFavorite(mediaId)
                }

                val media = mediaDeferred.await()
                val libraryItem = libraryDeferred.await()
                val favorite = favoriteDeferred.await()

                _uiState.value = MediaDetailUiState.Success(
                    media = media,
                    libraryStatus = libraryItem?.status,
                    isFavorite = favorite != null
                )
            } catch (exception: ApiException) {
                _uiState.value =
                    if (exception.code == 404) {
                        MediaDetailUiState.NotFound
                    } else {
                        MediaDetailUiState.Error(
                            apiErrorMessage(exception)
                        )
                    }
            } catch (exception: Exception) {
                _uiState.value =
                    MediaDetailUiState.Error(
                        exception.message
                            ?: "Unable to load media."
                    )
            }
        }
    }

    /**
     * Optimistically adds the media item to Want To.
     */
    fun addToLibrary() {
        val mediaId = currentMediaId ?: return
        val state = _uiState.value as? MediaDetailUiState.Success
            ?: return

        if (state.libraryStatus != null) {
            return
        }

        val previousState = state

        _uiState.value = state.copy(
            libraryStatus = LibraryStatus.WANT_TO
        )

        viewModelScope.launch {
            try {
                val item = repository.addToLibrary(
                    mediaId = mediaId,
                    status = LibraryStatus.WANT_TO
                )

                val current =
                    _uiState.value as? MediaDetailUiState.Success
                        ?: return@launch

                _uiState.value = current.copy(
                    libraryStatus =
                        item?.status ?: LibraryStatus.WANT_TO
                )
            } catch (exception: Exception) {
                // Roll back the optimistic update.
                _uiState.value = previousState
                _actionError.value =
                    actionErrorMessage(
                        exception,
                        "Unable to add item to library."
                    )
            }
        }
    }

    /**
     * Optimistically toggles the favorite state.
     */
    fun toggleFavorite() {
        val mediaId = currentMediaId ?: return
        val state = _uiState.value as? MediaDetailUiState.Success
            ?: return

        val previousState = state
        val shouldFavorite = !state.isFavorite

        _uiState.value = state.copy(
            isFavorite = shouldFavorite
        )

        viewModelScope.launch {
            try {
                if (shouldFavorite) {
                    repository.addFavorite(mediaId)
                } else {
                    repository.removeFavorite(mediaId)
                }
            } catch (exception: Exception) {
                // Restore the original heart state.
                _uiState.value = previousState
                _actionError.value =
                    actionErrorMessage(
                        exception,
                        if (shouldFavorite) {
                            "Unable to save favorite."
                        } else {
                            "Unable to remove favorite."
                        }
                    )
            }
        }
    }

    fun retry() {
        currentMediaId?.let(::load)
    }

    fun clearActionError() {
        _actionError.value = null
    }

    private fun apiErrorMessage(
        exception: ApiException
    ): String {
        return when (exception.code) {
            401 -> {
                "Your session has expired. Please sign in again."
            }

            else -> exception.message
        }
    }

    private fun actionErrorMessage(
        exception: Exception,
        fallback: String
    ): String {
        return when {
            exception is ApiException &&
                    exception.code == 401 -> {
                "Your session has expired. Please sign in again."
            }

            !exception.message.isNullOrBlank() -> {
                exception.message.orEmpty()
            }

            else -> fallback
        }
    }
}