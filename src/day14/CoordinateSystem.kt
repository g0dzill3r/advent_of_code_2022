package day14

data class CoordinateSystem (val xw: Int, val yw: Int, val dx: Int, val dy: Int) {
    fun xOffset (x: Int) = x - dx
    fun yOffset (y: Int) = y - dy
    fun toIndex (x: Int, y: Int): Int = yOffset (y) * xw + xOffset (x)
    fun toIndex (point: Point): Int = toIndex (point.x, point.y)
    fun fromIndex (i: Int): Point = Point (i % xw + dx, i / xw + dy)
}

fun main (args: Array<String>) {
    val xw = 7
    val yw = 17
    val dx = 10
    val dy = 20
    val cs = CoordinateSystem (xw, yw, dx, dy)

    // test toIndex
    println (cs.xOffset (dx + 3))
    println (cs.xOffset (dy + 7))
    println (cs.toIndex (dx, dy))
    println ("slots = ${xw * yw}")
    println (cs.toIndex (dx + xw - 1, dy + yw - 1))

    // test fromIndex
    val els = listOf (0, 1, xw - 1, xw, xw+1, xw*2, xw*yw-1)
    els.forEach {
        println ("[$it] - ${cs.fromIndex(it)}")
    }
    return
}

// EOF