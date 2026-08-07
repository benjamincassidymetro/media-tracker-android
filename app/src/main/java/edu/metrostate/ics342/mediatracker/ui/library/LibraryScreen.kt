package edu.metrostate.ics342.mediatracker.ui.library

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
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.MoreVert
import androidx.compose.material3.Button
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel
import edu.metrostate.ics342.mediatracker.data.model.LibraryItem
import edu.metrostate.ics342.mediatracker.data.model.LibraryStatus
import edu.metrostate.ics342.mediatracker.ui.priorities.PrioritiesViewModel
import edu.metrostate.ics342.mediatracker.ui.priorities.PriorityEditorDialog
import edu.metrostate.ics342.mediatracker.ui.priorities.prioritiesSection

@Composable
fun LibraryScreen(
    onMediaClick: (Int) -> Unit,
    viewModel: LibraryViewModel = viewModel(),
    prioritiesViewModel: PrioritiesViewModel = viewModel()
) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()
    val actionError by viewModel.actionError.collectAsStateWithLifecycle()
    val prioritiesState by prioritiesViewModel.uiState
        .collectAsStateWithLifecycle()

    var selectedPriorityItem by remember {
        mutableStateOf<LibraryItem?>(null)
    }

    val snackbarHostState = remember {
        SnackbarHostState()
    }

    LaunchedEffect(Unit) {
        prioritiesViewModel.loadPriorities()
    }

    LaunchedEffect(actionError) {
        actionError?.let { message ->
            snackbarHostState.showSnackbar(message)
            viewModel.clearActionError()
        }
    }

    LaunchedEffect(prioritiesState.errorMessage) {
        prioritiesState.errorMessage?.let { message ->
            snackbarHostState.showSnackbar(message)
            prioritiesViewModel.clearError()
        }
    }

    Scaffold(
        snackbarHost = {
            SnackbarHost(snackbarHostState)
        }
    ) { innerPadding ->

        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
        ) {
            Text(
                text = "Library",
                style = MaterialTheme.typography.headlineSmall,
                fontWeight = FontWeight.Bold,
                modifier = Modifier.padding(
                    start = 16.dp,
                    end = 16.dp,
                    top = 16.dp
                )
            )

            Spacer(
                modifier = Modifier.height(12.dp)
            )

            LibraryStatusSelector(
                selectedStatus = uiState.selectedStatus,
                onStatusSelected = viewModel::selectStatus,
                modifier = Modifier.padding(horizontal = 16.dp)
            )

            Spacer(
                modifier = Modifier.height(8.dp)
            )

            LazyColumn(
                modifier = Modifier.fillMaxSize(),
                verticalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                prioritiesSection(
                    priorities = prioritiesState.priorities,
                    isLoading = prioritiesState.isLoading,
                    isSaving = prioritiesState.isSaving,
                    errorMessage = prioritiesState.errorMessage,
                    onRetry = prioritiesViewModel::loadPriorities,
                    onMove = prioritiesViewModel::movePriority,
                    onEditPriority = { mediaId ->
                        selectedPriorityItem =
                            uiState.items.firstOrNull { libraryItem ->
                                libraryItem.mediaId == mediaId
                            }
                    },
                    onRemovePriority =
                        prioritiesViewModel::removePriority
                )

                when {
                    uiState.isLoading &&
                            uiState.items.isEmpty() -> {

                        item(key = "library-loading") {
                            Box(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .height(240.dp),
                                contentAlignment = Alignment.Center
                            ) {
                                CircularProgressIndicator()
                            }
                        }
                    }

                    uiState.errorMessage != null &&
                            uiState.items.isEmpty() -> {

                        item(key = "library-error") {
                            LibraryErrorContent(
                                message =
                                    uiState.errorMessage.orEmpty(),
                                onRetry = viewModel::loadLibrary
                            )
                        }
                    }

                    uiState.items.isEmpty() -> {
                        item(key = "library-empty") {
                            LibraryEmptyContent(
                                status = uiState.selectedStatus
                            )
                        }
                    }

                    else -> {
                        item(key = "item-count") {
                            Text(
                                text =
                                    "${uiState.items.size} items",
                                style =
                                    MaterialTheme.typography.bodySmall,
                                color =
                                    MaterialTheme.colorScheme
                                        .onSurfaceVariant,
                                modifier = Modifier.padding(
                                    horizontal = 16.dp,
                                    vertical = 4.dp
                                )
                            )
                        }

                        items(
                            items = uiState.items,
                            key = { libraryItem ->
                                "library-${libraryItem.mediaId}"
                            }
                        ) { libraryItem ->

                            val alreadyPrioritized =
                                prioritiesState.priorities.any {
                                        priority ->
                                    priority.mediaId ==
                                            libraryItem.mediaId
                                }

                            LibraryItemCard(
                                item = libraryItem,
                                alreadyPrioritized =
                                    alreadyPrioritized,
                                prioritiesFull =
                                    prioritiesState.isFull,
                                onClick = {
                                    onMediaClick(
                                        libraryItem.mediaId
                                    )
                                },
                                onEditPriority = {
                                    selectedPriorityItem =
                                        libraryItem
                                },
                                onUpdateStatus = { newStatus ->
                                    viewModel.updateStatus(
                                        mediaId =
                                            libraryItem.mediaId,
                                        newStatus = newStatus
                                    )
                                },
                                onRemove = {
                                    viewModel.removeItem(
                                        libraryItem.mediaId
                                    )
                                }
                            )
                        }

                        item(key = "bottom-space") {
                            Spacer(
                                modifier = Modifier.height(24.dp)
                            )
                        }
                    }
                }
            }
        }
    }

    selectedPriorityItem?.let { libraryItem ->
        PriorityEditorDialog(
            libraryItem = libraryItem,
            onDismiss = {
                selectedPriorityItem = null
            },
            onSave = { level, hours, notes ->
                prioritiesViewModel.addPriority(
                    libraryItem = libraryItem,
                    priorityLevel = level,
                    estimatedTimeHours = hours,
                    notes = notes
                )

                selectedPriorityItem = null
            }
        )
    }
}

