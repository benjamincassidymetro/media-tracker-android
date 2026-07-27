package edu.metrostate.ics342.mediatracker.ui.profile

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.outlined.ArrowBack
import androidx.compose.material3.Button
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardCapitalization
import androidx.compose.ui.unit.dp
import edu.metrostate.ics342.mediatracker.R
import edu.metrostate.ics342.mediatracker.data.model.UserProfile

private const val MAX_BIO_LENGTH = 500

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun EditProfileScreen(
    onNavigateBack: () -> Unit,
    currentUser: UserProfile? = null,
    isSaving: Boolean = false,
    errorMessage: String? = null,
    saveSuccessful: Boolean = false,
    onSave: (
        displayName: String,
        username: String,
        bio: String
    ) -> Unit = { _, _, _ -> }
) {
    var displayName by rememberSaveable {
        mutableStateOf(currentUser?.displayName.orEmpty())
    }

    var username by rememberSaveable {
        mutableStateOf(currentUser?.username.orEmpty())
    }

    var bio by rememberSaveable {
        mutableStateOf(currentUser?.bio.orEmpty())
    }

    var displayNameError by remember {
        mutableStateOf(false)
    }

    var usernameError by remember {
        mutableStateOf(false)
    }

    /*
     * Populate the form when the user profile finishes loading.
     */
    LaunchedEffect(currentUser?.id) {
        currentUser?.let { user ->
            displayName = user.displayName
            username = user.username
            bio = user.bio.orEmpty()
        }
    }

    /*
     * Return to the profile screen after a successful save.
     */
    LaunchedEffect(saveSuccessful) {
        if (saveSuccessful) {
            onNavigateBack()
        }
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Text(
                        text = stringResource(
                            R.string.edit_profile_title
                        )
                    )
                },
                navigationIcon = {
                    IconButton(
                        onClick = onNavigateBack,
                        enabled = !isSaving
                    ) {
                        Icon(
                            imageVector =
                                Icons.AutoMirrored.Outlined.ArrowBack,
                            contentDescription =
                                stringResource(R.string.action_back)
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
                .padding(
                    horizontal = 24.dp,
                    vertical = 16.dp
                ),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            OutlinedTextField(
                value = displayName,
                onValueChange = { newValue ->
                    displayName = newValue
                    displayNameError = false
                },
                label = {
                    Text(
                        text = stringResource(
                            R.string.display_name_label
                        )
                    )
                },
                singleLine = true,
                enabled = !isSaving,
                isError = displayNameError,
                supportingText = {
                    if (displayNameError) {
                        Text(
                            text = stringResource(
                                R.string.profile_display_name_required
                            )
                        )
                    }
                },
                keyboardOptions = KeyboardOptions(
                    capitalization = KeyboardCapitalization.Words,
                    imeAction = ImeAction.Next
                ),
                modifier = Modifier.fillMaxWidth()
            )

            OutlinedTextField(
                value = username,
                onValueChange = { newValue ->
                    username = newValue
                    usernameError = false
                },
                label = {
                    Text(
                        text = stringResource(
                            R.string.username_label
                        )
                    )
                },
                singleLine = true,
                enabled = !isSaving,
                isError = usernameError,
                supportingText = {
                    if (usernameError) {
                        Text(
                            text = stringResource(
                                R.string.profile_username_required
                            )
                        )
                    }
                },
                keyboardOptions = KeyboardOptions(
                    capitalization = KeyboardCapitalization.None,
                    imeAction = ImeAction.Next
                ),
                modifier = Modifier.fillMaxWidth()
            )

            OutlinedTextField(
                value = bio,
                onValueChange = { newValue ->
                    if (newValue.length <= MAX_BIO_LENGTH) {
                        bio = newValue
                    }
                },
                label = {
                    Text(
                        text = stringResource(R.string.bio_label)
                    )
                },
                placeholder = {
                    Text(
                        text = stringResource(
                            R.string.profile_bio_hint
                        )
                    )
                },
                enabled = !isSaving,
                minLines = 4,
                maxLines = 6,
                supportingText = {
                    Text(
                        text = "${bio.length}/$MAX_BIO_LENGTH"
                    )
                },
                keyboardOptions = KeyboardOptions(
                    capitalization = KeyboardCapitalization.Sentences,
                    imeAction = ImeAction.Done
                ),
                modifier = Modifier
                    .fillMaxWidth()
                    .height(160.dp)
            )

            if (!errorMessage.isNullOrBlank()) {
                Text(
                    text = errorMessage,
                    color = MaterialTheme.colorScheme.error,
                    style = MaterialTheme.typography.bodyMedium
                )
            }

            Spacer(
                modifier = Modifier.height(8.dp)
            )

            Button(
                onClick = {
                    val cleanedDisplayName = displayName.trim()
                    val cleanedUsername = username.trim()
                    val cleanedBio = bio.trim()

                    displayNameError = cleanedDisplayName.isBlank()
                    usernameError = cleanedUsername.isBlank()

                    if (!displayNameError && !usernameError) {
                        onSave(
                            cleanedDisplayName,
                            cleanedUsername,
                            cleanedBio
                        )
                    }
                },
                enabled = !isSaving,
                modifier = Modifier.fillMaxWidth()
            ) {
                if (isSaving) {
                    CircularProgressIndicator(
                        modifier = Modifier.size(20.dp),
                        strokeWidth = 2.dp,
                        color = MaterialTheme.colorScheme.onPrimary
                    )
                } else {
                    Text(
                        text = stringResource(
                            R.string.save_changes_button
                        )
                    )
                }
            }

            Spacer(
                modifier = Modifier.height(16.dp)
            )
        }
    }
}