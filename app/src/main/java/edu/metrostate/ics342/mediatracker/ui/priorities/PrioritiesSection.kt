package edu.metrostate.ics342.mediatracker.ui.priorities

import androidx.compose.foundation.gestures.detectDragGesturesAfterLongPress
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyListScope
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.DragHandle
import androidx.compose.material3.AssistChip
import androidx.compose.material3.Card
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableFloatStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.unit.dp
import edu.metrostate.ics342.mediatracker.data.model.Priority

fun LazyListScope.prioritiesSection(
    priorities: List<Priority>,
    isLoading: Boolean,
    isSaving: Boolean,
    errorMessage: String?,
    onRetry: () -> Unit,
    onMove: (Int, Int) -> Unit,
    onEditPriority: (Int) -> Unit,
    onRemovePriority: (Int) -> Unit
) {
    item(key = "priorities-header") {
        PrioritiesHeader(
            priorityCount = priorities.size,
            isLoading = isLoading,
            isSaving = isSaving,
            errorMessage = errorMessage,
            onRetry = onRetry
        )
    }

    itemsIndexed(
        items = priorities,
        key = { _, item ->
            "priority-${item.mediaId}"
        }
    ) { index, priority ->
        PriorityCard(
            priority = priority,
            index = index,
            itemCount = priorities.size,
            onMove = onMove,
            onEdit = {
                onEditPriority(priority.mediaId)
            },
            onRemove = {
                onRemovePriority(priority.mediaId)
            }
        )
    }
}

@Composable
private fun PrioritiesHeader(
    priorityCount: Int,
    isLoading: Boolean,
    isSaving: Boolean,
    errorMessage: String?,
    onRetry: () -> Unit
) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(
                start = 16.dp,
                end = 16.dp,
                top = 16.dp,
                bottom = 8.dp
            )
    ) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement =
                Arrangement.SpaceBetween
        ) {
            Text(
                text = "Priorities",
                style = MaterialTheme.typography.titleLarge
            )

            Text(
                text = "$priorityCount/5",
                style = MaterialTheme.typography.labelLarge
            )
        }

        Spacer(modifier = Modifier.height(4.dp))

        Text(
            text = "Long-press and drag to reorder.",
            style = MaterialTheme.typography.bodySmall,
            color =
                MaterialTheme.colorScheme.onSurfaceVariant
        )

        when {
            isLoading -> {
                Spacer(modifier = Modifier.height(12.dp))

                CircularProgressIndicator()
            }

            errorMessage != null -> {
                Spacer(modifier = Modifier.height(12.dp))

                Text(
                    text = errorMessage,
                    color = MaterialTheme.colorScheme.error
                )

                Spacer(modifier = Modifier.height(8.dp))

                AssistChip(
                    onClick = onRetry,
                    label = {
                        Text("Retry")
                    }
                )
            }

            priorityCount == 0 -> {
                Spacer(modifier = Modifier.height(12.dp))

                Text(
                    text =
                        "No priorities set — mark a 'Want To' item as a priority to see it here.",
                    style =
                        MaterialTheme.typography.bodyMedium
                )
            }
        }

        if (isSaving) {
            Spacer(modifier = Modifier.height(8.dp))

            Text(
                text = "Saving changes…",
                style =
                    MaterialTheme.typography.labelMedium
            )
        }
    }
}

@Composable
private fun PriorityCard(
    priority: Priority,
    index: Int,
    itemCount: Int,
    onMove: (Int, Int) -> Unit,
    onEdit: () -> Unit,
    onRemove: () -> Unit
) {
    var accumulatedDrag by remember(index) {
        mutableFloatStateOf(0f)
    }

    Card(
        onClick = onEdit,
        modifier = Modifier
            .fillMaxWidth()
            .padding(
                horizontal = 16.dp,
                vertical = 6.dp
            )
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(12.dp),
            verticalAlignment =
                Alignment.CenterVertically
        ) {
            Icon(
                imageVector = Icons.Default.DragHandle,
                contentDescription = "Drag to reorder",
                modifier = Modifier.pointerInput(
                    priority.mediaId,
                    index,
                    itemCount
                ) {
                    detectDragGesturesAfterLongPress(
                        onDragStart = {
                            accumulatedDrag = 0f
                        },
                        onDragEnd = {
                            accumulatedDrag = 0f
                        },
                        onDragCancel = {
                            accumulatedDrag = 0f
                        },
                        onDrag = { change, dragAmount ->
                            change.consume()

                            accumulatedDrag +=
                                dragAmount.y

                            val threshold = 80f

                            when {
                                accumulatedDrag > threshold &&
                                        index < itemCount - 1 -> {

                                    onMove(index, index + 1)
                                    accumulatedDrag = 0f
                                }

                                accumulatedDrag < -threshold &&
                                        index > 0 -> {

                                    onMove(index, index - 1)
                                    accumulatedDrag = 0f
                                }
                            }
                        }
                    )
                }
            )

            Column(
                modifier = Modifier
                    .weight(1f)
                    .padding(horizontal = 12.dp)
            ) {
                Text(
                    text = priority.media.title,
                    style =
                        MaterialTheme.typography.titleMedium
                )

                Text(
                    text = when (priority.priority) {
                        1 -> "High priority"
                        2 -> "Medium priority"
                        else -> "Low priority"
                    },
                    style =
                        MaterialTheme.typography.labelMedium
                )

                priority.estimatedTimeHours?.let { hours ->
                    Text(
                        text =
                            "Estimated time: ${formatHours(hours)}",
                        style =
                            MaterialTheme.typography.bodySmall
                    )
                }

                priority.notes
                    ?.takeIf { it.isNotBlank() }
                    ?.let { notes ->
                        Text(
                            text = notes,
                            style =
                                MaterialTheme.typography.bodySmall
                        )
                    }
            }

            IconButton(
                onClick = onRemove
            ) {
                Icon(
                    imageVector = Icons.Default.Delete,
                    contentDescription =
                        "Remove from priorities"
                )
            }
        }
    }
}

private fun formatHours(hours: Double): String {
    return if (hours % 1.0 == 0.0) {
        "${hours.toInt()} hours"
    } else {
        "$hours hours"
    }
}