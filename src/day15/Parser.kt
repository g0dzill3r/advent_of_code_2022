package day15

import readInput
import java.util.regex.Pattern

data class Point (val x: Int, val y: Int) {
    fun add (dx: Int, dy: Int) = Point (x + dx, y + dy)
    fun min (other: Point): Point = Point (Math.min (x, other.x), Math.min (y, other.y))
    fun max (other: Point): Point = Point (Math.max (x, other.x), Math.max (y, other.y))
}

data class Bounds (val min: Point, val max: Point) {
    fun join (other: Bounds) = Bounds (min.min (other.min), max.max (other.max))
}

data class Measurement (val sensor: Point, val beacon: Point) {
    val bounds: Bounds  
        get () {
            val d = Math.abs (distance)
            val (x, y) = sensor
            return Bounds (Point (x - d, y - d), Point (x + d, y + d))
        }

    fun hasRow (y: Int): Boolean {
        val (p0, p1) = bounds
        return y in p0.y .. p1.y
    }

    fun forRow (y: Int): Pair<Int, Int>? {
        if (! hasRow (y)) {
            return null
        }
        val delta = Math.abs (distance) - Math.abs (y - sensor.y)
        return Pair(sensor.x - delta, sensor.x + delta)
    }

    val dx: Int = beacon.x - sensor.x
    val dy: Int = beacon.y - sensor.y

    val distance: Int
        get () = Math.abs (dx) + Math.abs (dy)

    override fun toString(): String {
        return ("S:$sensor B:$beacon D:$distance")
    }
}

data class Measurements (val data: List<Measurement>) {
    val bounds: Bounds
        get () {
            var bounds = data[0].bounds
            data.forEach { el ->
                bounds = bounds.join (el.bounds)
            }
            return bounds
        }
    fun dump () = println (toString ())

    override fun toString (): String {
        val buf = StringBuffer ()
        data.forEachIndexed { i, m ->
            buf.append ("${i}: $m\n")
        }
        return buf.toString ()
    }

    companion object {
        private val REGEX = "^Sensor at x=([-\\d]+), y=([-\\d]+): closest beacon is at x=([-\\d]+), y=([-\\d]+)$"
        private val PATTERN = Pattern.compile (REGEX)

        fun parse (input: String): Measurements {
            val list = input.trim ().split ("\n").map { str ->
                val m = PATTERN.matcher (str)
                if (! m.matches()) {
                    throw IllegalStateException ("Malformed input: $str")
                }
                var i = 1
                val v = { m.group (i ++).toInt () }
                Measurement (
                    Point (v (), v ()),
                    Point (v (), v ())
                )
            }
            return Measurements (list)
        }
    }
}

fun loadMeasurements (example: Boolean): Measurements = Measurements.parse (readInput (15, example))

fun main() {
    val example = true
    val ms = loadMeasurements (example)
    ms.data.forEach { el ->
        println (el)
        println (" bounds=${el.bounds}")
    }
    println ("BOUNDS=${ms.bounds}")
    return
}

// EOF