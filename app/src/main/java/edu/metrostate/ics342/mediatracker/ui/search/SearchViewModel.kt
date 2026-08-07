package edu.metrostate.ics342.mediatracker.ui.search

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import edu.metrostate.ics342.mediatracker.data.model.Media
import edu.metrostate.ics342.mediatracker.data.network.RetrofitInstance
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

data class SearchUiState(
    val items: List<Media> = emptyList(),
    val isLoading: Boolean = false,
    val errorMessage: String? = null
)

class SearchViewModel : ViewModel() {

    private val _uiState =
        MutableStateFlow(SearchUiState())

    val uiState: StateFlow<SearchUiState> =
        _uiState.asStateFlow()

    init {
        search()
    }

    fun search(
        query: String? = null
    ) {
        viewModelScope.launch {
            _uiState.value =
                _uiState.value.copy(
                    isLoading = true,
                    errorMessage = null
                )

            try {
                val response =
                    RetrofitInstance.mediaApi.getMedia(
                        query = query
                            ?.trim()
                            ?.takeIf { it.isNotBlank() }
                    )

                if (!response.isSuccessful) {
                    _uiState.value =
                        SearchUiState(
                            errorMessage =
                                "Unable to load media."
                        )
                    return@launch
                }

                _uiState.value =
                    SearchUiState(
                        items =
                            response.body().orEmpty()
                    )

            } catch (exception: Exception) {
                _uiState.value =
                    SearchUiState(
                        errorMessage =
                            exception.message
                                ?: "Unable to load media."
                    )
            }
        }
    }
}