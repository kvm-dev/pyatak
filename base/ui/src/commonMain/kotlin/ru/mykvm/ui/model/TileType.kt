package ru.mykvm.ui.model

import org.jetbrains.compose.resources.DrawableResource
import ru.mykvm.ui.ComposeResources.Res
import ru.mykvm.ui.ComposeResources.emptyTile
import ru.mykvm.ui.ComposeResources.grassTile
import ru.mykvm.ui.ComposeResources.stoneWallEndTile
import ru.mykvm.ui.ComposeResources.stoneWallStartTile

enum class TileType(
    val isWalkable: Boolean,
    val colorHex: Long,
    val drawableRes: DrawableResource?,
    val requiredResource: ResourceType? = null // 🔥 Должно быть строго заполнено!
) {
    EMPTY(false, 0xFF121212, Res.drawable.emptyTile),
    GRASS(true, 0xFF4CAF50, Res.drawable.grassTile),
    WALL_START(false, 0xFF795548, Res.drawable.stoneWallStartTile),
    WALL_END(false, 0xFF795548, Res.drawable.stoneWallEndTile),

    // 🔥 ИСПРАВЛЕНИЕ: Явно передаем типы ресурсов для каждого технического тайла
    HUMAN_TILE(true, 0xFF1E88E5, Res.drawable.stoneWallStartTile, ResourceType.HUMAN_RES),
    DOCTOR_TILE(true, 0xFF00E676, Res.drawable.stoneWallStartTile, ResourceType.DOCTOR_RES),
    HUNTSMAN_TILE(true, 0xFF8D6E63, Res.drawable.stoneWallStartTile, ResourceType.HUNTSMAN_RES),
    MECHANIC_TILE(true, 0xFFFFB300, Res.drawable.stoneWallStartTile, ResourceType.MECHANIC_RES),
    MINER_TILE(true, 0xFF757575, Res.drawable.stoneWallStartTile, ResourceType.MINER_RES),
    SHAMAN_TILE(true, 0xFF9C27B0, Res.drawable.stoneWallStartTile, ResourceType.SHAMAN_RES)
}
