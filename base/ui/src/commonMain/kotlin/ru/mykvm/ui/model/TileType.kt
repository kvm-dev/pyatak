package ru.mykvm.ui.model

enum class TileType(val isWalkable: Boolean, val colorHex: Long) {
    EMPTY(isWalkable = false, colorHex = 0xFF121212), // Пустота / пропасть
    GRASS(isWalkable = true, colorHex = 0xFF4CAF50), // Трава (можно ходить)
    WALL(isWalkable = false, colorHex = 0xFF795548)   // Стена (нельзя ходить)
}