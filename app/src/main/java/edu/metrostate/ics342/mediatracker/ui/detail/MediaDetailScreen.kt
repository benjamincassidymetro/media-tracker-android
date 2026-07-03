package edu.metrostate.ics342.mediatracker.ui.detail

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.FavoriteBorder
import androidx.compose.material.icons.filled.MoreVert
import androidx.compose.material.icons.filled.Star
import androidx.compose.material.icons.outlined.MenuBook
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import edu.metrostate.ics342.mediatracker.data.model.Media

// ── STUB — Students build this in Week 7 ─────────────────────────────────────
//
// Week 7 task: Build the Media Detail screen.
//   1. Receive mediaId from the navigation argument (typed Int — see NavGraph).
//   2. Call GET /media/{mediaId} to load full details.
//   3. Display: cover image, title, creator credit, metadata grid, genre chips,
//      average rating, description, and a library status control.
//   4. Display the reviews list from GET /reviews?mediaId={id}.
//   5. Handle loading and error states (full-screen — no half-built screens).
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MediaDetailScreen(
    mediaId: Int,
    onNavigateBack: () -> Unit,
    onWriteReview: (Int) -> Unit
) {
    val viewModel: MediaDetailViewModel = viewModel()

    LaunchedEffect(mediaId) {
        viewModel.loadMedia(mediaId)
    }

    val media by viewModel.media.collectAsState()

    if (media == null) {
        Box(
            modifier = Modifier.fillMaxSize(),
            contentAlignment = Alignment.Center
        ) {
            Text("Media not found.")
        }
        return
    }

    val currentMedia = media!!

    Scaffold(
        topBar = {
            TopAppBar(
                title = { },
                navigationIcon = {
                    IconButton(onClick = onNavigateBack) {
                        Icon(Icons.Default.ArrowBack, contentDescription = "Back")
                    }
                },
                actions = {
                    IconButton(onClick = { }) {
                        Icon(Icons.Default.MoreVert, contentDescription = "More options")
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

            Spacer(Modifier.height(16.dp))

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

            Spacer(Modifier.height(8.dp))

            RatingRow(currentMedia)

            Spacer(Modifier.height(20.dp))

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                Button(
                    onClick = { },
                    modifier = Modifier.weight(1f)
                ) {
                    Text("+ Want To")
                }

                OutlinedButton(
                    onClick = { },
                    modifier = Modifier.weight(1f)
                ) {
                    Icon(Icons.Default.FavoriteBorder, contentDescription = null)
                    Spacer(Modifier.width(8.dp))
                    Text("Save")
                }
            }

            Spacer(Modifier.height(24.dp))

            SectionLabel("ABOUT")

            Text(
                text = currentMedia.descriptionText(),
                style = MaterialTheme.typography.bodyMedium,
                color = MaterialTheme.colorScheme.onSurfaceVariant,
                modifier = Modifier.fillMaxWidth()
            )

            Spacer(Modifier.height(20.dp))

            StatGrid(currentMedia)

            Spacer(Modifier.height(24.dp))

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                SectionLabel("REVIEWS (${currentMedia.ratingCount})")

                TextButton(onClick = { onWriteReview(currentMedia.id) }) {
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

            Spacer(Modifier.height(12.dp))

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
private fun CoverPlaceholder() {
    Card(
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.primaryContainer
        ),
        modifier = Modifier.size(width = 120.dp, height = 150.dp)
    ) {
        Box(
            modifier = Modifier.fillMaxSize(),
            contentAlignment = Alignment.Center
        ) {
            Icon(
                Icons.Outlined.MenuBook,
                contentDescription = null,
                modifier = Modifier.size(48.dp),
                tint = MaterialTheme.colorScheme.primary
            )
        }
    }
}

@Composable
private fun RatingRow(media: Media) {
    Row(verticalAlignment = Alignment.CenterVertically) {
        repeat(5) {
            Icon(
                Icons.Default.Star,
                contentDescription = null,
                tint = MaterialTheme.colorScheme.secondary,
                modifier = Modifier.size(18.dp)
            )
        }

        Spacer(Modifier.width(6.dp))

        Text(
            text = "${media.averageRating}",
            fontWeight = FontWeight.Bold,
            color = MaterialTheme.colorScheme.secondary
        )

        Spacer(Modifier.width(4.dp))

        Text(
            text = "(${media.ratingCount})",
            color = MaterialTheme.colorScheme.onSurfaceVariant
        )
    }
}

@Composable
private fun SectionLabel(text: String) {
    Text(
        text = text,
        style = MaterialTheme.typography.labelMedium,
        fontWeight = FontWeight.Bold,
        color = MaterialTheme.colorScheme.onSurfaceVariant,
        modifier = Modifier.fillMaxWidth()
    )
}

@Composable
private fun StatGrid(media: Media) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        StatBox("YEAR", media.publishedYear?.toString() ?: "Unknown", Modifier.weight(1f))
        StatBox(media.middleStatLabel(), media.middleStatValue(), Modifier.weight(1f))
        StatBox("GENRE", media.genres.firstOrNull() ?: "Unknown", Modifier.weight(1f))
    }
}

@Composable
private fun StatBox(label: String, value: String, modifier: Modifier = Modifier) {
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
                .padding(vertical = 16.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(label, style = MaterialTheme.typography.labelSmall)
            Text(value, fontWeight = FontWeight.Bold)
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
                onClick = { },
                label = { Text(avatar) }
            )

            Spacer(Modifier.width(12.dp))

            Column(modifier = Modifier.weight(1f)) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Text(username, fontWeight = FontWeight.Bold)
                    Text(time, style = MaterialTheme.typography.bodySmall)
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
        "book" -> "310"
        "movie" -> "169 min"
        "show" -> "2 seasons"
        else -> "-"
    }
}

private fun Media.descriptionText(): String {
    return when (title) {
        "Dune" -> "A noble family becomes embroiled in a war for control over the most valuable substance in the universe on the desert planet Arrakis."
        "Interstellar" -> "A team of explorers travels through a wormhole in space in an attempt to ensure humanity's survival."
        "Severance" -> "Employees at a mysterious company undergo a procedure that separates their work memories from their personal lives."
        else -> "Description coming soon."
    }
}