package day12

class TerrainTwo (input: String) : Terrain (input) {
    val rc = RouteCache (this)
    fun paths (start: Point): List<List<Point>> {
        val paths = paths (start, toPoint (end), mutableListOf (start), PathCache ())
        rc.save (paths)
        return paths
    }

    fun paths (from: Point, to: Point, acc: MutableList<Point>, cache: PathCache): List<List<Point>> {
        val paths = mutableListOf<List<Point>>()

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

                // See if we can short circuit based on an already discovered final segment

                val check = rc.getSegment (target)
                if (check != null) {
                    val overlap = acc.intersect (check)
                    if (overlap.isEmpty()) {
                        val composite = mutableListOf<Point>().apply {
                            addAll (check)
                        }
                        assert (composite.size == composite.distinct().size)
                        paths.add(composite)
                        return@forEach
                    }
                }

                // Otherwise, assess this possible path

                val copy = mutableListOf<Point> ().apply {
                    addAll (acc)
                    add (target)
                }
                assert (copy.size == copy.distinct().size)
                val res = paths (target, to, copy, cache)
                res.forEach {
                    val path = mutableListOf<Point> ().apply {
                        assert (it.size == it.distinct().size)
                        addAll (it)
                    }
                    paths.add (path)
                }
            }
        }

        return mutableListOf<List<Point>> ().apply {
            paths.forEach { path ->
                if (! path.contains (from)) {
                    add (mutableListOf<Point>().apply {
                        add (from)
                        addAll (path)
                    })
                }
            }
        }
    }
}

// EOF