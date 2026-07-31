package ru.mykvm.ui.utils

object EmailTextFieldValidation {
    fun validateEmail(email: String): Boolean {
        var result = false
        val emailAddressRegex = Regex(
            "[a-zA-Z0-9\\+\\.\\_\\%\\-\\+]{1,256}" +
                    "\\@" +
                    "[a-zA-Z0-9][a-zA-Z0-9\\-]{0,64}" +
                    "(" +
                    "\\." +
                    "[a-zA-Z0-9][a-zA-Z0-9\\-]{0,25}" +
                    ")+"
        )
        val regexCheck = email.matches(emailAddressRegex)
        if(regexCheck){
            val dogPartLength = email.split("@")[1].split(".")[0].length
            val afterDotLength = email.split(".")[1].length
            if(dogPartLength>=2 && afterDotLength>=2)
                result = true
        }
        return result
    }
}