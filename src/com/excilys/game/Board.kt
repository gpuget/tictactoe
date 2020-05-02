package com.excilys.game

typealias Matrix<T> = Array<Array<T>>

abstract class Board<T : Action>(protected val length: Int, protected val width: Int = length) :
    Displayable {
    protected val positions: Matrix<Player> = Array(length) { Array(width) { Player.empty() } }
    protected lateinit var lastAction: T

    fun size() = length * width

    override fun display() = println(this)

    override fun toString(): String =
        positions.joinToString(LINE_SEPARATOR) { line -> line.joinToString(" | ", " ", " ") { it.name } }

    abstract fun play(player: Player, action: T): Boolean

    abstract fun resolve(): Boolean

    fun Matrix<Player>.allLine(index: Int): Array<Player> = positions[index]
    fun Matrix<Player>.allColumn(index: Int): Array<Player> = Array(width) { positions[it][index] }

    companion object {
        const val LINE_SEPARATOR = "\n---|---|---\n"
    }
}