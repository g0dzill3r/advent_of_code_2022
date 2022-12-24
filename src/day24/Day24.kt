import day24.loadValley

fun main (args: Array<String>) {
    val example = false

    // part1
    loadValley(example).let { valley ->
        valley.dump()
        while (! valley.positions.contains (valley.endPoint)) {
            valley.round ()
        }
        println ("part1=${valley.time}")
    }

    // part2
    loadValley(example).let { valley ->
        valley.dump()
        while (! valley.positions.contains (valley.endPoint)) {
            valley.round ()
        }
        valley.positions = mutableListOf (valley.endPoint)
        while (! valley.positions.contains (valley.startPoint)) {
            valley.round ()
        }
        valley.positions = mutableListOf (valley.startPoint)
        while (! valley.positions.contains (valley.endPoint)) {
            valley.round ()
        }
        println ("part2=${valley.time}")
    }

    return
}

// EOF