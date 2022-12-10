package day9

/**
 *
 *
 */

class Grid<T> (var width: Int = 1, var height: Int = 1, val init: () -> T)  {
    private var sx = 0
    private var sy = 0

    private fun remap (point: Point) = Point (point.x + sx, point.y + sy)

    private val values = mutableListOf<MutableList<T>> ().apply {
        height.repeat {
            add (mutableListOf<T> ().apply {
                width.repeat {
                    add (init ())
                }
            })
        }
    }

    override fun toString (): String {
        return "Grid[width=$width, height=$height, values=$values]"
    }

    fun clear () {
        values.forEach { row ->
            row.forEachIndexed { i, _ ->
                row[i] = init ()
            }
        }
        return
    }

    fun addRow (count: Int = 1) {
        when {
            count >= 0 -> {
                count.repeat {
                    values.add (mutableListOf<T> ().apply {
                        width.repeat {
                            add (init ())
                        }
                    })
                }
                height += count
            }
            count < 0 -> {
                TODO ("Unimplemented")
            }
        }
        return
    }

    fun addColumn (count: Int = 1) {
        when {
            count >= 0 -> {
                count.repeat {
                    values.forEach {
                        it.add (init ())
                    }
                }
                width += count
            }
            count < 0 -> {
                TODO ("Unimplemented")
            }
        }
        return
    }

    fun visit (func: (Int, Int, T) -> Unit) {
        values.forEachIndexed { y, row ->
            row.forEachIndexed { x, t ->
                func (x, y, t)
            }
        }
        return
    }

    fun toString (xform: (T) -> String): String {
        val buf = StringBuffer ()
        var i = 1
        visit { _, _, t ->
            i = Math.max (i, xform (t).length)
        }
        values.reversed ().forEach { row ->
            row.forEach { t ->
                buf.append (String.format ("%${i}s", xform (t)))
                buf.append (" ")
            }
            buf.append ("\n")
        }
        buf.append ("\n")
        return buf.toString ()
    }

    fun dump (xform: (T) -> String = { it.toString ()}) {
        println (toString (xform))
    }

    private fun checkBounds (col: Int, row: Int) {
        if (row >= height) {
            addRow (row - height + 1)
        }
        if (col >= width) {
            addColumn (col - width + 1)
        }
        return
    }

    fun get (point: Point) = get (point.x, point.y)
    fun get (col: Int, row: Int): T {
        checkBounds (col, row)
        return values[row][col]
    }

    fun set (point: Point, t: T) = set (point.x, point.y, t)
    fun set (col: Int, row: Int, t: T) {
        checkBounds (col, row)
        values[row][col] = t
    }
}

fun Grid<Int>.touch (point: Point) {
    set (point, get (point) + 1)
    return
}

// EOF