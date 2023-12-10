package com.fwhyn.myapplication.domain.helper

sealed class Results<out RESULT, out ERROR> {
    data class Success<RESULT>(val data: RESULT) : Results<RESULT, Nothing>()

    data class Loading(val progress: Int) : Results<Nothing, Nothing>()

    data class Failure<ERROR>(val error: ERROR) : Results<Nothing, ERROR>()
}