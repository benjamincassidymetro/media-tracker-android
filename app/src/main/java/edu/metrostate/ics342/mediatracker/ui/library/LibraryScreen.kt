package edu.metrostate.ics342.mediatracker.ui.library

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
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
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.outlined.MenuBook
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.MoreVert
import androidx.compose.material.icons.filled.PlayCircle
import androidx.compose.material.icons.filled.Schedule
import androidx.compose.material3.Button
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import edu.metrostate.ics342.mediatracker.R
import edu.metrostate.ics342.mediatracker.data.model.LibraryItem
import edu.metrostate.ics342.mediatracker.data.model.LibraryStatus
import edu.metrostate.ics342.mediatracker.data.model.Media
import androidx.compose.ui.res.stringResource

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun LibraryScreen(
    onMediaClick: (Int) -> Unit,
    viewModel: LibraryViewModel = viewModel()
) {
    val uiState by viewModel.uiState.collectAsState()
    val actionError by viewModel.actionError.collectAsState()

    val snackbarHostState = remember {
        SnackbarHostState()
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
                title = {
                    Text(
                        text = stringResource(
                            R.string.library_title
                        ),
                        fontWeight = FontWeight.Bold
                    )
                }
            )
        }
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
                .padding(horizontal = 16.dp)
        ) {
            LibraryStatusSelector(
                selectedStatus = uiState.selectedStatus,
                onStatusSelected =
                    viewModel::selectStatus
            )

            Spacer(
                modifier = Modifier.height(12.dp)
            )

            when {
                uiState.isLoading &&
                        uiState.items.isEmpty() -> {
                    LibraryLoadingContent()
                }

                uiState.errorMessage != null &&
                        uiState.items.isEmpty() -> {
                    LibraryErrorContent(
                        message =
                            uiState.errorMessage.orEmpty(),
                        onRetry =
                            viewModel::loadLibrary
                    )
                }

                uiState.items.isEmpty() -> {
                    LibraryEmptyContent(
                        status = uiState.selectedStatus
                    )
                }

                else -> {
                    LibraryListContent(
                        items = uiState.items,
                        selectedStatus =
                            uiState.selectedStatus,
                        onMediaClick = onMediaClick,
                        onUpdateStatus =
                            viewModel::updateStatus,
                        onRemoveItem =
                            viewModel::removeItem
                    )
                }
            }
        }
    }
}

@Composable
private fun LibraryStatusSelector(
    selectedStatus: LibraryStatus,
    onStatusSelected: (LibraryStatus) -> Unit
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .background(
                color =
                    MaterialTheme.colorScheme.surfaceContainer,
                shape = RoundedCornerShape(14.dp)
            )
            .padding(4.dp),
        horizontalArrangement =
            Arrangement.spacedBy(4.dp)
    ) {
        LibraryStatusTab(
            text = stringResource(
                R.string.status_want_to
            ),
            selected =
                selectedStatus == LibraryStatus.WANT_TO,
            onClick = {
                onStatusSelected(
                    LibraryStatus.WANT_TO
                )
            },
            modifier = Modifier.weight(1f)
        )

        LibraryStatusTab(
            text = stringResource(
                R.string.status_in_progress
            ),
            selected =
                selectedStatus ==
                        LibraryStatus.IN_PROGRESS,
            onClick = {
                onStatusSelected(
                    LibraryStatus.IN_PROGRESS
                )
            },
            modifier = Modifier.weight(1f)
        )

        LibraryStatusTab(
            text = stringResource(
                R.string.status_finished
            ),
            selected =
                selectedStatus ==
                        LibraryStatus.FINISHED,
            onClick = {
                onStatusSelected(
                    LibraryStatus.FINISHED
                )
            },
            modifier = Modifier.weight(1f)
        )
    }
}

@Composable
private fun LibraryStatusTab(
    text: String,
    selected: Boolean,
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    Surface(
        modifier = modifier.clickable(
            onClick = onClick
        ),
        shape = RoundedCornerShape(10.dp),
        color = if (selected) {
            MaterialTheme.colorScheme.primaryContainer
        } else {
            MaterialTheme.colorScheme.surfaceContainer
        },
        contentColor = if (selected) {
            MaterialTheme.colorScheme.onPrimaryContainer
        } else {
            MaterialTheme.colorScheme.onSurfaceVariant
        }
    ) {
        Box(
            modifier = Modifier.padding(
                horizontal = 6.dp,
                vertical = 10.dp
            ),
            contentAlignment = Alignment.Center
        ) {
            Text(
                text = text,
                style = MaterialTheme.typography.labelMedium,
                fontWeight = if (selected) {
                    FontWeight.Bold
                } else {
                    FontWeight.Normal
                },
                maxLines = 1
            )
        }
    }
}

