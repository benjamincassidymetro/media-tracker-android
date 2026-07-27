package edu.metrostate.ics342.mediatracker.ui.detail

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.automirrored.outlined.MenuBook
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.MoreVert
import androidx.compose.material.icons.filled.Star
import androidx.compose.material.icons.outlined.FavoriteBorder
import androidx.compose.material3.AssistChip
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel
import edu.metrostate.ics342.mediatracker.data.model.LibraryStatus
import edu.metrostate.ics342.mediatracker.data.model.Media

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MediaDetailScreen(
    mediaId: Int,
    onNavigateBack: () -> Unit,
    onWriteReview: (Int) -> Unit,
    viewModel: MediaDetailViewModel = viewModel()
) {
    val media by viewModel.media.collectAsStateWithLifecycle()
    val libraryStatus by viewModel.libraryStatus.collectAsStateWithLifecycle()
    val isFavorite by viewModel.isFavorite.collectAsStateWithLifecycle()
    val isAddingToLibrary by viewModel.isAddingToLibrary.collectAsStateWithLifecycle()
    val isSavingFavorite by viewModel.isSavingFavorite.collectAsStateWithLifecycle()
    val isLoading by viewModel.isLoading.collectAsStateWithLifecycle()
    val errorMessage by viewModel.errorMessage.collectAsStateWithLifecycle()

    LaunchedEffect(mediaId) {
        viewModel.loadMedia(mediaId)
    }

    /*
     * Loading is full screen only during the initial media request.
     * The existing content remains visible during button operations.
     */
    if (isLoading && media == null) {
        FullScreenLoading()
        return
    }

    /*
     * A loading failure replaces the screen only when no media was loaded.
     * Button failures are shown inside the successful detail layout.
     */
    if (media == null) {
        FullScreenError(
            message = errorMessage ?: "Media not found.",
            onRetry = viewModel::retry,
            onNavigateBack = onNavigateBack
        )
        return
    }

    val currentMedia = media ?: return

    Scaffold(
        topBar = {
            TopAppBar(
                title = {},
                navigationIcon = {
                    IconButton(onClick = onNavigateBack) {
                        Icon(
                            imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                            contentDescription = "Back"
                        )
                    }
                },
                actions = {
                    IconButton(onClick = {}) {
                        Icon(
                            imageVector = Icons.Default.MoreVert,
                            contentDescription = "More options"
                        )
                    }
                }
            )
        }
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
                .verticalScroll(rememberScrollState())
                .padding(16.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            CoverPlaceholder()

            Spacer(modifier = Modifier.height(16.dp))

            Text(
                text = currentMedia.title,
                style = MaterialTheme.typography.headlineSmall,
                fontWeight = FontWeight.Bold
            )

            Text(
                text = currentMedia.creatorCredit(),
                style = MaterialTheme.typography.bodyMedium,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )

            Spacer(modifier = Modifier.height(8.dp))

            RatingRow(currentMedia)

            Spacer(modifier = Modifier.height(20.dp))

            MediaActionButtons(
                libraryStatus = libraryStatus,
                isFavorite = isFavorite,
                isAddingToLibrary = isAddingToLibrary,
                isSavingFavorite = isSavingFavorite,
                onAddToLibrary = viewModel::addToWantTo,
                onSaveFavorite = viewModel::saveFavorite
            )

            /*
             * An add/save error should not remove otherwise valid media content.
             */
            if (errorMessage != null) {
                Spacer(modifier = Modifier.height(10.dp))

                Text(
                    text = errorMessage.orEmpty(),
                    style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.error,
                    modifier = Modifier.fillMaxWidth()
                )

                TextButton(onClick = viewModel::clearError) {
                    Text("Dismiss")
                }
            }

            Spacer(modifier = Modifier.height(24.dp))

            SectionLabel("ABOUT")

            Spacer(modifier = Modifier.height(6.dp))

            Text(
                text = currentMedia.descriptionText(),
                style = MaterialTheme.typography.bodyMedium,
                color = MaterialTheme.colorScheme.onSurfaceVariant,
                modifier = Modifier.fillMaxWidth()
            )

            Spacer(modifier = Modifier.height(20.dp))

            StatGrid(currentMedia)

            Spacer(modifier = Modifier.height(24.dp))

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                SectionLabel(
                    text = "REVIEWS (${currentMedia.reviewCount})",
                    modifier = Modifier.weight(1f)
                )

                TextButton(
                    onClick = {
                        onWriteReview(currentMedia.id)
                    }
                ) {
                    Text("+ Write Review")
                }
            }

            FakeReviewCard(
                avatar = "A",
                username = "@alice_reads",
                time = "2d ago",
                rating = "★★★★★",
                review = "A timeless classic. Fresh every time."
            )

            Spacer(modifier = Modifier.height(12.dp))

            FakeReviewCard(
                avatar = "B",
                username = "@bob_books",
                time = "1w ago",
                rating = "★★★★☆",
                review = "Great world-building, slow in the middle."
            )
        }
    }
}

