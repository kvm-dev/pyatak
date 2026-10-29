package ru.mykvm.pyatak.base.engine.control

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.ripple
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.BlendMode
import androidx.compose.ui.unit.dp
import org.jetbrains.compose.resources.painterResource
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ColorFilter
import ru.mykvm.pyatak.base.engine.ComposeResources.Res
import ru.mykvm.pyatak.base.engine.ComposeResources.deadAvatar
import ru.mykvm.pyatak.base.engine.hero.HeroState

@Composable
fun TopHeroSelector(
    team: List<HeroState>,
    activeHeroIndex: Int,
    onHeroSelected: (Int) -> Unit,
    modifier: Modifier = Modifier
) {
    Row(
        horizontalArrangement = Arrangement.spacedBy(16.dp),
        modifier = modifier
            .background(color = Color(0x772D2D2D), shape = RoundedCornerShape(16.dp))
            .border(width = 1.dp, color = Color(0xFF444444), shape = RoundedCornerShape(16.dp))
            .padding(horizontal = 16.dp, vertical = 8.dp)
    ) {
        team.forEachIndexed { index, hero ->
            val isActive = index == activeHeroIndex
            val isDead = hero.health <= 0 // 🔥 Проверяем, жив ли персонаж
            val interactionSource = remember { MutableInteractionSource() }


            Box(
                modifier = Modifier
                    .size(64.dp)
                    .background(
                        color = when {
                            isDead -> Color(0xFF212121) // Темный фон для мертвеца
                            isActive -> Color(0xFFFFB300)
                            else -> Color(0xFF424242)
                        },
                        shape = RoundedCornerShape(12.dp)
                    )
                    .border(
                        width = if (isActive && !isDead) 3.dp else 1.dp,
                        color = if (isActive && !isDead) Color.White else Color(0xFF666666),
                        shape = RoundedCornerShape(12.dp)
                    )
                    .clickable(
                        interactionSource = interactionSource,
                        indication = ripple(
                            bounded = true,
                            color = Color.White,
                            radius = 32.dp
                        )
                    ) {
                        if (!isDead) onHeroSelected(index)
                    },
                contentAlignment = Alignment.Center
            ) {
                // Если персонаж умер, подставляем иконку могилы/черепа, иначе его обычный аватар
                val avatarResource = if (isDead) Res.drawable.deadAvatar else hero.heroType.icon

                Image(
                    painter = painterResource(avatarResource),
                    contentDescription = hero.heroType.displayName,
                    modifier = Modifier.size(54.dp),
                    // Добавляем черно-белый/затемняющий эффект для мертвых персонажей
                    colorFilter = if (isDead) ColorFilter.tint(Color(0xAA000000), BlendMode.SrcAtop) else null
                )
            }
        }
    }
}