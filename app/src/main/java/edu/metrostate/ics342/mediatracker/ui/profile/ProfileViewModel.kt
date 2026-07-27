package edu.metrostate.ics342.mediatracker.ui.profile

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import edu.metrostate.ics342.mediatracker.data.FakeMediaRepository
import edu.metrostate.ics342.mediatracker.data.model.LibraryItem
import edu.metrostate.ics342.mediatracker.data.model.UserProfile
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class ProfileViewModel : ViewModel() {

    private val _currentUser =
        MutableStateFlow<UserProfile?>(null)

    val currentUser: StateFlow<UserProfile?> =
        _currentUser.asStateFlow()

    private val _libraryPreview =
        MutableStateFlow<List<LibraryItem>>(emptyList())

    val libraryPreview: StateFlow<List<LibraryItem>> =
        _libraryPreview.asStateFlow()

    private val _isLoading =
        MutableStateFlow(false)

    val isLoading: StateFlow<Boolean> =
        _isLoading.asStateFlow()

    private val _isSaving =
        MutableStateFlow(false)

    val isSaving: StateFlow<Boolean> =
        _isSaving.asStateFlow()

    private val _saveSuccessful =
        MutableStateFlow(false)

    val saveSuccessful: StateFlow<Boolean> =
        _saveSuccessful.asStateFlow()

    private val _errorMessage =
        MutableStateFlow<String?>(null)

    val errorMessage: StateFlow<String?> =
        _errorMessage.asStateFlow()

    /*
     * These properties are retained for compatibility with any existing
     * Edit Profile UI that observes individual ViewModel fields.
     */
    private val _editDisplayName =
        MutableStateFlow("")

    val editDisplayName: StateFlow<String> =
        _editDisplayName.asStateFlow()

    private val _editUsername =
        MutableStateFlow("")

    val editUsername: StateFlow<String> =
        _editUsername.asStateFlow()

    private val _editBio =
        MutableStateFlow("")

    val editBio: StateFlow<String> =
        _editBio.asStateFlow()

    init {
        loadProfile()
    }

    /**
     * Loads the current user's profile and recent library preview.
     *
     * This currently uses FakeMediaRepository. When the profile endpoints
     * are available, replace these assignments with:
     *
     * GET /users/me
     * GET /library
     */
    fun loadProfile() {
        if (_isLoading.value) return

        viewModelScope.launch {
            _isLoading.value = true
            _errorMessage.value = null

            try {
                val user = FakeMediaRepository.currentUser

                _currentUser.value = user
                _libraryPreview.value =
                    FakeMediaRepository.libraryItems.take(6)

                populateEditFields(user)
            } catch (exception: Exception) {
                _errorMessage.value =
                    exception.message ?: "Unable to load profile."
            } finally {
                _isLoading.value = false
            }
        }
    }

    fun onEditDisplayNameChange(value: String) {
        _editDisplayName.value = value
        clearError()
    }

    fun onEditUsernameChange(value: String) {
        _editUsername.value = value
        clearError()
    }

    fun onEditBioChange(value: String) {
        if (value.length <= MAX_BIO_LENGTH) {
            _editBio.value = value
            clearError()
        }
    }

    /**
     * Used by an EditProfileScreen that reads its field values directly
     * from this ViewModel.
     */
    fun saveProfile() {
        updateProfile(
            displayName = _editDisplayName.value,
            username = _editUsername.value,
            bio = _editBio.value
        )
    }

    /**
     * Used by the updated EditProfileScreen.
     */
    fun updateProfile(
        displayName: String,
        username: String,
        bio: String
    ) {
        if (_isSaving.value) return

        val cleanedDisplayName = displayName.trim()
        val cleanedUsername = username.trim()
        val cleanedBio = bio.trim()

        when {
            cleanedDisplayName.isBlank() -> {
                _errorMessage.value =
                    "Display name is required."
                return
            }

            cleanedUsername.isBlank() -> {
                _errorMessage.value =
                    "Username is required."
                return
            }

            cleanedUsername.contains(" ") -> {
                _errorMessage.value =
                    "Username cannot contain spaces."
                return
            }

            cleanedBio.length > MAX_BIO_LENGTH -> {
                _errorMessage.value =
                    "Bio cannot exceed $MAX_BIO_LENGTH characters."
                return
            }
        }

        viewModelScope.launch {
            _isSaving.value = true
            _saveSuccessful.value = false
            _errorMessage.value = null

            try {
                /*
                 * TODO: Replace this temporary delay and local copy with:
                 *
                 * PUT /users/me
                 *
                 * Example body:
                 * {
                 *   "displayName": cleanedDisplayName,
                 *   "username": cleanedUsername,
                 *   "bio": cleanedBio.ifBlank { null }
                 * }
                 */
                delay(500)

                val updatedUser = _currentUser.value?.copy(
                    displayName = cleanedDisplayName,
                    username = cleanedUsername,
                    bio = cleanedBio.ifBlank { null }
                )

                if (updatedUser == null) {
                    _errorMessage.value =
                        "Unable to update profile."
                    return@launch
                }

                _currentUser.value = updatedUser
                populateEditFields(updatedUser)
                _saveSuccessful.value = true
            } catch (exception: Exception) {
                _errorMessage.value =
                    exception.message
                        ?: "Unable to update profile. Please try again."
            } finally {
                _isSaving.value = false
            }
        }
    }

    /**
     * Call after EditProfileScreen handles a successful update and navigates
     * back. This prevents the success event from firing again.
     */
    fun resetSaveState() {
        _saveSuccessful.value = false
    }

    fun clearError() {
        _errorMessage.value = null
    }

    /**
     * Temporary synchronous lookup for another user's profile.
     *
     * Replace with GET /users/{userId} when that endpoint is connected.
     */
    fun loadUserById(userId: String): UserProfile? {
        return sequenceOf(
            FakeMediaRepository.currentUser,
            *FakeMediaRepository.followers.toTypedArray(),
            *FakeMediaRepository.following.toTypedArray()
        ).firstOrNull { user ->
            user.id == userId
        }
    }

    private fun populateEditFields(user: UserProfile) {
        _editDisplayName.value = user.displayName
        _editUsername.value = user.username
        _editBio.value = user.bio.orEmpty()
    }

    companion object {
        private const val MAX_BIO_LENGTH = 500
    }
}