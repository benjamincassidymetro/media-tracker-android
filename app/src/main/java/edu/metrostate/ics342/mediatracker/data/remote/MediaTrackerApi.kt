package edu.metrostate.ics342.mediatracker.data.remote

import edu.metrostate.ics342.mediatracker.data.model.Favorite
import edu.metrostate.ics342.mediatracker.data.model.LibraryItem
import edu.metrostate.ics342.mediatracker.data.model.Media
import edu.metrostate.ics342.mediatracker.data.model.Review
import edu.metrostate.ics342.mediatracker.data.network.AddFavoriteRequest
import edu.metrostate.ics342.mediatracker.data.network.AddToLibraryRequest
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.Path
import retrofit2.http.Query

interface MediaTrackerApi {

    @GET("media")
    suspend fun getMedia(
        @Query("query") query: String? = null,
        @Query("limit") limit: Int = 20,
        @Query("after") after: String? = null
    ): Response<List<Media>>

    @GET("media/{mediaId}")
    suspend fun getMediaDetail(
        @Path("mediaId") mediaId: Int
    ): Response<Media>

    @GET("library")
    suspend fun getLibrary(
        @Query("status") status: String? = null,
        @Query("limit") limit: Int = 20,
        @Query("after") after: String? = null
    ): Response<List<LibraryItem>>

    @GET("library/{mediaId}")
    suspend fun getLibraryItem(
        @Path("mediaId") mediaId: Int
    ): Response<LibraryItem>

    @POST("library")
    suspend fun addToLibrary(
        @Body request: AddToLibraryRequest
    ): Response<LibraryItem>

    @GET("favorites")
    suspend fun getFavorites(
        @Query("limit") limit: Int = 20,
        @Query("after") after: String? = null
    ): Response<List<Favorite>>

    @GET("favorites/{mediaId}")
    suspend fun getFavorite(
        @Path("mediaId") mediaId: Int
    ): Response<Favorite>

    @POST("favorites")
    suspend fun addFavorite(
        @Body request: AddFavoriteRequest
    ): Response<Favorite>

    @GET("reviews")
    suspend fun getReviews(
        @Query("mediaId") mediaId: Int? = null,
        @Query("userId") userId: String? = null,
        @Query("limit") limit: Int = 20,
        @Query("after") after: String? = null
    ): Response<List<Review>>
}