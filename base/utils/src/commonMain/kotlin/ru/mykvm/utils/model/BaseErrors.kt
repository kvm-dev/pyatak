package ru.mykvm.utils.model

enum class BaseErrors {
    EXPECTATION_HEADERS,
    UNAUTHORIZED,
    TOKEN_EXPIRED,
    USER_IS_BLOCKED,
    UNKNOWN_ERROR,

    LOCAL_DATABASE_ERROR
}