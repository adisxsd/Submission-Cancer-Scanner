package com.dicoding.asclepius.data

sealed class Results<out T> {
    data class Success<out T>(val data: T) : Results<T>()
    data class Error(val error: String) : Results<Nothing>()
    object Loading : Results<Nothing>()
}

