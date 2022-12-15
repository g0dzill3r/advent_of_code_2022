package day14

import java.lang.IllegalStateException
import kotlin.math.sign


data class Point (val x: Int, val y: Int) {
    override fun toString (): String = "($x, $y)"
    fun move (dx: Int, dy: Int) = Point (x + dx, y + dy)
    operator fun rangeTo(other: Point) = PointIterator (this, other)
    operator fun plus (other: Point): Point = move (other.x, other.y)
    operator fun minus (other: Point): Point = move (-other.x, -other.y)
    operator fun unaryMinus (): Point = Point (-x, -y)
}

class PointIterator (val start: Point, val endInclusive: Point): Iterator<Point> {
    private val dx = (endInclusive.x - start.x).sign
    private val dy = (endInclusive.y - start.y).sign
    private var next = start
    private val end = endInclusive.move (dx, dy)

    override fun hasNext() = next != end

    override fun next (): Point {
        if (! hasNext ()) {
            throw IllegalStateException ("No more elements")
        }
        val tmp = next
        next = next.move (dx, dy)
        return tmp
    }
}

fun main (args: Array<String>) {
    val start = Point (10, 12)
    val end = start.move (0, 5)
    for (point in start .. end) {
        println (point)
    }
    println (".")
    println (start + end)
    println (start - end)
    println (-start)
    return
}