package ru.mykvm.pyatak.base.engine.levels.lostCity

import ru.mykvm.pyatak.base.engine.enemy.EnemyState
import ru.mykvm.pyatak.base.engine.enemy.EnemyType
import ru.mykvm.pyatak.base.engine.model.EnemyData

val lostCityEnemies = EnemyData(
    enemies = listOf(
        EnemyState(EnemyType.PATROL, startX = 6),
        EnemyState(EnemyType.PATROL, startX = 14),
        EnemyState(EnemyType.STORMTROOPER, startX = 19),
        EnemyState(EnemyType.STORMTROOPER, startX = 22),
        EnemyState(EnemyType.STORMTROOPER, startX = 26),
        EnemyState(EnemyType.BOSS_ROBOT, startX = 29),
        EnemyState(EnemyType.PATROL, startX = 38),
        EnemyState(EnemyType.STORMTROOPER, startX = 42),
        EnemyState(EnemyType.BOSS_LORD_HREWNDEL, startX = 44)
    ),
    enemyTextures = listOf(
        EnemyType.PATROL, EnemyType.PATROL,
        EnemyType.STORMTROOPER, EnemyType.STORMTROOPER, EnemyType.STORMTROOPER,
        EnemyType.BOSS_ROBOT,
        EnemyType.PATROL,
        EnemyType.STORMTROOPER,
        EnemyType.BOSS_LORD_HREWNDEL
    )
)