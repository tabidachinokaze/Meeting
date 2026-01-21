package moe.tabidachi.meeting.ui.auth

import androidx.annotation.StringRes
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ColumnScope
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.imePadding
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.layout.systemBarsPadding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.AccountCircle
import androidx.compose.material.icons.rounded.Email
import androidx.compose.material.icons.rounded.Lock
import androidx.compose.material.icons.rounded.Person
import androidx.compose.material.icons.rounded.Visibility
import androidx.compose.material.icons.rounded.VisibilityOff
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.ElevatedButton
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.unit.dp
import moe.tabidachi.meeting.R
import moe.tabidachi.meeting.ui.common.keyboardAsState
import moe.tabidachi.meeting.ui.preview.PreviewTheme
import moe.tabidachi.meeting.ui.preview.Previews

@Composable
fun AuthScreen(
    state: AuthContract.State,
    actions: AuthContract.Actions
) {
    val backgroundColors = if (isSystemInDarkTheme()) {
        listOf(
            Color(0xff01174e),
            Color(0xff00392c),
        )
    } else {
        listOf(
            Color(0xffd6e0ff),
            Color(0xffc3fffa),
        )
    }
    val isKeyboardOpen by keyboardAsState()
    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = Modifier
            .background(
                brush = Brush.linearGradient(
                    colors = backgroundColors
                )
            )
            .fillMaxSize()
    ) {
        AnimatedVisibility(!isKeyboardOpen) {
            TopContent(state = state)
        }
        Column(
            modifier = Modifier
                .padding(bottom = 24.dp)
                .navigationBarsPadding()
                .then(if (!isKeyboardOpen) Modifier else Modifier.systemBarsPadding())
                .imePadding()
                .padding(horizontal = 24.dp)
                .padding(top = 32.dp)
                .clip(RoundedCornerShape(24.dp))
                .background(color = MaterialTheme.colorScheme.surface)
                .border(
                    width = 1.dp,
                    color = MaterialTheme.colorScheme.surfaceVariant,
                    shape = RoundedCornerShape(24.dp)
                )
                .fillMaxWidth()
                .padding(24.dp)
                .verticalScroll(state = rememberScrollState())
        ) {
            Row(
                modifier = Modifier
                    .clip(RoundedCornerShape(12.dp))
                    .background(
                        color = MaterialTheme.colorScheme.surfaceContainer
                    )
                    .padding(horizontal = 4.dp)
                    .fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(4.dp)
            ) {
                AuthType.entries.forEach { authMethod ->
                    val isActive = authMethod == state.authType
                    val colors =
                        if (isActive) ButtonDefaults.elevatedButtonColors() else ButtonDefaults.elevatedButtonColors(
                            containerColor = MaterialTheme.colorScheme.surfaceContainer,
                            contentColor = MaterialTheme.colorScheme.secondary
                        )
                    val elevation =
                        if (isActive) ButtonDefaults.elevatedButtonElevation() else ButtonDefaults.elevatedButtonElevation(
                            defaultElevation = 0.dp,
                            focusedElevation = 1.dp,
                        )
                    ElevatedButton(
                        onClick = { actions.onAuthTypeChange(authMethod) },
                        shape = RoundedCornerShape(8.dp),
                        colors = colors,
                        elevation = elevation,
                        modifier = Modifier.weight(1f)
                    ) {
                        Text(text = stringResource(authMethod.text))
                    }
                }
            }
            Spacer(modifier = Modifier.height(16.dp))
            when (state.authType) {
                AuthType.Login -> Login(
                    state = state,
                    actions = actions
                )

                AuthType.SignUp -> SignUp(
                    state = state,
                    actions = actions
                )
            }
        }
    }
}

fun errorMessage(@StringRes text: Int): @Composable () -> Unit {
    return {
        Text(text = stringResource(text), color = MaterialTheme.colorScheme.error)
    }
}

@Composable
private fun ColumnScope.Login(
    state: AuthContract.State,
    actions: AuthContract.Actions
) {
    Text(text = stringResource(R.string.auth_screen_account))
    Spacer(modifier = Modifier.height(8.dp))
    OutlinedTextField(
        value = state.account,
        onValueChange = actions.onAccountChange,
        shape = RoundedCornerShape(12.dp),
        leadingIcon = {
            Icon(
                imageVector = Icons.Rounded.AccountCircle,
                contentDescription = Icons.Rounded.AccountCircle.name
            )
        },
        singleLine = true,
        isError = state.accountErrorMessage != null,
        supportingText = when {
            state.accountErrorMessage != null -> errorMessage(state.accountErrorMessage)

            else -> null
        },
        modifier = Modifier.fillMaxWidth()
    )
    Spacer(modifier = Modifier.height(16.dp))
    Text(text = stringResource(R.string.auth_screen_password))
    Spacer(modifier = Modifier.height(8.dp))
    OutlinedTextField(
        value = state.password,
        onValueChange = actions.onPasswordChange,
        shape = RoundedCornerShape(12.dp),
        leadingIcon = {
            Icon(
                imageVector = Icons.Rounded.Lock,
                contentDescription = Icons.Rounded.Lock.name
            )
        },
        visualTransformation = if (state.isPasswordVisible) VisualTransformation.None else PasswordVisualTransformation(),
        singleLine = true,
        isError = state.passwordErrorMessage != null,
        supportingText = when {
            state.passwordErrorMessage != null -> errorMessage(state.passwordErrorMessage)

            else -> null
        },
        trailingIcon = {
            IconButton(
                onClick = actions.onPasswordVisibleToggle
            ) {
                if (state.isPasswordVisible) {
                    Icon(
                        imageVector = Icons.Rounded.Visibility,
                        contentDescription = Icons.Rounded.Visibility.name
                    )
                } else {
                    Icon(
                        imageVector = Icons.Rounded.VisibilityOff,
                        contentDescription = Icons.Rounded.VisibilityOff.name
                    )
                }
            }

        },
        modifier = Modifier.fillMaxWidth()
    )
    Spacer(modifier = Modifier.height(8.dp))
    Text(
        text = stringResource(R.string.auth_screen_forgot_password),
        style = MaterialTheme.typography.labelLarge,
        color = MaterialTheme.colorScheme.primary,
        modifier = Modifier
            .clip(RoundedCornerShape(4.dp))
            .clickable(onClick = actions.onForgotPassword)
            .padding(2.dp)
            .align(Alignment.End)
    )
    Spacer(modifier = Modifier.height(16.dp))
    Button(
        onClick = actions.onLogin, shape = RoundedCornerShape(12.dp),
        modifier = Modifier
            .height(52.dp)
            .fillMaxWidth()
    ) {
        Text(stringResource(R.string.auth_screen_login))
    }
}

