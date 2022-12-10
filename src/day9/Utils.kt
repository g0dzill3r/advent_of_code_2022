package day9

/**
 * Extension function for maps that allows for easy increment of an integer
 * value associated with the key.
 */
fun <T> MutableMap<T, Int>.increment (t: T, inc: Int = 1): Int {
    return if (containsKey (t)) {
        val res = get (t) as Int + inc
        put (t, res)
        res
    } else {
        put (t, inc)
        inc
    }
}

/**
 * Repeat a lambda n times.
 */
fun Int.repeat (func: (i: Int) -> Unit) {
    for (i in 0 until this) {
        func (i)
    }
}

fun block (func: () -> Unit) {
    func ()
}

fun main (args: Array<String>) {

    // This is annoying
    block {
        println ("hi")
    }

    // Int.repeat
    3.repeat {
        println (it)
    }

    /*
     * MutableMap.increment test
     */

    val map = mutableMapOf<String, Int> ()
    map.increment ("a")
    map.increment ("b", 3)
    println (map)
    return
}

// EOF