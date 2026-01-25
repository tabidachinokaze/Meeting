package moe.tabidachi.meeting.ui.main.page

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.IntrinsicSize
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.PeopleOutline
import androidx.compose.material.icons.outlined.AccessTime
import androidx.compose.material.icons.outlined.TipsAndUpdates
import androidx.compose.material.icons.outlined.Videocam
import androidx.compose.material.icons.rounded.Add
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.VerticalDivider
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.unit.dp
import moe.tabidachi.meeting.ui.common.ProvideContentColorTextStyle
import moe.tabidachi.meeting.ui.common.autoTimeString
import moe.tabidachi.meeting.ui.common.monthDayWeekTimeString
import moe.tabidachi.meeting.ui.main.MainContract
import moe.tabidachi.meeting.ui.preview.PreviewTheme
import moe.tabidachi.meeting.ui.preview.Previews

@Composable
fun HomePage(
    state: MainContract.State,
    actions: MainContract.Actions,
    modifier: Modifier = Modifier
) = Column(
    verticalArrangement = Arrangement.spacedBy(16.dp),
    modifier = modifier
) {
    TopApp(
        state = state,
        actions = actions,
        modifier = Modifier.padding(16.dp)
    )
    MeetingsButtons(
        state = state,
        actions = actions,
        modifier = Modifier.padding(horizontal = 16.dp)
    )
    Row(
        modifier = Modifier.padding(horizontal = 16.dp)
    ) {
        Text(text = "Today's Meetings", modifier = Modifier.weight(1f))
        TextButton(
            onClick = {

            }
        ) {
            Text(text = "View All")
        }
    }
    state.meetings.forEach {
        MeetingCard(
            modifier = Modifier.padding(horizontal = 16.dp),
            title = it.name,
            time = autoTimeString(it.time),
            duration = it.duration.toString(),
            participants = it.participants.joinToString(", ") { it.username },
            onJoin = {

            }
        )
    }
}

@Composable
fun TextButton(
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
    containerColor: Color = MaterialTheme.colorScheme.primaryContainer.copy(0.2f),
    contentColor: Color = MaterialTheme.colorScheme.primary,
    shape: Shape = RoundedCornerShape(4.dp),
    contentPadding: PaddingValues = PaddingValues(horizontal = 8.dp, vertical = 4.dp),
    content: @Composable () -> Unit
) = Box(
    modifier = modifier
        .clip(shape)
        .clickable(onClick = onClick)
        .background(color = containerColor)
        .padding(contentPadding)
) {
    ProvideContentColorTextStyle(
        contentColor = contentColor,
        textStyle = MaterialTheme.typography.labelLarge,
        content = content
    )
}

@Composable
fun MeetingCard(
    modifier: Modifier = Modifier,
    title: String,
    time: String,
    duration: String,
    participants: String,
    onJoin: () -> Unit,
) = Column(
    verticalArrangement = Arrangement.spacedBy(8.dp),
    modifier = modifier
        .shadow(1.dp, shape = RoundedCornerShape(16.dp))
        .clip(RoundedCornerShape(16.dp))
        .background(color = MaterialTheme.colorScheme.surfaceContainerLow)
        .padding(16.dp)
        .fillMaxWidth()
) {
    Row(
        horizontalArrangement = Arrangement.spacedBy(8.dp),
        modifier = Modifier.height(IntrinsicSize.Min)
    ) {
        VerticalDivider(
            thickness = 4.dp,
            color = MaterialTheme.colorScheme.primary,
            modifier = Modifier
                .fillMaxHeight()
                .clip(CircleShape)
        )
        Column(
            verticalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            Text(text = title, style = MaterialTheme.typography.bodyLarge)
            ProvideContentColorTextStyle(
                contentColor = MaterialTheme.colorScheme.onSurfaceVariant,
                textStyle = MaterialTheme.typography.bodyMedium
            ) {
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    Icon(
                        imageVector = Icons.Outlined.AccessTime,
                        contentDescription = Icons.Outlined.AccessTime.name,
                        modifier = Modifier.size(20.dp)
                    )
                    Text(text = time)
                    Text(text = duration)
                }
            }
        }
    }
    Row(
        horizontalArrangement = Arrangement.spacedBy(8.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Spacer(modifier = Modifier.width(4.dp))
        ProvideContentColorTextStyle(
            contentColor = MaterialTheme.colorScheme.onSurfaceVariant,
            textStyle = MaterialTheme.typography.bodyMedium
        ) {
            Icon(
                imageVector = Icons.Default.PeopleOutline,
                contentDescription = Icons.Default.PeopleOutline.name,
                modifier = Modifier.size(20.dp)
            )
            Text(text = participants, modifier = Modifier.weight(1f))
        }
        Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(4.dp),
            modifier = Modifier
                .clip(RoundedCornerShape(12.dp))
                .clickable(onClick = onJoin)
                .background(color = MaterialTheme.colorScheme.primary)
                .padding(vertical = 6.dp, horizontal = 8.dp)
        ) {
            ProvideContentColorTextStyle(
                contentColor = MaterialTheme.colorScheme.onPrimary,
                textStyle = MaterialTheme.typography.labelLarge
            ) {
                Icon(
                    imageVector = Icons.Outlined.Videocam,
                    contentDescription = Icons.Outlined.Videocam.name,
                    modifier = Modifier.size(20.dp)
                )
                Text(text = "Join")
            }
        }
    }
}

