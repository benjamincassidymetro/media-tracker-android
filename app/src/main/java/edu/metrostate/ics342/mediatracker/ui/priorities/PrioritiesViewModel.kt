package edu.metrostate.ics342.mediatracker.ui.priorities

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import edu.metrostate.ics342.mediatracker.data.model.LibraryItem
import edu.metrostate.ics342.mediatracker.data.model.Priority
import edu.metrostate.ics342.mediatracker.data.repository.ApiException
import edu.metrostate.ics342.mediatracker.data.repository.DefaultMediaRepository
import edu.metrostate.ics342.mediatracker.data.repository.MediaRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

data class PrioritiesUiState(
    val isLoading: Boolean = false,
    val priorities: List<Priority> = emptyList(),
    val errorMessage: String? = null,
    val isSaving: Boolean = false
) {
    val isFull: Boolean
        get() = priorities.size >= MAX_PRIORITIES

    companion object {
        const val MAX_PRIORITIES = 5
    }
}

class PrioritiesViewModel(
    private val repository: MediaRepository = DefaultMediaRepository()
) : ViewModel() {

    private val _uiState = MutableStateFlow(PrioritiesUiState())
    val uiState: StateFlow<PrioritiesUiState> =
        _uiState.asStateFlow()

    fun loadPriorities() {
        viewModelScope.launch {
            _uiState.value = _uiState.value.copy(
                isLoading = true,
                errorMessage = null
            )

            try {
                val priorities = repository
                    .getPriorities()
                    .sortedBy { it.orderIndex }

                _uiState.value = PrioritiesUiState(
                    priorities = priorities
                )
            } catch (exception: Exception) {
                _uiState.value = _uiState.value.copy(
                    isLoading = false,
                    errorMessage = errorMessage(exception)
                )
            }
        }
    }

    fun addPriority(
        libraryItem: LibraryItem,
        priorityLevel: Int,
        estimatedTimeHours: Double?,
        notes: String?
    ) {
        val state = _uiState.value

        if (state.priorities.any {
                it.mediaId == libraryItem.mediaId
            }
        ) {
            updatePriority(
                mediaId = libraryItem.mediaId,
                priorityLevel = priorityLevel,
                estimatedTimeHours = estimatedTimeHours,
                notes = notes
            )
            return
        }

        if (state.isFull) {
            _uiState.value = state.copy(
                errorMessage =
                    "You can prioritize up to 5 items."
            )
            return
        }

        val media = libraryItem.media ?: run {
            _uiState.value = state.copy(
                errorMessage =
                    "Media details are unavailable."
            )
            return
        }

        val newPriority = Priority(
            mediaId = libraryItem.mediaId,
            priority = priorityLevel.coerceIn(1, 3),
            orderIndex = state.priorities.size,
            estimatedTimeHours = estimatedTimeHours,
            notes = notes?.trim()?.ifBlank { null },
            media = media
        )

        persistOptimistically(
            updatedList = state.priorities + newPriority,
            fallbackMessage = "Unable to add priority."
        )
    }

    fun updatePriority(
        mediaId: Int,
        priorityLevel: Int,
        estimatedTimeHours: Double?,
        notes: String?
    ) {
        val state = _uiState.value

        val updated = state.priorities.map { item ->
            if (item.mediaId == mediaId) {
                item.copy(
                    priority = priorityLevel.coerceIn(1, 3),
                    estimatedTimeHours =
                        estimatedTimeHours,
                    notes = notes
                        ?.trim()
                        ?.ifBlank { null }
                )
            } else {
                item
            }
        }

        persistOptimistically(
            updatedList = updated,
            fallbackMessage = "Unable to update priority."
        )
    }

    fun removePriority(mediaId: Int) {
        val updated = _uiState.value.priorities
            .filterNot { it.mediaId == mediaId }
            .mapIndexed { index, item ->
                item.copy(orderIndex = index)
            }

        persistOptimistically(
            updatedList = updated,
            fallbackMessage = "Unable to remove priority."
        )
    }

    fun movePriority(
        fromIndex: Int,
        toIndex: Int
    ) {
        val current = _uiState.value.priorities

        if (
            fromIndex !in current.indices ||
            toIndex !in current.indices ||
            fromIndex == toIndex
        ) {
            return
        }

        val reordered = current
            .toMutableList()
            .apply {
                add(
                    toIndex,
                    removeAt(fromIndex)
                )
            }
            .mapIndexed { index, item ->
                item.copy(orderIndex = index)
            }

        persistOptimistically(
            updatedList = reordered,
            fallbackMessage =
                "Unable to save the new priority order."
        )
    }

    private fun persistOptimistically(
        updatedList: List<Priority>,
        fallbackMessage: String
    ) {
        val previous = _uiState.value.priorities

        val normalized = updatedList
            .take(PrioritiesUiState.MAX_PRIORITIES)
            .mapIndexed { index, item ->
                item.copy(orderIndex = index)
            }

        _uiState.value = _uiState.value.copy(
            priorities = normalized,
            isSaving = true,
            errorMessage = null
        )

        viewModelScope.launch {
            try {
                val saved = repository
                    .updatePriorities(normalized)
                    .sortedBy { it.orderIndex }

                _uiState.value = _uiState.value.copy(
                    priorities = saved,
                    isSaving = false
                )
            } catch (exception: Exception) {
                _uiState.value = _uiState.value.copy(
                    priorities = previous,
                    isSaving = false,
                    errorMessage =
                        errorMessage(
                            exception,
                            fallbackMessage
                        )
                )
            }
        }
    }

    fun clearError() {
        _uiState.value = _uiState.value.copy(
            errorMessage = null
        )
    }

    private fun errorMessage(
        exception: Exception,
        fallback: String =
            "Unable to load priorities."
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