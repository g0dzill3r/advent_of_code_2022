package day12

import readInput

data class Terrain (
    val input: String,
) {
    private val rows: List<String> = input.trim ().split ("\n")
    private val raw: String = input.replace ("\n", "")
    private val xs: Int = rows[0].length
    private val ys: Int = rows.size
    private val dimensions: Pair<Int, Int> = Pair (xs, ys)
    private val grid: Array<Int> = Array<Int> (xs * ys) { 0 }
    val start: Int = raw.indexOf ('S')
    val startPoint: Point = toPoint (start)
    val end: Int = raw.indexOf ('E')
    val endPoint: Point = toPoint (end)
    fun toIndex (x: Int, y: Int) = y * rows.size + x
    fun toCoord (i: Int) = Pair (i % xs, i / xs)
    fun toPoint (i: Int) = Point (i % xs, i /xs)
    fun fromPoint (point: Point) = point.y * xs + point.x
    fun get (point: Point): Int = grid [fromPoint (point)]
    fun get (x: Int, y: Int): Int = grid [toIndex (x, y)]
    fun set (x: Int, y: Int, v: Int) {
        grid [toIndex (x, y)] = v
    }

    init {
        raw.forEachIndexed { i, c ->
            if (c in 'a' .. 'z') {
                grid [i] = c - 'a'
            } else if (c == 'S') {
                grid[i] = 'a' - 'a'
            } else if (c == 'E') {
                grid[i] = 'z' - 'a'
            } else {
                throw IllegalArgumentException ("Unrecognized terrain height: $c")
            }
        }
    }

    fun find (height: Int): List<Point> {
        val list = mutableListOf<Point>()
        visit { p, i ->
            if (i == height) {
                list.add (p)
            }
        }
        return list
    }

    fun visit (lambda: (Point, Int) -> Unit) {
        for (x in 0 until xs) {
            for (y in 0 until ys) {
                val point = Point (x, y)
                lambda (point, get (point))
            }
        }
    }

    fun dump () {
        val buf = StringBuffer ()
        grid.forEachIndexed { i, v ->
            if (i > 0 && i % xs == 0) {
                buf.append ("\n")
            }
            buf.append ('a'.plus (v))
        }
        buf.append ("\nstart=${toCoord (start)}")
        buf.append ("\nend=${toCoord (end)}")
        buf.append ("\nsize=($xs, $ys)")
        println (buf.toString ())
    }

    fun paths () = paths (toPoint (start), toPoint (end))

    fun contains (point: Point): Boolean {
        val (x, y) = point
        return x in 0 until xs && y in 0 until ys
    }

    fun gradient (from: Point, to: Point): Int {
        return get (to) - get (from)
    }

    /**
     * Test to see if we can move from the specified point in
     * the specified direction. This is only possible if we don't move
     * off of the grid and no more than 1 elevation higher
     */

    private fun canMove (point: Point, direction: Direction): Boolean {
        val move = point.move (direction)
        return contains (move) && gradient (point, move) <= 1
    }

    fun paths (from: Point, to: Point) = paths (from, to, mutableListOf (from), PathCache ())

    fun paths (from: Point, to: Point, acc: MutableList<Point>, cache: PathCache): List<List<Point>>? {
        val paths = mutableListOf<List<Point>> ()

        // If we're already at the destination, then we're done

        if (from == to) {
            paths.add (listOf (to))
            return paths
        }

        // Cache the path we took to get here and short-circuit if it isn't already longer
        // than the one previously discovered

        if (acc.size > 1) {
            val target = acc.last()
            val cached = cache.get (target)
            if (cached != null) {
                if (cached.size <= acc.size + 1) {
                    return paths
                }
            }
            cache.put (target, mutableListOf<Point> ().apply {
                addAll (acc)
            })
        }

        // Try all possible directions

        Direction.values ().forEach { direction ->
            if (canMove (from, direction)) {
                val target = from.move (direction)

                // Skip this possible move if we've already got the target in our current path

                if (acc.contains (target)) {
                    return@forEach
                }

                // Otherwise, assess this possible path

                val copy = mutableListOf<Point> ().apply {
                    addAll (acc)
                    add (target)
                }
                val res = paths (target, to, copy, cache)
                res?.forEach {
                    val path = mutableListOf<Point> ().apply {
                        addAll (it)
                    }
                    paths.add (path)
                }
            }
        }

        return if (paths.isEmpty ()) {
            null
        } else {
            val complete = mutableListOf<List<Point>> ().apply {
                paths.forEach {
                    add (mutableListOf<Point>().apply {
                        add (from)
                        addAll (it)
                    })
                }
            }
            complete
        }
    }
}

fun main (args: Array<String>) {
    val example = true
    val terrain = readTerrain (example)
    terrain.dump ()
    return
}

// EOF