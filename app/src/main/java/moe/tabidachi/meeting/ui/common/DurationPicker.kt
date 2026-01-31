package moe.tabidachi.meeting.ui.common

import androidx.annotation.IntRange
import androidx.compose.foundation.background
import androidx.compose.foundation.gestures.snapping.rememberSnapFlingBehavior
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxScope
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyItemScope
import androidx.compose.foundation.lazy.LazyListState
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.BasicAlertDialog
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.LocalContentColor
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.Saver
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.snapshotFlow
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.composed
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.layout.SubcomposeLayout
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.DialogProperties
import kotlinx.coroutines.flow.distinctUntilChanged
import moe.tabidachi.meeting.R
import moe.tabidachi.meeting.ui.preview.PreviewTheme
import moe.tabidachi.meeting.ui.preview.Previews
import kotlin.math.abs
import kotlin.math.sqrt
import kotlin.time.Duration
import kotlin.time.Duration.Companion.hours
import kotlin.time.Duration.Companion.minutes
import kotlin.time.Duration.Companion.seconds

interface DurationPickerState {

    /** The currently selected second (0-59). */
    @get:IntRange(from = 0, to = 59)
    @setparam:IntRange(from = 0, to = 59)
    var second: Int

    /** The currently selected minute (0-59). */
    @get:IntRange(from = 0, to = 59)
    @setparam:IntRange(from = 0, to = 59)
    var minute: Int

    /** The currently selected hour (0-23). */
    @get:IntRange(from = 0, to = 23)
    @setparam:IntRange(from = 0, to = 23)
    var hour: Int

    val duration: Duration
}

class DurationPickerStateImpl(
    initialHour: Int,
    initialMinute: Int,
    initialSecond: Int
) : DurationPickerState {
    val houtState = mutableIntStateOf(initialHour)
    val minuteState = mutableIntStateOf(initialMinute)
    val secondState = mutableIntStateOf(initialSecond)

    override var second: Int
        get() = secondState.intValue
        set(value) {
            secondState.intValue = value
        }
    override var minute: Int
        get() = minuteState.intValue
        set(value) {
            minuteState.intValue = value
        }
    override var hour: Int
        get() = houtState.intValue
        set(value) {
            houtState.intValue = value
        }

    override val duration: Duration by derivedStateOf {
        hour.hours + minute.minutes + second.seconds
    }

    companion object {
        fun Saver(): Saver<DurationPickerStateImpl, *> =
            Saver(
                save = {
                    listOf(it.hour, it.minute, it.second)
                },
                restore = {
                    DurationPickerStateImpl(
                        initialHour = it[0],
                        initialMinute = it[1],
                        initialSecond = it[2]
                    )
                }
            )
    }
}

@Composable
fun rememberDurationPickerState(
    initialHour: Int = 0,
    initialMinute: Int = 0,
    initialSecond: Int = 0
): DurationPickerState = rememberSaveable(saver = DurationPickerStateImpl.Saver()) {
    DurationPickerStateImpl(
        initialHour = initialHour,
        initialMinute = initialMinute,
        initialSecond = initialSecond
    )
}

