package moe.tabidachi.meeting.ui.common

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.RowScope
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp

@Composable
fun BottomButtons(
    negativeContent: @Composable RowScope.() -> Unit,
    positiveContent: @Composable RowScope.() -> Unit,
    onNegativeClick: () -> Unit,
    onPositiveClick: () -> Unit,
    modifier: Modifier = Modifier,
) = Row(
    verticalAlignment = Alignment.CenterVertically,
    horizontalArrangement = Arrangement.spacedBy(16.dp),
    modifier = modifier
) {
    Button(
        onClick = onNegativeClick, shape = RoundedCornerShape(16.dp),
        colors = ButtonDefaults.buttonColors(
            containerColor = MaterialTheme.colorScheme.secondaryContainer,
            contentColor = MaterialTheme.colorScheme.onSecondaryContainer
        ),
        modifier = Modifier
            .weight(1f)
            .height(48.dp),
        content = negativeContent
    )
    Button(
        onClick = onPositiveClick, shape = RoundedCornerShape(16.dp),
        modifier = Modifier
            .weight(1f)
            .height(48.dp),
        content = positiveContent
    )
}