@Composable
private fun MediaActionButtons(
    libraryStatus: LibraryStatus?,
    isFavorite: Boolean,
    isAddingToLibrary: Boolean,
    isSavingFavorite: Boolean,
    onAddToLibrary: () -> Unit,
    onSaveFavorite: () -> Unit
) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        Button(
            onClick = onAddToLibrary,
            enabled = libraryStatus == null && !isAddingToLibrary,
            modifier = Modifier.weight(1f)
        ) {
            when {
                isAddingToLibrary -> {
                    CircularProgressIndicator(
                        modifier = Modifier.size(18.dp),
                        strokeWidth = 2.dp,
                        color = MaterialTheme.colorScheme.onPrimary
                    )

                    Spacer(modifier = Modifier.width(8.dp))

                    Text("Adding")
                }

                libraryStatus != null -> {
                    Icon(
                        imageVector = Icons.Default.Check,
                        contentDescription = null,
                        modifier = Modifier.size(18.dp)
                    )

                    Spacer(modifier = Modifier.width(6.dp))

                    Text(libraryStatus.buttonText())
                }

                else -> {
                    Text("+ Want To")
                }
            }
        }

        OutlinedButton(
            onClick = onSaveFavorite,
            enabled = !isFavorite && !isSavingFavorite,
            modifier = Modifier.weight(1f)
        ) {
            when {
                isSavingFavorite -> {
                    CircularProgressIndicator(
                        modifier = Modifier.size(18.dp),
                        strokeWidth = 2.dp
                    )

                    Spacer(modifier = Modifier.width(8.dp))

                    Text("Saving")
                }

                isFavorite -> {
                    Icon(
                        imageVector = Icons.Default.Favorite,
                        contentDescription = null,
                        modifier = Modifier.size(18.dp)
                    )

                    Spacer(modifier = Modifier.width(8.dp))

                    Text("Saved")
                }

                else -> {
                    Icon(
                        imageVector = Icons.Outlined.FavoriteBorder,
                        contentDescription = null,
                        modifier = Modifier.size(18.dp)
                    )

                    Spacer(modifier = Modifier.width(8.dp))

                    Text("Save")
                }
            }
        }
    }
}

@Composable
private fun FullScreenLoading() {
    Box(
        modifier = Modifier.fillMaxSize(),
        contentAlignment = Alignment.Center
    ) {
        CircularProgressIndicator()
    }
}

@Composable
private fun FullScreenError(
    message: String,
    onRetry: () -> Unit,
    onNavigateBack: () -> Unit
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(24.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Text(
            text = message,
            style = MaterialTheme.typography.bodyLarge,
            color = MaterialTheme.colorScheme.error
        )

        Spacer(modifier = Modifier.height(16.dp))

        Button(onClick = onRetry) {
            Text("Retry")
        }

        TextButton(onClick = onNavigateBack) {
            Text("Go Back")
        }
    }
}

@Composable
private fun CoverPlaceholder() {
    Card(
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.primaryContainer
        ),
        modifier = Modifier.size(
            width = 120.dp,
            height = 150.dp
        )
    ) {
        Box(
            modifier = Modifier.fillMaxSize(),
            contentAlignment = Alignment.Center
        ) {
            Icon(
                imageVector = Icons.AutoMirrored.Outlined.MenuBook,
                contentDescription = null,
                modifier = Modifier.size(48.dp),
                tint = MaterialTheme.colorScheme.primary
            )
        }
    }
}

