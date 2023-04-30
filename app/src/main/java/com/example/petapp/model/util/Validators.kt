package com.example.petapp.model.util

class Validators {
    companion object {
        fun validateNumberToTwoDecimalPlaces(value: String): String {
            var str = value.filter { (it.isDigit() || it == '.') }.replace("..", ".")
            str = if (str.count { it == '.' } > 1) str.dropLastWhile { it == '.' } else str
            return if (str.contains('.')) {
                var strBeforeDelimiter = str.substringBefore('.')
                if (strBeforeDelimiter.startsWith("00") && strBeforeDelimiter.length > 2) strBeforeDelimiter =
                    strBeforeDelimiter.substring(2)
                var strAfterDelimiter = str.substringAfter('.')
                if (strAfterDelimiter.length > 2) strAfterDelimiter = strAfterDelimiter.substring(0..1)
                "$strBeforeDelimiter.$strAfterDelimiter"
            } else if (str.startsWith("0") && str.length > 2) str.substring(1)
            else str
        }
    }
}