@Composable
private fun LibraryStatusSelector(
    selectedStatus: LibraryStatus,
    onStatusSelected: (LibraryStatus) -> Unit,
    modifier: Modifier = Modifier
) {
    Row(
        modifier = modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.spacedBy(6.dp)
    ) {
        StatusButton(
            text = "Want To",
            selected =
                selectedStatus == LibraryStatus.WANT_TO,
            onClick = {
                onStatusSelected(LibraryStatus.WANT_TO)
            },
            modifier = Modifier.weight(1f)
        )

        StatusButton(
            text = "In Progress",
            selected =
                selectedStatus == LibraryStatus.IN_PROGRESS,
            onClick = {
                onStatusSelected(
                    LibraryStatus.IN_PROGRESS
                )
            },
            modifier = Modifier.weight(1f)
        )

        StatusButton(
            text = "Finished",
            selected =
                selectedStatus == LibraryStatus.FINISHED,
            onClick = {
                onStatusSelected(LibraryStatus.FINISHED)
            },
            modifier = Modifier.weight(1f)
        )
    }
}

@Composable
private fun StatusButton(
    text: String,
    selected: Boolean,
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    Surface(
        modifier = modifier.clickable(onClick = onClick),
        shape = RoundedCornerShape(12.dp),
        color = if (selected) {
            MaterialTheme.colorScheme.primaryContainer
        } else {
            MaterialTheme.colorScheme.surfaceVariant
        }
    ) {
        Text(
            text = text,
            modifier = Modifier.padding(
                horizontal = 8.dp,
                vertical = 10.dp
            ),
            textAlign = TextAlign.Center,
            style = MaterialTheme.typography.labelMedium
        )
    }
}

