package ru.mykvm.ui.control

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import org.jetbrains.compose.resources.painterResource
import ru.mykvm.ui.hero.HeroState

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
            Box(
                modifier = Modifier
                    .size(64.dp) // Ваши иконки 64x64 пикселя встанут идеально
                    .background(
                        color = if (isActive) Color(0xFFFFB300) else Color(0xFF424242),
                        shape = RoundedCornerShape(12.dp)
                    )
                    .border(
                        width = if (isActive) 3.dp else 1.dp,
                        color = if (isActive) Color.White else Color(0xFF666666),
                        shape = RoundedCornerShape(12.dp)
                    )
                    .clickable { onHeroSelected(index) },
                contentAlignment = Alignment.Center
            ) {
                androidx.compose.foundation.Image(
                    painter = painterResource(hero.heroClass.icon),
                    contentDescription = hero.heroClass.displayName,
                    modifier = Modifier.size(54.dp) // Небольшой внутренний отступ для рамки
                )
            }
        }
    }
}