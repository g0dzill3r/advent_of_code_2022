package day18

import readInput
import kotlin.math.absoluteValue

data class Dimensions (val min: Point, val max: Point) {
    val cells: Int
        get () {
        val point = max.add (min.negate ()).add (Point (1, 1, 1))
        val (x, y, z) = point
        return x * y * z
    }
    fun contains (point: Point): Boolean {
        val (x, y, z) = point
        return x >= min.x && x <= max.x && y >= min.y && y <= max.y && z >= min.z && z <= max.z
    }
    override fun toString (): String = "$min x $max"
}

data class Point (val x: Int, val y: Int, val z: Int) {
    fun min (other: Point) = Point (Math.min (x, other.x), Math.min (y, other.y), Math.min (z, other.z))
    fun max (other: Point) = Point (Math.max (x, other.x), Math.max (y, other.y), Math.max (z, other.z))
    override fun toString (): String = "($x, $y, $z)"

    fun touches (points: List<Point>): Boolean {
        for (point in points) {
            if (touches (point)) {
                return true
            }
        }
        return false
    }

    fun touches (other: Point): Boolean {
        val delta = add (other.negate ())
        val (x, y, z) = delta
        return (x.absoluteValue + y.absoluteValue + z.absoluteValue) == 1
    }

    fun negate () = Point (-x, -y, -z)
    fun add (other: Point) = Point (x + other.x, y + other.y, z + other.z)
    companion object {
        val DIRECTIONS = mutableListOf<Point> ().apply {
            for (i in listOf (-1, 1)) {
                add (Point (i, 0, 0))
                add (Point (0, i, 0))
                add (Point (0, 0, i))
            }
        }

        fun adjacencies (points: List<Point>): Int {
            var total = 0
            for (i in points) {
                for (j in points) {
                    if (i != j && i.touches (j)) {
                        total ++
                    }
                }
            }
            return total / 2
        }

        fun touches (list1: List<Point>, list2: List<Point>): Boolean {
            for (p0 in list1) {
                for (p1 in list2) {
                    if (p0.touches (p1)) {
                        return true
                    }
                }
            }
            return false
        }
    }
}

data class Grid (val points: List<Point>) {
    var min: Point = points[0]
    var max: Point = points[0]
    val bounds: Dimensions
        get () = Dimensions (min, max)

    val data = mutableMapOf<Point, Boolean> ()

    init {
        points.forEach {
            set (it)
        }
    }

    fun set (point: Point) {
        data[point] = true
        min = min.min (point)
        max = max.max (point)
        return
    }

    fun get (point: Point): Boolean {
        return data[point] != null
    }
    fun visit (func: (Point) -> Unit) {
        data.keys.forEach { func (it) }
        return
    }

    fun dump () { println (toString ()) }
    override fun toString (): String {
        val buf = StringBuffer ()
        buf.append ("${data.size} points, ${min} x ${max}\n")
        data.keys.forEach {
            buf.append (" - $it\n")
        }
        return buf.toString ()
    }
}
fun loadGrid (example: Boolean): Grid {
    val input = readInput (18, example)
    val points = input.trim ().split ("\n").map {
        val strs = it.split (",")
        Point (strs[0].toInt(), strs[1].toInt (), strs[2].toInt ())
    }
    return Grid (points)
}

fun main (args: Array<String>) {
    val example = true
    val grid = loadGrid (example)
    grid.dump ()
    return
}

// EOF