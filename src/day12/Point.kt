package day12

data class Point (val x: Int = 0, val y: Int = 0) {
    fun distance (other: Point): Int = Math.abs (x - other.x) + Math.abs (y - other.y)
    fun move (direction: Direction) = Point (x + direction.dx, y + direction.dy)

    fun delta (other: Point): Pair<Int, Int> = Pair (other.x - x, other.y - y)

    fun direction (other: Point): Direction {
        val (dx, dy) = delta (other)
        return Direction.values ().first { it.dx == dx && it.dy == dy }
    }

    override fun toString() = "($x, $y)"
}

enum class Direction (val dx: Int, val dy: Int) {
    UP (0, -1),
    DOWN (0, 1),
    LEFT (-1, 0),
    RIGHT (1, 0);

    val pair: Pair<Int, Int> = Pair (dx, dy)
}

fun main (args: Array<String>) {
    val a = Point ()
    val b = a.move (Direction.UP)
    val c = a.move (Direction.DOWN)
    val d = a.move (Direction.LEFT)
    val e = a.move (Direction.RIGHT)

    println ("a")
    listOf (a, b, c, d, e).forEach {
        println ("distance from $a to $it is ${a.distance (it)}")
        if (a.distance (it) == 1) {
            println (" - ${a.direction(it)}")
        }
    }
    println ("b")
    listOf (a, b, c, d, e).forEach {
        println ("distance from $b to $it is ${b.distance (it)}")
        if (b.distance (it) == 1) {
            println (" - ${b.direction(it)}")
        }
    }

    return
}

// EOF