package edu.metrostate.ics342.mediatracker.ui.detail

import androidx.compose.foundation.background
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
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.outlined.ArrowBack
import androidx.compose.material.icons.automirrored.outlined.MenuBook
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.outlined.FavoriteBorder
import androidx.compose.material.icons.outlined.MoreVert
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import edu.metrostate.ics342.mediatracker.R
import edu.metrostate.ics342.mediatracker.data.model.LibraryStatus
import edu.metrostate.ics342.mediatracker.data.model.Media
import androidx.compose.ui.res.stringResource
import kotlin.math.roundToInt

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MediaDetailScreen(
    mediaId: Int,
    onNavigateBack: () -> Unit,
    onWriteReview: (Int) -> Unit,
    viewModel: MediaDetailViewModel = viewModel()
) {
    val uiState by viewModel.uiState.collectAsState()
    val actionError by viewModel.actionError.collectAsState()

    val snackbarHostState = remember {
        SnackbarHostState()
    }

    LaunchedEffect(mediaId) {
        viewModel.load(mediaId)
    }

    LaunchedEffect(actionError) {
        actionError?.let { message ->
            snackbarHostState.showSnackbar(message)
            viewModel.clearActionError()
        }
    }

    Scaffold(
        snackbarHost = {
            SnackbarHost(
                hostState = snackbarHostState
            )
        },
        topBar = {
            TopAppBar(
                title = {},
                navigationIcon = {
                    IconButton(
                        onClick = onNavigateBack
                    ) {
                        Icon(
                            imageVector =
                                Icons.AutoMirrored.Outlined.ArrowBack,
                            contentDescription =
                                stringResource(R.string.action_back)
                        )
                    }
                },
                actions = {
                    IconButton(
                        onClick = {
                            // Reserved for the media overflow menu.
                        }
                    ) {
                        Icon(
                            imageVector = Icons.Outlined.MoreVert,
                            contentDescription =
                                stringResource(
                                    R.string.action_more_options
                                )
                        )
                    }
                }
            )
        }
    ) { innerPadding ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
        ) {
            when (val state = uiState) {
                MediaDetailUiState.Loading -> {
                    LoadingContent()
                }

                MediaDetailUiState.NotFound -> {
                    MessageContent(
                        message = stringResource(
                            R.string.error_media_not_found
                        ),
                        buttonText = stringResource(
                            R.string.action_retry
                        ),
                        onButtonClick = viewModel::retry
                    )
                }

                is MediaDetailUiState.Error -> {
                    MessageContent(
                        message = state.message,
                        buttonText = stringResource(
                            R.string.action_retry
                        ),
                        onButtonClick = viewModel::retry
                    )
                }

                is MediaDetailUiState.Success -> {
                    MediaDetailSuccessContent(
                        state = state,
                        onAddToLibrary =
                            viewModel::addToLibrary,
                        onToggleFavorite =
                            viewModel::toggleFavorite,
                        onWriteReview = {
                            onWriteReview(mediaId)
                        }
                    )
                }
            }
        }
    }
}

@Composable
private fun LoadingContent() {
    Box(
        modifier = Modifier.fillMaxSize(),
        contentAlignment = Alignment.Center
    ) {
        CircularProgressIndicator()
    }
}

@Composable
private fun MessageContent(
    message: String,
    buttonText: String,
    onButtonClick: () -> Unit
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(32.dp),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(
            text = message,
            style = MaterialTheme.typography.bodyLarge,
            textAlign = TextAlign.Center,
            color = MaterialTheme.colorScheme.onSurfaceVariant
        )

        Spacer(
            modifier = Modifier.height(16.dp)
        )

        Button(
            onClick = onButtonClick
        ) {
            Text(text = buttonText)
        }
    }
}

