package ru.mykvm.ui.utils

import ru.mykvm.ui.enemy.EnemyType
import ru.mykvm.ui.hero.HeroType
import ru.mykvm.ui.model.GameSound

expect class SoundPlayer() {
    fun playSound(sound: GameSound, heroType: HeroType? = null, enemyType: EnemyType? = null)
}

fun getSoundResourcePath(sound: GameSound, heroType: HeroType?, enemyType: EnemyType?): String {
    val fileName = when {
        heroType != null -> "${heroType.name.lowercase()}_${sound.name.lowercase()}"
        enemyType != null -> "${enemyType.name.lowercase()}_${sound.name.lowercase()}"
        else -> sound.name.lowercase()
    }
    return "files/sounds/$fileName.mp3"
}