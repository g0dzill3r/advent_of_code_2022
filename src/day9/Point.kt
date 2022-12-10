package day9

import kotlin.math.sign

data class Point (var x: Int  = 0, var y: Int = 0) {
    fun clone() = Point(x, y)

    fun move(dir: Direction) {
        x += dir.dx;
        y += dir.dy;
        return
    }

    fun follow(follow: Point): Boolean {
        val dx = follow.x - x
        val dy = follow.y - y
        if (Math.abs(dx) > 1 || Math.abs(dy) > 1) {
            x += dx.sign
            y += dy.sign
            return true
        }
        return false
    }

    override fun toString() = "($x, $y)"
}

// EOF