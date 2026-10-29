package ru.mykvm.pyatak.base.engine.control

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
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
import org.jetbrains.compose.resources.painterResource
import ru.mykvm.pyatak.base.engine.hero.HeroState

@Composable
fun GameHUDControls(
    team: List<HeroState>,          // Список из 3-х выбранных героев
    activeHeroIndex: Int,           // Индекс текущего управляемого героя
    onHeroSelected: (Int) -> Unit,  // Событие клика по иконке
    controlsEnabled: Boolean,
    staminaAvailable: Boolean,
    onLeftClick: () -> Unit,
    onRightClick: () -> Unit,
    onJumpClick: () -> Unit,
    onAttackClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    val activeHero = team[activeHeroIndex]

    Box(
        modifier = modifier
            .fillMaxWidth()
            .background(color = Color(0xAA2D2D2D), shape = RoundedCornerShape(24.dp))
            .border(width = 2.dp, color = Color(0xFF444444), shape = RoundedCornerShape(24.dp))
            .padding(horizontal = 16.dp, vertical = 12.dp)
    ) {
        // ЛЕВАЯ ЧАСТЬ: Иконки выбора персонажей (3 штуки)
        Row(
            horizontalArrangement = Arrangement.spacedBy(8.dp),
            modifier = Modifier.align(Alignment.CenterStart)
        ) {
            team.forEachIndexed { index, hero ->
                val isActive = index == activeHeroIndex
                Box(
                    modifier = Modifier
                        .size(54.dp) // Чуть меньше 64, чтобы влезло с рамкой, либо делайте 64.dp
                        .background(
                            color = if (isActive) Color(0xFFFFB300) else Color(0xFF424242),
                            shape = RoundedCornerShape(8.dp)
                        )
                        .border(
                            width = if (isActive) 3.dp else 1.dp,
                            color = if (isActive) Color.White else Color(0xFF666666),
                            shape = RoundedCornerShape(8.dp)
                        )
                        .clickable { onHeroSelected(index) },
                    contentAlignment = Alignment.Center
                ) {
                    // Отрисовка вашей иконки 64x64
                    Image(
                        painter = painterResource(hero.heroType.icon), // Используем painterResource вместо imageResource
                        contentDescription = hero.heroType.displayName,
                        modifier = Modifier.size(48.dp)
                    )
                }
            }

            // Кнопки движения сдвигаем правее иконок
            Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                DirectionButton(text = "◀", onClick = { if (controlsEnabled) onLeftClick() })
                DirectionButton(text = "▶", onClick = { if (controlsEnabled) onRightClick() })
            }
        }

        // ЦЕНТРАЛЬНАЯ ЧАСТЬ: Метрики АКТИВНОГО персонажа
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.spacedBy(4.dp),
            modifier = Modifier.align(Alignment.Center)
        ) {
            Text(text = activeHero.heroType.displayName, color = Color.White, fontSize = 12.sp, maxLines = 1)
            Text(text = "HP: " + "❤️ ".repeat(activeHero.health).ifEmpty { "💀" }, color = Color.White, fontSize = 14.sp)
            Text(
                text = "STAMINA: ⚡ ${activeHero.stamina}/10",
                color = if (activeHero.stamina <= 2) Color(0xFFE53935) else Color(0xFFFFB300),
                fontSize = 12.sp
            )
        }

        // ПРАВАЯ ЧАСТЬ: Действия
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
                color = if (staminaAvailable) Color(0xFFC62828) else Color(0xFF757575), // Чистый цвет
                modifier = Modifier.size(68.dp) // Чистый размер вынесен в модификатор
            ) {
                Box(contentAlignment = Alignment.Center) {
                    Text(text = "ATK", color = Color.White, fontSize = 14.sp)
                }
            }
        }
    }
}