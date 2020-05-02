package com.excilys.game

abstract class Game(val name: String) : Displayable {
    abstract fun start()
    abstract fun play()
    abstract fun end()

    abstract fun isStarted(): Boolean
    abstract fun isFinished(): Boolean
    fun isNotFinished(): Boolean = isFinished().not()
}