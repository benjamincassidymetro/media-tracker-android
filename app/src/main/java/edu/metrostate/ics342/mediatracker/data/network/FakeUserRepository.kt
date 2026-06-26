package edu.metrostate.ics342.mediatracker.data.network

import edu.metrostate.ics342.mediatracker.data.FakeMediaRepository
import edu.metrostate.ics342.mediatracker.data.LoginResult
import edu.metrostate.ics342.mediatracker.data.RegisterResult
import edu.metrostate.ics342.mediatracker.data.UserRepository

/**
 * A mock implementation of [UserRepository] that allows any login or registration to succeed.
 * Useful for testing the UI when API keys are missing or the backend is unavailable.
 */
class FakeUserRepository : UserRepository {

    override suspend fun register(
        email: String,
        password: String,
        username: String,
        displayName: String
    ): RegisterResult {
        // Always succeed for any registration attempt
        return RegisterResult.Success
    }

    override suspend fun login(email: String, password: String): LoginResult {
        // Always succeed for any non-blank credentials
        return LoginResult.Success(
            accessToken = "mock_access_token",
            refreshToken = "mock_refresh_token",
            user = FakeMediaRepository.currentUser.copy(
                email = email,
                username = email.substringBefore("@"),
                displayName = email.substringBefore("@").replaceFirstChar { it.uppercase() }
            )
        )
    }
}