@Composable
private fun RatingRow(media: Media) {
    Row(
        verticalAlignment = Alignment.CenterVertically
    ) {
        repeat(5) {
            Icon(
                imageVector = Icons.Default.Star,
                contentDescription = null,
                tint = MaterialTheme.colorScheme.secondary,
                modifier = Modifier.size(18.dp)
            )
        }

        Spacer(modifier = Modifier.width(6.dp))

        Text(
            text = media.averageRating.toString(),
            fontWeight = FontWeight.Bold,
            color = MaterialTheme.colorScheme.secondary
        )

        Spacer(modifier = Modifier.width(4.dp))

        Text(
            text = "(${media.ratingCount})",
            color = MaterialTheme.colorScheme.onSurfaceVariant
        )
    }
}

@Composable
private fun SectionLabel(
    text: String,
    modifier: Modifier = Modifier
) {
    Text(
        text = text,
        style = MaterialTheme.typography.labelMedium,
        fontWeight = FontWeight.Bold,
        color = MaterialTheme.colorScheme.onSurfaceVariant,
        modifier = modifier.fillMaxWidth()
    )
}

@Composable
private fun StatGrid(media: Media) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        StatBox(
            label = "YEAR",
            value = media.publishedYear?.toString() ?: "Unknown",
            modifier = Modifier.weight(1f)
        )

        StatBox(
            label = media.middleStatLabel(),
            value = media.middleStatValue(),
            modifier = Modifier.weight(1f)
        )

        StatBox(
            label = "GENRE",
            value = media.genres.firstOrNull() ?: "Unknown",
            modifier = Modifier.weight(1f)
        )
    }
}

@Composable
private fun StatBox(
    label: String,
    value: String,
    modifier: Modifier = Modifier
) {
    Card(
        modifier = modifier,
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.surfaceVariant
        )
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(
                    vertical = 16.dp,
                    horizontal = 8.dp
                ),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(
                text = label,
                style = MaterialTheme.typography.labelSmall
            )

            Text(
                text = value,
                fontWeight = FontWeight.Bold
            )
        }
    }
}

@Composable
private fun FakeReviewCard(
    avatar: String,
    username: String,
    time: String,
    rating: String,
    review: String
) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(16.dp)
    ) {
        Row(
            modifier = Modifier.padding(16.dp),
            verticalAlignment = Alignment.Top
        ) {
            AssistChip(
                onClick = {},
                label = {
                    Text(avatar)
                }
            )

            Spacer(modifier = Modifier.width(12.dp))

            Column(
                modifier = Modifier.weight(1f)
            ) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Text(
                        text = username,
                        fontWeight = FontWeight.Bold
                    )

                    Text(
                        text = time,
                        style = MaterialTheme.typography.bodySmall
                    )
                }

                Text(
                    text = rating,
                    color = MaterialTheme.colorScheme.secondary
                )

                Text(
                    text = review,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
            }
        }
    }
}

private fun LibraryStatus.buttonText(): String {
    return when (this) {
        LibraryStatus.WANT_TO -> "Want To"
        LibraryStatus.IN_PROGRESS -> "In Progress"
        LibraryStatus.FINISHED -> "Finished"
    }
}

private fun Media.creatorCredit(): String {
    return when (mediaType) {
        "book" -> author ?: "Unknown Author"
        "movie" -> director ?: "Unknown Director"
        "show" -> creator ?: "Unknown Creator"
        else -> author ?: director ?: creator ?: "Unknown"
    }
}

private fun Media.middleStatLabel(): String {
    return when (mediaType) {
        "book" -> "PAGES"
        "movie" -> "RUNTIME"
        "show" -> "SEASONS"
        else -> "INFO"
    }
}

private fun Media.middleStatValue(): String {
    return when (mediaType) {
        "book" -> pageCount?.toString() ?: "Unknown"

        "movie" -> runtimeMinutes?.let {
            "$it min"
        } ?: "Unknown"

        "show" -> when {
            seasonCount != null && episodeCount != null ->
                "$seasonCount seasons / $episodeCount episodes"

            seasonCount != null ->
                "$seasonCount seasons"

            episodeCount != null ->
                "$episodeCount episodes"

            else ->
                "Unknown"
        }

        else -> "Unknown"
    }
}

private fun Media.descriptionText(): String {
    return description
        ?.takeIf { it.isNotBlank() }
        ?: "Description coming soon."
}