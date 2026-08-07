package edu.metrostate.ics342.mediatracker.ui.auth

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import edu.metrostate.ics342.mediatracker.BuildConfig
import edu.metrostate.ics342.mediatracker.data.network.RegisterRequest
import edu.metrostate.ics342.mediatracker.data.network.RetrofitInstance
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import java.io.IOException

data class RegisterFormState(
    val email: String = "",
    val username: String = "",
    val displayName: String = "",
    val password: String = "",
    val confirmPassword: String = ""
)

sealed class RegisterUiState {
    data object Idle : RegisterUiState()
    data object Loading : RegisterUiState()
    data object Success : RegisterUiState()

    data class Error(
        val message: String
    ) : RegisterUiState()
}

class RegisterViewModel : ViewModel() {

    private val _formState =
        MutableStateFlow(RegisterFormState())

    val formState: StateFlow<RegisterFormState> =
        _formState.asStateFlow()

    private val _registerState =
        MutableStateFlow<RegisterUiState>(
            RegisterUiState.Idle
        )

    val registerState: StateFlow<RegisterUiState> =
        _registerState.asStateFlow()

    fun onEmailChange(value: String) {
        _formState.value =
            _formState.value.copy(email = value)

        clearError()
    }

    fun onUsernameChange(value: String) {
        _formState.value =
            _formState.value.copy(username = value)

        clearError()
    }

    fun onDisplayNameChange(value: String) {
        _formState.value =
            _formState.value.copy(displayName = value)

        clearError()
    }

    fun onPasswordChange(value: String) {
        _formState.value =
            _formState.value.copy(password = value)

        clearError()
    }

    fun onConfirmPasswordChange(value: String) {
        _formState.value =
            _formState.value.copy(
                confirmPassword = value
            )

        clearError()
    }

    fun register() {
        if (_registerState.value is RegisterUiState.Loading) {
            return
        }

        val form = _formState.value

        val validationError = validate(form)

        if (validationError != null) {
            _registerState.value =
                RegisterUiState.Error(validationError)

            return
        }

        viewModelScope.launch {
            _registerState.value =
                RegisterUiState.Loading

            try {
                val response =
                    RetrofitInstance.api.createUser(
                        RegisterRequest(
                            email = form.email.trim(),
                            password = form.password,
                            username = form.username.trim(),
                            displayName = form.displayName.trim(),
                            clientId = BuildConfig.API_CLIENT_ID,
                            clientSecret = BuildConfig.API_CLIENT_SECRET
                        )
                    )

                when {
                    response.isSuccessful -> {
                        _registerState.value =
                            RegisterUiState.Success
                    }

                    response.code() == 409 -> {
                        _registerState.value =
                            RegisterUiState.Error(
                                "That email or username is already in use."
                            )
                    }

                    response.code() == 400 -> {
                        _registerState.value =
                            RegisterUiState.Error(
                                "Please check your registration information."
                            )
                    }

                    response.code() == 401 -> {
                        _registerState.value =
                            RegisterUiState.Error(
                                "The application credentials were rejected."
                            )
                    }

                    else -> {
                        _registerState.value =
                            RegisterUiState.Error(
                                "Unable to create your account."
                            )
                    }
                }
            } catch (_: IOException) {
                _registerState.value =
                    RegisterUiState.Error(
                        "Network error. Check your connection and try again."
                    )
            } catch (_: Exception) {
                _registerState.value =
                    RegisterUiState.Error(
                        "Something went wrong. Please try again."
                    )
            }
        }
    }

    fun resetRegisterState() {
        _registerState.value = RegisterUiState.Idle
    }

    private fun clearError() {
        if (_registerState.value is RegisterUiState.Error) {
            _registerState.value = RegisterUiState.Idle
        }
    }

    private fun validate(
        form: RegisterFormState
    ): String? {
        if (
            form.email.isBlank() ||
            form.username.isBlank() ||
            form.displayName.isBlank() ||
            form.password.isBlank() ||
            form.confirmPassword.isBlank()
        ) {
            return "Please complete all fields."
        }

        if (
            !android.util.Patterns.EMAIL_ADDRESS
                .matcher(form.email.trim())
                .matches()
        ) {
            return "Enter a valid email address."
        }

        if (form.username.trim().length < 3) {
            return "Username must contain at least 3 characters."
        }

        if (form.password.length < 8) {
            return "Password must contain at least 8 characters."
        }

        if (form.password != form.confirmPassword) {
            return "Passwords do not match."
        }

        if (
            BuildConfig.API_CLIENT_ID.isBlank() ||
            BuildConfig.API_CLIENT_SECRET.isBlank()
        ) {
            return "API client credentials are missing."
        }

        return null
    }
}