package ru.gb.dz12

sealed class State {
    data object Loading : State()
    data object Success : State()
    data class Error(
        val requestError: String?
    ) : State()
}