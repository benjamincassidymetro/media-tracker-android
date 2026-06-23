package edu.metrostate.ics342.mediatracker.ui.search

import androidx.compose.foundation.layout.*
import androidx.compose.ui.Modifier
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.foundation.clickable
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.runtime.*
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel

// ── STUB — Students build this in Week 5 ─────────────────────────────────────
//
// Week 5 task: Build the Search screen.
//   1. Add a search bar (SearchBar or OutlinedTextField) at the top.
//   2. Add FilterChips for All / Books / Movies / Shows in a horizontally scrollable Row.
//   3. Display results in a LazyColumn (you'll learn why Column won't work here).
//   4. Wire to GET /media?query=...&type=...
//   5. Handle loading, empty, and error states.
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SearchScreen(
    onMediaClick: (Int) -> Unit
) {
    var query by rememberSaveable { mutableStateOf("") }

    var selectedType by rememberSaveable { mutableStateOf("All") }

    val viewModel: SearchViewModel = viewModel()

    val mediaItems by viewModel.results.collectAsState()

    val filteredMedia = mediaItems.filter {
        query.isBlank() ||
                it.title.contains(query, ignoreCase = true) ||
                it.subtitle.contains(query, ignoreCase = true)
    }
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
    ) {
        Text("Media Tracker", style = MaterialTheme.typography.headlineSmall)

        Spacer(Modifier.height(16.dp))

        OutlinedTextField(
            value = query,
            onValueChange = { query = it },
            placeholder = {
                Text("Search Books, Movies, Shows...")
            },
            singleLine = true,
            shape = RoundedCornerShape(50),
            modifier = Modifier.fillMaxWidth()
        )

        Spacer(Modifier.height(12.dp))

        LazyRow(horizontalArrangement = Arrangement.spacedBy(8.dp))
        {
            items(listOf("All", "Book", "Movie", "Show")) { type ->
                FilterChip(
                    selected = selectedType == type,
                    onClick = { selectedType = type },
                    label = { Text(type.replaceFirstChar { it.uppercase() }) }
                )
            }
        }

        Spacer(Modifier.height(16.dp))

        Text("Popular This Week", style = MaterialTheme.typography.titleMedium)

        Spacer(Modifier.height(8.dp))

        LazyColumn(verticalArrangement = Arrangement.spacedBy(8.dp)) {
            items(filteredMedia) { media ->
                Card(
                    modifier = Modifier
                        .fillMaxWidth()
                        .clickable { onMediaClick(media.id) },
                    shape = RoundedCornerShape(16.dp)
                ) {
                    Column(Modifier.padding(16.dp)) {
                        Text(
                            media.title,
                            style = MaterialTheme.typography.titleMedium
                        )

                        Text(
                            media.subtitle,
                            style = MaterialTheme.typography.bodySmall
                        )
                    }
                }
            }
        }
    }
}
