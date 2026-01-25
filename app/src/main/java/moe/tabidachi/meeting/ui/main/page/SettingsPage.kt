package moe.tabidachi.meeting.ui.main.page

import androidx.annotation.StringRes
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ColumnScope
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.outlined.HelpOutline
import androidx.compose.material.icons.automirrored.outlined.KeyboardArrowRight
import androidx.compose.material.icons.automirrored.outlined.Logout
import androidx.compose.material.icons.outlined.CalendarMonth
import androidx.compose.material.icons.outlined.DarkMode
import androidx.compose.material.icons.outlined.Email
import androidx.compose.material.icons.outlined.KeyboardVoice
import androidx.compose.material.icons.outlined.NotificationsNone
import androidx.compose.material.icons.outlined.Person
import androidx.compose.material.icons.outlined.PrivacyTip
import androidx.compose.material.icons.outlined.Translate
import androidx.compose.material.icons.outlined.Videocam
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.ElevatedButton
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.ListItem
import androidx.compose.material3.ListItemDefaults
import androidx.compose.material3.LocalContentColor
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Switch
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import coil3.compose.AsyncImagePainter
import coil3.compose.SubcomposeAsyncImage
import coil3.compose.SubcomposeAsyncImageContent
import moe.tabidachi.meeting.R
import moe.tabidachi.meeting.ui.common.MonogramAvatar
import moe.tabidachi.meeting.ui.common.ProvideContentColorTextStyle
import moe.tabidachi.meeting.ui.common.getLocale
import moe.tabidachi.meeting.ui.main.MainContract
import moe.tabidachi.meeting.ui.preview.PreviewTheme
import moe.tabidachi.meeting.ui.preview.Previews

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SettingsPage(
    state: MainContract.State,
    actions: MainContract.Actions,
    modifier: Modifier = Modifier
) = Column(
    verticalArrangement = Arrangement.spacedBy(16.dp),
    modifier = modifier.verticalScroll(rememberScrollState())
) {
    ProfileHeader(
        state = state,
        actions = actions,
        modifier = Modifier
            .background(color = MaterialTheme.colorScheme.surfaceContainer)
            .statusBarsPadding()
    )
    SettingsItems(
        state = state,
        actions = actions
    )
    ElevatedButton(
        onClick = actions.onLogout,
        shape = RoundedCornerShape(16.dp),
        elevation = ButtonDefaults.buttonElevation(),
        modifier = Modifier
            .fillMaxWidth()
            .height(48.dp)
            .padding(horizontal = 16.dp)
    ) {
        CompositionLocalProvider(
            LocalContentColor provides MaterialTheme.colorScheme.error.copy(0.75f)
        ) {
            Icon(
                imageVector = Icons.AutoMirrored.Outlined.Logout,
                contentDescription = Icons.AutoMirrored.Outlined.Logout.name,
            )
            Text(text = stringResource(R.string.settings_page_logout))
        }
    }
    Spacer(modifier = Modifier)
}

