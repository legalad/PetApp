package com.example.petapp.data

sealed class Result<out R> {
    data class Success<out T>(val data: T) : Result<T>()
    data class Warning<out T>(val data: T) : Result<T>()
    data class Error(val exception: Exception) : Result<Nothing>()

    override fun toString(): String {
        return when (this) {
            is Success<*> -> "Success[data=$data]"
            is Warning<*> -> "Warning[data=$data]"
            is Error -> "Error[exception=$exception]"
        }
    }
}

sealed class Async<out T> {
    object Loading : Async<Nothing>()

    data class Error(val errorMessage: String) : Async<Nothing>()

    data class Success<out T>(val data: T) : Async<T>()
}
