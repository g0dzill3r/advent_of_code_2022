package day12

typealias RouteCacheEntry = Pair<Point, Int>
class RouteCache (val terrain: Terrain) {
    val grid = Array<RouteCacheEntry?> (terrain.xs * terrain.ys) { null }
    fun toIndex(x: Int, y: Int) = y * terrain.xs + x
    fun toIndex (point: Point)  = point.y * terrain.xs + point.x
    fun get (x: Int, y: Int): RouteCacheEntry? = grid[toIndex (x, y)]
    fun get (point: Point): RouteCacheEntry? = grid[toIndex (point)]
    fun put (x: Int, y: Int, entry: RouteCacheEntry?) {
        grid[toIndex (x, y)] =  entry
    }

    fun getSegment (start: Point): List<Point>? {
        var entry: RouteCacheEntry? = get (start) ?: return null
        val path = mutableListOf<Point> (start)
        while (entry != null) {
            path.add (entry.first)
            entry = get (entry.first)
        }
        return path
    }
    fun clear () {
        visit { x, y, _ -> grid[toIndex (x, y)] = null }
        return
    }

    fun visit (func: (Int, Int, RouteCacheEntry?) -> Unit) {
        for (y in 0 until terrain.ys) {
            for (x in 0 until terrain.xs) {
                func (x, y, get (x, y))
            }
        }
        return
    }

    fun dump () {
        println (toString ())
        return
    }
    override fun toString (): String {
        val buf = StringBuffer ()
        val render: (v: Pair<Point, Int>?) -> String = { v ->
            if (v != null) "[${v.first.x},${v.first.y}]-${v.second}" else "-"
        }
        var max = 1
        visit { x, y, v ->
            max = Math.max (max, render (v).length)
        }
        visit { x, y, v ->
            if (x == 0 && y != 0) {
                buf.append ("\n")
            }
            buf.append (String.format ("%${max}s", render (v)))
            buf.append (" ")
        }
        return buf.toString ()
    }

    /**
     * Used to save the optimal paths to an end point from a set of possible paths from
     * a given starting point.
     */

    fun save (paths: List<List<Point>>) {
        paths.forEach { path ->
            val size = path.size
            for (i in 0 until size - 1) {
                val (x, y) = path [i]
                val hops = size - i - 1
                val already = get (x, y)
                if (already != null) {
                    assert (already.second == hops)
                }
                put (x, y, Pair (path [i+1], hops))
            }
        }
        return
    }
}

fun main (args: Array<String>) {
    val example = true
    val terrain = readTerrain(example)
    val rc = RouteCache (terrain)

    rc.save (listOf (
        listOf (Point(0,0), Point(0,1), Point(1,1), Point(2,1), Point(2,2))
    ))

    rc.dump ()
    println (rc.getSegment(Point (0, 0)))
    println (rc.getSegment(Point (1,1)))
    return
}

// EOF