@Composable
private fun LibraryLoadingContent() {
    Box(
        modifier = Modifier.fillMaxSize(),
        contentAlignment = Alignment.Center
    ) {
        CircularProgressIndicator()
    }
}

@Composable
private fun LibraryErrorContent(
    message: String,
    onRetry: () -> Unit
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(32.dp),
        horizontalAlignment =
            Alignment.CenterHorizontally,
        verticalArrangement =
            Arrangement.Center
    ) {
        Text(
            text = message,
            style = MaterialTheme.typography.bodyLarge,
            color = MaterialTheme.colorScheme.error,
            textAlign = TextAlign.Center
        )

        Spacer(
            modifier = Modifier.height(16.dp)
        )

        Button(
            onClick = onRetry
        ) {
            Text(
                text = stringResource(
                    R.string.action_retry
                )
            )
        }
    }
}

@Composable
private fun LibraryEmptyContent(
    status: LibraryStatus
) {
    val message = when (status) {
        LibraryStatus.WANT_TO ->
            "Nothing in Want To yet."

        LibraryStatus.IN_PROGRESS ->
            "Nothing In Progress yet."

        LibraryStatus.FINISHED ->
            "Nothing in Finished yet."
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(32.dp),
        horizontalAlignment =
            Alignment.CenterHorizontally,
        verticalArrangement =
            Arrangement.Center
    ) {
        Icon(
            imageVector =
                Icons.AutoMirrored.Outlined.MenuBook,
            contentDescription = null,
            modifier = Modifier.size(64.dp),
            tint =
                MaterialTheme.colorScheme.onSurfaceVariant
        )

        Spacer(
            modifier = Modifier.height(16.dp)
        )

        Text(
            text = message,
            style = MaterialTheme.typography.titleMedium,
            fontWeight = FontWeight.SemiBold,
            textAlign = TextAlign.Center
        )

        Spacer(
            modifier = Modifier.height(6.dp)
        )

        Text(
            text = stringResource(
                R.string.library_empty
            ),
            style = MaterialTheme.typography.bodyMedium,
            color =
                MaterialTheme.colorScheme.onSurfaceVariant,
            textAlign = TextAlign.Center
        )
    }
}

@Composable
private fun LibraryListContent(
    items: List<LibraryItem>,
    selectedStatus: LibraryStatus,
    onMediaClick: (Int) -> Unit,
    onUpdateStatus:
        (Int, LibraryStatus) -> Unit,
    onRemoveItem: (Int) -> Unit
) {
    Column(
        modifier = Modifier.fillMaxSize()
    ) {
        Text(
            text = if (items.size == 1) {
                stringResource(
                    R.string.library_item_count,
                    items.size
                )
            } else {
                stringResource(
                    R.string.library_items_count,
                    items.size
                )
            },
            style = MaterialTheme.typography.bodySmall,
            color =
                MaterialTheme.colorScheme.onSurfaceVariant,
            modifier = Modifier.padding(
                horizontal = 2.dp,
                vertical = 4.dp
            )
        )

        LazyColumn(
            modifier = Modifier.fillMaxSize(),
            verticalArrangement =
                Arrangement.spacedBy(10.dp)
        ) {
            items(
                items = items,
                key = { item ->
                    item.mediaId
                }
            ) { item ->
                LibraryItemCard(
                    item = item,
                    selectedStatus = selectedStatus,
                    onClick = {
                        onMediaClick(item.mediaId)
                    },
                    onUpdateStatus = {
                            newStatus ->
                        onUpdateStatus(
                            item.mediaId,
                            newStatus
                        )
                    },
                    onRemove = {
                        onRemoveItem(item.mediaId)
                    }
                )
            }

            item {
                Spacer(
                    modifier = Modifier.height(20.dp)
                )
            }
        }
    }
}

