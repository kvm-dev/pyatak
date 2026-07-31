package ru.mykvm.ui.strings

import ru.mykvm.language.api.model.CurrentLanguageDomain

//general
fun getGameName(lang: CurrentLanguageDomain): String {
    return when (lang) {
        CurrentLanguageDomain.RU -> "Пятак"
        CurrentLanguageDomain.EN -> "Pyatak"
    }
}

fun getGameSubTitle(lang: CurrentLanguageDomain): String {
    return when (lang) {
        CurrentLanguageDomain.RU -> "Хрюшачья угроза"
        CurrentLanguageDomain.EN -> "The pig menace"
    }
}
//errors
fun getErrorEndpointNotFound(lang: CurrentLanguageDomain): String {
    return when (lang) {
        CurrentLanguageDomain.RU -> "На сервере произошла ошибка, зайди попозже"
        CurrentLanguageDomain.EN -> "Internal server error, try again later"
    }
}

fun getErrorConnectionNotFoundTitle(lang: CurrentLanguageDomain): String {
    return when (lang) {
        CurrentLanguageDomain.RU -> "Отсутствует соединение"
        CurrentLanguageDomain.EN -> "Connection is unavailable"
    }
}

fun getErrorConnectionNotFoundDescription(lang: CurrentLanguageDomain): String {
    return when (lang) {
        CurrentLanguageDomain.RU -> "Проверьте подключение к сети интернет и повторите попытку"
        CurrentLanguageDomain.EN -> "Check your internet connection and try again later"
    }
}

//information

fun getUpdateDialogTitle(lang: CurrentLanguageDomain): String {
    return when (lang) {
        CurrentLanguageDomain.RU -> "У нас вышло обновление"
        CurrentLanguageDomain.EN -> "We have released an update"
    }
}

fun getUpdateDialogDescription(lang: CurrentLanguageDomain): String {
    return when (lang) {
        CurrentLanguageDomain.RU -> "Обнови игру и оцени новые возможности"
        CurrentLanguageDomain.EN -> "Update game and check new features"
    }
}



fun getCommentButtonText(lang: CurrentLanguageDomain): String {
    return when (lang) {
        CurrentLanguageDomain.RU -> "Комментарий"
        CurrentLanguageDomain.EN -> "Comment"
    }
}






//errorTexts
fun getErrorTextCodeIsAlreadySent() = "Code Is Already Sent"
fun getErrorTextUserIsUnconfirmed() = "User Is Unconfirmed"
fun getErrorTextUserIsNotFound() = "User Is Not Found"
fun getErrorTextUserIsAlreadyConfirmed() = "Email Is Not Found, Invalid Verification Code Or User Is Already Confirmed"
fun getErrorTextCodeIsIncorrect() = "Code Is Empty Or Incorrect"
fun getErrorTextCodeIsWrongOrExpired() = "Code Is Wrong Or Expired"
fun hardCreateUnknownError() = "Unknown"

//links
fun getChatLink() = "https://t.me/+r9DlqNi2Ymk0OGNi"

//other
fun getOkButton() = "Ok"



