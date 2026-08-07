package edu.metrostate.ics342.mediatracker.ui.auth

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import edu.metrostate.ics342.mediatracker.BuildConfig
import edu.metrostate.ics342.mediatracker.R
import edu.metrostate.ics342.mediatracker.data.network.LoginRequest
import edu.metrostate.ics342.mediatracker.data.network.RetrofitInstance
import edu.metrostate.ics342.mediatracker.data.network.TokenStore
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import java.io.IOException

class AuthViewModel : ViewModel() {

    sealed class AuthUiState {
        data object Idle : AuthUiState()
        data object Loading : AuthUiState()
        data object Success : AuthUiState()

        data class Error(
            val msgResId: Int
        ) : AuthUiState()
    }

    private val _email = MutableStateFlow("")
    val email: StateFlow<String> =
        _email.asStateFlow()

    private val _password = MutableStateFlow("")
    val password: StateFlow<String> =
        _password.asStateFlow()

    private val _loginState =
        MutableStateFlow<AuthUiState>(
            AuthUiState.Idle
        )

    val loginState: StateFlow<AuthUiState> =
        _loginState.asStateFlow()

    fun onEmailChange(value: String) {
        _email.value = value
    }

    fun onPasswordChange(value: String) {
        _password.value = value
    }

    fun resetLoginState() {
        _loginState.value = AuthUiState.Idle
    }

    fun onLoginClick() {
        val emailValue = _email.value.trim()
        val passwordValue = _password.value

        if (
            emailValue.isBlank() ||
            passwordValue.isBlank()
        ) {
            _loginState.value =
                AuthUiState.Error(
                    R.string.error_empty_credentials
                )
            return
        }

        if (
            _loginState.value is AuthUiState.Loading
        ) {
            return
        }

        viewModelScope.launch {
            _loginState.value =
                AuthUiState.Loading

            try {
                val response =
                    RetrofitInstance.api.login(
                        LoginRequest(
                            email = emailValue,
                            password = passwordValue,
                            clientId =
                                BuildConfig.API_CLIENT_ID,
                            clientSecret =
                                BuildConfig.API_CLIENT_SECRET
                        )
                    )

                if (!response.isSuccessful) {
                    _loginState.value =
                        AuthUiState.Error(
                            when (response.code()) {
                                400, 401 ->
                                    R.string
                                        .error_invalid_credentials

                                else ->
                                    R.string
                                        .error_login_failed
                            }
                        )

                    return@launch
                }

                val authResponse = response.body()

                if (
                    authResponse == null ||
                    authResponse.accessToken.isBlank()
                ) {
                    _loginState.value =
                        AuthUiState.Error(
                            R.string.error_login_failed
                        )

                    return@launch
                }

                TokenStore.accessToken =
                    authResponse.accessToken

                TokenStore.refreshToken =
                    authResponse.refreshToken

                _loginState.value =
                    AuthUiState.Success

            } catch (_: IOException) {
                _loginState.value =
                    AuthUiState.Error(
                        R.string.error_network
                    )
            } catch (_: Exception) {
                _loginState.value =
                    AuthUiState.Error(
                        R.string.error_login_failed
                    )
            }
        }
    }
}