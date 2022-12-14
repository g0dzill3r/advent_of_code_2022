package day14

/**
 * Advent of code: Day 14 solution
 */
fun main() {
    val example = false

    // part1
    readCave (example).let { cave ->
        cave.dump ()

        var count = 0
        while (cave.newSand() != null) {
            count ++
        }
        cave.dump ()
        println ("part1=$count")
    }

    // part2
    readCave (example).let { cave ->
        cave.addFloor()
        cave.dump ()

        var count = 0
        while (cave.newSand2 () != null) {
            count++
        }
        cave.dump()
        println("part2=$count")
    }
    return
}

// EOF