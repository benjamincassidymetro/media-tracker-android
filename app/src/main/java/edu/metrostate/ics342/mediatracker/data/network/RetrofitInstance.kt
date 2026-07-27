package edu.metrostate.ics342.mediatracker.data.network

import edu.metrostate.ics342.mediatracker.data.remote.MediaTrackerApi
import okhttp3.Interceptor
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

object RetrofitInstance {

    /*
     * Adds the logged-in user's access token to protected API requests.
     *
     * Login itself still works because no Authorization header is added
     * when TokenStore.accessToken is null or blank.
     */
    private val authInterceptor = Interceptor { chain ->
        val originalRequest = chain.request()
        val token = TokenStore.accessToken

        val authenticatedRequest = originalRequest
            .newBuilder()
            .apply {
                if (!token.isNullOrBlank()) {
                    header(
                        "Authorization",
                        "Bearer $token"
                    )
                }
            }
            .build()

        chain.proceed(authenticatedRequest)
    }

    private val loggingInterceptor =
        HttpLoggingInterceptor().apply {
            level = HttpLoggingInterceptor.Level.BODY
        }

    private val client = OkHttpClient.Builder()
        .addInterceptor(authInterceptor)
        .addInterceptor(loggingInterceptor)
        .build()

    private val retrofit: Retrofit = Retrofit.Builder()
        .baseUrl(ApiConstants.BASE_URL)
        .client(client)
        .addConverterFactory(GsonConverterFactory.create())
        .build()

    // Keep this name so existing authentication code does not break.
    val api: UserApiService =
        retrofit.create(UserApiService::class.java)

    val mediaApi: MediaTrackerApi =
        retrofit.create(MediaTrackerApi::class.java)
}