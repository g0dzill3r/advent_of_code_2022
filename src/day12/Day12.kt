package day12

import readInput

fun main (args: Array<String>) {
    val example = false
    val input = readInput (12, example)

    // part1
    time {
        val terrain1 = TerrainOne (input)
        terrain1.dump ()
        val paths = terrain1.paths ()
        if (paths == null) {
            println ("No paths.")
        } else {
            val shortest = paths.map { it.size }.sorted ().take (1)[0]
            println ("part1=${shortest - 1}")
        }
    }

    // part2
    time {
        val terrain2 = TerrainTwo (input)
        terrain2.dump ()
        val starts = terrain2.find (0)
        println ("starts[${starts.size}]")

        starts.forEach { start ->
            val paths = terrain2.paths (start)
            if (paths.isNotEmpty()) {
                val min = paths.sortedBy { it.size }[0].size
                Pair(start, min)
            }
        }
        terrain2.rc.dump ()
        var min = Integer.MAX_VALUE
        starts.forEach { point ->
            val rce = terrain2.rc.get (point)
            rce?.let {
                min = Math.min (min, it.second)
            }
        }
        println ("part2=${min}")
    }

    return
}


// EOF