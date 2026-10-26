package ru.mykvm.ui.control

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.ripple.rememberRipple
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.graphics.Color
import ru.mykvm.ui.hero.HeroState
import androidx.compose.foundation.clickable
import androidx.compose.material3.ripple
import androidx.compose.ui.text.font.FontWeight


@Composable
fun BottomGamePad(
    activeHero: HeroState,
    controlsEnabled: Boolean,
    staminaAvailable: Boolean,
    isResourceTile: Boolean,
    onLeftClick: () -> Unit,
    onRightClick: () -> Unit,
    onAttackClick: () -> Unit,
    onJumpClick: () -> Unit,
    onExtractClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    Row(
        modifier = modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        // ========================================================
        // ЛЕВАЯ СТОРОНА: СТИЛЬНЫЕ КНОПКИ ДВИЖЕНИЯ В РАМОЧКАХ
        // ========================================================
        Row(horizontalArrangement = Arrangement.spacedBy(12.dp)) {
            // Кнопка Влево (◀)
            val interactionLeft = remember { MutableInteractionSource() }
            Box(
                modifier = Modifier
                    .size(56.dp)
                    .background(Color(0xFF333333), RoundedCornerShape(14.dp))
                    .border(width = 1.dp, color = Color(0xFF666666), shape = RoundedCornerShape(14.dp))
                    .clickable(
                        interactionSource = interactionLeft,
                        indication = ripple(bounded = true, color = Color.White)
                    ) { if (controlsEnabled) onLeftClick() },
                contentAlignment = Alignment.Center
            ) {
                Text(text = "◀", color = Color.White, fontSize = 20.sp)
            }

            // Кнопка Вправо (▶)
            val interactionRight = remember { MutableInteractionSource() }
            Box(
                modifier = Modifier
                    .size(56.dp)
                    .background(Color(0xFF333333), RoundedCornerShape(14.dp))
                    .border(width = 1.dp, color = Color(0xFF666666), shape = RoundedCornerShape(14.dp))
                    .clickable(
                        interactionSource = interactionRight,
                        indication = ripple(bounded = true, color = Color.White)
                    ) { if (controlsEnabled) onRightClick() },
                contentAlignment = Alignment.Center
            ) {
                Text(text = "▶", color = Color.White, fontSize = 20.sp)
            }
        }

        // ========================================================
        // ЦЕНТР: СТАТУС-БАР ГЕРОЯ (ИМЯ, ЗДОРОВЬЕ, СТАМИНА)
        // ========================================================
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.spacedBy(4.dp),
            modifier = Modifier.padding(horizontal = 8.dp)
        ) {
            Text(
                text = activeHero.heroType.displayName.uppercase(),
                color = Color.White,
                fontSize = 15.sp,
                fontWeight = FontWeight.Bold
            )
            Text(
                text = "HP: ❤️ ${activeHero.health}",
                color = Color(0xFFFF5252),
                fontSize = 14.sp,
                fontWeight = FontWeight.SemiBold
            )
            Text(
                text = "STM: ⚡ ${activeHero.stamina}/10",
                color = Color(0xFFFFD740),
                fontSize = 14.sp,
                fontWeight = FontWeight.SemiBold
            )
        }

        // ========================================================
        // ПРАВАЯ СТОРОНА: БОЕВЫЕ КНОПКИ И ДОБЫЧА В РАМОЧКАХ
        // ========================================================
        Row(
            horizontalArrangement = Arrangement.spacedBy(12.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            // 🔥 КНОПКА СБОРА РЕСУРСА (Появляется строго по условию и имеет золотую рамку)
            if (isResourceTile) {
                val interactionExtract = remember { MutableInteractionSource() }
                Box(
                    modifier = Modifier
                        .size(56.dp)
                        .background(Color(0xFFFFB300), RoundedCornerShape(14.dp))
                        .border(width = 1.5.dp, color = Color.White, shape = RoundedCornerShape(14.dp))
                        .clickable(
                            interactionSource = interactionExtract,
                            indication = ripple(bounded = true, color = Color.White)
                        ) { if (controlsEnabled) onExtractClick() },
                    contentAlignment = Alignment.Center
                ) {
                    Text(text = "⛏️", color = Color.White, fontSize = 22.sp)
                }
            }

            // Кнопка Прыжка (▲)
            val interactionJump = remember { MutableInteractionSource() }
            Box(
                modifier = Modifier
                    .size(56.dp)
                    .background(Color(0xFF333333), RoundedCornerShape(14.dp))
                    .border(width = 1.dp, color = Color(0xFF666666), shape = RoundedCornerShape(14.dp))
                    .clickable(
                        interactionSource = interactionJump,
                        indication = ripple(bounded = true, color = Color.White)
                    ) { if (controlsEnabled) onJumpClick() },
                contentAlignment = Alignment.Center
            ) {
                Text(text = "▲", color = Color.White, fontSize = 20.sp)
            }

            // Кнопка Атаки (ATK)
            val interactionAttack = remember { MutableInteractionSource() }
            Box(
                modifier = Modifier
                    .size(60.dp) // Немного выделяем кнопку атаки размером
                    .background(
                        color = if (staminaAvailable) Color(0xFFE57373) else Color(0xFF555555),
                        shape = RoundedCornerShape(16.dp)
                    )
                    .border(
                        width = 1.5.dp,
                        color = if (staminaAvailable) Color(0xFFFFCDD2) else Color(0xFF777777),
                        shape = RoundedCornerShape(16.dp)
                    )
                    .clickable(
                        interactionSource = interactionAttack,
                        indication = ripple(bounded = true, color = Color.White)
                    ) { if (controlsEnabled && staminaAvailable) onAttackClick() },
                contentAlignment = Alignment.Center
            ) {
                Text(
                    text = "ATK",
                    color = Color.White,
                    fontSize = 15.sp,
                    fontWeight = FontWeight.Bold
                )
            }
        }
    }
}