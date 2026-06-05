package edu.metrostate.ics342.mediatracker.ui.auth

import androidx.lifecycle.ViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow

class RegisterViewModel : ViewModel() {

    private val _displayName = MutableStateFlow("")
    val displayName = _displayName.asStateFlow()

    fun setDisplayName(newValue: String) {
        _displayName.value = newValue
    }

    fun onSignUpClicked() {
        // Registration logic will be added later
    }
}