package day8

import readInput

fun main (args: Array<String>) {
    val input = readInput(8, true)
    val grid = TreeGrid.loadGrid (input)
    grid.dump()
    grid.dumpVisible()

    // part1
    println (grid.visible)

    // part2
    println (grid.maxScenicScore)
    return
}

// EOF