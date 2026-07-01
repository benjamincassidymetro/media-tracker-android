package edu.metrostate.ics342.mediatracker.ui.detail

import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
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

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
    ) {
        Text(currentMedia.title, style = MaterialTheme.typography.headlineSmall)

        Spacer(Modifier.height(8.dp))

        Text(
            text = currentMedia.creatorCredit(),
            style = MaterialTheme.typography.bodyMedium
        )

        Spacer(Modifier.height(8.dp))

        Text(
            text = "${currentMedia.mediaType} • ${currentMedia.publishedYear ?: "Unknown"} • ★ ${currentMedia.averageRating}",
            style = MaterialTheme.typography.bodySmall
        )

        Spacer(Modifier.height(16.dp))

        Button(onClick = { onWriteReview(currentMedia.id) }) {
            Text("Write Review")
        }

        Spacer(Modifier.height(16.dp))

        OutlinedButton(onClick = onNavigateBack) {
            Text("Back")
        }
    }
}

private fun Media.creatorCredit(): String {
    return when (mediaType) {
        "book" -> author ?: "Unknown Author"
        "movie" -> director ?: "Unknown Director"
        "show" -> creator ?: "Unknown Creator"
        else -> ""
    }
}