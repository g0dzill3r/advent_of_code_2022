package day12

data class Point (val x: Int, val y: Int) {
    fun move (direction: Direction) = Point (x + direction.dx, y + direction.dy)
}

enum class Direction (val dx: Int, val dy: Int) {
    UP (0, -1),
    DOWN (0, 1),
    LEFT (-1, 0),
    RIGHT (1, 0)
}

// EOF