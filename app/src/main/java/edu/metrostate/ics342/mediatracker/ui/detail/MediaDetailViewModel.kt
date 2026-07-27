package edu.metrostate.ics342.mediatracker.ui.detail

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import edu.metrostate.ics342.mediatracker.data.model.LibraryStatus
import edu.metrostate.ics342.mediatracker.data.model.Media
import edu.metrostate.ics342.mediatracker.data.network.AddFavoriteRequest
import edu.metrostate.ics342.mediatracker.data.network.AddToLibraryRequest
import edu.metrostate.ics342.mediatracker.data.network.RetrofitInstance
import kotlinx.coroutines.async
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class MediaDetailViewModel : ViewModel() {

    private val api = RetrofitInstance.mediaApi

    private val _media = MutableStateFlow<Media?>(null)
    val media: StateFlow<Media?> = _media.asStateFlow()

    private val _libraryStatus = MutableStateFlow<LibraryStatus?>(null)
    val libraryStatus: StateFlow<LibraryStatus?> =
        _libraryStatus.asStateFlow()

    private val _isFavorite = MutableStateFlow(false)
    val isFavorite: StateFlow<Boolean> =
        _isFavorite.asStateFlow()

    private val _isLoading = MutableStateFlow(false)
    val isLoading: StateFlow<Boolean> =
        _isLoading.asStateFlow()

    private val _isAddingToLibrary = MutableStateFlow(false)
    val isAddingToLibrary: StateFlow<Boolean> =
        _isAddingToLibrary.asStateFlow()

    private val _isSavingFavorite = MutableStateFlow(false)
    val isSavingFavorite: StateFlow<Boolean> =
        _isSavingFavorite.asStateFlow()

    private val _errorMessage = MutableStateFlow<String?>(null)
    val errorMessage: StateFlow<String?> =
        _errorMessage.asStateFlow()

    private var currentMediaId: Int? = null

    /**
     * Loads:
     * GET /media/{mediaId}
     * GET /library/{mediaId}
     * GET /favorites/{mediaId}
     */
    fun loadMedia(mediaId: Int) {
        currentMediaId = mediaId

        viewModelScope.launch {
            _isLoading.value = true
            _errorMessage.value = null

            try {
                val detailDeferred = async {
                    api.getMediaDetail(mediaId)
                }

                val libraryDeferred = async {
                    api.getLibraryItem(mediaId)
                }

                val favoriteDeferred = async {
                    api.getFavorite(mediaId)
                }

                val detailResponse = detailDeferred.await()

                if (!detailResponse.isSuccessful) {
                    _media.value = null

                    _errorMessage.value = when (detailResponse.code()) {
                        404 -> "Media not found."
                        401 -> "Your session has expired. Please sign in again."
                        else -> {
                            "Unable to load media. " +
                                    "HTTP ${detailResponse.code()}: " +
                                    detailResponse.message()
                        }
                    }

                    return@launch
                }

                val detail = detailResponse.body()

                if (detail == null) {
                    _media.value = null
                    _errorMessage.value =
                        "The server returned an empty media response."
                    return@launch
                }

                _media.value = detail

                val libraryResponse = libraryDeferred.await()

                _libraryStatus.value = when {
                    libraryResponse.isSuccessful ->
                        libraryResponse.body()?.status

                    libraryResponse.code() == 404 ->
                        null

                    else ->
                        null
                }

                val favoriteResponse = favoriteDeferred.await()

                _isFavorite.value = when {
                    favoriteResponse.isSuccessful ->
                        favoriteResponse.body() != null

                    favoriteResponse.code() == 404 ->
                        false

                    else ->
                        false
                }
            } catch (exception: Exception) {
                _media.value = null
                _errorMessage.value =
                    exception.message ?: "Unable to load media."
            } finally {
                _isLoading.value = false
            }
        }
    }

    /**
     * Called by MediaDetailScreen.
     *
     * POST /library
     * Body: mediaId + status "want_to"
     */
    fun addToLibrary(mediaId: Int) {
        currentMediaId = mediaId
        addToWantTo()
    }

    /**
     * Adds the current media item to Want To.
     */
    fun addToWantTo() {
        val mediaId = currentMediaId ?: return

        if (_isAddingToLibrary.value) return
        if (_libraryStatus.value != null) return

        viewModelScope.launch {
            _isAddingToLibrary.value = true
            _errorMessage.value = null

            try {
                val response = api.addToLibrary(
                    AddToLibraryRequest(
                        mediaId = mediaId,
                        status = "want_to"
                    )
                )

                when {
                    response.isSuccessful -> {
                        _libraryStatus.value =
                            response.body()?.status ?: LibraryStatus.WANT_TO
                    }

                    response.code() == 409 -> {
                        refreshLibraryStatus(mediaId)
                    }

                    response.code() == 401 -> {
                        _errorMessage.value =
                            "Your session has expired. Please sign in again."
                    }

                    else -> {
                        _errorMessage.value =
                            "Unable to add item to library. " +
                                    "HTTP ${response.code()}: ${response.message()}"
                    }
                }
            } catch (exception: Exception) {
                _errorMessage.value =
                    exception.message ?: "Unable to add item to library."
            } finally {
                _isAddingToLibrary.value = false
            }
        }
    }

    /**
     * Called by MediaDetailScreen.
     *
     * POST /favorites
     * Body: mediaId
     */
    fun addFavorite(mediaId: Int) {
        currentMediaId = mediaId
        saveFavorite()
    }

    /**
     * Saves the current media item as a favorite.
     */
    fun saveFavorite() {
        val mediaId = currentMediaId ?: return

        if (_isSavingFavorite.value) return
        if (_isFavorite.value) return

        viewModelScope.launch {
            _isSavingFavorite.value = true
            _errorMessage.value = null

            try {
                val response = api.addFavorite(
                    AddFavoriteRequest(mediaId = mediaId)
                )

                when {
                    response.isSuccessful -> {
                        _isFavorite.value = true
                    }

                    response.code() == 409 -> {
                        // Already saved is an acceptable result.
                        _isFavorite.value = true
                    }

                    response.code() == 401 -> {
                        _errorMessage.value =
                            "Your session has expired. Please sign in again."
                    }

                    else -> {
                        _errorMessage.value =
                            "Unable to save favorite. " +
                                    "HTTP ${response.code()}: ${response.message()}"
                    }
                }
            } catch (exception: Exception) {
                _errorMessage.value =
                    exception.message ?: "Unable to save favorite."
            } finally {
                _isSavingFavorite.value = false
            }
        }
    }

    fun retry() {
        currentMediaId?.let(::loadMedia)
    }

    fun clearError() {
        _errorMessage.value = null
    }

    private suspend fun refreshLibraryStatus(mediaId: Int) {
        val response = api.getLibraryItem(mediaId)

        _libraryStatus.value = when {
            response.isSuccessful ->
                response.body()?.status

            response.code() == 404 ->
                null

            else ->
                _libraryStatus.value
        }
    }
}