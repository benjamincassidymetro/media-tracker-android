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
    val selectedStatus: LibraryStatus =
        LibraryStatus.WANT_TO,
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

        _uiState.value =
            _uiState.value.copy(
                selectedStatus = status,
                items = emptyList(),
                errorMessage = null
            )

        loadLibrary()
    }

    fun loadLibrary() {
        val selectedStatus = _uiState.value.selectedStatus

        viewModelScope.launch {
            _uiState.value =
                _uiState.value.copy(
                    isLoading = true,
                    errorMessage = null
                )

            try {
                val items =
                    repository.getLibrary(selectedStatus)

                // Make sure an old request does not overwrite a newer tab.
                if (_uiState.value.selectedStatus ==
                    selectedStatus
                ) {
                    _uiState.value =
                        _uiState.value.copy(
                            items = items,
                            isLoading = false
                        )
                }
            } catch (exception: Exception) {
                if (_uiState.value.selectedStatus ==
                    selectedStatus
                ) {
                    _uiState.value =
                        _uiState.value.copy(
                            isLoading = false,
                            errorMessage =
                                readableError(
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
     * Because this screen only displays one status tab,
     * the item is removed from the current list immediately.
     */
    fun updateStatus(
        mediaId: Int,
        newStatus: LibraryStatus
    ) {
        val currentState = _uiState.value
        val previousItems = currentState.items

        val targetItem =
            previousItems.firstOrNull {
                it.mediaId == mediaId
            } ?: return

        if (targetItem.status == newStatus) {
            return
        }

        _uiState.value = currentState.copy(
            items = previousItems.filterNot {
                it.mediaId == mediaId
            }
        )

        viewModelScope.launch {
            try {
                repository.updateLibraryStatus(
                    mediaId = mediaId,
                    status = newStatus
                )
            } catch (exception: Exception) {
                // Roll back to the original list.
                _uiState.value =
                    _uiState.value.copy(
                        items = previousItems
                    )

                _actionError.value =
                    readableError(
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
        val currentState = _uiState.value
        val previousItems = currentState.items

        if (previousItems.none { it.mediaId == mediaId }) {
            return
        }

        _uiState.value = currentState.copy(
            items = previousItems.filterNot {
                it.mediaId == mediaId
            }
        )

        viewModelScope.launch {
            try {
                repository.removeFromLibrary(mediaId)
            } catch (exception: Exception) {
                // Restore the removed item after a genuine failure.
                _uiState.value =
                    _uiState.value.copy(
                        items = previousItems
                    )

                _actionError.value =
                    readableError(
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
