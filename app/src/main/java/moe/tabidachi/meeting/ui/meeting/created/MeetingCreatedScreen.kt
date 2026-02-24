package moe.tabidachi.meeting.ui.meeting.created

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.rounded.ArrowBack
import androidx.compose.material.icons.outlined.AccessTime
import androidx.compose.material.icons.outlined.CalendarToday
import androidx.compose.material.icons.outlined.ContentCopy
import androidx.compose.material.icons.outlined.Lightbulb
import androidx.compose.material.icons.outlined.Share
import androidx.compose.material.icons.rounded.Link
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.ElevatedButton
import androidx.compose.material3.FilledTonalButton
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.LocalContentColor
import androidx.compose.material3.LocalTextStyle
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import moe.tabidachi.meeting.R
import moe.tabidachi.meeting.ui.common.AppBar
import moe.tabidachi.meeting.ui.common.BottomButtons
import moe.tabidachi.meeting.ui.common.ProvideContentColorTextStyle
import moe.tabidachi.meeting.ui.preview.PreviewTheme
import moe.tabidachi.meeting.ui.preview.Previews

@Composable
fun MeetingCreatedScreen(
    state: MeetingCreatedContract.State,
    actions: MeetingCreatedContract.Actions,
) {
    Scaffold(
        topBar = {
            AppBar(
                title = {
                    Text(text = stringResource(R.string.meeting_created_screen_title))
                },
                subtitle = {
                    Text(text = stringResource(R.string.meeting_created_screen_subtitle))
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
        },
        bottomBar = {
            BottomButtons(
                positiveContent = {
                    Text(text = stringResource(R.string.meeting_created_screen_confirm_button))
                },
                onPositiveClick = actions.onNavigateUp,
                modifier = Modifier
                    .background(color = MaterialTheme.colorScheme.surface.copy(alpha = 0.96f))
                    .padding(16.dp)
                    .navigationBarsPadding()
            )
        }
    ) {
        Column(
            verticalArrangement = Arrangement.spacedBy(16.dp),
            modifier = Modifier
                .padding(top = it.calculateTopPadding())
                .padding(horizontal = 16.dp)
                .verticalScroll(state = rememberScrollState())
        ) {
            Spacer(modifier = Modifier)
            MeetingInfoCard(
                state = state,
            )
            MeetingLinkCard(
                state = state
            )
            ElevatedButton(
                onClick = {

                },
                shape = RoundedCornerShape(12.dp),
                modifier = Modifier.fillMaxWidth()
            ) {
                Icon(
                    imageVector = Icons.Outlined.CalendarToday,
                    contentDescription = Icons.Outlined.Share.name,
                    modifier = Modifier.size(20.dp)
                )
                Spacer(modifier = Modifier.width(8.dp))
                Text(text = stringResource(R.string.meeting_created_screen_add_to_calendar))
            }
            MeetingTipsCard()
            Spacer(modifier = Modifier.padding(bottom = it.calculateBottomPadding()))
        }
    }
}

@Composable
private fun MeetingTipsCard(modifier: Modifier = Modifier) = Column(
    verticalArrangement = Arrangement.spacedBy(8.dp),
    modifier = modifier
        .border(
            width = 1.dp,
            color = MaterialTheme.colorScheme.secondary.copy(alpha = 0.5f),
            shape = RoundedCornerShape(16.dp)
        )
        .clip(RoundedCornerShape(16.dp))
        .background(color = MaterialTheme.colorScheme.secondaryContainer)
        .padding(16.dp)
) {
    Row(
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        Icon(
            imageVector = Icons.Outlined.Lightbulb,
            contentDescription = Icons.Outlined.Lightbulb.name,
            tint = MaterialTheme.colorScheme.onSecondaryContainer
        )
        Text(text = stringResource(R.string.meeting_created_screen_tips_title))
    }
    Text(
        text = stringResource(R.string.meeting_created_screen_tips_content),
        style = MaterialTheme.typography.bodyMedium
    )
}

@Composable
private fun MeetingLinkCard(
    state: MeetingCreatedContract.State,
    modifier: Modifier = Modifier
) = Column(
    verticalArrangement = Arrangement.spacedBy(8.dp),
    modifier = modifier
        .border(
            1.dp,
            color = MaterialTheme.colorScheme.primary.copy(alpha = 0.5f),
            shape = RoundedCornerShape(16.dp)
        )
        .clip(RoundedCornerShape(16.dp))
        .background(color = MaterialTheme.colorScheme.primaryContainer)
        .padding(16.dp)
) {
    Row(
        horizontalArrangement = Arrangement.spacedBy(8.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Icon(
            imageVector = Icons.Rounded.Link,
            contentDescription = Icons.Rounded.Link.name,
            tint = MaterialTheme.colorScheme.onPrimaryContainer
        )
        Text(text = stringResource(R.string.meeting_created_screen_meeting_link_label))
    }
    BasicTextField(
        value = state.meetingLink,
        onValueChange = {},
        readOnly = true,
        textStyle = LocalTextStyle.current.copy(color = MaterialTheme.colorScheme.onSurface),
        singleLine = true,
        decorationBox = { innerTextField ->
            Box(
                contentAlignment = Alignment.CenterStart,
                modifier = Modifier
                    .clip(RoundedCornerShape(16.dp))
                    .background(color = MaterialTheme.colorScheme.surfaceContainerLow)
                    .padding(16.dp)
            ) {
                innerTextField.invoke()
            }
        }
    )
    Row(
        horizontalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        FilledTonalButton(
            onClick = {

            },
            shape = RoundedCornerShape(12.dp),
            colors = ButtonDefaults.filledTonalButtonColors(
                containerColor = MaterialTheme.colorScheme.surfaceContainerLow,
                contentColor = MaterialTheme.colorScheme.onSurface
            ),
            modifier = Modifier.weight(1f)
        ) {
            Icon(
                imageVector = Icons.Outlined.ContentCopy,
                contentDescription = Icons.Outlined.ContentCopy.name,
                modifier = Modifier.size(20.dp)
            )
            Spacer(modifier = Modifier.width(8.dp))
            Text(text = stringResource(R.string.meeting_created_screen_copy_link_button))
        }
        FilledTonalButton(
            onClick = {

            },
            shape = RoundedCornerShape(12.dp),
            colors = ButtonDefaults.filledTonalButtonColors(
                containerColor = MaterialTheme.colorScheme.surfaceContainerLow,
                contentColor = MaterialTheme.colorScheme.onSurface
            ),
            modifier = Modifier.weight(1f)
        ) {
            Icon(
                imageVector = Icons.Outlined.Share,
                contentDescription = Icons.Outlined.Share.name,
                modifier = Modifier.size(20.dp)
            )
            Spacer(modifier = Modifier.width(8.dp))
            Text(text = stringResource(R.string.meeting_created_screen_share_button))
        }
    }
}

@Composable
private fun MeetingInfoCard(
    state: MeetingCreatedContract.State,
    modifier: Modifier = Modifier
) = Column(
    verticalArrangement = Arrangement.spacedBy(16.dp),
    modifier = modifier
        .shadow(1.dp, RoundedCornerShape(16.dp))
        .clip(RoundedCornerShape(16.dp))
        .background(color = MaterialTheme.colorScheme.surfaceContainerLow)
        .padding(16.dp)
) {
    Text(text = state.meeting.name)
    HorizontalDivider()
    DateTimeListItem(
        overlineContent = {
            Text(text = stringResource(R.string.meeting_created_screen_datetime_label))
        },
        headlineContent = {
            Text(text = state.meeting.time.toString())
        },
        leadingContent = {
            Icon(
                imageVector = Icons.Outlined.CalendarToday,
                contentDescription = Icons.Outlined.CalendarToday.name,
            )
        }
    )
    HorizontalDivider()
    DateTimeListItem(
        overlineContent = {
            Text(text = stringResource(R.string.meeting_created_screen_duration_label))
        },
        headlineContent = {
            Text(text = state.meeting.duration.toString())
        },
        leadingContent = {
            Icon(
                imageVector = Icons.Outlined.AccessTime,
                contentDescription = Icons.Outlined.AccessTime.name,
            )
        }
    )
}

@Composable
private fun DateTimeListItem(
    overlineContent: @Composable () -> Unit,
    headlineContent: @Composable () -> Unit,
    leadingContent: @Composable () -> Unit,
    modifier: Modifier = Modifier
) = Row(
    horizontalArrangement = Arrangement.spacedBy(16.dp),
    modifier = modifier
) {
    CompositionLocalProvider(
        LocalContentColor provides MaterialTheme.colorScheme.primary,
        content = leadingContent
    )
    Column(
        verticalArrangement = Arrangement.spacedBy(8.dp),
        modifier = Modifier.weight(1f)
    ) {
        ProvideContentColorTextStyle(
            contentColor = MaterialTheme.colorScheme.onSurfaceVariant,
            textStyle = MaterialTheme.typography.bodyMedium
        ) {
            overlineContent()
        }
        ProvideContentColorTextStyle(
            contentColor = MaterialTheme.colorScheme.onSurface,
            textStyle = MaterialTheme.typography.bodyLarge
        ) {
            headlineContent()
        }
    }
}

@Composable
@Previews
fun MeetingCreatedScreenPreview() = PreviewTheme {
    MeetingCreatedScreen(
        state = MeetingCreatedContract.State.Preview,
        actions = MeetingCreatedContract.Actions()
    )
}