@Composable
private fun MediaDetailSuccessContent(
    state: MediaDetailUiState.Success,
    onAddToLibrary: () -> Unit,
    onToggleFavorite: () -> Unit,
    onWriteReview: () -> Unit
) {
    val media = state.media
    val creatorText = media.creatorText()

    Column(
        modifier = Modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState())
            .padding(horizontal = 20.dp)
            .padding(bottom = 32.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Spacer(
            modifier = Modifier.height(12.dp)
        )

        MediaArtworkPlaceholder()

        Spacer(
            modifier = Modifier.height(18.dp)
        )

        Text(
            text = media.title,
            style = MaterialTheme.typography.headlineSmall,
            fontWeight = FontWeight.Bold,
            textAlign = TextAlign.Center
        )

        if (creatorText.isNotBlank()) {
            Spacer(
                modifier = Modifier.height(4.dp)
            )

            Text(
                text = creatorText,
                style = MaterialTheme.typography.bodyMedium,
                color = MaterialTheme.colorScheme.onSurfaceVariant,
                textAlign = TextAlign.Center
            )
        }

        if (media.averageRating > 0f) {
            Spacer(
                modifier = Modifier.height(10.dp)
            )

            Text(
                text = buildString {
                    append("★ ")
                    append(
                        String.format(
                            "%.1f",
                            media.averageRating
                        )
                    )

                    if (media.ratingCount > 0) {
                        append("  (")
                        append(media.ratingCount)
                        append(")")
                    }
                },
                style = MaterialTheme.typography.bodyMedium,
                color = MaterialTheme.colorScheme.primary,
                fontWeight = FontWeight.SemiBold
            )
        }

        Spacer(
            modifier = Modifier.height(22.dp)
        )

        ActionButtons(
            libraryStatus = state.libraryStatus,
            isFavorite = state.isFavorite,
            onAddToLibrary = onAddToLibrary,
            onToggleFavorite = onToggleFavorite
        )

        Spacer(
            modifier = Modifier.height(28.dp)
        )

        DetailSectionTitle(
            text = stringResource(R.string.media_about)
        )

        Spacer(
            modifier = Modifier.height(12.dp)
        )

        MediaInformationRow(media = media)

        if (media.genres.isNotEmpty()) {
            Spacer(
                modifier = Modifier.height(16.dp)
            )

            Text(
                text = media.genres.joinToString(
                    separator = " • "
                ),
                style = MaterialTheme.typography.bodyMedium,
                color = MaterialTheme.colorScheme.onSurfaceVariant,
                modifier = Modifier.fillMaxWidth()
            )
        }

        Spacer(
            modifier = Modifier.height(28.dp)
        )

        HorizontalDivider()

        Spacer(
            modifier = Modifier.height(20.dp)
        )

        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement =
                Arrangement.SpaceBetween,
            verticalAlignment =
                Alignment.CenterVertically
        ) {
            DetailSectionTitle(
                text = stringResource(
                    R.string.media_reviews
                )
            )

            TextButton(
                onClick = onWriteReview
            ) {
                Text(
                    text = stringResource(
                        R.string.media_write_review
                    )
                )
            }
        }

        Spacer(
            modifier = Modifier.height(12.dp)
        )

        Surface(
            modifier = Modifier.fillMaxWidth(),
            shape = RoundedCornerShape(16.dp),
            color =
                MaterialTheme.colorScheme.surfaceContainerLow
        ) {
            Text(
                text = "No reviews have been posted yet.",
                modifier = Modifier.padding(20.dp),
                style = MaterialTheme.typography.bodyMedium,
                color =
                    MaterialTheme.colorScheme.onSurfaceVariant
            )
        }
    }
}

@Composable
private fun MediaArtworkPlaceholder() {
    Surface(
        modifier = Modifier.size(
            width = 130.dp,
            height = 170.dp
        ),
        shape = RoundedCornerShape(18.dp),
        color = MaterialTheme.colorScheme.primaryContainer
    ) {
        Box(
            contentAlignment = Alignment.Center
        ) {
            Icon(
                imageVector =
                    Icons.AutoMirrored.Outlined.MenuBook,
                contentDescription = null,
                tint =
                    MaterialTheme.colorScheme.onPrimaryContainer,
                modifier = Modifier.size(56.dp)
            )
        }
    }
}

