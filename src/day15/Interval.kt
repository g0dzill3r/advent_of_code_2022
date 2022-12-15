package day15

data class Interval (val x0: Int, val x1: Int) {
    fun touches (other: Interval): Boolean {
        val (y0, y1) = other
        if (x0 <= y0 && x1 >= y1) {
            return true
        }
        if (x0 in y0 - 1 .. y1 + 1) {
            return true
        }
        if (x1 in y0 - 1 .. y1 + 1) {
            return true
        }
        return false
    }

    fun before (other: Interval): Boolean = x1 < other.x0 - 1
    fun after (other: Interval): Boolean = x0 > other.x1 + 1

    fun intersect (other: Interval): Interval {
        if (! touches (other)) {
            throw IllegalStateException ()
        }
        val (y0, y1) = other
        return Interval (Math.min (x0, y0), Math.max (x1, y1))
    }

    override fun toString (): String = "[$x0, $x1]"
}

class Intervals {
    private val intervals: MutableList<Interval> = mutableListOf ()

    val segments: List<Interval>
        get () = intervals

    fun clip (min: Int, max: Int) {
        var i = 0
        while (i < intervals.size) {
            val el = intervals[i]
            if (el.x1 < min || el.x0 > max) {
                intervals.removeAt (i)
                continue
            }
            if (el.x0 < min || el.x1 > max) {
                intervals[i] = Interval (Math.max (el.x0, min), Math.min (max, el.x1))
            }
            i ++
        }
        return
    }
    fun add (other: Interval) {
        if (intervals.isEmpty ()) {
            intervals.add (other)
        } else {
            var i = 0
            var mutated = false
            outer@ while (i < intervals.size) {
                val el = intervals[i]
                if (other.before (el)) {
                    intervals.add (i, other)
                    return
                }
                if (el.touches (other)) {
                    intervals[i] = el.intersect (other)
                    while (i != intervals.size - 1 && intervals[i].touches (intervals[i + 1])) {
                        val removed = intervals.removeAt (i + 1)
                        intervals[i] = intervals[i].intersect (removed)
                    }
                    mutated = true
                    break@outer
                }
                i ++
            }
            if (! mutated) {
                intervals.add (other)
            }
        }
        return
    }

    override fun toString(): String {
        val buf = StringBuffer ()
        intervals.forEach { i ->
            buf.append (i)
        }
        return buf.toString ()
    }
}

fun main (args: Array<String>) {

    // test Interval

    val a = Interval (45, 55)
    val b = Interval (40, 50)
    val c = Interval (50,60)
    val d = Interval (40, 60)
    val e = Interval (10, 40)
    val f = Interval (60, 100)
    val g = Interval (43, 44)
    val h = Interval (56, 57)

    val compare = { i0: Interval, i1: Interval ->
        val check = i0.touches (i1)
        println ("$a and $b ${if (check) "TOUCH" else "DO NOT TOUCH"}")
        println ("  before: ${a.before(b)}")
        println ("  after:  ${a.after(b)}")
        if (check) {
            println ("  -> ${a.intersect(b)}")
        }
    }

    println ("===============")
    val i = Interval (10, 20)
    val j = Interval (30, 40)
    println ("$i after $j: ${i.after(j)}")
    println ("$i before $j: ${i.before(j)}")
    println ("$j after $i: ${j.after(i)}")
    println ("$j before $i: ${j.before(i)}")
    println ("===============")

    val list = listOf (a, b, c, d, e, f, g, h)
    list.forEach { el -> compare (a, el) }

    // test Intervals

    val pairs = listOf (
        Pair(10, 20),
        Pair(30, 40),
        Pair(90, 100),
        Pair(70, 80),
        Pair(50, 60),
        Pair(8,9), Pair (21,22),
        Pair (48,49), Pair (61, 62),
        Pair (23, 29),
        Pair (41, 69),
        Pair (102, 103), Pair (105, 106), Pair (108, 109), Pair (111, 113),
        Pair (91, 200)
    )
    val intervals = Intervals ()
    println (intervals)
    pairs.forEach { pair ->
        val (x0, x1) = pair
        val i = Interval (x0, x1)
        intervals.add (i)
        println ("\n ADD $i")
        println (intervals)
    }

    intervals.clip (50, 150)
    println (intervals)

    return
}

// EOF