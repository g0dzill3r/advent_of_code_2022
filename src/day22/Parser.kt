package day22

import readInput
import java.io.File
import java.lang.IllegalStateException
import java.util.regex.Pattern

enum class Direction {
    LEFT, RIGHT;

    companion object {
        fun parse (encoded: Char): Direction {
            return when (encoded) {
                'L' -> Direction.LEFT
                'R' -> Direction.RIGHT
                else -> throw IllegalArgumentException ("Found a: $encoded")
            }
        }
    }
}



enum class Facing (val value: Int, val render: Char) {
    LEFT(2, '<'), RIGHT(0, '>'), UP(3, '^'), DOWN(1, 'v');

    val invert: Facing
        get () = when (this) {
            LEFT -> RIGHT
            RIGHT -> LEFT
            UP -> DOWN
            DOWN -> UP
        }

    val left: Facing
        get () = turn (Direction.LEFT)

    val right: Facing
        get () = turn (Direction.RIGHT)

    fun turn (direction: Direction): Facing {
        when (direction) {
            Direction.LEFT -> {
                return when (this) {
                    LEFT -> DOWN
                    UP -> LEFT
                    RIGHT -> UP
                    DOWN -> RIGHT
                }
            }
            Direction.RIGHT -> {
                return when (this) {
                    LEFT -> UP
                    UP -> RIGHT
                    RIGHT -> DOWN
                    DOWN -> LEFT
                }
            }
        }
    }
    fun same (other: Facing): Boolean = same (this, other)

    companion object {
        val SAME = mutableMapOf<Pair<Facing, Facing>, Boolean>().apply {
            Facing.values().forEach {
                this[Pair(it, it)] = true
                this[Pair(it, it.invert)] = false
            }

            val set = { f1: Facing, f2: Facing, same: Boolean ->
                this[Pair(f1, f2)] = same
                this[Pair(f2, f1)] = same
            }
            set (UP, LEFT, false)
            set (UP, RIGHT, true)
            set (DOWN, LEFT, true)
            set (DOWN, RIGHT, false)
        }

        fun same (f1: Facing, f2: Facing): Boolean = SAME[Pair(f1, f2)] as Boolean
    }
}

enum class Tile (val encoded: Char) {
    WALL ('#'), FLOOR ('.')
}

data class Point (val x: Int, val y: Int) {
    fun add (other: Point): Point = Point (x + other.x, y + other.y)
    fun move (facing: Facing): Point = add (delta (facing))
    override fun toString(): String = "($x, $y)"

    companion object {
        fun delta (facing: Facing): Point {
            return when (facing) {
                Facing.UP -> Point (0, - 1)
                Facing.DOWN -> Point (0, 1)
                Facing.RIGHT -> Point (+ 1, 0)
                Facing.LEFT -> Point (- 1, 0)
            }
        }
    }
}

sealed class Step {
    data class Forward (val steps: Int) : Step () {
        override fun toString (): String = "$steps"
    }
    data class Turn (val direction: Direction) : Step () {
        override fun toString (): String = "$direction"
    }
}





fun loadSimple(): Board = Board.parse (File ("src/day22/day22-simple.txt").readText())

fun loadBoard (example: Boolean): Board {
    val input = readInput(22, example)
    val board = Board.parse (input)
    return board
}

fun main (args: Array<String>) {
    val example = true
    val board = loadBoard(example)

    board.dump ()

    return
}