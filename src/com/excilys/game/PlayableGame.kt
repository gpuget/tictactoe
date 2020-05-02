package com.excilys.game

abstract class PlayableGame protected constructor(name: String, val players: List<Player> = emptyList()) : Game(name) {
    var winner: Player? = null

    override fun play() {
        resolve()
        winner = lastPlayer().takeIf { hasWinner() }
    }

    abstract fun resolve()
    abstract fun hasWinner(): Boolean
    abstract fun lastPlayer(): Player
}