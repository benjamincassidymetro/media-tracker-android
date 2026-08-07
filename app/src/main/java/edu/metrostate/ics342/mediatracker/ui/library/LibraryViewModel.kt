package edu.metrostate.ics342.mediatracker.ui.library

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import edu.metrostate.ics342.mediatracker.data.model.LibraryItem
import edu.metrostate.ics342.mediatracker.data.model.LibraryStatus
import edu.metrostate.ics342.mediatracker.data.repository.ApiException
import edu.metrostate.ics342.mediatracker.data.repository.DefaultMediaRepository
import edu.metrostate.ics342.mediatracker.data.repository.MediaRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

data class LibraryUiState(
    val selectedStatus: LibraryStatus = LibraryStatus.WANT_TO,
    val items: List<LibraryItem> = emptyList(),
    val isLoading: Boolean = false,
    val errorMessage: String? = null
)

class LibraryViewModel(
    private val repository: MediaRepository =
        DefaultMediaRepository()
) : ViewModel() {

    private val _uiState =
        MutableStateFlow(LibraryUiState())

    val uiState: StateFlow<LibraryUiState> =
        _uiState.asStateFlow()

    private val _actionError =
        MutableStateFlow<String?>(null)

    val actionError: StateFlow<String?> =
        _actionError.asStateFlow()

    init {
        loadLibrary()
    }

    fun selectStatus(status: LibraryStatus) {
        if (_uiState.value.selectedStatus == status) {
            return
        }

        _uiState.value = _uiState.value.copy(
            selectedStatus = status,
            items = emptyList(),
            isLoading = false,
            errorMessage = null
        )

        loadLibrary()
    }

    fun loadLibrary() {
        val requestedStatus =
            _uiState.value.selectedStatus

        viewModelScope.launch {
            _uiState.value = _uiState.value.copy(
                isLoading = true,
                errorMessage = null
            )

            try {
                val items =
                    repository.getLibrary(requestedStatus)

                /*
                 * Do not let an older network request overwrite
                 * a newer tab selection.
                 */
                if (
                    _uiState.value.selectedStatus ==
                    requestedStatus
                ) {
                    _uiState.value =
                        _uiState.value.copy(
                            items = items,
                            isLoading = false,
                            errorMessage = null
                        )
                }
            } catch (exception: Exception) {
                if (
                    _uiState.value.selectedStatus ==
                    requestedStatus
                ) {
                    _uiState.value =
                        _uiState.value.copy(
                            isLoading = false,
                            errorMessage = readableError(
                                exception,
                                "Unable to load your library."
                            )
                        )
                }
            }
        }
    }

    /**
     * Optimistically changes an item's status.
     *
     * Since the screen displays one status tab at a time,
     * the item disappears immediately after being moved.
     */
    fun updateStatus(
        mediaId: Int,
        newStatus: LibraryStatus
    ) {
        val originalState = _uiState.value
        val originalStatus =
            originalState.selectedStatus
        val previousItems =
            originalState.items

        val targetItem =
            previousItems.firstOrNull { item ->
                item.mediaId == mediaId
            } ?: return

        if (targetItem.status == newStatus) {
            return
        }

        _uiState.value = originalState.copy(
            items = previousItems.filterNot { item ->
                item.mediaId == mediaId
            },
            errorMessage = null
        )

        viewModelScope.launch {
            try {
                repository.updateLibraryStatus(
                    mediaId = mediaId,
                    status = newStatus
                )
            } catch (exception: Exception) {
                /*
                 * Only restore the old list if the user is
                 * still viewing the same tab.
                 */
                if (
                    _uiState.value.selectedStatus ==
                    originalStatus
                ) {
                    _uiState.value =
                        _uiState.value.copy(
                            items = previousItems
                        )
                }

                _actionError.value = readableError(
                    exception,
                    "Unable to update library status."
                )
            }
        }
    }

    /**
     * Optimistically removes an item.
     */
    fun removeItem(mediaId: Int) {
        val originalState = _uiState.value
        val originalStatus =
            originalState.selectedStatus
        val previousItems =
            originalState.items

        val itemExists =
            previousItems.any { item ->
                item.mediaId == mediaId
            }

        if (!itemExists) {
            return
        }

        _uiState.value = originalState.copy(
            items = previousItems.filterNot { item ->
                item.mediaId == mediaId
            },
            errorMessage = null
        )

        viewModelScope.launch {
            try {
                repository.removeFromLibrary(mediaId)
            } catch (exception: Exception) {
                /*
                 * Avoid restoring Want To items into another
                 * tab if the user switched tabs meanwhile.
                 */
                if (
                    _uiState.value.selectedStatus ==
                    originalStatus
                ) {
                    _uiState.value =
                        _uiState.value.copy(
                            items = previousItems
                        )
                }

                _actionError.value = readableError(
                    exception,
                    "Unable to remove item from library."
                )
            }
        }
    }

    fun clearActionError() {
        _actionError.value = null
    }

    private fun readableError(
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