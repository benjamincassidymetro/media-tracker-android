package edu.metrostate.ics342.mediatracker.data.network


import java.io.IOException

class DefaultUserRepository(
    private val service: UserApiService = RetrofitInstance.api
) : UserRepository {

    override suspend fun register(
        email: String,
        password: String,
        username: String,
        displayName: String
    ): RegisterResult {
        return try {
            val response = service.createUser(
                RegisterRequest(
                    email = email,
                    password = password,
                    username = username,
                    displayName = displayName,
                    clientId = ApiConstants.CLIENT_ID,
                    clientSecret = ApiConstants.CLIENT_SECRET
                )
            )

            when (response.code()) {
                201 -> RegisterResult.Success
                409 -> RegisterResult.Conflict
                else -> RegisterResult.UnknownError
            }
        } catch (_: IOException) {
            RegisterResult.NetworkError
        }
    }

    override suspend fun login(email: String, password: String): LoginResult {
        return try {
            val response = service.login(
                LoginRequest(
                    grantType = "password",
                    email = email,
                    password = password,
                    clientId = ApiConstants.CLIENT_ID,
                    clientSecret = ApiConstants.CLIENT_SECRET
                )
            )

            when (response.code()) {
                200 -> {
                    val body = response.body()!!
                    LoginResult.Success(
                        accessToken = body.accessToken,
                        refreshToken = body.refreshToken,
                        user = body.user
                    )
                }
                401 -> LoginResult.InvalidCredentials
                else -> LoginResult.UnknownError
            }
        } catch (_: IOException) {
            LoginResult.NetworkError
        }
    }
}