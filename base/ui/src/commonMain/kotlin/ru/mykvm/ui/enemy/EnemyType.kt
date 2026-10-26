package ru.mykvm.ui.enemy

import ru.mykvm.ui.ComposeResources.Res
import ru.mykvm.ui.ComposeResources.girlAttackLightSaber
import ru.mykvm.ui.ComposeResources.girlAttackSword
import ru.mykvm.ui.ComposeResources.girlIdle
import ru.mykvm.ui.ComposeResources.girlWalk
import ru.mykvm.ui.ComposeResources.patrolAttack
import ru.mykvm.ui.ComposeResources.patrolHurt
import ru.mykvm.ui.ComposeResources.patrolIdle
import ru.mykvm.ui.ComposeResources.patrolWalk
import ru.mykvm.ui.ComposeResources.roboPigAttack
import ru.mykvm.ui.ComposeResources.roboPigHurt
import ru.mykvm.ui.ComposeResources.roboPigIdle
import ru.mykvm.ui.ComposeResources.roboPigWalk
import ru.mykvm.ui.ComposeResources.trooperAttack
import ru.mykvm.ui.ComposeResources.trooperHurt
import ru.mykvm.ui.ComposeResources.trooperIdle
import ru.mykvm.ui.ComposeResources.trooperWalk
import ru.mykvm.ui.hero.HeroType
import ru.mykvm.ui.model.AnimationConfig
import ru.mykvm.ui.model.EnemyAnimation

enum class EnemyType(
    val displayName: String,
    val maxHealth: Int,
    val vulnerableTo: List<HeroType>? = null,
    val isRangeAttack: Boolean = false,
    val animations: Map<EnemyAnimation, AnimationConfig>
) {
    STORMTROOPER(
        displayName = "Хрюшевик",
        maxHealth = 5,
        animations = mapOf(
            EnemyAnimation.IDLE to AnimationConfig(Res.drawable.trooperIdle, 25, 5, 500, true),
            EnemyAnimation.WALK to AnimationConfig(Res.drawable.trooperWalk, 25, 5, 1000, true),
            EnemyAnimation.ATTACK to AnimationConfig(Res.drawable.trooperAttack, 25, 5, 800, false),
            EnemyAnimation.HURT to AnimationConfig(Res.drawable.trooperHurt, 25, 5, 1000, false)
        )
    ),
    ROBOT(
        displayName = "Хрюбот",
        maxHealth = 8,
        isRangeAttack = true,
        animations = mapOf(
            EnemyAnimation.IDLE to AnimationConfig(Res.drawable.roboPigIdle, 25, 5, 600, true),
            EnemyAnimation.WALK to AnimationConfig(Res.drawable.roboPigWalk, 25, 5, 1200, true),
            EnemyAnimation.ATTACK to AnimationConfig(Res.drawable.roboPigAttack, 25, 5, 900, false),
            EnemyAnimation.HURT to AnimationConfig(Res.drawable.roboPigHurt, 25, 5, 1100, false)
        )
    ),
    PATROL(
        displayName = "Патрульный",
        maxHealth = 3,
        animations = mapOf(
            EnemyAnimation.IDLE to AnimationConfig(Res.drawable.patrolIdle, 25, 5, 600, true),
            EnemyAnimation.WALK to AnimationConfig(Res.drawable.patrolWalk, 25, 5, 1200, true),
            EnemyAnimation.ATTACK to AnimationConfig(Res.drawable.patrolAttack, 25, 5, 900, false),
            EnemyAnimation.HURT to AnimationConfig(Res.drawable.patrolHurt, 25, 5, 1100, false)
        )
    ),
    BOSS_KNIGHT(
        displayName = "Сэр Хрюндевальд (Босс)",
        vulnerableTo = listOf(HeroType.BOY_SWORD, HeroType.GIRL_SWORD),
        maxHealth = 20,
        animations = mapOf(
            EnemyAnimation.IDLE to AnimationConfig(Res.drawable.girlIdle, 25, 5, 500, true),
            EnemyAnimation.WALK to AnimationConfig(Res.drawable.girlWalk, 25, 5, 1000, true),
            EnemyAnimation.ATTACK to AnimationConfig(Res.drawable.girlAttackLightSaber, 25, 5, 1000, false),
            EnemyAnimation.SPECIAL to AnimationConfig(Res.drawable.girlAttackSword, 25, 5, 1500, false)
        )
    ),
    BOSS_ROBOT(
        displayName = "Свинобот (Босс)",
        vulnerableTo = listOf(HeroType.BOY_BOW, HeroType.GIRL_BOW),
        maxHealth = 20,
        animations = mapOf(
            EnemyAnimation.IDLE to AnimationConfig(Res.drawable.girlIdle, 25, 5, 500, true),
            EnemyAnimation.WALK to AnimationConfig(Res.drawable.girlWalk, 25, 5, 1000, true),
            EnemyAnimation.ATTACK to AnimationConfig(Res.drawable.girlAttackLightSaber, 25, 5, 1000, false),
            EnemyAnimation.SPECIAL to AnimationConfig(Res.drawable.girlAttackSword, 25, 5, 1500, false)
        )
    ),
    LORD_HRUNDEL(
        displayName = "Лорд Хрюндель (Босс)",
        vulnerableTo = listOf(HeroType.BOY_LIGHTSABER, HeroType.GIRL_LIGHTSABER),
        maxHealth = 20,
        animations = mapOf(
            EnemyAnimation.IDLE to AnimationConfig(Res.drawable.girlIdle, 25, 5, 500, true),
            EnemyAnimation.WALK to AnimationConfig(Res.drawable.girlWalk, 25, 5, 1000, true),
            EnemyAnimation.ATTACK to AnimationConfig(Res.drawable.girlAttackLightSaber, 25, 5, 1000, false),
            EnemyAnimation.SPECIAL to AnimationConfig(Res.drawable.girlAttackSword, 25, 5, 1500, false)
        )
    )
}