package edu.metrostate.ics342.mediatracker.ui.priorities

import edu.metrostate.ics342.mediatracker.data.model.Media
import edu.metrostate.ics342.mediatracker.data.model.Priority
import edu.metrostate.ics342.mediatracker.data.repository.MediaRepository
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.mockk
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.StandardTestDispatcher
import kotlinx.coroutines.test.advanceUntilIdle
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.runTest
import kotlinx.coroutines.test.setMain
import org.junit.After
import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import org.junit.Before
import org.junit.Test

@OptIn(ExperimentalCoroutinesApi::class)
class PrioritiesViewModelTest {

    private val dispatcher =
        StandardTestDispatcher()

    private lateinit var repository: MediaRepository

    @Before
    fun setup() {
        Dispatchers.setMain(dispatcher)
        repository = mockk(relaxed = true)
    }

    @After
    fun tearDown() {
        Dispatchers.resetMain()
    }

    @Test
    fun `moving a priority updates order indexes`() =
        runTest {
            val priorities = listOf(
                priority(1, 0),
                priority(2, 1),
                priority(3, 2)
            )

            coEvery {
                repository.getPriorities()
            } returns priorities

            coEvery {
                repository.updatePriorities(any())
            } answers {
                firstArg()
            }

            val viewModel =
                PrioritiesViewModel(repository)

            viewModel.loadPriorities()
            advanceUntilIdle()

            viewModel.movePriority(
                fromIndex = 0,
                toIndex = 2
            )

            advanceUntilIdle()

            val reordered =
                viewModel.uiState.value.priorities

            assertEquals(
                listOf(2, 3, 1),
                reordered.map { it.mediaId }
            )

            assertEquals(
                listOf(0, 1, 2),
                reordered.map { it.orderIndex }
            )

            coVerify {
                repository.updatePriorities(
                    match { list ->
                        list.size == 3 &&
                                list.map { it.orderIndex } ==
                                listOf(0, 1, 2)
                    }
                )
            }
        }

    @Test
    fun `five priorities marks list as full`() =
        runTest {
            coEvery {
                repository.getPriorities()
            } returns List(5) { index ->
                priority(
                    mediaId = index + 1,
                    orderIndex = index
                )
            }

            val viewModel =
                PrioritiesViewModel(repository)

            viewModel.loadPriorities()
            advanceUntilIdle()

            assertEquals(
                5,
                viewModel.uiState.value.priorities.size
            )

            assertTrue(
                viewModel.uiState.value.isFull
            )
        }

    private fun priority(
        mediaId: Int,
        orderIndex: Int
    ): Priority {
        return Priority(
            mediaId = mediaId,
            priority = 1,
            orderIndex = orderIndex,
            estimatedTimeHours = 2.0,
            notes = null,
            media = Media(
                id = mediaId,
                mediaType = "movie",
                title = "Media $mediaId"
            )
        )
    }
}