@Composable
private fun ProfileHeader(
    state: MainContract.State,
    actions: MainContract.Actions,
    modifier: Modifier = Modifier
) = Column(
    modifier = modifier
) {
    @Composable
    fun ListItem(
        headlineContent: @Composable () -> Unit,
        supportingContent: @Composable () -> Unit,
        modifier: Modifier = Modifier,
        verticalArrangement: Arrangement.Vertical = Arrangement.Top,
        horizontalAlignment: Alignment.Horizontal = Alignment.Start,
    ) = Column(
        modifier = modifier,
        verticalArrangement = verticalArrangement,
        horizontalAlignment = horizontalAlignment
    ) {
        ProvideContentColorTextStyle(
            contentColor = MaterialTheme.colorScheme.onSurface,
            textStyle = MaterialTheme.typography.bodyLarge,
            content = headlineContent
        )
        ProvideContentColorTextStyle(
            contentColor = MaterialTheme.colorScheme.onSurfaceVariant,
            textStyle = MaterialTheme.typography.bodyMedium,
            content = supportingContent
        )
    }

    Row(
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(16.dp),
        modifier = Modifier
            .fillMaxWidth()
            .padding(16.dp)
    ) {
        SubcomposeAsyncImage(
            model = state.userInfo?.avatar,
            contentDescription = "profile image",
            modifier = Modifier
                .clip(CircleShape)
                .size(56.dp)
        ) {
            when (painter.state.collectAsState().value) {
                is AsyncImagePainter.State.Success -> SubcomposeAsyncImageContent()
                else -> MonogramAvatar(
                    name = state.userInfo?.username.orEmpty(),
                    modifier = Modifier.fillMaxSize()
                )
            }
        }
        ListItem(
            headlineContent = {
                Text(
                    text = state.userInfo?.username.orEmpty(),
                    modifier = Modifier.padding(vertical = 4.dp)
                )
            },
            supportingContent = {
                Text(text = state.userInfo?.email.orEmpty())
            }
        )
    }
    HorizontalDivider(modifier = Modifier.padding(horizontal = 16.dp))
    Row(
        horizontalArrangement = Arrangement.SpaceAround,
        modifier = Modifier
            .fillMaxWidth()
            .padding(16.dp)
    ) {
        ListItem(
            headlineContent = {
                Text(text = "${state.monthMeetingCount}")
            },
            supportingContent = {
                Text(text = stringResource(R.string.settings_page_profile_header_meetings_this_month))
            },
            horizontalAlignment = Alignment.CenterHorizontally,
            modifier = Modifier.weight(1f)
        )
        ListItem(
            headlineContent = {
                Text(text = "${state.todolistCount}")
            },
            supportingContent = {
                Text(text = stringResource(R.string.settings_page_profile_header_todo_items))
            },
            horizontalAlignment = Alignment.CenterHorizontally,
            modifier = Modifier.weight(1f)
        )
        ListItem(
            headlineContent = {
                Text(text = "${state.meetingHours}h")
            },
            supportingContent = {
                Text(text = stringResource(R.string.settings_page_profile_header_meeting_hours))
            },
            horizontalAlignment = Alignment.CenterHorizontally,
            modifier = Modifier.weight(1f)
        )
    }
}

@Composable
fun SettingsItems(
    state: MainContract.State,
    actions: MainContract.Actions,
    modifier: Modifier = Modifier
) = Column(
    verticalArrangement = Arrangement.spacedBy(16.dp),
    modifier = modifier
) {
    @Composable
    fun SettingsGroup(
        category: SettingsCategory,
        title: @Composable (String) -> Unit,
        modifier: Modifier = Modifier,
        subitem: @Composable ColumnScope.(SettingsItem) -> Unit
    ) = Column(
        modifier = modifier,
        verticalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        title(stringResource(category.text))
        Column(
            modifier = Modifier
                .clip(RoundedCornerShape(16.dp))
                .background(color = MaterialTheme.colorScheme.surfaceContainerLow)
        ) {
            category.subitems.forEachIndexed { index, item ->
                subitem(item)
                if (index != category.subitems.lastIndex) {
                    HorizontalDivider(thickness = 0.5.dp)
                }
            }
        }
    }
    SettingsCategory.entries.forEach {
        SettingsGroup(
            category = it,
            title = {
                Text(text = it, style = MaterialTheme.typography.labelLarge)
            }, modifier = Modifier.padding(horizontal = 16.dp)
        ) { item ->
            val subtitle = when (item) {
                AccountItem.Email -> state.userInfo?.email
                ApplicationItem.Language -> getLocale().displayName
                else -> item.subtitle?.let { stringResource(it) }
            }
            ListItem(
                headlineContent = {
                    Text(text = stringResource(item.title))
                },
                supportingContent = subtitle?.let {
                    @Composable {
                        Text(text = subtitle)
                    }
                },
                leadingContent = {
                    Box(
                        contentAlignment = Alignment.Center,
                        modifier = Modifier
                            .clip(RoundedCornerShape(16.dp))
                            .size(48.dp)
                            .background(color = MaterialTheme.colorScheme.surfaceVariant)
                    ) {
                        Icon(
                            imageVector = item.icon,
                            contentDescription = item.icon.name,
                        )
                    }
                }, trailingContent = {
                    @Composable
                    fun NavigationIcon() {
                        Icon(
                            imageVector = Icons.AutoMirrored.Outlined.KeyboardArrowRight,
                            contentDescription = Icons.AutoMirrored.Outlined.KeyboardArrowRight.name
                        )
                    }
                    when (item) {
                        is AccountItem -> NavigationIcon()
                        is ApplicationItem -> when (item) {
                            ApplicationItem.DarkMode -> Switch(
                                checked = state.isDarkModeEnabled,
                                onCheckedChange = actions.onDarkModeChange,
                            )

                            ApplicationItem.AutoRecord -> Switch(
                                checked = state.isAutoRecordEnabled,
                                onCheckedChange = actions.onAutoRecordChange,
                            )

                            ApplicationItem.Language -> NavigationIcon()
                        }

                        is HelpItem -> NavigationIcon()
                        is MeetingItem -> NavigationIcon()
                        is NotificationItem -> when (item) {
                            NotificationItem.PushNotifications -> Switch(
                                checked = state.isNotificationEnabled,
                                onCheckedChange = actions.onNotificationChange,
                            )
                        }
                    }
                },
                colors = ListItemDefaults.colors(containerColor = Color.Transparent),
                modifier = Modifier.clickable(
                    onClick = {

                    }
                )
            )
        }
    }
}

