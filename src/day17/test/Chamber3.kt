package day17.test

import day17.Point
import day17.Chamber
import day17.toBinaryString

fun main (args: Array<String>) {
    val c = Chamber ()
    c.addRows (2)
    c.dump ()

    val points = listOf<Point> (Point (3, 0), Point (3, 1), Point (3, 2), Point (3, 3))
    points.forEach {
        c.set (it)
        println ("highest=${c.highest}")
        c.dump ()
    }
}



// EOF