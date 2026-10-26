package ru.mykvm.ui.levels.lostCity

import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import org.jetbrains.compose.resources.imageResource
import ru.mykvm.ui.ComposeResources.Res
import ru.mykvm.ui.ComposeResources.blueSky
import ru.mykvm.ui.ComposeResources.lostCityBackground
import ru.mykvm.ui.enemy.EnemyType
import ru.mykvm.ui.model.EnemyAnimation
import ru.mykvm.ui.model.TileType
import androidx.compose.ui.graphics.ImageBitmap

// Статичная карта игрового мира
val gameMap: List<List<TileType>> = listOf(
    listOf(2, 1, 1, 1, 1, 0, 1, 1, 1, 1, 0, 1, 1, 1, 1, 0, 1, 1, 0, 0, 1, 3)
).map { row ->
    row.map { id ->
        when (id) {
            1 -> TileType.GRASS
            2 -> TileType.WALL_START
            3 -> TileType.WALL_END
            else -> TileType.EMPTY
        }
    }
}

val MAP_COLUMNS = gameMap[0].size
const val TILE_DISPLAY_SIZE = 220f

//class LevelTextures(
//    val surface: androidx.compose.ui.graphics.ImageBitmap,
//    val wallStart: androidx.compose.ui.graphics.ImageBitmap,
//    val wallEnd: androidx.compose.ui.graphics.ImageBitmap,
//    val empty: androidx.compose.ui.graphics.ImageBitmap,
//    val sky: androidx.compose.ui.graphics.ImageBitmap,
//    val levelScene: androidx.compose.ui.graphics.ImageBitmap,
//    val enemyBitmaps: Map<EnemyType, androidx.compose.ui.graphics.ImageBitmap>
//) {
//    companion object {
//        @Composable
//        fun rememberTextures(): LevelTextures {
//            return LevelTextures(
//                // ИСПРАВЛЕНИЕ: Кэшируем ВСЕ три спрайт-листа персонажа сразу на старте!
//                surface = imageResource(TileType.GRASS.drawableRes!!),
//                wallStart = imageResource(TileType.WALL_START.drawableRes!!),
//                wallEnd = imageResource(TileType.WALL_END.drawableRes!!),
//                empty = imageResource(TileType.EMPTY.drawableRes!!),
//                sky = imageResource(Res.drawable.blueSky),
//                levelScene = imageResource(Res.drawable.lostCityBackground)
//            )
//        }
//    }
//}

class LevelTextures(
    val surface: ImageBitmap,
    val wallStart: ImageBitmap,
    val wallEnd: ImageBitmap,
    val empty: ImageBitmap,
    val sky: ImageBitmap,
    val levelScene: ImageBitmap,
    // Кэш для спрайт-листов врагов (тип врага -> карта его анимаций)
    val enemyBitmaps: Map<EnemyType, Map<EnemyAnimation, ImageBitmap>>
) {
    companion object {
        @Composable
        fun rememberTextures(enemiesInLevel: List<EnemyType>): LevelTextures {
            // 1. Извлекаем только уникальные типы врагов, чтобы не загружать лишнее
            val uniqueEnemyTypes = remember(enemiesInLevel) { enemiesInLevel.distinct() }

            // 2. Строим карту анимаций так, чтобы imageResource вызывался в явном Composable-контексте
            val cachedEnemies = mutableMapOf<EnemyType, Map<EnemyAnimation, ImageBitmap>>()

            for (type in uniqueEnemyTypes) {
                val animationsMap = mutableMapOf<EnemyAnimation, ImageBitmap>()
                for ((animation, config) in type.animations) {
                    // Здесь контекст @Composable сохранен, компилятор будет доволен
                    animationsMap[animation] = imageResource(config.drawable)
                }
                cachedEnemies[type] = animationsMap
            }

            // 3. Загружаем стандартное окружение и возвращаем готовый класс
            return LevelTextures(
                surface = imageResource(TileType.GRASS.drawableRes!!),
                wallStart = imageResource(TileType.WALL_START.drawableRes!!),
                wallEnd = imageResource(TileType.WALL_END.drawableRes!!),
                empty = imageResource(TileType.EMPTY.drawableRes!!),
                sky = imageResource(Res.drawable.blueSky),
                levelScene = imageResource(Res.drawable.lostCityBackground),
                enemyBitmaps = cachedEnemies
            )
        }
    }
}