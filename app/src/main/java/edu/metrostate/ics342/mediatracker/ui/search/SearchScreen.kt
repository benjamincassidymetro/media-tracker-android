package edu.metrostate.ics342.mediatracker.ui.search

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FilterChip
import androidx.compose.material3.FilterChipDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SearchScreen(
    onMediaClick: (Int) -> Unit
) {
    var query by rememberSaveable {
        mutableStateOf("")
    }

    var selectedType by rememberSaveable {
        mutableStateOf("All")
    }

    val viewModel: SearchViewModel = viewModel()

    val uiState by viewModel.uiState
        .collectAsStateWithLifecycle()

    val mediaItems = uiState.items

    val filteredMedia = mediaItems.filter { media ->

        val credit =
            when (media.mediaType) {
                "book" -> media.author.orEmpty()
                "movie" -> media.director.orEmpty()
                "show" -> media.creator.orEmpty()
                else -> ""
            }

        val matchesQuery =
            query.isBlank() ||
                    media.title.contains(
                        query,
                        ignoreCase = true
                    ) ||
                    credit.contains(
                        query,
                        ignoreCase = true
                    )

        val selectedApiType =
            when (selectedType) {
                "Book" -> "book"
                "Movie" -> "movie"
                "Show" -> "show"
                else -> null
            }

        val matchesType =
            selectedApiType == null ||
                    media.mediaType == selectedApiType

        matchesQuery && matchesType
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
    ) {
        Text(
            text = "Search",
            style = MaterialTheme.typography.headlineSmall
        )

        Spacer(
            modifier = Modifier.height(16.dp)
        )

        OutlinedTextField(
            value = query,
            onValueChange = {
                query = it
            },
            placeholder = {
                Text(
                    "Search books, movies, shows..."
                )
            },
            singleLine = true,
            shape = RoundedCornerShape(28.dp),
            colors =
                OutlinedTextFieldDefaults.colors(
                    focusedBorderColor =
                        MaterialTheme.colorScheme.primary
                ),
            modifier = Modifier.fillMaxWidth()
        )

        Spacer(
            modifier = Modifier.height(12.dp)
        )

        LazyRow(
            horizontalArrangement =
                Arrangement.spacedBy(8.dp)
        ) {
            items(
                listOf(
                    "All",
                    "Book",
                    "Movie",
                    "Show"
                )
            ) { type ->

                FilterChip(
                    selected =
                        selectedType == type,
                    onClick = {
                        selectedType = type
                    },
                    label = {
                        Text(
                            if (type == "All") {
                                "All"
                            } else {
                                "${type}s"
                            }
                        )
                    },
                    shape =
                        RoundedCornerShape(8.dp),
                    colors =
                        FilterChipDefaults
                            .filterChipColors(
                                selectedContainerColor =
                                    MaterialTheme
                                        .colorScheme
                                        .primaryContainer,
                                selectedLabelColor =
                                    MaterialTheme
                                        .colorScheme
                                        .primary,
                                containerColor =
                                    MaterialTheme
                                        .colorScheme
                                        .surface,
                                labelColor =
                                    MaterialTheme
                                        .colorScheme
                                        .onSurfaceVariant
                            ),
                    border =
                        FilterChipDefaults
                            .filterChipBorder(
                                enabled = true,
                                selected =
                                    selectedType == type,
                                borderColor =
                                    MaterialTheme
                                        .colorScheme
                                        .outline,
                                selectedBorderColor =
                                    MaterialTheme
                                        .colorScheme
                                        .primaryContainer
                            )
                )
            }
        }

        Spacer(
            modifier = Modifier.height(16.dp)
        )

        Text(
            text = "POPULAR THIS WEEK",
            style = MaterialTheme.typography.labelSmall
        )

        Spacer(
            modifier = Modifier.height(8.dp)
        )

        when {
            uiState.isLoading -> {
                Text("Loading media...")
            }

            uiState.errorMessage != null -> {
                Text(
                    text =
                        uiState.errorMessage
                            ?: "Unable to load media.",
                    color =
                        MaterialTheme.colorScheme.error
                )
            }

            filteredMedia.isEmpty() -> {
                Text("No media found.")
            }

            else -> {
                LazyColumn(
                    verticalArrangement =
                        Arrangement.spacedBy(8.dp)
                ) {
                    items(
                        items = filteredMedia,
                        key = { media ->
                            media.id
                        }
                    ) { media ->

                        val credit =
                            when (media.mediaType) {
                                "book" ->
                                    media.author.orEmpty()

                                "movie" ->
                                    media.director.orEmpty()

                                "show" ->
                                    media.creator.orEmpty()

                                else -> ""
                            }

                        Card(
                            modifier = Modifier
                                .fillMaxWidth()
                                .clickable {
                                    onMediaClick(media.id)
                                },
                            shape =
                                RoundedCornerShape(12.dp),
                            elevation =
                                CardDefaults.cardElevation(
                                    defaultElevation = 2.dp
                                ),
                            colors =
                                CardDefaults.cardColors(
                                    containerColor =
                                        MaterialTheme
                                            .colorScheme
                                            .surface
                                )
                        ) {
                            Column(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(16.dp)
                            ) {
                                Text(
                                    text = media.title,
                                    style =
                                        MaterialTheme
                                            .typography
                                            .titleMedium
                                )

                                Spacer(
                                    modifier =
                                        Modifier.height(4.dp)
                                )

                                Text(
                                    text = credit,
                                    style =
                                        MaterialTheme
                                            .typography
                                            .bodyMedium,
                                    color =
                                        MaterialTheme
                                            .colorScheme
                                            .onSurfaceVariant
                                )

                                Spacer(
                                    modifier =
                                        Modifier.height(4.dp)
                                )

                                Text(
                                    text =
                                        "★ ${media.averageRating} · " +
                                                media.mediaType
                                                    .replaceFirstChar {
                                                        it.uppercase()
                                                    } +
                                                " · " +
                                                (media.publishedYear
                                                    ?: ""),
                                    style =
                                        MaterialTheme
                                            .typography
                                            .bodySmall,
                                    color =
                                        MaterialTheme
                                            .colorScheme
                                            .tertiary
                                )
                            }
                        }
                    }
                }
            }
        }
    }
}