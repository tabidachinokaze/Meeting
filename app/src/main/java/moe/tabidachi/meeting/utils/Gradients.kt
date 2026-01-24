package moe.tabidachi.meeting.utils

import android.content.Context
import androidx.compose.ui.graphics.Color
import androidx.core.graphics.toColorInt
import kotlinx.serialization.ExperimentalSerializationApi
import kotlinx.serialization.Serializable
import kotlinx.serialization.json.Json
import kotlinx.serialization.json.decodeFromStream
import org.koin.core.component.KoinComponent
import org.koin.core.component.inject
import kotlin.math.absoluteValue

object Gradients : KoinComponent {
    private val context: Context by inject()

    @OptIn(ExperimentalSerializationApi::class)
    private val gradients: List<Gradient> by lazy {
        context.assets.open("gradients.json").use(Json.Default::decodeFromStream)
    }

    private val gradientColors: List<List<Color>> by lazy {
        gradients.map { gradient ->
            gradient.colors.map { Color(it.toColorInt()) }
        }
    }

    fun getGradientColors(name: String): List<Color> {
        val hash = name.hashCode().absoluteValue
        val index = hash % gradientColors.size
        return gradientColors[index]
    }

    @Serializable
    data class Gradient(
        val name: String,
        val colors: List<String>
    )
}