package com.excilys.game

import java.util.regex.Pattern

class TicTacToeAction(val line: Int, val column: Int) : Action

class TicTacToeBoard : Board<TicTacToeAction>(3) {
    override fun play(player: Player, action: TicTacToeAction): Boolean {
        if (allow(action)) {
            positions[action.line][action.column] = player
            lastAction = action
            return true
        }

        println("Invalid input: [${action.line}, ${action.column}]")
        return false
    }

    private fun allow(action: TicTacToeAction): Boolean {
        if (action.line >= width || action.column >= length) {
            return false
        }
        val player = positions[action.line][action.column]
        return player == Player.empty()
    }

    override fun resolve(): Boolean = resolveLine() || resolveColumn() || resolveDiagonal()

    private fun resolveLine(): Boolean = positions.allLine(lastAction.line).all { it == playerOf(lastAction) }

    private fun resolveColumn(): Boolean = positions.allColumn(lastAction.column).all { it == playerOf(lastAction) }

    private fun resolveDiagonal(): Boolean {
        val left = mutableListOf<Player>()
        val right = mutableListOf<Player>()
        for (i in positions.indices) {
            if (hasLeft(lastAction.line, lastAction.column)) {
                left.add(positions[i][i])
            }

            if (hasRight(lastAction.line, lastAction.column)) {
                right.add(positions[i][transformed(i, width)])
            }
        }

        return left.takeIf { it.size > 1 }?.all { it == playerOf(lastAction) } ?: false || right.takeIf { it.size > 1 }
            ?.all { it == playerOf(lastAction) } ?: false
    }

    private fun hasLeft(line: Int, column: Int): Boolean = line == column
    private fun hasRight(line: Int, column: Int): Boolean = line == transformed(column, width)
    private fun transformed(i: Int, limit: Int): Int = (limit - 1 - i)

    private fun playerOf(action: TicTacToeAction): Player = positions[action.line][action.column]
}

class TicTacToe private constructor(name: String, players: List<Player>) : PlayableGame(name, players) {
    private val board = TicTacToeBoard()
    private var round = 0
    private var finished = false

    override fun start() {
        println("Start the game: $name")
        round++
    }

    override fun resolve() {
        var played: Boolean
        do {
            print("${currentPlayer()} is playing: ")
            val action = handleAction() {
                val first = Character.getNumericValue(it.first())
                val second = Character.getNumericValue(it.last())
                TicTacToeAction(first, second)
            }

            played = board.play(currentPlayer(), action)
        } while (!played)
        round++
    }

    private fun currentPlayer() = players[round % 2]

    override fun lastPlayer() = players[(round - 1) % 2]

    override fun hasWinner(): Boolean = board.resolve().also { finished = it }

    override fun end() {
        println("GAME OVER")
        board.display()
        when (val winner = winner) {
            null -> println("DRAW")
            else ->
                println("${winner.name} won the game !")
        }
    }

    override fun isStarted(): Boolean = round > 0

    override fun isFinished(): Boolean = round > board.size() || finished

    override fun display() {
        println("ROUND ${round}")
        board.display()
    }

    companion object {
        private const val REGEX = "\\d\\d"

        fun create(): TicTacToe {
            val players = listOf(Player("X"), Player("O"))
            return TicTacToe("Tic Tac Toe", players)
        }

        fun <T : Action> handleAction(extractor: (String) -> T): T {
            var input: String?
            do {
                input = readLine()
                if (input != null && validate(input)) {
                    return extractor.invoke(input)
                } else {
                    println("Invalid input: must be [$REGEX]")
                }
            } while (true)
        }

        private fun validate(input: String): Boolean = Pattern.matches(REGEX, input)
    }
}

fun main() {
    val game = TicTacToe.create()
    game.start()
    do {
        game.display()
        game.play()
    } while (game.isNotFinished())
    game.end()
}