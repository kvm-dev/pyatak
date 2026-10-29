package ru.mykvm.pyatak.base.engine.hero

import org.jetbrains.compose.resources.DrawableResource
import ru.mykvm.pyatak.base.engine.ComposeResources.Res
import ru.mykvm.pyatak.base.engine.ComposeResources.boyAttackEnergyBow
import ru.mykvm.pyatak.base.engine.ComposeResources.boyAttackLightSaber
import ru.mykvm.pyatak.base.engine.ComposeResources.boyAttackSlingshot
import ru.mykvm.pyatak.base.engine.ComposeResources.boyAttackSword
import ru.mykvm.pyatak.base.engine.ComposeResources.boyAvatar
import ru.mykvm.pyatak.base.engine.ComposeResources.boyExtract
import ru.mykvm.pyatak.base.engine.ComposeResources.boyIdle
import ru.mykvm.pyatak.base.engine.ComposeResources.boyJump
import ru.mykvm.pyatak.base.engine.ComposeResources.boyRefused
import ru.mykvm.pyatak.base.engine.ComposeResources.boyWalk
import ru.mykvm.pyatak.base.engine.ComposeResources.doctorAttack
import ru.mykvm.pyatak.base.engine.ComposeResources.doctorAvatar
import ru.mykvm.pyatak.base.engine.ComposeResources.doctorExtract
import ru.mykvm.pyatak.base.engine.ComposeResources.doctorIdle
import ru.mykvm.pyatak.base.engine.ComposeResources.doctorJump
import ru.mykvm.pyatak.base.engine.ComposeResources.doctorRefused
import ru.mykvm.pyatak.base.engine.ComposeResources.doctorWalk
import ru.mykvm.pyatak.base.engine.ComposeResources.girlAttackEnergyBow
import ru.mykvm.pyatak.base.engine.ComposeResources.girlAttackLightSaber
import ru.mykvm.pyatak.base.engine.ComposeResources.girlAttackSlingshot
import ru.mykvm.pyatak.base.engine.ComposeResources.girlAttackSword
import ru.mykvm.pyatak.base.engine.ComposeResources.girlAvatar
import ru.mykvm.pyatak.base.engine.ComposeResources.girlExtract
import ru.mykvm.pyatak.base.engine.ComposeResources.girlIdle
import ru.mykvm.pyatak.base.engine.ComposeResources.girlJump
import ru.mykvm.pyatak.base.engine.ComposeResources.girlRefused
import ru.mykvm.pyatak.base.engine.ComposeResources.girlWalk
import ru.mykvm.pyatak.base.engine.ComposeResources.huntsmanAttack
import ru.mykvm.pyatak.base.engine.ComposeResources.huntsmanAvatar
import ru.mykvm.pyatak.base.engine.ComposeResources.huntsmanExtract
import ru.mykvm.pyatak.base.engine.ComposeResources.huntsmanIdle
import ru.mykvm.pyatak.base.engine.ComposeResources.huntsmanJump
import ru.mykvm.pyatak.base.engine.ComposeResources.huntsmanRefused
import ru.mykvm.pyatak.base.engine.ComposeResources.huntsmanWalk
import ru.mykvm.pyatak.base.engine.ComposeResources.mechanicAttack
import ru.mykvm.pyatak.base.engine.ComposeResources.mechanicAvatar
import ru.mykvm.pyatak.base.engine.ComposeResources.mechanicExtract
import ru.mykvm.pyatak.base.engine.ComposeResources.mechanicIdle
import ru.mykvm.pyatak.base.engine.ComposeResources.mechanicJump
import ru.mykvm.pyatak.base.engine.ComposeResources.mechanicRefused
import ru.mykvm.pyatak.base.engine.ComposeResources.mechanicWalk
import ru.mykvm.pyatak.base.engine.ComposeResources.minerAttack
import ru.mykvm.pyatak.base.engine.ComposeResources.minerAvatar
import ru.mykvm.pyatak.base.engine.ComposeResources.minerExtract
import ru.mykvm.pyatak.base.engine.ComposeResources.minerIdle
import ru.mykvm.pyatak.base.engine.ComposeResources.minerJump
import ru.mykvm.pyatak.base.engine.ComposeResources.minerRefused
import ru.mykvm.pyatak.base.engine.ComposeResources.minerWalk
import ru.mykvm.pyatak.base.engine.ComposeResources.shamanAttack
import ru.mykvm.pyatak.base.engine.ComposeResources.shamanAvatar
import ru.mykvm.pyatak.base.engine.ComposeResources.shamanExtract
import ru.mykvm.pyatak.base.engine.ComposeResources.shamanIdle
import ru.mykvm.pyatak.base.engine.ComposeResources.shamanJump
import ru.mykvm.pyatak.base.engine.ComposeResources.shamanRefused
import ru.mykvm.pyatak.base.engine.ComposeResources.shamanWalk
import ru.mykvm.pyatak.base.engine.model.AnimationConfig
import ru.mykvm.pyatak.base.engine.model.CharacterAnimation
import ru.mykvm.pyatak.base.engine.model.ResourceType

