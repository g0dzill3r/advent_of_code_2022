package day23

fun main (args: Array<String>) {
    val example = false
    val grid = loadGrid (example)

    // part1
    loadGrid (example).let { grid ->
        repeat (10) {
            grid.round ()
        }
        grid.dump ()
        println ("part1=${grid.emptyTiles}")
    }

    // part2
    loadGrid (example).let { grid ->
        var count = 0
        while (true) {
            grid.round1 ()
            count ++
            if (grid.validMoves.isEmpty()) {
                break
            }
            grid.round2 ()
        }
        println ("part2=${count}")
    }

    return
}
