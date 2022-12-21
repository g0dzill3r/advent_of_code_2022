package day17

class Chamber {
    var removed: Long = 0
    var removes: Long = 0
    var rocks: Long = 0
    var data = IntArray (1)
    val top: Int
        get () = data.size * 4 - 1

    val highest: Int
        get () {
            val index = data.indexOfLast { it != 0 }
            if (index < 0) {
                return -1
            }
            val i = data[index]
            return index * 4 + i.highestOctet()
        }

    val highestRow: Int
        get () {
            val index = data.indexOfLast { it != 0 }
            if (index < 0) {
                return -1
            }
            val i = data[index]
            return (i shr i.highestOctet() * 8) and 0xff
        }

    var rock: Rock? = null
    var offset: Point? = null

    fun falling (x: Int, y: Int): Boolean {
        rock?.let { rock ->
            val match = Point (x, y)
            for (p in rock.points) {
                if (offset!!.add (p) == match) {
                    return true
                }
            }
        }
        return false
    }

    fun fall (): Boolean {
        val _rock = rock!!
        val _offset = offset!!
        val maybe = _offset.add (0, -1)
        if (checkHit (_rock, maybe)) {
            _rock.points.forEach {
                set (_offset.add (it))
            }
            rock = null
            offset = null
            return false
        }
        offset = maybe
        return true
    }

    fun checkHit (rock: Rock, pos: Point): Boolean {
        if (pos.y - rock.height < -1) {
            return true
        }
        rock.points.forEach {
            if (get (pos.add (it))) {
                return true
            }
        }
        return false
    }

    fun jet (dir: JetDirection): Boolean {
        rock?.let { rock ->
            val (x, y) = offset as Point
            when (dir) {
                JetDirection.LEFT -> {
                    if (x == 0) {
                        return false
                    }
                    val maybe = Point (x - 1, y)
                    if (checkHit (rock, maybe)) {
                        return false
                    }
                    offset = maybe
                    return true
                }
                JetDirection.RIGHT -> {
                    if (x + rock.width == WIDTH) {
                        return false
                    }
                    val maybe = Point (x + 1, y)
                    if (checkHit (rock, maybe)) {
                        return false
                    }
                    offset = maybe
                    return true
                }
            }
        }
        return false
    }
    fun addRock (falling: Rock) {
        if (rock != null) {
            throw IllegalStateException ("Already has a rock")
        }
        val y = if (highest < 0) {
            2 + falling.height
        } else {
            highest + 3 + falling.height
        }
        while (y > top) {
            addRows ()
        }
        if (data[data.size - 1] != 0) {
            addRows (3)
        }
        addRock (falling, Point (2, y))
        rocks ++
        return
    }

    /**
     * Used to manually set the position of the rock in the chambed; used only for testing purposes.
     */

    fun addRock (falling: Rock, pos: Point) {
        rock = falling
        offset = pos
        return
    }

    fun addRows (count: Int = 1) {
        data = IntArray (data.size + count) { 0 }.apply {
            data.copyInto (this)
        }
        return
    }

    fun removeRows (count: Int) {
        data = IntArray (data.size - count) { 0 }.apply {
            for (i in 0 until this.size) {
                this[i] = data[i + count]
            }
        }
        removed += count
        removes ++
        return
    }

    fun setRow (y: Int, seq: String) {
        if (seq.length != WIDTH) {
            throw IllegalArgumentException ("Invalid length: ${seq.length}")
        }
        seq.forEachIndexed { x, c ->
            if (c == '#') {
                set (x, y)
            }
        }
        return
    }
    fun set (p: Point) = set (p.x, p.y)
    fun set (ps: List<Point>) = ps.forEach { set (it) }

    fun check (x: Int, y: Int) {
        if (x < 0 || x >= WIDTH || y < 0) {
            throw IllegalArgumentException ("Invalid coordinates: ($x, $y)")
        }
    }
    fun get (p: Point) = get (p.x, p.y)
    fun get (x: Int, y: Int): Boolean {
        check (x, y)
        val shift = y % 4 * 8
        val idx = y / 4
        val i = data[idx] shr shift
        val v = i and (1 shl x)
        return v != 0
    }

    fun set (x: Int, y: Int) {
        check (x, y)
        val shift = y % 4 * 8
        val idx = y / 4
        val i = data[idx]
        data [idx] = i or ((1 shl x) shl shift)
        return
    }

    fun dump () = println (toString ())

    override fun toString (): String {
        val buf = StringBuffer ()
        val max = data.size * 4
        val maxWidth = max.toString ().length

        for (y in max - 1 downTo 0) {
            buf.append (String.format ("%${maxWidth}d +", y))
            for (x in 0 until WIDTH) {
                if (get (x, y)) {
                    buf.append ('#')
                } else if (falling (x, y)) {
                    buf.append('@')
                } else {
                    buf.append ('.')
                }
            }
            buf.append ("+")
            if (y % 4 == 1) {
                buf.append ("  ${data[y / 4]}")
            }
            if (y % 4 == 0) {
                buf.append ("  ${data[y / 4].toBinaryString()}")
            }
            buf.append ("\n")
        }

        buf.append ("${" ".repeat (maxWidth)} +-------+  $highest ($removed)\n")
        buf.append ()
        return buf.toString ()
    }

    companion object {
        val WIDTH: Int = 7
        val FULL_ROW: Int = Integer.parseUnsignedInt ("01111111", 2)
    }
}

fun main (args: Array<String>) {
    println (Chamber.FULL_ROW)
    println (Chamber.FULL_ROW.toBinaryString ())

    val c = Chamber ()
    for (x in 0 until 7) {
        c.set (x, 2)
    }
    c.dump ()

    val lastRow = c.highestRow
    if (lastRow == Chamber.FULL_ROW) {
        val last = c.data[c.data.indexOfLast { it != 0 }]
        println ("HIT: ${last.toBinaryString ()}")
    }
    return
}

// EOF