package edu.metrostate.ics342.mediatracker.data.repository

import edu.metrostate.ics342.mediatracker.data.model.Favorite
import edu.metrostate.ics342.mediatracker.data.model.LibraryItem
import edu.metrostate.ics342.mediatracker.data.model.LibraryStatus
import edu.metrostate.ics342.mediatracker.data.model.Media
import edu.metrostate.ics342.mediatracker.data.model.Priority

interface MediaRepository {

    suspend fun getPriorities(): List<Priority>

    suspend fun updatePriorities(
        priorities: List<Priority>
    ): List<Priority>

    suspend fun getMediaDetail(mediaId: Int): Media

    suspend fun getLibraryItem(mediaId: Int): LibraryItem?

    suspend fun getFavorite(mediaId: Int): Favorite?

    suspend fun addToLibrary(
        mediaId: Int,
        status: LibraryStatus
    ): LibraryItem?

    suspend fun updateLibraryStatus(
        mediaId: Int,
        status: LibraryStatus
    ): LibraryItem?

    suspend fun removeFromLibrary(mediaId: Int)

    suspend fun getLibrary(
        status: LibraryStatus
    ): List<LibraryItem>

    suspend fun addFavorite(mediaId: Int): Favorite?

    suspend fun removeFavorite(mediaId: Int)
}