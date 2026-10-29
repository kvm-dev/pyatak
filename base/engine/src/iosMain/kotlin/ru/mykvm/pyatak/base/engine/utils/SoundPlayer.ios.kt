package ru.mykvm.pyatak.base.engine.utils

import kotlinx.cinterop.ExperimentalForeignApi
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import org.jetbrains.compose.resources.ExperimentalResourceApi
import platform.AVFAudio.AVAudioPlayer
import platform.Foundation.NSURL
import ru.mykvm.ui.ComposeResources.Res
import ru.mykvm.pyatak.base.engine.enemy.EnemyType
import ru.mykvm.pyatak.base.engine.hero.HeroType
import ru.mykvm.pyatak.base.engine.model.GameSound

actual class SoundPlayer {
    private val scope = CoroutineScope(Dispatchers.Main)
    // Удерживаем ссылки на активные плееры, чтобы ARC не удалял их во время звучания
    private val activePlayers = mutableListOf<AVAudioPlayer>()

    @OptIn(ExperimentalResourceApi::class, ExperimentalForeignApi::class)
    actual fun playSound(sound: GameSound, heroType: HeroType?, enemyType: EnemyType?) {
        scope.launch {
            try {
                val path = getSoundResourcePath(sound, heroType, enemyType)
                val uriString = Res.getUri(path)

                val url = NSURL(string = uriString)
                val player = AVAudioPlayer(contentsOfURL = url, error = null)

                player.prepareToPlay()
                player.play()

                // Очищаем отыгравшие плееры и кэшируем текущий
                activePlayers.removeAll { !it.playing }
                activePlayers.add(player)
            } catch (e: Exception) {
                println("Ошибка звука iOS: ${e.message}")
            }
        }
    }
}