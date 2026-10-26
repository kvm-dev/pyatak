package ru.mykvm.ui.enemy

import ru.mykvm.ui.ComposeResources.Res
import ru.mykvm.ui.ComposeResources.boss1Attack
import ru.mykvm.ui.ComposeResources.boss1Hurt
import ru.mykvm.ui.ComposeResources.boss1Idle
import ru.mykvm.ui.ComposeResources.boss1Jump
import ru.mykvm.ui.ComposeResources.boss1Special
import ru.mykvm.ui.ComposeResources.boss1Walk
import ru.mykvm.ui.ComposeResources.boss2Attack
import ru.mykvm.ui.ComposeResources.boss2Hurt
import ru.mykvm.ui.ComposeResources.boss2Idle
import ru.mykvm.ui.ComposeResources.boss2Jump
import ru.mykvm.ui.ComposeResources.boss2Special
import ru.mykvm.ui.ComposeResources.boss2Walk
import ru.mykvm.ui.ComposeResources.boss3Attack
import ru.mykvm.ui.ComposeResources.boss3Hurt
import ru.mykvm.ui.ComposeResources.boss3Idle
import ru.mykvm.ui.ComposeResources.boss3Jump
import ru.mykvm.ui.ComposeResources.boss3Resurrect
import ru.mykvm.ui.ComposeResources.boss3Special
import ru.mykvm.ui.ComposeResources.boss3SpecialAttack
import ru.mykvm.ui.ComposeResources.boss3Walk
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
        maxHealth = 3,
        animations = mapOf(
            EnemyAnimation.IDLE to AnimationConfig(Res.drawable.trooperIdle, 25, 5, 500, true),
            EnemyAnimation.WALK to AnimationConfig(Res.drawable.trooperWalk, 25, 5, 1000, true),
            EnemyAnimation.ATTACK to AnimationConfig(Res.drawable.trooperAttack, 25, 5, 800, false),
            EnemyAnimation.HURT to AnimationConfig(Res.drawable.trooperHurt, 25, 5, 1000, false)
        )
    ),
    ROBOT(
        displayName = "Хрюбот",
        maxHealth = 4,
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
        maxHealth = 2,
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
        maxHealth = 8,
        animations = mapOf(
            EnemyAnimation.IDLE to AnimationConfig(Res.drawable.boss1Idle, 25, 5, 500, true),
            EnemyAnimation.WALK to AnimationConfig(Res.drawable.boss1Walk, 25, 5, 1000, true),
            EnemyAnimation.ATTACK to AnimationConfig(Res.drawable.boss1Attack, 25, 5, 1000, false),
            EnemyAnimation.JUMP to AnimationConfig(Res.drawable.boss1Jump, 25, 5, 800, false),
            EnemyAnimation.HURT to AnimationConfig(Res.drawable.boss1Hurt, 25, 5, 1000, false),
            EnemyAnimation.SPECIAL to AnimationConfig(Res.drawable.boss1Special, 25, 5, 1500, true)
        )
    ),
    BOSS_ROBOT(
        displayName = "Хрякус (Босс)",
        vulnerableTo = listOf(HeroType.BOY_BOW, HeroType.GIRL_BOW),
        isRangeAttack = true,
        maxHealth = 9,
        animations = mapOf(
            EnemyAnimation.IDLE to AnimationConfig(Res.drawable.boss2Idle, 25, 5, 500, true),
            EnemyAnimation.WALK to AnimationConfig(Res.drawable.boss2Walk, 25, 5, 1000, true),
            EnemyAnimation.ATTACK to AnimationConfig(Res.drawable.boss2Attack, 25, 5, 1000, false),
            EnemyAnimation.JUMP to AnimationConfig(Res.drawable.boss2Jump, 25, 5, 800, false),
            EnemyAnimation.HURT to AnimationConfig(Res.drawable.boss2Hurt, 25, 5, 1000, false),
            EnemyAnimation.SPECIAL to AnimationConfig(Res.drawable.boss2Special, 25, 5, 1500, true)
        )
    ),
    BOSS_LORD_HREWNDEL(
        displayName = "Лорд Хрюндель (Босс)",
        vulnerableTo = listOf(HeroType.BOY_LIGHTSABER, HeroType.GIRL_LIGHTSABER),
        maxHealth = 12,
        animations = mapOf(
            EnemyAnimation.IDLE to AnimationConfig(Res.drawable.boss3Idle, 25, 5, 500, true),
            EnemyAnimation.WALK to AnimationConfig(Res.drawable.boss3Walk, 25, 5, 1000, true),
            EnemyAnimation.ATTACK to AnimationConfig(Res.drawable.boss3Attack, 25, 5, 1000, false),
            EnemyAnimation.JUMP to AnimationConfig(Res.drawable.boss3Jump, 25, 5, 800, false),
            EnemyAnimation.HURT to AnimationConfig(Res.drawable.boss3Hurt, 25, 5, 1000, false),
            EnemyAnimation.SPECIAL to AnimationConfig(Res.drawable.boss3Special, 25, 5, 1500, true),
            EnemyAnimation.SPECIAL_ATTACK to AnimationConfig(Res.drawable.boss3SpecialAttack, 25, 5, 1500, false),
            EnemyAnimation.RESURRECT to AnimationConfig(Res.drawable.boss3Resurrect, 25, 5, 7500, false)
            )
    )
}