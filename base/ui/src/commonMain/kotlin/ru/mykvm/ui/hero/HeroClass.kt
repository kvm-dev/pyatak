package ru.mykvm.ui.hero

import org.jetbrains.compose.resources.DrawableResource
import ru.mykvm.ui.ComposeResources.Res
import ru.mykvm.ui.ComposeResources.boyAttackSlingshot
import ru.mykvm.ui.ComposeResources.boyAvatar
import ru.mykvm.ui.ComposeResources.boyIdle
import ru.mykvm.ui.ComposeResources.boyJump
import ru.mykvm.ui.ComposeResources.boyWalk
import ru.mykvm.ui.ComposeResources.doctorAttack
import ru.mykvm.ui.ComposeResources.doctorAvatar
import ru.mykvm.ui.ComposeResources.doctorIdle
import ru.mykvm.ui.ComposeResources.doctorJump
import ru.mykvm.ui.ComposeResources.doctorWalk
import ru.mykvm.ui.ComposeResources.girlAttackSlingshot
import ru.mykvm.ui.ComposeResources.girlAvatar
import ru.mykvm.ui.ComposeResources.girlIdle
import ru.mykvm.ui.ComposeResources.girlJump
import ru.mykvm.ui.ComposeResources.girlWalk
import ru.mykvm.ui.ComposeResources.huntsmanAttack
import ru.mykvm.ui.ComposeResources.huntsmanAvatar
import ru.mykvm.ui.ComposeResources.huntsmanIdle
import ru.mykvm.ui.ComposeResources.huntsmanJump
import ru.mykvm.ui.ComposeResources.huntsmanWalk
import ru.mykvm.ui.ComposeResources.mechanicAttack
import ru.mykvm.ui.ComposeResources.mechanicAvatar
import ru.mykvm.ui.ComposeResources.mechanicIdle
import ru.mykvm.ui.ComposeResources.mechanicJump
import ru.mykvm.ui.ComposeResources.mechanicWalk
import ru.mykvm.ui.ComposeResources.minerAttack
import ru.mykvm.ui.ComposeResources.minerAvatar
import ru.mykvm.ui.ComposeResources.minerIdle
import ru.mykvm.ui.ComposeResources.minerJump
import ru.mykvm.ui.ComposeResources.minerWalk
import ru.mykvm.ui.ComposeResources.shamanAttack
import ru.mykvm.ui.ComposeResources.shamanAvatar
import ru.mykvm.ui.ComposeResources.shamanIdle
import ru.mykvm.ui.ComposeResources.shamanJump
import ru.mykvm.ui.ComposeResources.shamanWalk
import ru.mykvm.ui.model.AnimationConfig
import ru.mykvm.ui.model.CharacterAnimation

enum class HeroClass(
    val displayName: String,
    val icon: DrawableResource, // Ваша иконка 64x64
    val animations: Map<CharacterAnimation, AnimationConfig>
) {
    HUNTSMAN(
        displayName = "Охотник",
        icon = Res.drawable.huntsmanAvatar, // Замените на ваши ресурсы иконок
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
            ) // Или свой спрайт
        )
    ),
    BOY(
        displayName = "Мальчик",
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
            CharacterAnimation.JUMP to AnimationConfig(Res.drawable.boyJump, 25, 5, 1000, true)
        )
    ),
    GIRL(
        displayName = "Девочка",
        icon = Res.drawable.girlAvatar,
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
            CharacterAnimation.JUMP to AnimationConfig(Res.drawable.girlJump, 25, 5, 1000, true)
        )
    ),
    DOCTOR(
        displayName = "Доктор",
        icon = Res.drawable.doctorAvatar,
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
            CharacterAnimation.JUMP to AnimationConfig(Res.drawable.doctorJump, 25, 5, 1000, true)
        )
    ),
    MECHANIC(
        displayName = "Механик",
        icon = Res.drawable.mechanicAvatar,
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
            CharacterAnimation.JUMP to AnimationConfig(Res.drawable.mechanicJump, 25, 5, 1000, true)
        )
    ),
    MINER(
        displayName = "Шахтер",
        icon = Res.drawable.minerAvatar,
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
            CharacterAnimation.JUMP to AnimationConfig(Res.drawable.minerJump, 25, 5, 1000, true)
        )
    ),
    SHAMAN(
        displayName = "Шаман",
        icon = Res.drawable.shamanAvatar,
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
            CharacterAnimation.JUMP to AnimationConfig(Res.drawable.shamanJump, 25, 5, 1000, true)
        )
    ),
}