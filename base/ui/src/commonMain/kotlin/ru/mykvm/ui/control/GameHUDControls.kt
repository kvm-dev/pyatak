package ru.mykvm.ui.control

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

@Composable
fun GameHUDControls(
    health: Int,
    stamina: Int,
    controlsEnabled: Boolean,
    staminaAvailable: Boolean,
    // ИСПРАВЛЕНИЕ: Возвращаем чистые и понятные события клика
    onLeftClick: () -> Unit,
    onRightClick: () -> Unit,
    onJumpClick: () -> Unit,
    onAttackClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    Box(
        modifier = modifier
            .fillMaxWidth()
            .background(color = Color(0xAA2D2D2D), shape = RoundedCornerShape(24.dp))
            .border(width = 2.dp, color = Color(0xFF444444), shape = RoundedCornerShape(24.dp))
            .padding(horizontal = 16.dp, vertical = 16.dp)
    ) {
        Row(
            horizontalArrangement = Arrangement.spacedBy(12.dp),
            modifier = Modifier.align(Alignment.CenterStart)
        ) {
            // Вызываем простые клики, если управление доступно
            DirectionButton(text = "◀", onClick = { if (controlsEnabled) onLeftClick() })
            DirectionButton(text = "▶", onClick = { if (controlsEnabled) onRightClick() })
        }

        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.spacedBy(4.dp),
            modifier = Modifier.align(Alignment.Center)
        ) {
            Text(text = "HP: " + "❤️ ".repeat(health).ifEmpty { "💀" }, color = Color.White, fontSize = 14.sp)
            Text(
                text = "STAMINA: ⚡ $stamina/10",
                color = if (stamina <= 2) Color(0xFFE53935) else Color(0xFFFFB300),
                fontSize = 12.sp
            )
        }

        Row(
            horizontalArrangement = Arrangement.spacedBy(12.dp),
            modifier = Modifier.align(Alignment.CenterEnd),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Surface(
                onClick = { if (controlsEnabled) onJumpClick() },
                shape = CircleShape,
                color = if (staminaAvailable) Color(0xFF1976D2) else Color(0xFF757575),
                modifier = Modifier.size(60.dp)
            ) {
                Box(contentAlignment = Alignment.Center) { Text(text = "▲", color = Color.White, fontSize = 22.sp) }
            }

            Surface(
                onClick = { if (controlsEnabled) onAttackClick() },
                shape = CircleShape,
                color = if (staminaAvailable) Color(0xFFC62828) else Color(0xFF757575),
                modifier = Modifier.size(68.dp)
            ) {
                Box(contentAlignment = Alignment.Center) { Text(text = "ATK", color = Color.White, fontSize = 14.sp) }
            }
        }
    }
}