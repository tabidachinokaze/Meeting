package moe.tabidachi.meeting.ui.meeting.create

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.RowScope
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.imePadding
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.rounded.ArrowBack
import androidx.compose.material.icons.automirrored.rounded.KeyboardArrowRight
import androidx.compose.material.icons.filled.PeopleOutline
import androidx.compose.material.icons.outlined.CalendarToday
import androidx.compose.material.icons.outlined.Link
import androidx.compose.material.icons.outlined.Videocam
import androidx.compose.material.icons.rounded.Clear
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Switch
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.drawWithCache
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.geometry.CornerRadius
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.PathEffect
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import moe.tabidachi.meeting.R
import moe.tabidachi.meeting.ui.common.AppBar
import moe.tabidachi.meeting.ui.common.Avatar
import moe.tabidachi.meeting.ui.common.BottomButtons
import moe.tabidachi.meeting.ui.common.ProvideContentColorTextStyle
import moe.tabidachi.meeting.ui.preview.PreviewTheme
import moe.tabidachi.meeting.ui.preview.Previews

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CreateMeetingScreen(
    state: CreateMeetingContract.State,
    actions: CreateMeetingContract.Actions,
) {
    Scaffold(
        topBar = {
            AppBar(
                title = {
                    Text(text = stringResource(R.string.create_meeting_screen_title))
                },
                subtitle = {
                    Text(text = stringResource(R.string.create_meeting_screen_subtitle))
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
        }, bottomBar = {
            BottomButtons(
                negativeContent = {
                    Text(text = stringResource(R.string.create_meeting_screen_cancel_button))
                },
                positiveContent = {
                    Text(text = stringResource(R.string.create_meeting_screen_schedule_button))
                },
                onNegativeClick = actions.onNavigateUp,
                onPositiveClick = actions.onScheduleMeeting,
                modifier = Modifier
                    .background(color = MaterialTheme.colorScheme.surface.copy(alpha = 0.96f))
                    .padding(16.dp)
                    .navigationBarsPadding()
            )
        }, modifier = Modifier.imePadding()
    ) {
        LazyColumn(
            verticalArrangement = Arrangement.spacedBy(16.dp),
            modifier = Modifier
                .padding(top = it.calculateTopPadding())
                .padding(horizontal = 16.dp)
                .fillMaxSize()
        ) {
            item {
                Spacer(modifier = Modifier)
            }
            item {
                LabelTextField(
                    value = "",
                    onValueChange = {},
                    label = {
                        Text(text = stringResource(R.string.create_meeting_screen_topic_label))
                    }, placeholder = {
                        Text(text = stringResource(R.string.create_meeting_screen_topic_placeholder))
                    }
                )
            }
            item {
                LabelTextField(
                    value = "",
                    onValueChange = {},
                    label = {
                        Text(text = stringResource(R.string.create_meeting_screen_description_label))
                    }, placeholder = {
                        Text(text = stringResource(R.string.create_meeting_screen_description_placeholder))
                    }
                )
            }
            item {
                DateTime()
            }
            item {
                Participants(
                    state = state,
                    actions = actions,
                )
            }
            item {
                MeetingSettings()
            }
            item {
                CreateMeetingTips()
            }
            item {
                Spacer(modifier = Modifier.padding(bottom = it.calculateBottomPadding()))
            }
        }
    }
}

@Composable
private fun CreateMeetingTips(
    modifier: Modifier = Modifier
) = Row(
    horizontalArrangement = Arrangement.spacedBy(16.dp),
    modifier = modifier
        .clip(RoundedCornerShape(16.dp))
        .border(
            1.dp,
            color = MaterialTheme.colorScheme.secondary,
            shape = RoundedCornerShape(16.dp)
        )
        .background(
            color = MaterialTheme.colorScheme.secondaryContainer,
            shape = RoundedCornerShape(16.dp)
        )
        .padding(16.dp)
        .fillMaxWidth()
) {
    Icon(
        imageVector = Icons.Outlined.Link,
        contentDescription = Icons.Outlined.Link.name,
        tint = MaterialTheme.colorScheme.onSecondaryContainer,
    )
    Column(
        verticalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        ProvideContentColorTextStyle(
            contentColor = MaterialTheme.colorScheme.onSurface,
            textStyle = MaterialTheme.typography.bodyMedium
        ) {
            Text(text = stringResource(R.string.create_meeting_screen_tips_title))
        }
        ProvideContentColorTextStyle(
            contentColor = MaterialTheme.colorScheme.onSurfaceVariant,
            textStyle = MaterialTheme.typography.bodySmall
        ) {
            Text(text = stringResource(R.string.create_meeting_screen_tips_description))
        }
    }
}

@Composable
fun MeetingSettings(modifier: Modifier = Modifier) = Column(
    verticalArrangement = Arrangement.spacedBy(16.dp),
    modifier = modifier
) {
    Row(
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        Icon(
            imageVector = Icons.Outlined.Videocam,
            contentDescription = Icons.Outlined.Videocam.name,
            tint = MaterialTheme.colorScheme.primary,
            modifier = Modifier.size(20.dp)
        )
        Text(text = stringResource(R.string.create_meeting_screen_meeting_settings_label))
    }
    Column(
        modifier = Modifier
            .shadow(1.dp, RoundedCornerShape(16.dp))
            .clip(RoundedCornerShape(16.dp))
            .background(color = MaterialTheme.colorScheme.surfaceContainerLow)
    ) {
        MeetingSettingsListItem(
            headlineContent = {
                Text(text = stringResource(R.string.create_meeting_screen_auto_record))
            },
            supportingContent = {
                Text(text = stringResource(R.string.create_meeting_screen_auto_record_subtitle))
            },
            checked = false,
            onCheckedChange = {},
        )
        HorizontalDivider(thickness = 0.5.dp)
        MeetingSettingsListItem(
            headlineContent = {
                Text(text = stringResource(R.string.create_meeting_screen_ai_transcription))
            },
            supportingContent = {
                Text(text = stringResource(R.string.create_meeting_screen_ai_transcription_subtitle))
            },
            checked = false,
            onCheckedChange = {},
        )
        HorizontalDivider(thickness = 0.5.dp)
        MeetingSettingsListItem(
            headlineContent = {
                Text(text = stringResource(R.string.create_meeting_screen_reminder))
            },
            supportingContent = {
                Text(text = stringResource(R.string.create_meeting_screen_reminder_subtitle))
            },
            checked = false,
            onCheckedChange = {},
        )
    }
}

@Composable
fun MeetingSettingsListItem(
    headlineContent: @Composable () -> Unit,
    supportingContent: @Composable () -> Unit,
    checked: Boolean,
    onCheckedChange: (Boolean) -> Unit,
    modifier: Modifier = Modifier,
) = Row(
    verticalAlignment = Alignment.CenterVertically,
    horizontalArrangement = Arrangement.spacedBy(16.dp),
    modifier = modifier
        .padding(vertical = 16.dp, horizontal = 16.dp)
) {
    Column(
        verticalArrangement = Arrangement.spacedBy(4.dp),
        modifier = Modifier.weight(1f)
    ) {
        ProvideContentColorTextStyle(
            contentColor = MaterialTheme.colorScheme.onSurface,
            textStyle = MaterialTheme.typography.bodyLarge
        ) {
            headlineContent()
        }
        ProvideContentColorTextStyle(
            contentColor = MaterialTheme.colorScheme.onSurfaceVariant,
            textStyle = MaterialTheme.typography.bodyMedium
        ) {
            supportingContent()
        }
    }
    Switch(
        checked = checked,
        onCheckedChange = onCheckedChange
    )
}

@Composable
fun Participants(
    state: CreateMeetingContract.State,
    actions: CreateMeetingContract.Actions,
    modifier: Modifier = Modifier
) = Column(
    verticalArrangement = Arrangement.spacedBy(16.dp),
    modifier = modifier
) {
    Row(
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        Icon(
            imageVector = Icons.Default.PeopleOutline,
            contentDescription = Icons.Default.PeopleOutline.name,
            tint = MaterialTheme.colorScheme.primary,
            modifier = Modifier.size(20.dp)
        )
        Text(text = stringResource(R.string.create_meeting_screen_participants_label))
    }
    Column(
        verticalArrangement = Arrangement.spacedBy(8.dp),
    ) {
        state.participants.forEach {
            ParticipantsListItem(
                headlineContent = {
                    Text(text = it.username)
                },
                supportingContent = it.email?.let {
                    {
                        Text(text = it)
                    }
                },
                leadingContent = {
                    Avatar(
                        name = it.username,
                        model = null,
                        modifier = Modifier.size(40.dp)
                    )
                }
            )
        }
        ParticipantsAddItem(
            onClick = actions.onNavigateToSelectParticipants
        ) {
            Icon(
                imageVector = Icons.Default.PeopleOutline,
                contentDescription = Icons.Default.PeopleOutline.name,
                modifier = Modifier.size(20.dp)
            )
            Text(
                text = when (state.participants.isEmpty()) {
                    true -> stringResource(R.string.create_meeting_screen_participants_empty)
                    else -> stringResource(R.string.create_meeting_screen_participants_add_more)
                }
            )
        }
    }
}

@Composable
fun ParticipantsListItem(
    headlineContent: @Composable () -> Unit,
    supportingContent: @Composable (() -> Unit)? = null,
    leadingContent: @Composable () -> Unit,
) = Row(
    verticalAlignment = Alignment.CenterVertically,
    horizontalArrangement = Arrangement.spacedBy(16.dp),
    modifier = Modifier
        .shadow(1.dp, RoundedCornerShape(16.dp))
        .clip(RoundedCornerShape(16.dp))
        .background(color = MaterialTheme.colorScheme.surfaceContainerLow)
        .padding(vertical = 8.dp, horizontal = 16.dp)
) {
    leadingContent.invoke()
    Column(
        modifier = Modifier.weight(1f)
    ) {
        ProvideContentColorTextStyle(
            contentColor = MaterialTheme.colorScheme.onSurface,
            textStyle = MaterialTheme.typography.bodyLarge
        ) {
            headlineContent()
        }
        supportingContent?.let {
            ProvideContentColorTextStyle(
                contentColor = MaterialTheme.colorScheme.onSurfaceVariant,
                textStyle = MaterialTheme.typography.bodyMedium
            ) {
                it()
            }
        }
    }
    Icon(
        imageVector = Icons.Rounded.Clear,
        contentDescription = Icons.Rounded.Clear.name,
        tint = MaterialTheme.colorScheme.onSurfaceVariant
    )
}

@Composable
fun ParticipantsAddItem(
    onClick: () -> Unit,
    dashColor: Color = MaterialTheme.colorScheme.onSurfaceVariant,
    content: @Composable RowScope.() -> Unit,
) = Box(
    contentAlignment = Alignment.Center,
    modifier = Modifier
        .fillMaxWidth()
        .heightIn(min = 56.dp)
        .drawWithCache {
            val strokeWidth = 1.dp.toPx()
            val dashPathEffect = PathEffect.dashPathEffect(
                intervals = floatArrayOf(10f, 5f),
                phase = 0f
            )
            onDrawWithContent {
                drawContent()
                drawRoundRect(
                    color = dashColor,
                    style = Stroke(
                        width = strokeWidth,
                        pathEffect = dashPathEffect
                    ),
                    cornerRadius = CornerRadius(16.dp.toPx())
                )
            }
        }
        .clip(RoundedCornerShape(16.dp))
        .background(color = MaterialTheme.colorScheme.surfaceContainerLow)
        .clickable(onClick = onClick)
        .padding(vertical = 8.dp, horizontal = 16.dp)

) {
    Row(
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(16.dp),
        content = content
    )
}


@Composable
fun DateTime(
    modifier: Modifier = Modifier,
) = Column(
    verticalArrangement = Arrangement.spacedBy(16.dp),
    modifier = modifier
) {
    Row(
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        Icon(
            imageVector = Icons.Outlined.CalendarToday,
            contentDescription = Icons.Outlined.CalendarToday.name,
            tint = MaterialTheme.colorScheme.primary,
            modifier = Modifier.size(20.dp)
        )
        Text(text = stringResource(R.string.create_meeting_screen_date_time_label))
    }
    DateTimeListItem(
        overlineContent = {
            Text(text = stringResource(R.string.create_meeting_screen_start_time_label))
        },
        headlineContent = {
            Text(text = stringResource(R.string.create_meeting_screen_start_time_placeholder))
        }
    )
    DateTimeListItem(
        overlineContent = {
            Text(text = stringResource(R.string.create_meeting_screen_duration_label))
        },
        headlineContent = {
            Text(text = stringResource(R.string.create_meeting_screen_duration_placeholder))
        }
    )
}

@Composable
fun DateTimeListItem(
    overlineContent: @Composable () -> Unit,
    headlineContent: @Composable () -> Unit,
) = Row(
    verticalAlignment = Alignment.CenterVertically,
    modifier = Modifier
        .shadow(1.dp, RoundedCornerShape(16.dp))
        .clip(RoundedCornerShape(16.dp))
        .background(color = MaterialTheme.colorScheme.surfaceContainerLow)
        .padding(16.dp)
) {
    Column(
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
    Icon(
        imageVector = Icons.AutoMirrored.Rounded.KeyboardArrowRight,
        contentDescription = Icons.AutoMirrored.Rounded.KeyboardArrowRight.name,
        tint = MaterialTheme.colorScheme.onSurfaceVariant
    )
}

@Composable
fun LabelTextField(
    value: String,
    onValueChange: (String) -> Unit,
    label: @Composable () -> Unit,
    modifier: Modifier = Modifier,
    placeholder: @Composable (() -> Unit)? = null,
    shape: Shape = RoundedCornerShape(16.dp),
) = Column(
    verticalArrangement = Arrangement.spacedBy(8.dp),
    modifier = modifier,
) {
    label.invoke()
    OutlinedTextField(
        value = value,
        onValueChange = onValueChange,
        placeholder = placeholder,
        shape = shape,
        modifier = Modifier.fillMaxWidth()
    )
}

@Composable
@Previews
private fun CreateMeetingScreenPreview() = PreviewTheme {
    CreateMeetingScreen(
        state = CreateMeetingContract.State.Preview,
        actions = CreateMeetingContract.Actions()
    )
}