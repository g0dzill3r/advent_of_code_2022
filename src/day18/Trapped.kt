package day18


fun Grid.findTrapped (): List<Point> {
    val trapped = mutableListOf<Point> ()
    val notTrapped = findNotTrapped()
    for (x in min.x .. max.x) {
        for (y in min.y .. max.y) {
            for (z in min.z .. max.z) {
                val point = Point (x, y, z)
                if (! get (point) && notTrapped[point] != true) {
                    trapped.add (point)
                }
            }
        }
    }
    return trapped
}

fun Grid.findNotTrapped (): Map<Point, Boolean> {
    val notTrapped = mutableMapOf<Point, Boolean>()
    val check = mutableListOf<Point>()

    // To be completely proper, we have to attempt searches of the entire grid space
    // from all points on the exterior

    compose (listOf(min.x, max.x), min.y .. max.y, min.z .. max.z) { x, y, z ->
        val point = Point(x, y, z)
        if (get(point)) {
            notTrapped[point] = false
        } else {
            notTrapped[point] = true
            check.add(point)
        }
    }

    compose (listOf (min.y, max.y), min.x .. max.x, min.z .. max.z) { y, x, z ->
        val point = Point(x, y, z)
        if (get(point)) {
            notTrapped[point] = false
        } else {
            notTrapped[point] = true
            check.add(point)
        }
    }

    compose (listOf (min.z, max.z), min.x .. max.x, min.y .. max.y) { z, x, y ->
        val point = Point (x, y, z)
        if (get (point)) {
            notTrapped[point] = false
        } else {
            notTrapped[point] = true
            check.add (point)
        }
    }

    while (check.isNotEmpty()) {
        val next = check.removeAt (0)
        val addl = checkNotTrapped (next, notTrapped)
        check.addAll (addl)
    }

    return notTrapped
}

fun Grid.groupTrapped (points: List<Point>) : List<List<Point>> {
    val lists = mutableListOf<MutableList<Point>> ().apply {
        points.forEach {
            add (mutableListOf (it))
        }
    }

    var updated = true
    outer@ while (updated) {
        updated = false
        for (i in lists.indices) {
            for (j in i + 1 until lists.size) {
                if (Point.touches (lists[i], lists[j])) {
                    val removed = lists.removeAt (j)
                    lists[i].addAll (removed)
                    updated = true
                    continue@outer
                }
            }
        }
    }

    return lists
}

fun Grid.checkNotTrapped (point: Point, map: MutableMap<Point, Boolean>): List<Point> {
    if (get (point)) {
        map[point] = false
        return listOf ()
    }
    map[point] = true
    val addl = mutableListOf<Point> ()
    Point.DIRECTIONS.forEach { delta ->
        val test = point.add (delta)
        if (bounds.contains (test)) {
            if (! map.containsKey (test)) {
                addl.add (test)
            }
        }
    }
    return addl
}


fun main (args: Array<String>) {
    val example = false
    val grid = loadGrid (example)

    println ("bounds: ${grid.bounds}")

    val trapped = grid.findTrapped ()
    println (trapped)

    val groups = grid.groupTrapped (trapped)
    groups.forEach {
        println ("$it - ${Point.adjacencies(it)}")
    }
    return
}

// EOF