@Composable
private fun TopContent(state: AuthContract.State) = Column(
    horizontalAlignment = Alignment.CenterHorizontally
) {
    Spacer(
        modifier = Modifier
            .statusBarsPadding()
            .height(48.dp)
    )
    Text(
        text = stringResource(R.string.app_name),
        style = MaterialTheme.typography.headlineLarge
    )
    Spacer(modifier = Modifier.height(8.dp))
    Text(
        text = stringResource(state.authType.subtitle),
        style = MaterialTheme.typography.titleSmall
    )
}

@Composable
private fun ColumnScope.SignUp(
    state: AuthContract.State,
    actions: AuthContract.Actions
) {
    Text(text = stringResource(R.string.auth_screen_username))
    Spacer(modifier = Modifier.height(8.dp))
    OutlinedTextField(
        value = state.username,
        onValueChange = actions.onUsernameChange,
        shape = RoundedCornerShape(12.dp),
        leadingIcon = {
            Icon(
                imageVector = Icons.Rounded.Person,
                contentDescription = Icons.Rounded.Person.name
            )
        },
        singleLine = true,
        isError = state.usernameErrorMessage != null,
        supportingText = when {
            state.usernameErrorMessage != null -> errorMessage(state.usernameErrorMessage)

            else -> null
        },
        modifier = Modifier.fillMaxWidth()
    )
    Spacer(modifier = Modifier.height(16.dp))
    Text(text = stringResource(R.string.auth_screen_email))
    Spacer(modifier = Modifier.height(8.dp))
    OutlinedTextField(
        value = state.email,
        onValueChange = actions.onEmailChange,
        shape = RoundedCornerShape(12.dp),
        leadingIcon = {
            Icon(
                imageVector = Icons.Rounded.Email,
                contentDescription = Icons.Rounded.Email.name
            )
        },
        singleLine = true,
        isError = state.emailErrorMessage != null,
        supportingText = when {
            state.emailErrorMessage != null -> errorMessage(state.emailErrorMessage)

            else -> null
        },
        modifier = Modifier.fillMaxWidth()
    )
    Spacer(modifier = Modifier.height(16.dp))
    Text(text = stringResource(R.string.auth_screen_password))
    Spacer(modifier = Modifier.height(8.dp))
    OutlinedTextField(
        value = state.password,
        onValueChange = actions.onPasswordChange,
        shape = RoundedCornerShape(12.dp),
        leadingIcon = {
            Icon(
                imageVector = Icons.Rounded.Lock,
                contentDescription = Icons.Rounded.Lock.name
            )
        },
        visualTransformation = if (state.isPasswordVisible) VisualTransformation.None else PasswordVisualTransformation(),
        singleLine = true,
        isError = state.passwordErrorMessage != null,
        supportingText = when {
            state.passwordErrorMessage != null -> errorMessage(state.passwordErrorMessage)

            else -> null
        },
        trailingIcon = {
            IconButton(
                onClick = actions.onPasswordVisibleToggle
            ) {
                if (state.isPasswordVisible) {
                    Icon(
                        imageVector = Icons.Rounded.Visibility,
                        contentDescription = Icons.Rounded.Visibility.name
                    )
                } else {
                    Icon(
                        imageVector = Icons.Rounded.VisibilityOff,
                        contentDescription = Icons.Rounded.VisibilityOff.name
                    )
                }
            }

        },
        modifier = Modifier.fillMaxWidth()
    )
    Spacer(modifier = Modifier.height(32.dp))
    Button(
        onClick = actions.onSignUp, shape = RoundedCornerShape(12.dp),
        modifier = Modifier
            .height(52.dp)
            .fillMaxWidth()
    ) {
        Text(text = stringResource(R.string.auth_screen_sign_up))
    }
}

@Composable
@Previews
private fun AuthScreenPreview() = PreviewTheme {
    AuthScreen(
        state = AuthContract.State(authType = AuthType.Login),
        actions = AuthContract.Actions()
    )
}