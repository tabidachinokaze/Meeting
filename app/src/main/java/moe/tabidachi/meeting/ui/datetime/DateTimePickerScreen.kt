package moe.tabidachi.meeting.ui.datetime

import androidx.compose.animation.AnimatedContent
import androidx.compose.animation.AnimatedContentTransitionScope
import androidx.compose.animation.core.tween
import androidx.compose.animation.togetherWith
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.BasicAlertDialog
import androidx.compose.material3.DatePicker
import androidx.compose.material3.DatePickerDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.SelectableDates
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TimePicker
import androidx.compose.material3.rememberDatePickerState
import androidx.compose.material3.rememberTimePickerState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.DialogProperties
import kotlinx.datetime.LocalDateTime
import kotlinx.datetime.LocalTime
import kotlinx.datetime.TimeZone
import kotlinx.datetime.toLocalDateTime
import moe.tabidachi.meeting.R
import kotlin.time.Clock
import kotlin.time.Instant

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DateTimePickerScreen(
    onNavigateUp: () -> Unit,
    onDateTimePicked: (LocalDateTime) -> Unit
) {
    var dateTime by remember { mutableStateOf(DateTime.DATE) }
    val datePickerState = rememberDatePickerState(
        selectableDates = object : SelectableDates {
            private val today =
                Clock.System.now().toLocalDateTime(TimeZone.currentSystemDefault()).date

            override fun isSelectableDate(utcTimeMillis: Long): Boolean {
                val date = Instant.fromEpochMilliseconds(utcTimeMillis)
                    .toLocalDateTime(TimeZone.currentSystemDefault()).date
                return date >= today
            }

            override fun isSelectableYear(year: Int): Boolean {
                return year >= today.year
            }
        }
    )
    val timePickerState = rememberTimePickerState()

    BasicAlertDialog(
        onDismissRequest = onNavigateUp,
        properties = DialogProperties(usePlatformDefaultWidth = false),
        modifier = Modifier
            .padding(horizontal = 16.dp)
    ) {
        Column(
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.End,
            modifier = Modifier
                .clip(RoundedCornerShape(16.dp))
                .background(color = MaterialTheme.colorScheme.surface)
        ) {
            AnimatedContent(
                targetState = dateTime,
                transitionSpec = {
                    val slideDirection = when {
                        initialState == DateTime.DATE && targetState == DateTime.TIME ->
                            AnimatedContentTransitionScope.SlideDirection.Left

                        else -> AnimatedContentTransitionScope.SlideDirection.Right
                    }

                    slideIntoContainer(
                        towards = slideDirection,
                        animationSpec = tween(durationMillis = 300)
                    ) togetherWith slideOutOfContainer(
                        towards = slideDirection,
                        animationSpec = tween(durationMillis = 300)
                    )
                }
            ) { targetState ->
                when (targetState) {
                    DateTime.DATE -> DatePicker(
                        state = datePickerState,
                        colors = DatePickerDefaults.colors(
                            containerColor = MaterialTheme.colorScheme.surface,
                        )
                    )

                    DateTime.TIME -> TimePicker(
                        state = timePickerState,
                        modifier = Modifier.padding(16.dp)
                    )
                }
            }
            Row(
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier.padding(8.dp)
            ) {
                when (dateTime) {
                    DateTime.DATE -> {
                        TextButton(
                            onClick = onNavigateUp
                        ) {
                            Text(text = stringResource(R.string.date_time_picker_screen_cancel_button))
                        }
                        Spacer(modifier = Modifier.width(8.dp))
                        TextButton(
                            onClick = {
                                dateTime = DateTime.TIME
                            }, enabled = datePickerState.selectedDateMillis != null
                        ) {
                            Text(text = stringResource(R.string.date_time_picker_screen_next_button))
                        }
                    }

                    DateTime.TIME -> {
                        TextButton(
                            onClick = {
                                dateTime = DateTime.DATE
                            }
                        ) {
                            Text(text = stringResource(R.string.date_time_picker_screen_previous_button))
                        }
                        Spacer(modifier = Modifier.width(8.dp))
                        TextButton(
                            onClick = {
                                val date = Instant
                                    .fromEpochMilliseconds(datePickerState.selectedDateMillis ?: 0)
                                    .toLocalDateTime(TimeZone.currentSystemDefault()).date
                                val time = LocalTime(
                                    hour = timePickerState.hour,
                                    minute = timePickerState.minute,
                                    second = 0
                                )
                                onDateTimePicked(LocalDateTime(date, time))
                                onNavigateUp()
                            }
                        ) {
                            Text(text = stringResource(R.string.date_time_picker_screen_confirm_button))
                        }
                    }
                }
            }
        }
    }
}

enum class DateTime {
    DATE, TIME
}
