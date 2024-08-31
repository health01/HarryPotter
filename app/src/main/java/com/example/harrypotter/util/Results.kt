package com.example.harrypotter.util

sealed class Results<T> {
    class Success<T>(val data: T) : Results<T>()
    class Error<T>(val message: String?) : Results<T>()
}
