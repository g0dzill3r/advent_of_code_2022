package day8

import readInput

fun main (args: Array<String>) {
    val input = readInput(8, true)
    val grid = TreeGrid.loadGrid (input)
    grid.dump()
    grid.dumpVisible()

    // part1
    val visible = grid.countVisible ()
    println ("$visible")

    // part2
    val scores = mutableListOf<Int> ()
    grid.visit { x, y, ->
        scores.add (getScenicScore(x, y))
    }

    println ("${scores.maxOf { it }}")
    return
}

// EOF