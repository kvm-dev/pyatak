package ru.mykvm.ui.control

import androidx.compose.foundation.gestures.awaitFirstDown
import androidx.compose.foundation.gestures.forEachGesture
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.pointer.PointerEventType
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

@Composable
fun DirectionButton(
    text: String,
    onClick: () -> Unit // ИСПРАВЛЕНИЕ: Обычный простой клик без флагов true/false!
) {
    Surface(
        shape = CircleShape,
        color = Color(0xFF424242),
        modifier = Modifier
            .size(60.dp)
            .pointerInput(Unit) {
                forEachGesture {
                    awaitPointerEventScope {
                        // Фиксируем только прикосновение пальца
                        val down = awaitFirstDown(requireUnconsumed = false)
                        onClick() // Мгновенно даем команду пойти на 1 шаг

                        // Просто ждем, пока палец уберут, чтобы кнопка не спамила
                        val pointerId = down.id
                        while (true) {
                            val event = awaitPointerEvent()
                            val anyUp = event.changes.any { it.id == pointerId && !it.pressed }
                            if (anyUp || event.type == PointerEventType.Release) {
                                break
                            }
                        }
                    }
                }
            }
    ) {
        Box(contentAlignment = Alignment.Center) {
            Text(text = text, color = Color.White, fontSize = 22.sp)
        }
    }
}