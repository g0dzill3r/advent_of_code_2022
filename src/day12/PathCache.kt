package day12

class PathCache {
    private val cache = mutableMapOf<Point, List<Point>> ()
    fun contains (point: Point) = cache.containsKey (point)
    fun get (point: Point): List<Point>? = cache.get (point)

    fun length (point: Point): Int? = cache.get (point)?.size

    fun put (point: Point, path: List<Point>) {
        cache[point] = path
    }

    fun maybePut (point: Point, path: List<Point>): Boolean {
        if (cache.containsKey (point)) {
            val already = cache.get (point)
            if (already != null && already.size <= path.size) {
                return false
            }
        }
        cache.put (point, path)
        return true
    }

    override fun toString (): String {
        val buf = StringBuffer ()
        buf.append ("PathCache[\n")
        cache.forEach { (point, path) -> buf.append (" $point -> $path\n") }
        buf.append ("]")
        return buf.toString ()
    }
}

fun main (args: Array<String>) {
    val a = Point(0,0)
    val b = a.move (Direction.RIGHT)
    val c = a.move (Direction.DOWN)
    val d = b.move (Direction.DOWN)
    val e = c.move (Direction.RIGHT)

    val cache = PathCache ()
    cache.maybePut (b, listOf (a, b))
    cache.maybePut (c, listOf (a, c))
    cache.maybePut (d, listOf (a, b, d))
    cache.maybePut (d, listOf (a, c, d))
    println (cache)
    return
}