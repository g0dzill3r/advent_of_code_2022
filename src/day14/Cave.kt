package day14

import kotlin.math.sign

enum class Detected (val symbol: Char, val isEmpty: Boolean) {
    AIR ('.', true),
    ROCK ('#', false),
    SAND ('o', false),
    SOURCE ('+', true)
}

data class Cave (val paths: List<List<Point>>) {
    var x0: Int = paths.fold<Point, Int> (null) { s, _, _, p -> if (s == null) p.x else Math.min (s, p.x) } as Int
    var x1: Int = paths.fold<Point, Int> (null) { s, _, _, p -> if (s == null) p.x else Math.max (s, p.x) } as Int
    var y0: Int = 0
    var y1: Int = paths.fold<Point, Int> (null) { s, _, _, p -> if (s == null) p.y else Math.max (s, p.y) } as Int
    var xw: Int = x1 - x0 + 1
    var yw: Int = y1 - y0 + 1
    val sand: Int = 500

    var grid: Array<Detected> = Array(xw * yw) { Detected.AIR }.apply {
        paths.forEach { path ->
            for (i in 0 until path.size - 1) {
                for (point in path[i] .. path[i + 1]) {
                    this[toIndex (point.x, point.y)] = Detected.ROCK
                }
            }
            this[toIndex (sand, 0)] = Detected.SOURCE
        }
    }

    fun addFloor () {
        y1 += 2
        yw += 2
        grid = Array<Detected> (xw * yw) { Detected.AIR }.apply {
            for (i in grid.indices) {
                this[i] = grid[i]
            }
            for (i in 0 until xw) {
                this[grid.size + xw + i] = Detected.ROCK
            }
        }
        return
    }

    /**
     * Expands the cave mapping either to the left (-1) or right (1).
     */

    fun grow (direction: Int, times: Int = 1) {
        repeat (times) {
            val cs0 = CoordinateSystem (xw, yw, x0, y0)
            if (direction < 0) {
                x0 -= 1
            } else {
                x1 += 1
            }
            xw += 1
            val cs1 = CoordinateSystem (xw, yw, x0, y0)
            grid = Array (xw * yw) { Detected.AIR }.apply {
                for (i in grid.indices) {
                    val p = cs0.fromIndex (i)
                    this[cs1.toIndex (p)] = grid[i]
                }
                if (direction < 0) {
                    this[cs1.toIndex (x0, y1)] = Detected.ROCK
                } else {
                    this[cs1.toIndex (x1, y1)] = Detected.ROCK
                }
            }
        }
        return
    }

    fun contains (point: Point): Boolean {
        val (x, y) = point
        return x in x0 .. x1 && y in y0 .. y1
    }

    fun newSand (): Point? {
        var curr = Point (sand, 0)

        outer@while (true) {
            if (! grid[toIndex (curr)].isEmpty) {
                return null
            }
            for (dx in listOf (0, -1, 1)) {
                val maybe = curr.move (dx, 1)
                if (! contains (maybe)) {
                    return null
                }
                if (grid[toIndex (maybe)].isEmpty) {
                    curr = maybe
                    continue@outer
                }
            }
            grid[toIndex (curr)] = Detected.SAND
            return curr
        }

        // NOT REACHED
    }

    fun newSand2  (): Point? {
        var curr = Point (sand, 0)

        outer@while (true) {
            if (! grid[toIndex (curr)].isEmpty) {
                return null
            }
            for (dx in listOf (0, -1, 1)) {
                val maybe = curr.move (dx, 1)
                if (! contains (maybe)) {
                    grow (dx)
                }
                if (grid[toIndex (maybe)].isEmpty) {
                    curr = maybe
                    continue@outer
                }
            }
            grid[toIndex (curr)] = Detected.SAND
            return curr
        }
        // NOT REACHED
    }

    fun toIndex (x: Int, y: Int): Int = (y - y0) * xw + (x - x0)
    fun toIndex (point: Point) = toIndex (point.x, point.y)

    fun dump () {
        println (toString ())
        return
    }

    override fun toString (): String {
        val xw = x1.toString().length
        val yw = y1.toString().length
        val buf = StringBuffer ()
        for (i in 0 until xw) {
            buf.append (" ".repeat (yw + 2))
            buf.append (x0.toString()[i])
            buf.append (" ".repeat (x1 - x0 - 1))
            buf.append (x1.toString()[i])
            buf.append ("\n")
        }
        for (y in y0 .. y1) {
            buf.append (String.format ("%${yw}d", y))
            buf.append (": ")
            for (x in x0 .. x1) {
                buf.append (grid[toIndex (x, y)].symbol)
            }
            buf.append ("\n")
        }
        return buf.toString ()
    }
}

fun main () {
    val example = true
    val cave = readCave (example)
    cave.dump ()
    cave.addFloor ()
    cave.dump ()
    cave.grow (-1, 5)
    cave.grow (1, 5)
    cave.dump ()
    return
}

// EOF