@Composable
fun MeetingsButtons(
    state: MainContract.State,
    actions: MainContract.Actions,
    modifier: Modifier = Modifier
) = Row(
    horizontalArrangement = Arrangement.spacedBy(16.dp),
    modifier = modifier
) {
    Button(
        onClick = {

        },
        shape = RoundedCornerShape(16.dp),
        contentPadding = PaddingValues(),
        modifier = Modifier
            .weight(2f)
            .height(48.dp)
    ) {
        Icon(
            imageVector = Icons.Rounded.Add,
            contentDescription = Icons.Rounded.Add.name
        )
        Text(text = "Schedule Meeting")
    }

    Button(
        onClick = {

        },
        shape = RoundedCornerShape(16.dp),
        colors = ButtonDefaults.textButtonColors(
            containerColor = MaterialTheme.colorScheme.secondaryContainer
        ),
        contentPadding = PaddingValues(),
        modifier = Modifier
            .weight(1f)
            .height(48.dp)
    ) {
        Icon(
            imageVector = Icons.Outlined.AccessTime,
            contentDescription = Icons.Outlined.AccessTime.name
        )
        Text(text = "Join Now")
    }
}

@Composable
fun TopApp(
    state: MainContract.State,
    actions: MainContract.Actions,
    modifier: Modifier = Modifier
) = Column(
    verticalArrangement = Arrangement.spacedBy(16.dp),
    modifier = Modifier
        .background(color = MaterialTheme.colorScheme.surfaceContainer)
        .statusBarsPadding()
        .then(modifier)
        .fillMaxWidth()
) {
    Column {
        ProvideContentColorTextStyle(
            contentColor = MaterialTheme.colorScheme.onSurfaceVariant,
            textStyle = MaterialTheme.typography.titleSmall
        ) {
            Text(text = monthDayWeekTimeString())
        }

        ProvideContentColorTextStyle(
            contentColor = MaterialTheme.colorScheme.onSurface,
            textStyle = MaterialTheme.typography.titleMedium
        ) {
            Text(text = state.greeting)
        }
    }
    state.tips?.let { tips ->
        HomeTips(tips = tips)
    }
}

private val Space = 16.dp

private val CornerShape = RoundedCornerShape(Space)

@Composable
private fun HomeTips(
    tips: String,
    modifier: Modifier = Modifier
) = Row(
    horizontalArrangement = Arrangement.spacedBy(Space),
    modifier = modifier
        .clip(CornerShape)
        .border(
            1.dp,
            color = MaterialTheme.colorScheme.primary,
            shape = CornerShape
        )
        .background(
            color = MaterialTheme.colorScheme.primaryContainer,
            shape = CornerShape
        )
        .padding(Space)
        .fillMaxWidth()
) {
    Box(
        contentAlignment = Alignment.Center,
        modifier = Modifier
            .clip(CircleShape)
            .background(color = MaterialTheme.colorScheme.primary)
            .size(40.dp)
    ) {
        Icon(
            imageVector = Icons.Outlined.TipsAndUpdates,
            contentDescription = Icons.Outlined.TipsAndUpdates.name,
            tint = MaterialTheme.colorScheme.onPrimary
        )
    }
    Column(
        verticalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        Text(text = tips)
        TextButton(
            onClick = {

            },
            containerColor = MaterialTheme.colorScheme.onPrimaryContainer.copy(0.1f),
            contentColor = MaterialTheme.colorScheme.onPrimaryContainer,
            contentPadding = PaddingValues(horizontal = 16.dp, vertical = 4.dp)
        ) {
            Text(text = "确定")
        }
    }
}

@Composable
@Previews
private fun HomePagePreview() = PreviewTheme {
    HomePage(
        state = MainContract.State.Preview,
        actions = MainContract.Actions()
    )
}