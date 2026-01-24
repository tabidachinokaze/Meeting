package moe.tabidachi.meeting.ui.common

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.blur
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalInspectionMode
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import moe.tabidachi.meeting.utils.Gradients
import kotlin.math.absoluteValue

@Composable
fun MonogramAvatar(
    name: String,
    modifier: Modifier = Modifier,
    textColor: Color = Color.White
) {
    val initials = extractInitials(name)
    val gradient = when (LocalInspectionMode.current) {
        true -> generateConsistentGradient(name)
        else -> Gradients.getGradientColors(name)
    }
    Box(
        contentAlignment = Alignment.Center,
        modifier = modifier
            .clip(CircleShape)
            .size(40.dp)
    ) {
        Box(
            modifier = Modifier
                .blur(32.dp)
                .background(brush = Brush.linearGradient(colors = gradient))
                .matchParentSize()
        )
        Text(
            text = initials,
            color = textColor,
            fontWeight = FontWeight.Medium,
            fontSize = 16.sp
        )
    }
}

private fun extractInitials(name: String): String {
    return name.trim()
        .split("\\s+".toRegex())
        .take(2)
        .map { it.firstOrNull()?.uppercaseChar() ?: "" }
        .joinToString("")
}

private fun generateConsistentGradient(name: String): List<Color> {
    val hash = name.hashCode().absoluteValue

    val baseHue = (hash % 360).toFloat()

    val colors = listOf(
        Color.hsl(baseHue, 0.8f, 0.7f),
        Color.hsl((baseHue + 30f) % 360f, 0.75f, 0.75f),
        Color.hsl((baseHue - 30f) % 360f, 0.7f, 0.8f)
    )

    return colors
}

@Composable
@Preview
private fun MonogramAvatarPreview() {
    Column {
        MonogramAvatar(
            name = "Android Studio",
            modifier = Modifier.size(48.dp)
        )
    }
}
