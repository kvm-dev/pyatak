package ru.mykvm.pyatak.base.engine.model

import org.jetbrains.compose.resources.DrawableResource
import ru.mykvm.pyatak.base.engine.ComposeResources.Res
import ru.mykvm.pyatak.base.engine.ComposeResources.doctorRes
import ru.mykvm.pyatak.base.engine.ComposeResources.emptyTile
import ru.mykvm.pyatak.base.engine.ComposeResources.grassTile
import ru.mykvm.pyatak.base.engine.ComposeResources.humanRes
import ru.mykvm.pyatak.base.engine.ComposeResources.huntsmanRes
import ru.mykvm.pyatak.base.engine.ComposeResources.mechanicRes
import ru.mykvm.pyatak.base.engine.ComposeResources.minerRes
import ru.mykvm.pyatak.base.engine.ComposeResources.shamanRes
import ru.mykvm.pyatak.base.engine.ComposeResources.stoneWallEndTile
import ru.mykvm.pyatak.base.engine.ComposeResources.stoneWallStartTile

enum class TileType(
    val isWalkable: Boolean,
    val colorHex: Long,
    val drawableRes: DrawableResource?,
    val requiredResource: ResourceType? = null
) {
    EMPTY(false, 0xFF121212, Res.drawable.emptyTile),
    SURFACE(true, 0xFF4CAF50, Res.drawable.grassTile),
    WALL_START(false, 0xFF795548, Res.drawable.stoneWallStartTile),
    WALL_END(false, 0xFF795548, Res.drawable.stoneWallEndTile),

    // 🔥 ИСПРАВЛЕНИЕ: Явно передаем типы ресурсов для каждого технического тайла
    HUMAN_TILE(true, 0xFF1E88E5, Res.drawable.humanRes, ResourceType.HUMAN_RES),
    DOCTOR_TILE(true, 0xFF00E676, Res.drawable.doctorRes, ResourceType.DOCTOR_RES),
    HUNTSMAN_TILE(true, 0xFF8D6E63, Res.drawable.huntsmanRes, ResourceType.HUNTSMAN_RES),
    MECHANIC_TILE(true, 0xFFFFB300, Res.drawable.mechanicRes, ResourceType.MECHANIC_RES),
    MINER_TILE(true, 0xFF757575, Res.drawable.minerRes, ResourceType.MINER_RES),
    SHAMAN_TILE(true, 0xFF9C27B0, Res.drawable.shamanRes, ResourceType.SHAMAN_RES)
}
