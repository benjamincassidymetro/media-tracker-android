package edu.metrostate.ics342.mediatracker.data.remote

import edu.metrostate.ics342.mediatracker.data.model.Favorite
import edu.metrostate.ics342.mediatracker.data.model.LibraryItem
import edu.metrostate.ics342.mediatracker.data.model.Media
import edu.metrostate.ics342.mediatracker.data.model.Review
import edu.metrostate.ics342.mediatracker.data.network.AddFavoriteRequest
import edu.metrostate.ics342.mediatracker.data.network.AddToLibraryRequest
import edu.metrostate.ics342.mediatracker.data.network.UpdateLibraryStatusRequest
import edu.metrostate.ics342.mediatracker.data.model.Priority
import edu.metrostate.ics342.mediatracker.data.network.PriorityRequest
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.DELETE
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.PUT
import retrofit2.http.Path
import retrofit2.http.Query

interface MediaTrackerApi {

    @GET("media")
    suspend fun getMedia(
        @Query("query") query: String? = null,
        @Query("limit") limit: Int = 20,
        @Query("after") after: String? = null
    ): Response<List<Media>>

    @GET("priorities")
    suspend fun getPriorities(): Response<List<Priority>>

    @PUT("priorities")
    suspend fun updatePriority(
        @Body priority: PriorityRequest
    ): Response<Priority>
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

    @PUT("library/{mediaId}")
    suspend fun updateLibraryStatus(
        @Path("mediaId") mediaId: Int,
        @Body request: UpdateLibraryStatusRequest
    ): Response<LibraryItem>

    @DELETE("library/{mediaId}")
    suspend fun removeFromLibrary(
        @Path("mediaId") mediaId: Int
    ): Response<Unit>

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

    @DELETE("favorites/{mediaId}")
    suspend fun removeFavorite(
        @Path("mediaId") mediaId: Int
    ): Response<Unit>

    @GET("reviews")
    suspend fun getReviews(
        @Query("mediaId") mediaId: Int? = null,
        @Query("userId") userId: String? = null,
        @Query("limit") limit: Int = 20,
        @Query("after") after: String? = null
    ): Response<List<Review>>
}