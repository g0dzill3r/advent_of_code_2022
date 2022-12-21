package day17

data class Point (val x: Int, val y: Int) {
    fun add (point: Point): Point = add (point.x, point.y)
    fun add (dx: Int, dy: Int): Point = Point (x + dx, y + dy)
}
data class Rock (val raw: String) {
    val points: List<Point> = parseRock (raw)
    val width: Int = points.fold (0) { acc, p -> Math.max (acc, p.x + 1) }
    val height: Int = points.fold (0) { acc, p -> Math.max (acc, Math.abs (p.y) + 1) }
    companion object {
        fun parseRock (raw: String): List<Point> {
            val list = mutableListOf<Point> ()
            raw.split ("\n").forEachIndexed { y, row ->
                row.forEachIndexed { x, c ->
                    if (c == '#') {
                        list.add(Point(x, -y))
                    }
                }
            }
            return list
        }
    }
}
object Rocks {
    val rocks = parseRocks ()

    val iterator: TrackableIterator<Rock>
        get () {
            return object : TrackableIterator<Rock> {
                override var current = 0
                override val size = rocks.size
                override fun hasNext(): Boolean = true
                override fun next(): Rock {
                    val next = current ++
                    current %= rocks.size
                    return  rocks[next]
                }
            }
        }

    private fun parseRocks (): List<Rock> {
        val list = mutableListOf<Rock> ()
        ROCKS.trim ().split ("\n\n").forEach {
            list.add (Rock (it))
        }
        return list
    }
}

fun main (args: Array<String>) {
    val iter = Rocks.iterator
    repeat (10) {
        iter.next ().let {
            println ("\n${it}\n${it.width} x ${it.height}")
            it.points.forEach {
                println (it)
            }
        }
    }
}

private val ROCKS = """
####

.#.
###
.#.

..#
..#
###

#
#
#
#

##
##
"""

// EOF