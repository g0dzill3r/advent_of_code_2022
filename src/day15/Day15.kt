package day15

import java.math.BigInteger

fun main() {
    val example = false
    val (row, max) = if (example) Pair(10, 20) else Pair(2_000_000,4_000_000)

    val ms = loadMeasurements(example)
//    ms.dump ()

    // part1
    val beacons = mutableSetOf<Int>()
    val set = mutableSetOf<Int>()
    ms.data.forEach {
        if (it.beacon.y == row) {
            beacons.add (it.beacon.x)
        }
        it.forRow (row)?.let { segment ->
            val (x0, x1) = segment
            for (x in x0..x1) {
                set.add(x)
            }
        }
    }
    println ("part1=${set.size - beacons.size}")

    // part2
    val possible = mutableListOf<Point> ()
    for (y in 0 .. max) {
        val intervals = Intervals ()
        ms.data.forEach {
            it.forRow(y)?.let {
                val (x0, x1) = it
                intervals.add (Interval (x0, x1))
            }
        }
        intervals.clip (0, max)
        if (intervals.segments.size > 1) {
            possible.add (Point (intervals.segments[0].x1 + 1, y))
        }
    }
    println (possible)
    val loc = possible[0]
    val freq = loc.x.toBigInteger().times(max.toBigInteger ()).plus (loc.y.toBigInteger ())
    println ("part2=$freq")
    return
}

// EOF