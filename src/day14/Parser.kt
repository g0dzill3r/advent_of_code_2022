package day14

import readInput

object Parser {
    fun parse (input: String): List<List<Point>> {
        val paths = mutableListOf<List<Point>> ()
        input.split ("\n").forEach { row ->
            val path = mutableListOf<Point> ()
            row.split (" -> ").forEach { entry ->
                val (x, y) = entry.split(",")
                path.add (Point (x.toInt (), y.toInt ()))
            }
            paths.add (path)
        }
        return paths
    }
}

fun readCave (example: Boolean): Cave {
    val input = readInput (14, example)
    val paths = Parser.parse (input)
    return Cave (paths)
}

fun main (args: Array<String>) {
    val example = true
    val cave = readCave (example)
    println (cave)
    return
}

// EOF