@Composable
private fun LibraryItemCard(
    item: LibraryItem,
    alreadyPrioritized: Boolean,
    prioritiesFull: Boolean,
    onClick: () -> Unit,
    onEditPriority: () -> Unit,
    onUpdateStatus: (LibraryStatus) -> Unit,
    onRemove: () -> Unit
) {
    var menuExpanded by remember {
        mutableStateOf(false)
    }

    Surface(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp)
            .clickable(onClick = onClick),
        shape = RoundedCornerShape(14.dp),
        tonalElevation = 2.dp
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(14.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Column(
                modifier = Modifier.weight(1f)
            ) {
                Text(
                    text = item.media.title,
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.SemiBold
                )

                Text(
                    text = when (item.status) {
                        LibraryStatus.WANT_TO ->
                            "Want To"

                        LibraryStatus.IN_PROGRESS ->
                            "In Progress"

                        LibraryStatus.FINISHED ->
                            "Finished"
                    },
                    style = MaterialTheme.typography.bodySmall,
                    color =
                        MaterialTheme.colorScheme.onSurfaceVariant
                )
            }

            Box {
                IconButton(
                    onClick = {
                        menuExpanded = true
                    }
                ) {
                    Icon(
                        imageVector = Icons.Default.MoreVert,
                        contentDescription = "Item options"
                    )
                }

                DropdownMenu(
                    expanded = menuExpanded,
                    onDismissRequest = {
                        menuExpanded = false
                    }
                ) {
                    if (item.status == LibraryStatus.WANT_TO) {
                        DropdownMenuItem(
                            text = {
                                Text(
                                    if (alreadyPrioritized) {
                                        "Edit priority"
                                    } else {
                                        "Add to priorities"
                                    }
                                )
                            },
                            enabled =
                                alreadyPrioritized ||
                                        !prioritiesFull,
                            onClick = {
                                menuExpanded = false
                                onEditPriority()
                            }
                        )
                    }

                    if (
                        item.status !=
                        LibraryStatus.WANT_TO
                    ) {
                        DropdownMenuItem(
                            text = {
                                Text("Move to Want To")
                            },
                            onClick = {
                                menuExpanded = false
                                onUpdateStatus(
                                    LibraryStatus.WANT_TO
                                )
                            }
                        )
                    }

                    if (
                        item.status !=
                        LibraryStatus.IN_PROGRESS
                    ) {
                        DropdownMenuItem(
                            text = {
                                Text("Move to In Progress")
                            },
                            onClick = {
                                menuExpanded = false
                                onUpdateStatus(
                                    LibraryStatus.IN_PROGRESS
                                )
                            }
                        )
                    }

                    if (
                        item.status !=
                        LibraryStatus.FINISHED
                    ) {
                        DropdownMenuItem(
                            text = {
                                Text("Move to Finished")
                            },
                            onClick = {
                                menuExpanded = false
                                onUpdateStatus(
                                    LibraryStatus.FINISHED
                                )
                            }
                        )
                    }

                    DropdownMenuItem(
                        text = {
                            Text("Remove from library")
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
private fun LibraryErrorContent(
    message: String,
    onRetry: () -> Unit
) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .height(240.dp)
            .padding(24.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Text(
            text = message,
            color = MaterialTheme.colorScheme.error,
            textAlign = TextAlign.Center
        )

        Spacer(
            modifier = Modifier.height(16.dp)
        )

        Button(onClick = onRetry) {
            Text("Retry")
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

    Box(
        modifier = Modifier
            .fillMaxWidth()
            .height(240.dp)
            .padding(24.dp),
        contentAlignment = Alignment.Center
    ) {
        Text(
            text = message,
            style = MaterialTheme.typography.bodyLarge,
            textAlign = TextAlign.Center
        )
    }
}