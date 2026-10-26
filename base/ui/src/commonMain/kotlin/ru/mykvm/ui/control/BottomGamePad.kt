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
import ru.mykvm.ui.hero.HeroState

@Composable
fun BottomGamePad(
    activeHero: HeroState,
    controlsEnabled: Boolean,
    staminaAvailable: Boolean,
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
        // Кнопки движения влево/вправо в левом углу геймпада
        Row(
            horizontalArrangement = Arrangement.spacedBy(12.dp),
            modifier = Modifier.align(Alignment.CenterStart)
        ) {
            DirectionButton(text = "◀", onClick = { if (controlsEnabled) onLeftClick() })
            DirectionButton(text = "▶", onClick = { if (controlsEnabled) onRightClick() })
        }

        // Показатели здоровья и энергии активного персонажа по центру
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.spacedBy(4.dp),
            modifier = Modifier.align(Alignment.Center)
        ) {
            Text(
                text = activeHero.heroType.displayName.uppercase(),
                color = Color(0xFFFFB300),
                fontSize = 12.sp,
                letterSpacing = 1.sp
            )
            Text(text = "HP: " + "❤️ ".repeat(activeHero.health).ifEmpty { "💀" }, color = Color.White, fontSize = 14.sp)
            Text(
                text = "STAMINA: ⚡ ${activeHero.stamina}/10",
                color = if (activeHero.stamina <= 2) Color(0xFFE53935) else Color(0xFFFFB300),
                fontSize = 12.sp
            )
        }

        // Боевые кнопки действия в правом углу геймпада
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