@Composable
private fun ActionButtons(
    libraryStatus: LibraryStatus?,
    isFavorite: Boolean,
    onAddToLibrary: () -> Unit,
    onToggleFavorite: () -> Unit
) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement =
            Arrangement.spacedBy(12.dp)
    ) {
        Button(
            onClick = onAddToLibrary,
            enabled = libraryStatus == null,
            modifier = Modifier.weight(1f)
        ) {
            Text(
                text = when (libraryStatus) {
                    LibraryStatus.WANT_TO ->
                        stringResource(
                            R.string.status_want_to
                        )

                    LibraryStatus.IN_PROGRESS ->
                        stringResource(
                            R.string.status_in_progress
                        )

                    LibraryStatus.FINISHED ->
                        stringResource(
                            R.string.status_finished
                        )

                    null ->
                        stringResource(
                            R.string.media_add_want_to
                        )
                }
            )
        }

        OutlinedButton(
            onClick = onToggleFavorite,
            modifier = Modifier.weight(1f),
            colors =
                ButtonDefaults.outlinedButtonColors(
                    contentColor =
                        MaterialTheme.colorScheme.primary
                )
        ) {
            Icon(
                imageVector = if (isFavorite) {
                    Icons.Filled.Favorite
                } else {
                    Icons.Outlined.FavoriteBorder
                },
                contentDescription = null
            )

            Spacer(
                modifier = Modifier.size(8.dp)
            )

            Text(
                text = if (isFavorite) {
                    stringResource(R.string.media_saved)
                } else {
                    stringResource(R.string.media_save)
                }
            )
        }
    }
}

@Composable
private fun DetailSectionTitle(
    text: String
) {
    Text(
        text = text.uppercase(),
        modifier = Modifier.fillMaxWidth(),
        style = MaterialTheme.typography.labelMedium,
        fontWeight = FontWeight.Bold,
        color = MaterialTheme.colorScheme.onSurfaceVariant
    )
}

@Composable
private fun MediaInformationRow(
    media: Media
) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement =
            Arrangement.spacedBy(10.dp)
    ) {
        InformationCard(
            label = "YEAR",
            value =
                media.publishedYear?.toString() ?: "—",
            modifier = Modifier.weight(1f)
        )

        InformationCard(
            label = "RATING",
            value = if (media.averageRating > 0f) {
                String.format(
                    "%.1f",
                    media.averageRating
                )
            } else {
                "—"
            },
            modifier = Modifier.weight(1f)
        )

        InformationCard(
            label = "GENRES",
            value = media.genres.size.toString(),
            modifier = Modifier.weight(1f)
        )
    }
}

@Composable
private fun InformationCard(
    label: String,
    value: String,
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier
            .background(
                color =
                    MaterialTheme.colorScheme.surfaceContainerLow,
                shape = RoundedCornerShape(14.dp)
            )
            .padding(
                horizontal = 8.dp,
                vertical = 14.dp
            ),
        horizontalAlignment =
            Alignment.CenterHorizontally
    ) {
        Text(
            text = label,
            style = MaterialTheme.typography.labelSmall,
            color =
                MaterialTheme.colorScheme.onSurfaceVariant
        )

        Spacer(
            modifier = Modifier.height(4.dp)
        )

        Text(
            text = value,
            style = MaterialTheme.typography.bodyMedium,
            fontWeight = FontWeight.SemiBold,
            textAlign = TextAlign.Center
        )
    }
}

private fun Media.creatorText(): String {
    return when (mediaType.lowercase()) {
        "book" -> author.orEmpty()
        "movie" -> director.orEmpty()
        "show" -> creator.orEmpty()

        else -> {
            author
                ?: director
                ?: creator
                ?: ""
        }
    }
}