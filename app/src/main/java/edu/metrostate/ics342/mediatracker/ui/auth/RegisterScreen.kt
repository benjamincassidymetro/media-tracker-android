package edu.metrostate.ics342.mediatracker.ui.auth

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.imePadding
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Button
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusDirection
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel

@Composable
fun RegisterScreen(
    onRegisterSuccess: () -> Unit,
    onNavigateToLogin: () -> Unit,
    viewModel: RegisterViewModel = viewModel()
) {
    val formState by viewModel.formState
        .collectAsStateWithLifecycle()

    val registerState by viewModel.registerState
        .collectAsStateWithLifecycle()

    val focusManager = LocalFocusManager.current

    val isLoading =
        registerState is RegisterUiState.Loading

    val errorMessage =
        (registerState as? RegisterUiState.Error)
            ?.message

    LaunchedEffect(registerState) {
        if (registerState is RegisterUiState.Success) {
            viewModel.resetRegisterState()
            onRegisterSuccess()
        }
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState())
            .navigationBarsPadding()
            .imePadding()
            .padding(horizontal = 24.dp),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment =
            Alignment.CenterHorizontally
    ) {
        Spacer(
            modifier = Modifier.height(32.dp)
        )

        Text(
            text = "Create Account",
            style =
                MaterialTheme.typography.headlineMedium,
            color = MaterialTheme.colorScheme.primary
        )

        Spacer(
            modifier = Modifier.height(8.dp)
        )

        Text(
            text =
                "Create your Media Tracker profile.",
            style =
                MaterialTheme.typography.bodyMedium,
            color =
                MaterialTheme.colorScheme.onSurfaceVariant,
            textAlign = TextAlign.Center
        )

        Spacer(
            modifier = Modifier.height(28.dp)
        )

        OutlinedTextField(
            value = formState.email,
            onValueChange =
                viewModel::onEmailChange,
            label = {
                Text("Email")
            },
            singleLine = true,
            enabled = !isLoading,
            shape = RoundedCornerShape(12.dp),
            keyboardOptions =
                KeyboardOptions(
                    keyboardType =
                        KeyboardType.Email,
                    imeAction = ImeAction.Next
                ),
            keyboardActions =
                KeyboardActions(
                    onNext = {
                        focusManager.moveFocus(
                            FocusDirection.Down
                        )
                    }
                ),
            modifier = Modifier.fillMaxWidth()
        )

        Spacer(
            modifier = Modifier.height(12.dp)
        )

        OutlinedTextField(
            value = formState.username,
            onValueChange =
                viewModel::onUsernameChange,
            label = {
                Text("Username")
            },
            supportingText = {
                Text("At least 3 characters")
            },
            singleLine = true,
            enabled = !isLoading,
            shape = RoundedCornerShape(12.dp),
            keyboardOptions =
                KeyboardOptions(
                    keyboardType =
                        KeyboardType.Text,
                    imeAction = ImeAction.Next
                ),
            keyboardActions =
                KeyboardActions(
                    onNext = {
                        focusManager.moveFocus(
                            FocusDirection.Down
                        )
                    }
                ),
            modifier = Modifier.fillMaxWidth()
        )

        Spacer(
            modifier = Modifier.height(4.dp)
        )

        OutlinedTextField(
            value = formState.displayName,
            onValueChange =
                viewModel::onDisplayNameChange,
            label = {
                Text("Display name")
            },
            singleLine = true,
            enabled = !isLoading,
            shape = RoundedCornerShape(12.dp),
            keyboardOptions =
                KeyboardOptions(
                    keyboardType =
                        KeyboardType.Text,
                    imeAction = ImeAction.Next
                ),
            keyboardActions =
                KeyboardActions(
                    onNext = {
                        focusManager.moveFocus(
                            FocusDirection.Down
                        )
                    }
                ),
            modifier = Modifier.fillMaxWidth()
        )

        Spacer(
            modifier = Modifier.height(12.dp)
        )

        OutlinedTextField(
            value = formState.password,
            onValueChange =
                viewModel::onPasswordChange,
            label = {
                Text("Password")
            },
            supportingText = {
                Text("At least 8 characters")
            },
            singleLine = true,
            enabled = !isLoading,
            visualTransformation =
                PasswordVisualTransformation(),
            shape = RoundedCornerShape(12.dp),
            keyboardOptions =
                KeyboardOptions(
                    keyboardType =
                        KeyboardType.Password,
                    imeAction = ImeAction.Next
                ),
            keyboardActions =
                KeyboardActions(
                    onNext = {
                        focusManager.moveFocus(
                            FocusDirection.Down
                        )
                    }
                ),
            modifier = Modifier.fillMaxWidth()
        )

        Spacer(
            modifier = Modifier.height(4.dp)
        )

        OutlinedTextField(
            value = formState.confirmPassword,
            onValueChange =
                viewModel::onConfirmPasswordChange,
            label = {
                Text("Confirm password")
            },
            singleLine = true,
            enabled = !isLoading,
            visualTransformation =
                PasswordVisualTransformation(),
            shape = RoundedCornerShape(12.dp),
            keyboardOptions =
                KeyboardOptions(
                    keyboardType =
                        KeyboardType.Password,
                    imeAction = ImeAction.Done
                ),
            keyboardActions =
                KeyboardActions(
                    onDone = {
                        focusManager.clearFocus()
                        viewModel.register()
                    }
                ),
            modifier = Modifier.fillMaxWidth()
        )

        if (errorMessage != null) {
            Spacer(
                modifier = Modifier.height(10.dp)
            )

            Text(
                text = errorMessage,
                color =
                    MaterialTheme.colorScheme.error,
                style =
                    MaterialTheme.typography.bodySmall,
                textAlign = TextAlign.Center
            )
        }

        Spacer(
            modifier = Modifier.height(20.dp)
        )

        Button(
            onClick = {
                focusManager.clearFocus()
                viewModel.register()
            },
            enabled = !isLoading,
            shape = RoundedCornerShape(50),
            modifier = Modifier
                .fillMaxWidth()
                .height(48.dp)
        ) {
            if (isLoading) {
                CircularProgressIndicator(
                    modifier = Modifier.height(22.dp),
                    strokeWidth = 2.dp,
                    color =
                        MaterialTheme.colorScheme.onPrimary
                )
            } else {
                Text("Create Account")
            }
        }

        Spacer(
            modifier = Modifier.height(12.dp)
        )

        TextButton(
            onClick = onNavigateToLogin,
            enabled = !isLoading
        ) {
            Text("Already have an account? Sign in")
        }

        Spacer(
            modifier = Modifier.height(32.dp)
        )
    }
}