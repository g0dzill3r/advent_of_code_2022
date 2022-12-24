package day24

import readInput
import java.lang.IllegalArgumentException
import java.lang.IllegalStateException

enum class Direction (val dx: Int, val dy: Int, val encoded: Char) {
    UP (0, -1, '^'), LEFT (-1, 0, '<'), DOWN (0, 1, 'v'), RIGHT (1, 0, '>');

    val invert: Direction
        get () = values()[(ordinal + 2) % values().size]
}

data class Point (val x: Int, val y: Int) {
    fun add (other: Point): Point = Point (x + other.x, y + other.y)
    fun add (dx: Int, dy: Int): Point= Point (x + dx, y + dy)
    fun move (dir: Direction): Point = Point (x + dir.dx, y + dir.dy)
    fun move (dir: Direction, xwrap: Int, ywrap: Int): Point {
        var x = x + dir.dx
        if (x < 0) {
            if (x != -1) {
                throw Exception ()
            }
            x = xwrap - 1
        } else if (x == xwrap) {
            x = 0
        }
        var y = y + dir.dy
        if (y < 0) {
            if (y != -1) {
                throw Exception ()
            }
            y = ywrap - 1
        } else if (y == ywrap) {
            y = 0
        }
        return Point (x, y)
    }
}

class Valley (val width: Int, val height: Int, val start: Int, val end: Int, var init: MutableList<Pair<Point, Direction>>) {
    var blizzards = Array<MutableList<Direction>> (width * height) {
        mutableListOf<Direction> ()
    }
        private set
    fun toIndex (x: Int, y: Int): Int = x + y * width
    fun toIndex (point: Point): Int = toIndex (point.x, point.y)
    fun toPoint (index: Int): Point = Point (index % width, index / width)
    fun getBlizzards (x: Int, y: Int): MutableList<Direction> = blizzards[toIndex (x, y)] as MutableList<Direction>
    fun getBlizzards (point: Point): MutableList<Direction> = blizzards[toIndex (point)] as MutableList<Direction>
    var time = 0
    val startPoint = Point (start, -1)
    val endPoint = Point (end, height)
    var positions = mutableListOf<Point> ()

    fun isValid (p: Point): Boolean = p.x >= 0 && p.y >= 0 && p.x < width && p.y < height
    init {
        for (blizzard in init) {
            val (point, direction) = blizzard
            getBlizzards (point).add (direction)
        }
        positions.add (Point (start, -1))
    }


    /**
     * Returns all the possible moves from the specified point including
     * remaining stationary at that point.
     */
    fun possibleMoves (pos: Point): List<Point> {
        val list = mutableListOf<Point> (pos)
        list.add (pos.move (Direction.UP))
        list.add (pos.move (Direction.DOWN))
        list.add (pos.move (Direction.LEFT))
        list.add (pos.move (Direction.RIGHT))
        return list.filter {
            isValid (it) || (it == startPoint) || (it == endPoint)
        }
    }

    /**
     * Filters all the possible moves for those that are legal by
     * way of not being under threat from an adjacent blizzard.
     */

    fun legalMoves (possible: List<Point>): List<Point> {
        val legal = mutableListOf<Point> ()
        outer@for (point in possible) {
            if (point == startPoint || point == endPoint) {
                legal.add(point)
            } else {
                for (dir in Direction.values()) {
                    val threat = point.move (dir, width, height)
                    if (blizzards[toIndex (threat)].contains(dir.invert)) {
                        continue@outer
                    }
                }
                legal.add (point)
            }
        }
        return legal
    }

    fun round () {
        val newPositions = mutableListOf<Point> ()
        for (pos in positions) {
            val legal = legalMoves (possibleMoves (pos))
            for (move in legal) {
                if (! newPositions.contains (move)) {
                    newPositions.add (move)
                }
            }
        }
        tick ()
        positions = newPositions
        return
    }

    fun tick () {
        val temp = Array<MutableList<Direction>> (width * height) { mutableListOf<Direction> () }
        for (y in 0 until height) {
            for (x in 0 until width) {
                val point = Point (x, y)
                val dirs = getBlizzards (x, y)
                for (dir in dirs) {
                    val updated = point.move (dir, width, height)
                    temp[toIndex(updated)].add(dir)
                }
            }
        }
        blizzards = temp
        time++
        return
    }



    fun render (f: StringBuffer.(Point) -> Unit): String {
        val wall = '#'

        val ingress: StringBuffer.(Int, Boolean) -> Unit = { which, isStart ->
            append(wall)
            for (i in 0 until width) {
                if (i == which) {
                    val point = if (isStart) Point (start, -1) else Point (end, height)
                    f (point)
                } else {
                    append(wall)
                }
            }
            append(wall)
            append("\n")
        }

        return StringBuffer ().apply {
            append ("Valley dim=($width x $height), pos=${positions.size}, time=$time\n")
            ingress (start, true)
            for (y in 0 until height) {
                append ('#')
                for (x in 0 until width) {
                    val p = Point (x, y)
                    f (p)
                }
                append ("#\n")
            }
            ingress (end, false)
        }.toString ()
    }

    fun dump () = println (toString ())
    override fun toString(): String {
        return render { p ->
            if (positions.contains (p)) {
                append('E')
            } else if (p == startPoint || p == endPoint) {
                append ('.')
            } else {
                val blizzards = getBlizzards (p)

                append (when (blizzards.size) {
                    0 -> '.'
                    1 -> blizzards[0].encoded
                    in 2 .. 9 -> "${blizzards.size}"[0]
                    else -> 'B'
                })
            }
        }
    }

    companion object {
        fun parse (input: String): Valley {
            val strs = input.trim ().split ("\n")
            val width = strs.first ().length - 2
            val height = strs.size - 2
            val start = strs.first().indexOf ('.') - 1
            val end = strs.last().indexOf ('.')  - 1
            val blizzards = mutableListOf<Pair<Point, Direction>> ()
            for (i in 1 until strs.size - 1) {
                val y = i - 1
                val str = strs[i].substring (1, strs[i].length - 1)
                for (x in str.indices) {
                    val c = str[x]
                     if (c != '.') {
                        val dir = Direction.values().first { it.encoded == c }
                         blizzards.add (Pair (Point (x, y), dir))
                     }
                }
            }
            return Valley (width, height, start, end, blizzards)
        }
    }
}

fun loadValley (example: Boolean): Valley {
    val input = readInput (24, example)
    return Valley.parse (input)
}

fun main (args:Array<String>) {
    val example = true
    val valley = loadValley (example)
    valley.dump ()
    return
}