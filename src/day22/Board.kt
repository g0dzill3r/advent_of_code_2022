package day22

import java.lang.IllegalStateException
import java.util.regex.Pattern

open class Board (val grid: MutableMap<Point, Tile>, val steps: List<Step>) {
    val xs = grid.keys.fold (0) { acc, point -> Math.max (acc, point.x + 1) }
    val ys = grid.keys.fold (0) { acc, point -> Math.max (acc, point.y + 1) }
    var pos = start
    var facing = Facing.RIGHT

    fun step (step: Step) {
        when (step) {
            is Step.Forward -> {
                repeat (step.steps) {
                    forward ()
                }
            }
            is Step.Turn -> {
                turn (step.direction)
            }
        }
        return
    }

    fun turn (direction: Direction) {
        facing = facing.turn (direction)
        return
    }

    fun forward () {
        val next = nextPoint
        val tile = grid[next]
        if (tile == Tile.FLOOR) {
            pos = next
        }
        return
    }

    open val nextPoint: Point
        get () {
            val newPoint = pos.move (facing)
            if (grid.containsKey (newPoint)) {
                return newPoint
            }
            var start = when (facing) {
                Facing.UP -> Point (pos.x, ys)
                Facing.RIGHT -> Point (0, pos.y)
                Facing.DOWN -> Point (pos.x, 0)
                Facing.LEFT -> Point (xs, pos.y)
            }
            val delta = Point.delta (facing)
            while (! grid.containsKey (start)) {
                start = start.add (delta)
            }
            return start
        }
    val password: Int
        get () {
            val row = pos.y + 1
            val col = pos.x + 1
            return 1000 * row + 4 * col + facing.value
        }
    val start: Point
        get () {
            for (x in 0 until xs) {
                val point = Point (x, 0)
                grid[point]?.let {
                    if (it == Tile.FLOOR) {
                        return point
                    }
                }
            }
            throw IllegalStateException ()
        }

    fun dump () = println (toString ())
    override fun toString (): String {
        val that = this
        return StringBuffer ().apply {
            append ("${that::class.simpleName} dim=($xs x $ys), start=$start, pos=$pos, facing=$facing\n")
            for (y in 0 until ys) {
                for (x in 0 until xs) {
                    val point = Point (x, y)
                    if (point == pos) {
                        append (facing.render)
                    } else {
                        val tile = grid.get(point)
                        if (tile != null) {
                            append(tile.encoded)
                        } else {
                            append(' ')
                        }
                    }
                }
                append ("\n")
            }
            append ("\n")
            append (steps.joinToString (" "))
        }.toString ()
    }
    companion object {
        private val REGEX = "\\d+|\\D+"
        private val PATTERN = Pattern.compile (REGEX)
        fun parse (input: String): Board {
            val parts = input.split ("\n\n")

            // Parse the grid

            val grid = hashMapOf<Point, Tile> ()
            parts[0].split ("\n").forEachIndexed { y, row ->
                row.forEachIndexed { x, c ->
                    val point = Point (x, y)
                    if (c == '.') {
                        grid.put (point, Tile.FLOOR)
                    } else if (c == '#') {
                        grid.put (point, Tile.WALL)
                    } else if (c == ' ') {
                        // IGNORED
                    } else {
                        throw IllegalArgumentException ()
                    }
                }
            }

            // Parse the steps

            val steps = mutableListOf<Step> ().apply {
                val matcher = PATTERN.matcher (parts[1].trim ())
                while (matcher.find ()) {
                    val str = matcher.group ()
                    val step = if (str.isNumeric) {
                        Step.Forward (str.toInt ())
                    } else {
                        Step.Turn (Direction.parse (str[0]))
                    }
                    add (step)
                }
            }
            return Board (grid, steps)
        }
    }
}

// EOF