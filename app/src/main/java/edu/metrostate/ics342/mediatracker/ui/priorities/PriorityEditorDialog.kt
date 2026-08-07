package edu.metrostate.ics342.mediatracker.ui.priorities

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.FilterChip
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import edu.metrostate.ics342.mediatracker.data.model.LibraryItem

@Composable
fun PriorityEditorDialog(
    libraryItem: LibraryItem,
    onDismiss: () -> Unit,
    onSave: (
        priority: Int,
        estimatedHours: Double?,
        notes: String?
    ) -> Unit
) {
    var selectedPriority by remember {
        mutableIntStateOf(1)
    }

    var estimatedHours by remember {
        mutableStateOf("")
    }

    var notes by remember {
        mutableStateOf("")
    }

    val parsedHours = estimatedHours
        .toDoubleOrNull()
        ?.takeIf { it >= 0.0 }

    AlertDialog(
        onDismissRequest = onDismiss,
        title = {
            Text("Set priority")
        },
        text = {
            Column {
                Text(
                    text = libraryItem.media.title
                )

                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(vertical = 12.dp),
                    horizontalArrangement =
                        Arrangement.spacedBy(6.dp)
                ) {
                    FilterChip(
                        selected = selectedPriority == 1,
                        onClick = {
                            selectedPriority = 1
                        },
                        label = {
                            Text("High")
                        }
                    )

                    FilterChip(
                        selected = selectedPriority == 2,
                        onClick = {
                            selectedPriority = 2
                        },
                        label = {
                            Text("Medium")
                        }
                    )

                    FilterChip(
                        selected = selectedPriority == 3,
                        onClick = {
                            selectedPriority = 3
                        },
                        label = {
                            Text("Low")
                        }
                    )
                }

                OutlinedTextField(
                    value = estimatedHours,
                    onValueChange = { value ->
                        estimatedHours = value.filter {
                            it.isDigit() || it == '.'
                        }
                    },
                    label = {
                        Text("Estimated time (hours)")
                    },
                    keyboardOptions = KeyboardOptions(
                        keyboardType =
                            KeyboardType.Decimal
                    ),
                    singleLine = true,
                    modifier = Modifier.fillMaxWidth()
                )

                OutlinedTextField(
                    value = notes,
                    onValueChange = { value ->
                        notes = value.take(200)
                    },
                    label = {
                        Text("Notes")
                    },
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(top = 12.dp)
                )
            }
        },
        confirmButton = {
            TextButton(
                onClick = {
                    onSave(
                        selectedPriority,
                        parsedHours,
                        notes.trim().ifBlank { null }
                    )
                }
            ) {
                Text("Save")
            }
        },
        dismissButton = {
            TextButton(
                onClick = onDismiss
            ) {
                Text("Cancel")
            }
        }
    )
}