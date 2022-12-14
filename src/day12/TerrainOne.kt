package day12

/**
 * The Terrain data structure and logic used to solve the first part.
 */

class TerrainOne (input: String) : Terrain (input) {
    fun paths () = paths (toPoint (start), toPoint (end))

    fun paths (from: Point, to: Point) = paths (from, to, mutableListOf (from), PathCache ())

    fun paths (from: Point, to: Point, acc: MutableList<Point>, cache: PathCache): List<List<Point>>? {
        val paths = mutableListOf<List<Point>> ()

        // If we're already at the destination, then we're done

        if (from == to) {
            paths.add (listOf (to))
            return paths
        }

        // Cache the path we took to get here and short-circuit if it isn't already longer
        // than the one previously discovered

        if (acc.size > 1) {
            val target = acc.last()
            val cached = cache.get (target)
            if (cached != null) {
                if (cached.size <= acc.size + 1) {
                    return paths
                }
            }
            cache.put (target, mutableListOf<Point> ().apply {
                addAll (acc)
            })
        }

        // Try all possible directions

        Direction.values ().forEach { direction ->
            if (canMove (from, direction)) {
                val target = from.move (direction)

                // Skip this possible move if we've already got the target in our current path

                if (acc.contains (target)) {
                    return@forEach
                }

                // Otherwise, assess this possible path

                val copy = mutableListOf<Point> ().apply {
                    addAll (acc)
                    add (target)
                }
                val res = paths (target, to, copy, cache)
                res?.forEach {
                    val path = mutableListOf<Point> ().apply {
                        addAll (it)
                    }
                    paths.add (path)
                }
            }
        }

        return if (paths.isEmpty ()) {
            null
        } else {
            val complete = mutableListOf<List<Point>> ().apply {
                paths.forEach {
                    add (mutableListOf<Point>().apply {
                        add (from)
                        addAll (it)
                    })
                }
            }
            complete
        }
    }
}

// EOF