@Composable
fun rememberDurationPickerState(
    initialDuration: Duration?,
): DurationPickerState = when (initialDuration) {
    null -> rememberDurationPickerState()
    else -> rememberDurationPickerState(
        initialHour = initialDuration.inWholeHours.toInt(),
        initialMinute = (initialDuration.inWholeMinutes - initialDuration.inWholeHours * 60).toInt(),
        initialSecond = (initialDuration.inWholeSeconds - initialDuration.inWholeMinutes * 60).toInt()
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DurationPickerDialog(
    state: DurationPickerState,
    onDismissRequest: () -> Unit,
    confirmButton: @Composable () -> Unit,
    dismissButton: @Composable () -> Unit,
    modifier: Modifier = Modifier,
    isSecondPickerEnabled: Boolean = false,
    visibleItemCount: Int = 5,
    properties: DialogProperties = DialogProperties(),
) = BasicAlertDialog(
    onDismissRequest = onDismissRequest,
    modifier = modifier,
    properties = properties
) {
    Column(
        horizontalAlignment = Alignment.Start,
        modifier = Modifier
            .clip(RoundedCornerShape(16.dp))
            .background(color = MaterialTheme.colorScheme.surface)
    ) {
        Column(
            verticalArrangement = Arrangement.spacedBy(16.dp),
            modifier = Modifier.padding(16.dp)
        ) {
            CompositionLocalProvider(
                LocalContentColor provides MaterialTheme.colorScheme.onSurfaceVariant
            ) {
                Text(
                    text = stringResource(R.string.duration_picker_dialog_title),
                    style = MaterialTheme.typography.titleSmall
                )
                Text(
                    text = "${state.duration}",
                    style = MaterialTheme.typography.headlineLarge
                )
            }
        }
        HorizontalDivider()
        DurationPickerLayout(
            separatorText = {
                Text(
                    text = ":",
                    style = MaterialTheme.typography.displayMedium
                )
            },
            hourText = {
                Text(
                    text = "%02d".format(it),
                    style = MaterialTheme.typography.displayMedium,
                    modifier = Modifier.padding(horizontal = 16.dp)
                )
            },
            minuteText = {
                Text(
                    text = "%02d".format(it),
                    style = MaterialTheme.typography.displayMedium,
                    modifier = Modifier.padding(horizontal = 16.dp)
                )
            },
            secondText = if (isSecondPickerEnabled) {
                {
                    Text(
                        text = "%02d".format(it),
                        style = MaterialTheme.typography.displayMedium,
                        modifier = Modifier.padding(horizontal = 16.dp)
                    )
                }
            } else null,
            visibleItemCount = visibleItemCount,
            modifier = Modifier.align(Alignment.CenterHorizontally),
            state = state
        )
        Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(8.dp),
            modifier = Modifier
                .padding(8.dp)
                .align(Alignment.End)
        ) {
            dismissButton()
            confirmButton()
        }
    }
}

@Composable
private fun TimeList(
    numeralSystem: Int,
    itemHeight: Dp,
    formattedText: @Composable BoxScope.(Int) -> Unit,
    modifier: Modifier = Modifier,
    listState: LazyListState = rememberLazyListState(Int.MAX_VALUE / 2),
) = LazyColumn(
    state = listState,
    horizontalAlignment = Alignment.CenterHorizontally,
    flingBehavior = rememberSnapFlingBehavior(listState),
    modifier = modifier
) {
    items(Int.MAX_VALUE) { index ->
        Box(
            contentAlignment = Alignment.Center,
            modifier = Modifier
                .height(itemHeight)
                .smoothScrollEffect(index, listState, itemHeight),
        ) {
            formattedText(index % numeralSystem)
        }
    }
}

context(scope: LazyItemScope)
private fun Modifier.smoothScrollEffect(
    index: Int,
    state: LazyListState,
    itemHeight: Dp
): Modifier = composed {
    val density = LocalDensity.current

    val effect by remember(state) {
        derivedStateOf {
            val itemPx = with(density) { itemHeight.toPx() }

            val itemsBefore = index - state.firstVisibleItemIndex
            val itemTop = if (itemsBefore == 0) {
                -state.firstVisibleItemScrollOffset.toFloat()
            } else {
                itemPx * itemsBefore - state.firstVisibleItemScrollOffset.toFloat()
            }

            val itemCenter = itemTop + itemPx / 2
            val viewportCenter = state.layoutInfo.viewportEndOffset / 2f
            val distance = abs(itemCenter - viewportCenter)

            val maxDist = itemPx * 2.5f
            val x = (distance / maxDist).coerceIn(0f, 1f)

            val scaleCurve = -x + 1f
            val alphaCurve = 1 - sqrt(x)

            val scale = 0.6f + scaleCurve * 0.4f
            val alpha = 0.2f + alphaCurve * 0.8f

            scale.coerceIn(0.0f, 1f) to alpha.coerceIn(0.2f, 1f)
        }
    }

    val (scale, alpha) = effect

    graphicsLayer {
        scaleX = scale
        scaleY = scale
        this.alpha = alpha
    }
}

data class DurationColumnData(
    val numeralSystem: Int,
    val initialValue: Int,
    val onValueChange: (Int) -> Unit,
    val content: @Composable BoxScope.(value: Int) -> Unit,
)

@Composable
fun DurationPickerLayout(
    state: DurationPickerState,
    separatorText: @Composable BoxScope.() -> Unit,
    hourText: @Composable BoxScope.(hour: Int) -> Unit,
    minuteText: @Composable BoxScope.(minute: Int) -> Unit,
    modifier: Modifier = Modifier,
    secondText: @Composable (BoxScope.(second: Int) -> Unit)? = null,
    visibleItemCount: Int = 5
) = Box(
    contentAlignment = Alignment.Center,
    modifier = modifier
) {
    val density = LocalDensity.current
    SubcomposeLayout { constraints ->
        val columns = listOfNotNull(
            DurationColumnData(
                numeralSystem = 24,
                initialValue = state.hour,
                onValueChange = state::hour::set,
                content = hourText
            ),
            DurationColumnData(
                numeralSystem = 60,
                initialValue = state.minute,
                onValueChange = state::minute::set,
                content = minuteText
            ),
            secondText?.let {
                DurationColumnData(
                    numeralSystem = 60,
                    initialValue = state.second,
                    onValueChange = state::second::set,
                    content = it
                )
            }
        )

        val separatorPlaceables = (0..<columns.size - 1).map { index ->
            subcompose(slotId = "separator-$index") {
                Box(contentAlignment = Alignment.Center, content = separatorText)
            }[0].measure(constraints)
        }

        val itemHeight = separatorPlaceables.maxOf { it.height }
        val itemHeightDp = with(density) { itemHeight.toDp() }
        val containerHeight = itemHeight * visibleItemCount
        val columnWidth =
            (constraints.maxWidth - separatorPlaceables.sumOf { it.width }) / columns.size

        val columnPlaceables =
            columns.mapIndexed { index, (numeralSystem, initialValue, onValueChange, content) ->
                subcompose("column-$index") {
                    val listState = rememberLazyListState(
                        Int.MAX_VALUE / 2 + ((initialValue - (Int.MAX_VALUE / 2) % numeralSystem + numeralSystem) % numeralSystem) - (visibleItemCount - 1) / 2,
                        -((visibleItemCount + 1) % 2) * itemHeight / 2
                    )
                    LaunchedEffect(listState) {
                        snapshotFlow {
                            val visibleItems = listState.layoutInfo.visibleItemsInfo
                            if (visibleItems.isEmpty()) return@snapshotFlow 0

                            val viewportCenter = listState.layoutInfo.viewportEndOffset / 2
                            val centerItem = visibleItems.minByOrNull { item ->
                                val itemCenter = item.offset + item.size / 2
                                abs(itemCenter - viewportCenter)
                            }

                            centerItem?.let { it.index % numeralSystem } ?: 0
                        }.distinctUntilChanged()
                            .collect(onValueChange)
                    }
                    TimeList(
                        numeralSystem = numeralSystem,
                        itemHeight = itemHeightDp,
                        formattedText = content,
                        listState = listState
                    )
                }[0].measure(constraints.copy(maxWidth = columnWidth, maxHeight = containerHeight))
            }

        layout(
            width = columnPlaceables.sumOf { it.width } + separatorPlaceables.sumOf { it.width },
            height = containerHeight
        ) {
            var offsetX = 0
            columnPlaceables.forEachIndexed { index, placeable ->
                placeable.placeRelative(offsetX, 0)
                offsetX += placeable.width
                if (index != columnPlaceables.lastIndex) {
                    val placeable = separatorPlaceables[index]
                    placeable.placeRelative(offsetX, placeable.height * (visibleItemCount - 1) / 2)
                    offsetX += placeable.width
                }
            }
        }
    }
}

@Composable
@Previews
fun DurationPickerPreview() = PreviewTheme {
    DurationPickerLayout(
        separatorText = {},
        hourText = {},
        minuteText = {},
        state = rememberDurationPickerState(),
    )
}