enum class HeroType(
    val displayName: String,
    val icon: DrawableResource, // Ваша иконка 64x64
    val animations: Map<CharacterAnimation, AnimationConfig>,
    val isRangeAttack: Boolean = false,
    val health: Int = 5,
    val stamina: Int = 8,
    val extractableResource: ResourceType?
) {
    HUNTSMAN(
        displayName = "Охотник",
        icon = Res.drawable.huntsmanAvatar, // Замените на ваши ресурсы иконок
        isRangeAttack = true,
        health = HeroStats.huntsmanHealth,
        stamina = HeroStats.huntsmanStamina,
        extractableResource = ResourceType.HUNTSMAN_RES,
        animations = mapOf(
            CharacterAnimation.IDLE to AnimationConfig(Res.drawable.huntsmanIdle, 25, 5, 500, true),
            CharacterAnimation.WALK to AnimationConfig(
                Res.drawable.huntsmanWalk,
                25,
                5,
                1000,
                true
            ),
            CharacterAnimation.ATTACK to AnimationConfig(
                Res.drawable.huntsmanAttack,
                25,
                5,
                1000,
                false
            ),
            CharacterAnimation.JUMP to AnimationConfig(
                Res.drawable.huntsmanJump,
                25,
                5,
                1000,
                true
            ),
            CharacterAnimation.EXTRACT to AnimationConfig(Res.drawable.huntsmanExtract, 25, 5, 1200, false),
            CharacterAnimation.REFUSE to AnimationConfig(Res.drawable.huntsmanRefused, 25, 5, 600, false)
        )
    ),
    BOY_SLINGSHOT(
        displayName = "Мальчик",
        isRangeAttack = true,
        extractableResource = ResourceType.HUMAN_RES,
        icon = Res.drawable.boyAvatar,
        health = HeroStats.humanPlayerHealth,
        stamina = HeroStats.humanPlayerStamina,
        animations = mapOf(
            CharacterAnimation.IDLE to AnimationConfig(Res.drawable.boyIdle, 25, 5, 500, true),
            CharacterAnimation.WALK to AnimationConfig(Res.drawable.boyWalk, 25, 5, 1000, true),
            CharacterAnimation.ATTACK to AnimationConfig(
                Res.drawable.boyAttackSlingshot,
                25,
                5,
                1000,
                false
            ),
            CharacterAnimation.JUMP to AnimationConfig(Res.drawable.boyJump, 25, 5, 1000, true),
            CharacterAnimation.EXTRACT to AnimationConfig(Res.drawable.boyExtract, 25, 5, 1200, false),
            CharacterAnimation.REFUSE to AnimationConfig(Res.drawable.boyRefused, 25, 5, 600, false)
        )
    ),
    BOY_SWORD(
        displayName = "Мальчик",
        icon = Res.drawable.boyAvatar,
        extractableResource = ResourceType.HUMAN_RES,
        health = HeroStats.humanPlayerHealth,
        stamina = HeroStats.humanPlayerStamina,
        animations = mapOf(
            CharacterAnimation.IDLE to AnimationConfig(Res.drawable.boyIdle, 25, 5, 500, true),
            CharacterAnimation.WALK to AnimationConfig(Res.drawable.boyWalk, 25, 5, 1000, true),
            CharacterAnimation.ATTACK to AnimationConfig(
                Res.drawable.boyAttackSword,
                25,
                5,
                1000,
                false
            ),
            CharacterAnimation.JUMP to AnimationConfig(Res.drawable.boyJump, 25, 5, 1000, true),
            CharacterAnimation.EXTRACT to AnimationConfig(Res.drawable.boyExtract, 25, 5, 1200, false),
            CharacterAnimation.REFUSE to AnimationConfig(Res.drawable.boyRefused, 25, 5, 600, false)
        )
    ),
    BOY_BOW(
        displayName = "Мальчик",
        isRangeAttack = true,
        icon = Res.drawable.boyAvatar,
        extractableResource = ResourceType.HUMAN_RES,
        health = HeroStats.humanPlayerHealth,
        stamina = HeroStats.humanPlayerStamina,
        animations = mapOf(
            CharacterAnimation.IDLE to AnimationConfig(Res.drawable.boyIdle, 25, 5, 500, true),
            CharacterAnimation.WALK to AnimationConfig(Res.drawable.boyWalk, 25, 5, 1000, true),
            CharacterAnimation.ATTACK to AnimationConfig(
                Res.drawable.boyAttackEnergyBow,
                25,
                5,
                1000,
                false
            ),
            CharacterAnimation.JUMP to AnimationConfig(Res.drawable.boyJump, 25, 5, 1000, true),
            CharacterAnimation.EXTRACT to AnimationConfig(Res.drawable.boyExtract, 25, 5, 1200, false),
            CharacterAnimation.REFUSE to AnimationConfig(Res.drawable.boyRefused, 25, 5, 600, false)
        )
    ),
    BOY_LIGHTSABER(
        displayName = "Мальчик",
        icon = Res.drawable.boyAvatar,
        extractableResource = ResourceType.HUMAN_RES,
        health = HeroStats.humanPlayerHealth,
        stamina = HeroStats.humanPlayerStamina,
        animations = mapOf(
            CharacterAnimation.IDLE to AnimationConfig(Res.drawable.boyIdle, 25, 5, 500, true),
            CharacterAnimation.WALK to AnimationConfig(Res.drawable.boyWalk, 25, 5, 1000, true),
            CharacterAnimation.ATTACK to AnimationConfig(
                Res.drawable.boyAttackLightSaber,
                25,
                5,
                1000,
                false
            ),
            CharacterAnimation.JUMP to AnimationConfig(Res.drawable.boyJump, 25, 5, 1000, true),
            CharacterAnimation.EXTRACT to AnimationConfig(Res.drawable.boyExtract, 25, 5, 1200, false),
            CharacterAnimation.REFUSE to AnimationConfig(Res.drawable.boyRefused, 25, 5, 600, false)
        )
    ),
    GIRL_SLINGSHOT(
        displayName = "Девочка",
        isRangeAttack = true,
        icon = Res.drawable.girlAvatar,
        extractableResource = ResourceType.HUMAN_RES,
        health = HeroStats.humanPlayerHealth,
        stamina = HeroStats.humanPlayerStamina,
        animations = mapOf(
            CharacterAnimation.IDLE to AnimationConfig(Res.drawable.girlIdle, 25, 5, 500, true),
            CharacterAnimation.WALK to AnimationConfig(Res.drawable.girlWalk, 25, 5, 1000, true),
            CharacterAnimation.ATTACK to AnimationConfig(
                Res.drawable.girlAttackSlingshot,
                25,
                5,
                1000,
                false
            ),
            CharacterAnimation.JUMP to AnimationConfig(Res.drawable.girlJump, 25, 5, 1000, true),
            CharacterAnimation.EXTRACT to AnimationConfig(Res.drawable.girlExtract, 25, 5, 1200, false),
            CharacterAnimation.REFUSE to AnimationConfig(Res.drawable.girlRefused, 25, 5, 600, false)
        )
    ),
    GIRL_SWORD(
        displayName = "Девочка",
        icon = Res.drawable.girlAvatar,
        extractableResource = ResourceType.HUMAN_RES,
        health = HeroStats.humanPlayerHealth,
        stamina = HeroStats.humanPlayerStamina,
        animations = mapOf(
            CharacterAnimation.IDLE to AnimationConfig(Res.drawable.girlIdle, 25, 5, 500, true),
            CharacterAnimation.WALK to AnimationConfig(Res.drawable.girlWalk, 25, 5, 1000, true),
            CharacterAnimation.ATTACK to AnimationConfig(
                Res.drawable.girlAttackSword,
                25,
                5,
                1000,
                false
            ),
            CharacterAnimation.JUMP to AnimationConfig(Res.drawable.girlJump, 25, 5, 1000, true),
            CharacterAnimation.EXTRACT to AnimationConfig(Res.drawable.girlExtract, 25, 5, 1200, false),
            CharacterAnimation.REFUSE to AnimationConfig(Res.drawable.girlRefused, 25, 5, 600, false)
        )
    ),
    GIRL_BOW(
        displayName = "Девочка",
        isRangeAttack = true,
        icon = Res.drawable.girlAvatar,
        extractableResource = ResourceType.HUMAN_RES,
        health = HeroStats.humanPlayerHealth,
        stamina = HeroStats.humanPlayerStamina,
        animations = mapOf(
            CharacterAnimation.IDLE to AnimationConfig(Res.drawable.girlIdle, 25, 5, 500, true),
            CharacterAnimation.WALK to AnimationConfig(Res.drawable.girlWalk, 25, 5, 1000, true),
            CharacterAnimation.ATTACK to AnimationConfig(
                Res.drawable.girlAttackEnergyBow,
                25,
                5,
                1000,
                false
            ),
            CharacterAnimation.JUMP to AnimationConfig(Res.drawable.girlJump, 25, 5, 1000, true),
            CharacterAnimation.EXTRACT to AnimationConfig(Res.drawable.girlExtract, 25, 5, 1200, false),
            CharacterAnimation.REFUSE to AnimationConfig(Res.drawable.girlRefused, 25, 5, 600, false)
        )
    ),
    GIRL_LIGHTSABER(
        displayName = "Девочка",
        icon = Res.drawable.girlAvatar,
        extractableResource = ResourceType.HUMAN_RES,
        health = HeroStats.humanPlayerHealth,
        stamina = HeroStats.humanPlayerStamina,
        animations = mapOf(
            CharacterAnimation.IDLE to AnimationConfig(Res.drawable.girlIdle, 25, 5, 500, true),
            CharacterAnimation.WALK to AnimationConfig(Res.drawable.girlWalk, 25, 5, 1000, true),
            CharacterAnimation.ATTACK to AnimationConfig(
                Res.drawable.girlAttackLightSaber,
                25,
                5,
                1000,
                false
            ),
            CharacterAnimation.JUMP to AnimationConfig(Res.drawable.girlJump, 25, 5, 1000, true),
            CharacterAnimation.EXTRACT to AnimationConfig(Res.drawable.girlExtract, 25, 5, 1200, false),
            CharacterAnimation.REFUSE to AnimationConfig(Res.drawable.girlRefused, 25, 5, 600, false)
        )
    ),
    DOCTOR(
        displayName = "Доктор",
        icon = Res.drawable.doctorAvatar,
        extractableResource = ResourceType.DOCTOR_RES,
        health = HeroStats.doctorHealth,
        stamina = HeroStats.doctorStamina,
        animations = mapOf(
            CharacterAnimation.IDLE to AnimationConfig(Res.drawable.doctorIdle, 25, 5, 500, true),
            CharacterAnimation.WALK to AnimationConfig(Res.drawable.doctorWalk, 25, 5, 1000, true),
            CharacterAnimation.ATTACK to AnimationConfig(
                Res.drawable.doctorAttack,
                25,
                5,
                1000,
                false
            ),
            CharacterAnimation.JUMP to AnimationConfig(Res.drawable.doctorJump, 25, 5, 1000, true),
            CharacterAnimation.EXTRACT to AnimationConfig(Res.drawable.doctorExtract, 25, 5, 1200, false),
            CharacterAnimation.REFUSE to AnimationConfig(Res.drawable.doctorRefused, 25, 5, 600, false)
        )
    ),
    MECHANIC(
        displayName = "Механик",
        icon = Res.drawable.mechanicAvatar,
        extractableResource = ResourceType.MECHANIC_RES,
        health = HeroStats.mechanicHealth,
        stamina = HeroStats.mechanicStamina,
        animations = mapOf(
            CharacterAnimation.IDLE to AnimationConfig(Res.drawable.mechanicIdle, 25, 5, 500, true),
            CharacterAnimation.WALK to AnimationConfig(
                Res.drawable.mechanicWalk,
                25,
                5,
                1000,
                true
            ),
            CharacterAnimation.ATTACK to AnimationConfig(
                Res.drawable.mechanicAttack,
                25,
                5,
                1000,
                false
            ),
            CharacterAnimation.JUMP to AnimationConfig(Res.drawable.mechanicJump, 25, 5, 1000, true),
            CharacterAnimation.EXTRACT to AnimationConfig(Res.drawable.mechanicExtract, 25, 5, 1200, false),
            CharacterAnimation.REFUSE to AnimationConfig(Res.drawable.mechanicRefused, 25, 5, 600, false)
        )
    ),
    MINER(
        displayName = "Шахтер",
        icon = Res.drawable.minerAvatar,
        extractableResource = ResourceType.MINER_RES,
        health = HeroStats.minerHealth,
        stamina = HeroStats.minerStamina,
        animations = mapOf(
            CharacterAnimation.IDLE to AnimationConfig(Res.drawable.minerIdle, 25, 5, 500, true),
            CharacterAnimation.WALK to AnimationConfig(Res.drawable.minerWalk, 25, 5, 1000, true),
            CharacterAnimation.ATTACK to AnimationConfig(
                Res.drawable.minerAttack,
                25,
                5,
                1000,
                false
            ),
            CharacterAnimation.JUMP to AnimationConfig(Res.drawable.minerJump, 25, 5, 1000, true),
            CharacterAnimation.EXTRACT to AnimationConfig(Res.drawable.minerExtract, 25, 5, 1200, false),
            CharacterAnimation.REFUSE to AnimationConfig(Res.drawable.minerRefused, 25, 5, 600, false)
        )
    ),
    SHAMAN(
        displayName = "Шаман",
        isRangeAttack = true,
        icon = Res.drawable.shamanAvatar,
        extractableResource = ResourceType.SHAMAN_RES,
        health = HeroStats.shamanHealth,
        stamina = HeroStats.shamanStamina,
        animations = mapOf(
            CharacterAnimation.IDLE to AnimationConfig(Res.drawable.shamanIdle, 25, 5, 500, true),
            CharacterAnimation.WALK to AnimationConfig(Res.drawable.shamanWalk, 25, 5, 1000, true),
            CharacterAnimation.ATTACK to AnimationConfig(
                Res.drawable.shamanAttack,
                25,
                5,
                1000,
                false
            ),
            CharacterAnimation.JUMP to AnimationConfig(Res.drawable.shamanJump, 25, 5, 1000, true),
            CharacterAnimation.EXTRACT to AnimationConfig(Res.drawable.shamanExtract, 25, 5, 1200, false),
            CharacterAnimation.REFUSE to AnimationConfig(Res.drawable.shamanRefused, 25, 5, 600, false)
        )
    ),
}