package day18

fun main (args: Array<String>) {
    val example = false
    val grid = loadGrid (example)

    // part1
    var total = 0
    grid.visit { point ->
        var exposed = 0
        for (delta in Point.DIRECTIONS) {
            if (! grid.get (point.add (delta))) {
                exposed ++
            }
        }
        total += exposed
    }
    println ("part1=$total")

    // part2
    val trapped = grid.findTrapped ()
//    val groups = grid.groupTrapped (trapped)

    // Figure out what points were trapped

    val trappedPoints = mutableListOf<Point> ()
    grid.points.forEach { point ->
        if (point.touches (trapped)) {
            trappedPoints.add (point)
        }
    }

    // Now figure out how many faces they counted towards

    var remove = 0
    trappedPoints.forEach { point ->
        Point.DIRECTIONS.forEach { dir ->
            if (trapped.contains (point.add (dir))) {
                remove ++
            }
        }
    }

    println ("part2=${total - remove}")
    return
}