package day12

fun main (args: Array<String>) {
    val example = false
    val terrain = readTerrain (example)

    // part1
    val paths = terrain.paths ()
    if (paths == null) {
        println ("No paths.")
    } else {
        val shortest = paths.map { it.size }.sorted ().take (1)[0]
        println (shortest - 1)
    }

    // part2
    val starts = terrain.find (0)
    val distances = starts.map { start ->
        Pair (start, terrain.paths (start, terrain.endPoint)?.size)
    }
    println (distances.sortedBy { it.second }.first ().second)
    return
}

// EOF