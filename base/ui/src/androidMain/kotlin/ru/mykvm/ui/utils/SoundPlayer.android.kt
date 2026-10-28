package ru.mykvm.ui.utils

import android.content.Context
import android.content.res.AssetFileDescriptor
import android.media.MediaPlayer
import android.net.Uri
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import org.jetbrains.compose.resources.ExperimentalResourceApi
import ru.mykvm.ui.ComposeResources.Res
import ru.mykvm.ui.hero.HeroType
import ru.mykvm.ui.model.GameSound
import androidx.core.net.toUri
import kotlinx.coroutines.withContext
import org.koin.core.component.KoinComponent
import org.koin.core.component.inject
import ru.mykvm.ui.enemy.EnemyType

actual class SoundPlayer : KoinComponent {

    // Главный скоуп для контроля жизненного цикла аудио-корутин
    private val scope = CoroutineScope(Dispatchers.Main)

    // Извлекаем Android Context напрямую из DI-контейнера Koin
    private val context: Context by inject()

    // Хранилище активных аудиосессий для защиты плееров от преждевременной очистки сборщиком мусора
    private val activeSessions = mutableListOf<AudioSession>()

    @OptIn(ExperimentalResourceApi::class)
    actual fun playSound(sound: GameSound, heroType: HeroType?, enemyType: EnemyType?) {
        scope.launch {
            var assetFileDescriptor: AssetFileDescriptor? = null
            var mediaPlayer: MediaPlayer? = null

            try {
                // 1. Формируем относительный путь к файлу в общих ресурсах
                val rawPath = getSoundResourcePath(sound, heroType, enemyType)

                // 2. Указываем точный физический путь внутри ассетов собранного APK
                val androidAssetPath = "composeResources/ru.mykvm.ui.ComposeResources/$rawPath"

                // 3. Переключаемся на поток ввода-вывода (IO) для тяжелого чтения файла с диска
                assetFileDescriptor = withContext(Dispatchers.IO) {
                    context.assets.openFd(androidAssetPath)
                }

                // 4. Создаем экземпляр MediaPlayer и задаем ему низкоуровневый источник данных
                mediaPlayer = MediaPlayer().apply {
                    setDataSource(
                        assetFileDescriptor!!.fileDescriptor,
                        assetFileDescriptor!!.startOffset,
                        assetFileDescriptor!!.length
                    )

                    // Операцию парсинга аудио-заголовков и подготовки также уводим на IO-поток
                    withContext(Dispatchers.IO) {
                        prepare()
                    }
                }

                // 5. Упаковываем компоненты в сессию и сохраняем сильную ссылку в список класса
                val session = AudioSession(mediaPlayer, assetFileDescriptor!!)
                activeSessions.add(session)

                // 6. Запускаем воспроизведение звука на системном аудио-микшере
                mediaPlayer.start()

                // 7. Мягкая отложенная очистка ресурсов после завершения трека
                mediaPlayer.setOnCompletionListener { mp ->
                    scope.launch {
                        // Небольшая задержка, чтобы нативный плеер Android успел обработать внутренние системные события
                        kotlinx.coroutines.delay(50)

                        try {
                            mp.stop() // Принудительно переводим плеер в стабильное состояние остановки
                        } catch (e: Exception) {
                            // Игнорируем ошибку, если плеер уже успел остановиться самостоятельно
                        }

                        // Освобождаем нативные ресурсы плеера
                        mp.release()

                        // Закрываем файловый дескриптор ассета
                        try {
                            assetFileDescriptor.close()
                        } catch (e: Exception) {
                            e.printStackTrace()
                        }

                        // Только после полной очистки удаляем сессию из списка активных для освобождения памяти
                        activeSessions.remove(session)
                    }
                }

            } catch (e: Exception) {
                println("Ошибка воспроизведения звука на Android: ${e.message}")
                e.printStackTrace()

                // Безопасный экстренный сброс ресурсов в случае сбоя при инициализации
                mediaPlayer?.release()
                try {
                    assetFileDescriptor?.close()
                } catch (internalEx: Exception) {
                    internalEx.printStackTrace()
                }
            }
        }
    }

    // Вспомогательный класс для удержания ссылок на компоненты играющей аудиосессии
    private data class AudioSession(
        val player: MediaPlayer,
        val descriptor: AssetFileDescriptor
    )
}