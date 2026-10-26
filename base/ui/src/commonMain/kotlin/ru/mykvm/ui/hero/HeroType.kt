package ru.mykvm.ui.hero

import org.jetbrains.compose.resources.DrawableResource
import ru.mykvm.ui.ComposeResources.Res
import ru.mykvm.ui.ComposeResources.boyAttackEnergyBow
import ru.mykvm.ui.ComposeResources.boyAttackLightSaber
import ru.mykvm.ui.ComposeResources.boyAttackSlingshot
import ru.mykvm.ui.ComposeResources.boyAttackSword
import ru.mykvm.ui.ComposeResources.boyAvatar
import ru.mykvm.ui.ComposeResources.boyExtract
import ru.mykvm.ui.ComposeResources.boyIdle
import ru.mykvm.ui.ComposeResources.boyJump
import ru.mykvm.ui.ComposeResources.boyRefused
import ru.mykvm.ui.ComposeResources.boyWalk
import ru.mykvm.ui.ComposeResources.doctorAttack
import ru.mykvm.ui.ComposeResources.doctorAvatar
import ru.mykvm.ui.ComposeResources.doctorExtract
import ru.mykvm.ui.ComposeResources.doctorIdle
import ru.mykvm.ui.ComposeResources.doctorJump
import ru.mykvm.ui.ComposeResources.doctorRefused
import ru.mykvm.ui.ComposeResources.doctorWalk
import ru.mykvm.ui.ComposeResources.girlAttackEnergyBow
import ru.mykvm.ui.ComposeResources.girlAttackLightSaber
import ru.mykvm.ui.ComposeResources.girlAttackSlingshot
import ru.mykvm.ui.ComposeResources.girlAttackSword
import ru.mykvm.ui.ComposeResources.girlAvatar
import ru.mykvm.ui.ComposeResources.girlExtract
import ru.mykvm.ui.ComposeResources.girlIdle
import ru.mykvm.ui.ComposeResources.girlJump
import ru.mykvm.ui.ComposeResources.girlRefused
import ru.mykvm.ui.ComposeResources.girlWalk
import ru.mykvm.ui.ComposeResources.huntsmanAttack
import ru.mykvm.ui.ComposeResources.huntsmanAvatar
import ru.mykvm.ui.ComposeResources.huntsmanExtract
import ru.mykvm.ui.ComposeResources.huntsmanIdle
import ru.mykvm.ui.ComposeResources.huntsmanJump
import ru.mykvm.ui.ComposeResources.huntsmanRefused
import ru.mykvm.ui.ComposeResources.huntsmanWalk
import ru.mykvm.ui.ComposeResources.mechanicAttack
import ru.mykvm.ui.ComposeResources.mechanicAvatar
import ru.mykvm.ui.ComposeResources.mechanicExtract
import ru.mykvm.ui.ComposeResources.mechanicIdle
import ru.mykvm.ui.ComposeResources.mechanicJump
import ru.mykvm.ui.ComposeResources.mechanicRefused
import ru.mykvm.ui.ComposeResources.mechanicWalk
import ru.mykvm.ui.ComposeResources.minerAttack
import ru.mykvm.ui.ComposeResources.minerAvatar
import ru.mykvm.ui.ComposeResources.minerExtract
import ru.mykvm.ui.ComposeResources.minerIdle
import ru.mykvm.ui.ComposeResources.minerJump
import ru.mykvm.ui.ComposeResources.minerRefused
import ru.mykvm.ui.ComposeResources.minerWalk
import ru.mykvm.ui.ComposeResources.shamanAttack
import ru.mykvm.ui.ComposeResources.shamanAvatar
import ru.mykvm.ui.ComposeResources.shamanExtract
import ru.mykvm.ui.ComposeResources.shamanIdle
import ru.mykvm.ui.ComposeResources.shamanJump
import ru.mykvm.ui.ComposeResources.shamanRefused
import ru.mykvm.ui.ComposeResources.shamanWalk
import ru.mykvm.ui.model.AnimationConfig
import ru.mykvm.ui.model.CharacterAnimation
import ru.mykvm.ui.model.ResourceType

enum class HeroType(
    val displayName: String,
    val icon: DrawableResource, // Ваша иконка 64x64
    val animations: Map<CharacterAnimation, AnimationConfig>,
    val isRangeAttack: Boolean = false,
    val extractableResource: ResourceType?
) {
    HUNTSMAN(
        displayName = "Охотник",
        icon = Res.drawable.huntsmanAvatar, // Замените на ваши ресурсы иконок
        isRangeAttack = true,
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