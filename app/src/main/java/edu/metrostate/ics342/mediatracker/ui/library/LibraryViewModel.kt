package edu.metrostate.ics342.mediatracker.ui.library

import androidx.lifecycle.ViewModel
import edu.metrostate.ics342.mediatracker.data.FakeMediaRepository
import edu.metrostate.ics342.mediatracker.data.model.LibraryItem
import edu.metrostate.ics342.mediatracker.data.model.LibraryStatus
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class LibraryViewModel : ViewModel() {

    private val _libraryItems = MutableStateFlow<List<LibraryItem>>(emptyList())
    val libraryItems: StateFlow<List<LibraryItem>> = _libraryItems.asStateFlow()

    private val _isLoading = MutableStateFlow(false)
    val isLoading: StateFlow<Boolean> = _isLoading.asStateFlow()

    private val_filterState = MutableStateFlow( value = LibraryStatus.WANT_TO) 
    init {
        loadLibrary()
    }

    fun loadLibrary() {
        viewModelScope.launch {
            _isLoading.value = true
            _libraryItems.value = FakeMediaRepository.libraryItems
            _isLoading.value = false
        }
    }

    fun removeItem(mediaId: Int) {
        _libraryItems.value = _libraryItems.value.filter { it.mediaId != mediaId }
    }

    fun updateStatus(mediaId: Int, newStatus: LibraryStatus) {
        _libraryItems.value = _libraryItems.value.map { item ->
            if (item.mediaId == mediaId) item.copy(status = newStatus) else item
        }
    }

    fun updateFilter(status: LibraryStatus)
}