enum class SettingsCategory(
    @StringRes
    val text: Int,
    val subitems: List<SettingsItem>
) {
    Account(R.string.settings_page_category_account, AccountItem.entries),
    Meeting(R.string.settings_page_category_meeting, MeetingItem.entries),
    Notification(R.string.settings_page_category_notification, NotificationItem.entries),
    Application(R.string.settings_page_category_application, ApplicationItem.entries),
    Help(R.string.settings_page_category_help, HelpItem.entries),
}

sealed interface SettingsItem {
    val icon: ImageVector
    val title: Int
    val subtitle: Int?
}

enum class AccountItem(
    override val icon: ImageVector,
    override val title: Int,
    override val subtitle: Int?
) : SettingsItem {
    EditProfile(
        icon = Icons.Outlined.Person,
        title = R.string.settings_page_account_edit_profile,
        subtitle = R.string.settings_page_account_edit_profile_subtitle
    ),
    Email(
        icon = Icons.Outlined.Email,
        title = R.string.settings_page_account_email,
        subtitle = null
    ),
    PrivacySecurity(
        icon = Icons.Outlined.PrivacyTip,
        title = R.string.settings_page_account_privacy_security,
        subtitle = R.string.settings_page_account_privacy_security_subtitle
    ),
}

enum class MeetingItem(
    override val icon: ImageVector,
    override val title: Int,
    override val subtitle: Int?
) : SettingsItem {
    Video(
        icon = Icons.Outlined.Videocam,
        title = R.string.settings_page_meeting_video,
        subtitle = R.string.settings_page_meeting_video_subtitle
    ),
    Transcript(
        icon = Icons.Outlined.KeyboardVoice,
        title = R.string.settings_page_meeting_transcript,
        subtitle = R.string.settings_page_meeting_transcript_subtitle
    ),
    Calendar(
        icon = Icons.Outlined.CalendarMonth,
        title = R.string.settings_page_meeting_calendar,
        subtitle = R.string.settings_page_meeting_calendar_subtitle
    )
}

enum class NotificationItem(
    override val icon: ImageVector,
    override val title: Int,
    override val subtitle: Int?
) : SettingsItem {
    PushNotifications(
        icon = Icons.Outlined.NotificationsNone,
        title = R.string.settings_page_notification_push_notifications,
        subtitle = R.string.settings_page_notification_push_notifications_subtitle
    )
}

enum class ApplicationItem(
    override val icon: ImageVector,
    override val title: Int,
    override val subtitle: Int?
) : SettingsItem {
    DarkMode(
        icon = Icons.Outlined.DarkMode,
        title = R.string.settings_page_application_dark_mode,
        subtitle = R.string.settings_page_application_dark_mode_subtitle
    ),
    AutoRecord(
        icon = Icons.Outlined.Videocam,
        title = R.string.settings_page_application_auto_record,
        subtitle = R.string.settings_page_application_auto_record_subtitle
    ),
    Language(
        icon = Icons.Outlined.Translate,
        title = R.string.settings_page_application_language,
        subtitle = null
    )
}

enum class HelpItem(
    override val icon: ImageVector,
    override val title: Int,
    override val subtitle: Int?
) : SettingsItem {
    HelpCenter(
        icon = Icons.AutoMirrored.Outlined.HelpOutline,
        title = R.string.settings_page_help_help_center,
        subtitle = R.string.settings_page_help_help_center_subtitle
    )
}

@Composable
@Previews
fun SettingsPagePreview() = PreviewTheme {
    SettingsPage(
        state = MainContract.State.Preview,
        actions = MainContract.Actions()
    )
}