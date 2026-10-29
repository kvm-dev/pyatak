package ru.mykvm.pyatak.base.engine.levels.lostCity

import ru.mykvm.pyatak.base.engine.model.TileType

val lostCityLevel: List<List<TileType>> = listOf(
    listOf(2, 1, 1, 1, 1, 7, 4, 1, 9, 1, 0, 1, 1, 1, 1, 6, 1, 1, 4, 1, 1, 1, 7, 1, 1, 5, 1, 1, 7, 9, 1, 1, 8, 5, 4, 1, 1, 1, 1, 1, 0, 1, 1, 4, 1, 1, 1, 3)
).map { row ->
    row.map { id ->
        when (id) {
            1 -> TileType.SURFACE
            2 -> TileType.WALL_START
            3 -> TileType.WALL_END
            4 -> TileType.HUMAN_TILE
            5 -> TileType.DOCTOR_TILE
            6 -> TileType.HUNTSMAN_TILE
            7 -> TileType.MECHANIC_TILE
            8 -> TileType.MINER_TILE
            9 -> TileType.SHAMAN_TILE
            else -> TileType.EMPTY
        }
    }
}