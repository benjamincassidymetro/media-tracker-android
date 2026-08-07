package edu.metrostate.ics342.mediatracker.data.repository

import edu.metrostate.ics342.mediatracker.data.model.Favorite
import edu.metrostate.ics342.mediatracker.data.model.LibraryItem
import edu.metrostate.ics342.mediatracker.data.model.LibraryStatus
import edu.metrostate.ics342.mediatracker.data.model.Media
import edu.metrostate.ics342.mediatracker.data.model.Priority
import edu.metrostate.ics342.mediatracker.data.network.AddFavoriteRequest
import edu.metrostate.ics342.mediatracker.data.network.AddToLibraryRequest
import edu.metrostate.ics342.mediatracker.data.network.PriorityRequest
import edu.metrostate.ics342.mediatracker.data.network.RetrofitInstance
import edu.metrostate.ics342.mediatracker.data.network.UpdateLibraryStatusRequest
import edu.metrostate.ics342.mediatracker.data.remote.MediaTrackerApi

class DefaultMediaRepository(
    private val api: MediaTrackerApi =
        RetrofitInstance.mediaApi
) : MediaRepository {

    override suspend fun getMediaDetail(
        mediaId: Int
    ): Media {
        val response = api.getMediaDetail(mediaId)

        if (!response.isSuccessful) {
            throw ApiException(
                code = response.code(),
                message = "Unable to load media."
            )
        }

        return response.body()
            ?: throw ApiException(
                code = response.code(),
                message =
                    "The server returned an empty media response."
            )
    }

    override suspend fun getLibraryItem(
        mediaId: Int
    ): LibraryItem? {
        val response = api.getLibraryItem(mediaId)

        if (response.code() == 404) {
            return null
        }

        if (!response.isSuccessful) {
            throw ApiException(
                code = response.code(),
                message =
                    "Unable to check library status."
            )
        }

        return response.body()
    }

    override suspend fun getFavorite(
        mediaId: Int
    ): Favorite? {
        val response = api.getFavorite(mediaId)

        if (response.code() == 404) {
            return null
        }

        if (!response.isSuccessful) {
            throw ApiException(
                code = response.code(),
                message =
                    "Unable to check favorite status."
            )
        }

        return response.body()
    }

    override suspend fun addToLibrary(
        mediaId: Int,
        status: LibraryStatus
    ): LibraryItem? {
        val response = api.addToLibrary(
            AddToLibraryRequest(
                mediaId = mediaId,
                status = status.toApiValue()
            )
        )

        if (response.code() == 409) {
            return getLibraryItem(mediaId)
        }

        if (!response.isSuccessful) {
            throw ApiException(
                code = response.code(),
                message =
                    "Unable to add item to library."
            )
        }

        return response.body()
    }

    override suspend fun updateLibraryStatus(
        mediaId: Int,
        status: LibraryStatus
    ): LibraryItem? {
        val response = api.updateLibraryStatus(
            mediaId = mediaId,
            request = UpdateLibraryStatusRequest(
                status = status.toApiValue()
            )
        )

        if (!response.isSuccessful) {
            throw ApiException(
                code = response.code(),
                message =
                    "Unable to update library status."
            )
        }

        return response.body()
    }

    override suspend fun removeFromLibrary(
        mediaId: Int
    ) {
        val response =
            api.removeFromLibrary(mediaId)

        if (response.code() == 404) {
            return
        }

        if (!response.isSuccessful) {
            throw ApiException(
                code = response.code(),
                message =
                    "Unable to remove item from library."
            )
        }
    }

    override suspend fun getLibrary(
        status: LibraryStatus
    ): List<LibraryItem> {
        val response = api.getLibrary(
            status = status.toApiValue()
        )

        if (!response.isSuccessful) {
            throw ApiException(
                code = response.code(),
                message =
                    "Unable to load your library."
            )
        }

        return response.body().orEmpty()
    }

    override suspend fun addFavorite(
        mediaId: Int
    ): Favorite? {
        val response = api.addFavorite(
            AddFavoriteRequest(
                mediaId = mediaId
            )
        )

        if (response.code() == 409) {
            return getFavorite(mediaId)
        }

        if (!response.isSuccessful) {
            throw ApiException(
                code = response.code(),
                message = "Unable to save favorite."
            )
        }

        return response.body()
    }

    override suspend fun removeFavorite(
        mediaId: Int
    ) {
        val response =
            api.removeFavorite(mediaId)

        if (response.code() == 404) {
            return
        }

        if (!response.isSuccessful) {
            throw ApiException(
                code = response.code(),
                message =
                    "Unable to remove favorite."
            )
        }
    }

    override suspend fun getPriorities():
            List<Priority> {

        val response = api.getPriorities()

        if (!response.isSuccessful) {
            throw ApiException(
                code = response.code(),
                message = when (response.code()) {
                    401 ->
                        "Your session has expired. Please sign in again."

                    else ->
                        "Unable to load priorities."
                }
            )
        }

        return response.body()
            .orEmpty()
            .sortedBy { priority ->
                priority.orderIndex
            }
    }

    override suspend fun updatePriorities(
        priorities: List<Priority>
    ): List<Priority> {

        val normalizedPriorities =
            priorities.mapIndexed { index, priority ->
                priority.copy(
                    orderIndex = index
                )
            }

        normalizedPriorities.forEach { priority ->

            val request = PriorityRequest(
                mediaId = priority.mediaId,
                priority = priority.priority,
                orderIndex = priority.orderIndex,
                estimatedTimeHours = priority.estimatedTimeHours,
                notes = priority.notes
            )

            val response =
                api.updatePriority(request)

            if (!response.isSuccessful) {
                val serverError =
                    response.errorBody()?.string()

                throw ApiException(
                    code = response.code(),
                    message =
                        serverError
                            ?.takeIf { it.isNotBlank() }
                            ?: when (response.code()) {
                                400 ->
                                    "The priority could not be saved."

                                401 ->
                                    "Your session has expired. Please sign in again."

                                else ->
                                    "Unable to update priority."
                            }
                )
            }
        }

        return getPriorities()
    }

}

class ApiException(
    val code: Int,
    override val message: String
) : Exception(message)

fun LibraryStatus.toApiValue(): String {
    return when (this) {
        LibraryStatus.WANT_TO ->
            "want_to"

        LibraryStatus.IN_PROGRESS ->
            "in_progress"

        LibraryStatus.FINISHED ->
            "finished"
    }
}