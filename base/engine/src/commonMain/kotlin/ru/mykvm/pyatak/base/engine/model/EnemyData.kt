package ru.mykvm.pyatak.base.engine.model

import ru.mykvm.pyatak.base.engine.enemy.EnemyState
import ru.mykvm.pyatak.base.engine.enemy.EnemyType

data class EnemyData(
    val enemies: List<EnemyState> = listOf(),
    val enemyTextures: List<EnemyType> = listOf()
)