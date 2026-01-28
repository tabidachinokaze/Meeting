package moe.tabidachi.meeting.ui.participants.select

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.imePadding
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.rounded.ArrowBack
import androidx.compose.material.icons.rounded.CheckCircle
import androidx.compose.material.icons.rounded.Clear
import androidx.compose.material.icons.rounded.Search
import androidx.compose.material3.AssistChip
import androidx.compose.material3.AssistChipDefaults
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import moe.tabidachi.meeting.R
import moe.tabidachi.meeting.ui.common.AppBar
import moe.tabidachi.meeting.ui.common.Avatar
import moe.tabidachi.meeting.ui.common.BottomButtons
import moe.tabidachi.meeting.ui.common.ProvideContentColorTextStyle
import moe.tabidachi.meeting.ui.meeting.create.CreateMeetingContract
import moe.tabidachi.meeting.ui.preview.PreviewTheme
import moe.tabidachi.meeting.ui.preview.Previews

@Composable
fun SelectParticipantsScreen(
    state: CreateMeetingContract.State,
    actions: CreateMeetingContract.Actions,
    modifier: Modifier = Modifier
) {
    Scaffold(
        topBar = {
            Column(
                modifier = Modifier.pointerInput(Unit) {
                }
            ) {
                AppBar(
                    title = {
                        Text(text = stringResource(R.string.select_participants_screen_title))
                    },
                    modifier = Modifier,
                    subtitle = {
                        Text(
                            text = stringResource(
                                R.string.select_participants_screen_subtitle_format,
                                state.participants.size
                            )
                        )
                    },
                    navigationIcon = {
                        IconButton(
                            onClick = actions.onNavigateUp
                        ) {
                            Icon(
                                imageVector = Icons.AutoMirrored.Rounded.ArrowBack,
                                contentDescription = Icons.AutoMirrored.Rounded.ArrowBack.name
                            )
                        }
                    }
                )
                SearchTextField(
                    value = "",
                    onValueChange = {

                    },
                    placeholder = {
                        Text(text = stringResource(R.string.select_participants_screen_search_placeholder))
                    }
                )
                HorizontalDivider()
                FlowRow(
                    horizontalArrangement = Arrangement.spacedBy(8.dp),
                    verticalArrangement = Arrangement.spacedBy(0.dp),
                    modifier = Modifier
                        .background(color = MaterialTheme.colorScheme.surfaceContainer)
                        .padding(horizontal = 8.dp)
                        .fillMaxWidth()
                ) {
                    state.participants.forEach { participant ->
                        AssistChip(
                            onClick = {
                                actions.onParticipantAddOrRemove(participant)
                            },
                            label = { Text(text = participant.username) },
                            trailingIcon = {
                                Icon(
                                    imageVector = Icons.Rounded.Clear,
                                    contentDescription = Icons.Rounded.Clear.name,
                                    modifier = Modifier.size(16.dp)
                                )
                            }, shape = CircleShape,
                            colors = AssistChipDefaults.assistChipColors(
                                containerColor = MaterialTheme.colorScheme.secondaryContainer,
                                labelColor = MaterialTheme.colorScheme.primary
                            ),
                            border = AssistChipDefaults.assistChipBorder(
                                enabled = true,
                                borderColor = Color.Transparent
                            ),
                            modifier = Modifier.padding(0.dp)
                        )
                    }
                }
            }
        },
        bottomBar = {
            BottomButtons(
                negativeContent = {
                    Text(text = stringResource(R.string.select_participants_screen_cancel_button))
                },
                positiveContent = {
                    Text(text = stringResource(R.string.select_participants_screen_confirm_button))
                },
                onNegativeClick = actions.onNavigateUp,
                onPositiveClick = {

                },
                modifier = Modifier
                    .background(color = MaterialTheme.colorScheme.surface.copy(alpha = 0.96f))
                    .padding(16.dp)
                    .navigationBarsPadding()
            )

        }, modifier = modifier.imePadding()
    ) {
        LazyColumn(
            verticalArrangement = Arrangement.spacedBy(8.dp),
            contentPadding = PaddingValues(
                top = it.calculateTopPadding(),
                start = 8.dp,
                end = 8.dp
            ),
            modifier = Modifier.fillMaxSize()
        ) {
            item { Spacer(modifier = Modifier) }
            items(
                state.contacts,
                key = { it.uid },
            ) { contact ->
                val selected: Boolean = contact in state.participants
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(8.dp),
                    modifier = Modifier
                        .fillMaxWidth()
                        .clip(RoundedCornerShape(16.dp))
                        .then(
                            when (selected) {
                                true -> Modifier
                                    .border(
                                        width = 2.dp,
                                        color = MaterialTheme.colorScheme.primary,
                                        shape = RoundedCornerShape(16.dp)
                                    )
                                    .background(color = MaterialTheme.colorScheme.secondaryContainer)

                                false -> Modifier
                            }
                        )
                        .clickable(
                            onClick = {
                                actions.onParticipantAddOrRemove(contact)
                            }
                        )
                        .padding(8.dp)
                ) {
                    Avatar(
                        name = contact.username,
                        model = contact.avatar,
                        modifier = Modifier.size(40.dp)
                    )
                    Column(
                        modifier = Modifier.weight(1f)
                    ) {
                        ProvideContentColorTextStyle(
                            contentColor = MaterialTheme.colorScheme.onSurface,
                            textStyle = MaterialTheme.typography.bodyMedium
                        ) {
                            Text(text = contact.username)
                        }
                        contact.email?.let { email ->
                            ProvideContentColorTextStyle(
                                contentColor = MaterialTheme.colorScheme.onSurfaceVariant,
                                textStyle = MaterialTheme.typography.bodySmall
                            ) {
                                Text(text = email)
                            }
                        }
                    }
                    Box(
                        contentAlignment = Alignment.Center,
                        modifier = Modifier.size(48.dp)
                    ) {
                        if (selected) Icon(
                            imageVector = Icons.Rounded.CheckCircle,
                            contentDescription = Icons.Rounded.CheckCircle.name,
                            tint = MaterialTheme.colorScheme.primary
                        )
                    }
                }
            }
            item { Spacer(modifier = Modifier.padding(bottom = it.calculateBottomPadding())) }
        }
    }
}

@Composable
fun SearchTextField(
    value: String,
    onValueChange: (String) -> Unit,
    modifier: Modifier = Modifier,
    placeholder: @Composable (() -> Unit)? = null,
) = BasicTextField(
    value = value,
    onValueChange = onValueChange, decorationBox = { innerTextField ->
        ProvideContentColorTextStyle(
            contentColor = MaterialTheme.colorScheme.onSurfaceVariant,
            textStyle = MaterialTheme.typography.bodyMedium
        ) {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                Box(
                    contentAlignment = Alignment.Center,
                    modifier = Modifier.size(48.dp)
                ) {
                    Icon(
                        imageVector = Icons.Rounded.Search,
                        contentDescription = Icons.Rounded.Search.name
                    )
                }
                if (value.isEmpty()) {
                    placeholder?.invoke()
                } else {
                    innerTextField()
                }
            }
        }
    },
    modifier = modifier
        .background(color = MaterialTheme.colorScheme.surfaceContainer)
        .padding(horizontal = 8.dp)
        .fillMaxWidth()
)

@Composable
@Previews
private fun SelectParticipantsScreenPreview() = PreviewTheme {
    SelectParticipantsScreen(
        state = CreateMeetingContract.State.Preview,
        actions = CreateMeetingContract.Actions(),
    )
}