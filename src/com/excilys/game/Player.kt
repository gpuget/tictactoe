package com.excilys.game

class Player(val name: String) : Displayable {
    override fun display() = println(this)

    override fun toString(): String = name

    companion object {
        private val EMPTY = Player(" ")
        fun empty() = EMPTY
    }
}