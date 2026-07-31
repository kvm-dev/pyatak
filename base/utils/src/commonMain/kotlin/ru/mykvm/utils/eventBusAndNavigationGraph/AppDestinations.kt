package ru.mykvm.utils.eventBusAndNavigationGraph

import kotlinx.serialization.Serializable

@Serializable
sealed class AppDestinations{
    @Serializable
    data object Splash: AppDestinations()
    @Serializable
    data object Authorization: AppDestinations()

    @Serializable
    data object Main: AppDestinations()

    @Serializable
    data object BooksList: AppDestinations()

    @Serializable
    data class BooksInner(val bookId: Int = 0): AppDestinations()

    @Serializable
    data object EventsList: AppDestinations()

    @Serializable
    data class EventsInner(val eventId: Int = 0): AppDestinations()

    @Serializable
    data object InterviewList: AppDestinations()

    @Serializable
    data class InterviewInner(val materialId: Int = 0): AppDestinations()

    @Serializable
    data object NewsList: AppDestinations()

    @Serializable
    data class NewsInner(val newsId: Int = 0): AppDestinations()

    @Serializable
    data object Professions: AppDestinations()

    @Serializable
    data object Profile: AppDestinations()

    @Serializable
    data object Settings: AppDestinations()

    @Serializable
    data object StudyList: AppDestinations()

    @Serializable
    data object TestsList: AppDestinations()

    @Serializable
    data class TestInner(val testId: Int = 0): AppDestinations()
}

fun getLabel(destinations: AppDestinations, isLangRus: Boolean): String?{
    when(destinations){
        is AppDestinations.Main ->{
            return if(isLangRus) {
                "Главная"
            } else{
                "Main"
            }
        }
        is AppDestinations.InterviewList -> {
            return if(isLangRus) {
                "Интервью"
            } else{
                "Interview"
            }
        }
        is AppDestinations.NewsList -> {
            return if(isLangRus) {
                "Новости"
            } else{
                "News"
            }
        }
        is AppDestinations.TestsList -> {
            return if(isLangRus) {
                "Тесты"
            } else{
                "Tests"
            }
        }
        else -> return null
    }
}