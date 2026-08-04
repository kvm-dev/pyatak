package ru.mykvm.ui.model

import org.jetbrains.compose.resources.DrawableResource
import ru.mykvm.ui.ComposeResources.Res
import ru.mykvm.ui.ComposeResources.emptyTile
import ru.mykvm.ui.ComposeResources.grassTile
import ru.mykvm.ui.ComposeResources.stoneWallEndTile
import ru.mykvm.ui.ComposeResources.stoneWallStartTile

enum class TileType(val isWalkable: Boolean, val colorHex: Long, val drawableRes: DrawableResource?) {
    // Для пустоты картинка не нужна (остается фоновый цвет), либо можно сделать текстуру "дна"
    EMPTY(isWalkable = false, colorHex = 0xFF121212, drawableRes = Res.drawable.emptyTile),
    GRASS(isWalkable = true, colorHex = 0xFF4CAF50, drawableRes = Res.drawable.grassTile),
    WALL_START(isWalkable = false, colorHex = 0xFF795548, drawableRes = Res.drawable.stoneWallStartTile),
    WALL_END(isWalkable = false, colorHex = 0xFF795548, drawableRes = Res.drawable.stoneWallEndTile)
}