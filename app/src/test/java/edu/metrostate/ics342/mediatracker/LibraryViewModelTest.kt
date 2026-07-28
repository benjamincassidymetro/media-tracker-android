package edu.metrostate.ics342.mediatracker

import edu.metrostate.ics342.mediatracker.data.model.LibraryItem
import edu.metrostate.ics342.mediatracker.data.model.LibraryStatus
import edu.metrostate.ics342.mediatracker.data.model.Media
import edu.metrostate.ics342.mediatracker.data.repository.MediaRepository
import edu.metrostate.ics342.mediatracker.ui.library.LibraryViewModel
import io.mockk.coEvery
import io.mockk.mockk
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.StandardTestDispatcher
import kotlinx.coroutines.test.advanceUntilIdle
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.runTest
import kotlinx.coroutines.test.setMain
import org.junit.After
import org.junit.Assert.assertTrue
import org.junit.Before
import org.junit.Test
import java.io.IOException

@OptIn(ExperimentalCoroutinesApi::class)
class LibraryViewModelTest {

    private val testDispatcher = StandardTestDispatcher()

    private lateinit var repository: MediaRepository

    @Before
    fun setUp() {
        Dispatchers.setMain(testDispatcher)
        repository = mockk(relaxed = true)
    }

    @After
    fun tearDown() {
        Dispatchers.resetMain()
    }

    @Test
    fun `failed remove restores item to library`() = runTest {
        val media = Media(
            id = 1,
            mediaType = "book",
            title = "Dune",
            author = "Frank Herbert"
        )

        val libraryItem = LibraryItem(
            userId = "user-001",
            mediaId = 1,
            status = LibraryStatus.WANT_TO,
            addedAt = "2026-07-27T00:00:00Z",
            updatedAt = "2026-07-27T00:00:00Z",
            media = media
        )

        coEvery {
            repository.getLibrary(LibraryStatus.WANT_TO)
        } returns listOf(libraryItem)

        coEvery {
            repository.removeFromLibrary(1)
        } throws IOException("Network failure")

        val viewModel = LibraryViewModel(repository)

        advanceUntilIdle()

        viewModel.removeItem(1)

        advanceUntilIdle()

        assertTrue(
            viewModel.uiState.value.items.any {
                it.mediaId == 1
            }
        )
    }
}