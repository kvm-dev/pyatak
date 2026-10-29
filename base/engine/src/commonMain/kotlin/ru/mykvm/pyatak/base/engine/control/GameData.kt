package ru.mykvm.pyatak.base.engine.control

import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import org.jetbrains.compose.resources.imageResource
import ru.mykvm.pyatak.base.engine.ComposeResources.Res
import ru.mykvm.pyatak.base.engine.ComposeResources.blueSky
import ru.mykvm.pyatak.base.engine.ComposeResources.lostCityBackground
import ru.mykvm.pyatak.base.engine.enemy.EnemyType
import ru.mykvm.pyatak.base.engine.model.EnemyAnimation
import ru.mykvm.pyatak.base.engine.model.TileType
import androidx.compose.ui.graphics.ImageBitmap
import kotlin.collections.iterator

const val TILE_DISPLAY_SIZE = 220f

class LevelTextures(
    val surface: ImageBitmap,
    val wallStart: ImageBitmap,
    val wallEnd: ImageBitmap,
    val minerTile: ImageBitmap,
    val huntsmanTile: ImageBitmap,
    val mechanicTile: ImageBitmap,
    val doctorTile: ImageBitmap,
    val humanTile: ImageBitmap,
    val shamanTile: ImageBitmap,
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
                surface = imageResource(TileType.SURFACE.drawableRes!!),
                wallStart = imageResource(TileType.WALL_START.drawableRes!!),
                wallEnd = imageResource(TileType.WALL_END.drawableRes!!),
                huntsmanTile = imageResource(TileType.HUNTSMAN_TILE.drawableRes!!),
                humanTile = imageResource(TileType.HUMAN_TILE.drawableRes!!),
                minerTile = imageResource(TileType.MINER_TILE.drawableRes!!),
                mechanicTile = imageResource(TileType.MECHANIC_TILE.drawableRes!!),
                doctorTile = imageResource(TileType.DOCTOR_TILE.drawableRes!!),
                shamanTile = imageResource(TileType.SHAMAN_TILE.drawableRes!!),
                empty = imageResource(TileType.EMPTY.drawableRes!!),
                sky = imageResource(Res.drawable.blueSky),
                levelScene = imageResource(Res.drawable.lostCityBackground),
                enemyBitmaps = cachedEnemies
            )
        }
    }
}