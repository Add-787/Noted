/**
 * Created by developer on 21-03-2025.
 * Tismo Technology Solutions (P) Ltd.
 * developers@tismotech.net
 */

package com.psyluckco.noted.utils

/**
 * A generic class that holds a loading signal or the result of an async operation.
 */
sealed class Async<out T> {

    data object Loading : Async<Nothing>()

    data class Error(val errorMessage: Int) : Async<Nothing>()

    data class Success<out T>(val data: T) : Async<T>()
}