@Composable
private fun LibraryItemCard(
    item: LibraryItem,
    selectedStatus: LibraryStatus,
    onClick: () -> Unit,
    onUpdateStatus: (LibraryStatus) -> Unit,
    onRemove: () -> Unit
) {
    val media = item.media

    var menuExpanded by remember {
        mutableStateOf(false)
    }

    Surface(
        modifier = Modifier
            .fillMaxWidth()
            .clickable(onClick = onClick),
        shape = RoundedCornerShape(16.dp),
        color =
            MaterialTheme.colorScheme.surfaceContainerLow,
        tonalElevation = 1.dp
    ) {
        Row(
            modifier = Modifier.padding(12.dp),
            verticalAlignment =
                Alignment.CenterVertically
        ) {
            LibraryArtworkPlaceholder(
                mediaType =
                    media?.mediaType.orEmpty()
            )

            Spacer(
                modifier = Modifier.size(12.dp)
            )

            Column(
                modifier = Modifier.weight(1f)
            ) {
                Text(
                    text =
                        media?.title
                            ?: "Unknown media",
                    style =
                        MaterialTheme.typography.titleSmall,
                    fontWeight = FontWeight.Bold,
                    maxLines = 2,
                    overflow = TextOverflow.Ellipsis
                )

                val creator =
                    media?.creatorText().orEmpty()

                if (creator.isNotBlank()) {
                    Spacer(
                        modifier = Modifier.height(3.dp)
                    )

                    Text(
                        text = creator,
                        style =
                            MaterialTheme.typography.bodySmall,
                        color =
                            MaterialTheme.colorScheme.onSurfaceVariant,
                        maxLines = 1,
                        overflow = TextOverflow.Ellipsis
                    )
                }

                Spacer(
                    modifier = Modifier.height(8.dp)
                )

                LibraryStatusBadge(
                    status = item.status
                )
            }

            Box {
                IconButton(
                    onClick = {
                        menuExpanded = true
                    }
                ) {
                    Icon(
                        imageVector =
                            Icons.Filled.MoreVert,
                        contentDescription =
                            stringResource(
                                R.string.action_more_options
                            )
                    )
                }

                DropdownMenu(
                    expanded = menuExpanded,
                    onDismissRequest = {
                        menuExpanded = false
                    }
                ) {
                    LibraryStatus.entries
                        .filter {
                            it != selectedStatus
                        }
                        .forEach { status ->
                            DropdownMenuItem(
                                text = {
                                    Text(
                                        text = status.label()
                                    )
                                },
                                leadingIcon = {
                                    Icon(
                                        imageVector =
                                            status.icon(),
                                        contentDescription = null
                                    )
                                },
                                onClick = {
                                    menuExpanded = false
                                    onUpdateStatus(status)
                                }
                            )
                        }

                    DropdownMenuItem(
                        text = {
                            Text(
                                text = stringResource(
                                    R.string
                                        .action_remove_from_library
                                ),
                                color =
                                    MaterialTheme.colorScheme.error
                            )
                        },
                        onClick = {
                            menuExpanded = false
                            onRemove()
                        }
                    )
                }
            }
        }
    }
}

@Composable
private fun LibraryArtworkPlaceholder(
    mediaType: String
) {
    val containerColor = when (
        mediaType.lowercase()
    ) {
        "movie" ->
            MaterialTheme.colorScheme.secondaryContainer

        "show" ->
            MaterialTheme.colorScheme.tertiaryContainer

        else ->
            MaterialTheme.colorScheme.primaryContainer
    }

    Surface(
        modifier = Modifier.size(
            width = 60.dp,
            height = 76.dp
        ),
        shape = RoundedCornerShape(10.dp),
        color = containerColor
    ) {
        Box(
            contentAlignment = Alignment.Center
        ) {
            Icon(
                imageVector =
                    Icons.AutoMirrored.Outlined.MenuBook,
                contentDescription = null,
                modifier = Modifier.size(28.dp)
            )
        }
    }
}

@Composable
private fun LibraryStatusBadge(
    status: LibraryStatus
) {
    Surface(
        shape = RoundedCornerShape(50),
        color =
            MaterialTheme.colorScheme.primaryContainer
    ) {
        Row(
            modifier = Modifier.padding(
                horizontal = 9.dp,
                vertical = 4.dp
            ),
            verticalAlignment =
                Alignment.CenterVertically,
            horizontalArrangement =
                Arrangement.spacedBy(4.dp)
        ) {
            Icon(
                imageVector = status.icon(),
                contentDescription = null,
                modifier = Modifier.size(14.dp)
            )

            Text(
                text = status.label(),
                style =
                    MaterialTheme.typography.labelSmall,
                color =
                    MaterialTheme.colorScheme.onPrimaryContainer
            )
        }
    }
}

@Composable
private fun LibraryStatus.label(): String {
    return when (this) {
        LibraryStatus.WANT_TO ->
            stringResource(R.string.status_want_to)

        LibraryStatus.IN_PROGRESS ->
            stringResource(
                R.string.status_in_progress
            )

        LibraryStatus.FINISHED ->
            stringResource(R.string.status_finished)
    }
}

private fun LibraryStatus.icon() =
    when (this) {
        LibraryStatus.WANT_TO ->
            Icons.Filled.Schedule

        LibraryStatus.IN_PROGRESS ->
            Icons.Filled.PlayCircle

        LibraryStatus.FINISHED ->
            Icons.Filled.CheckCircle
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