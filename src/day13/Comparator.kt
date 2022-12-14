package day13

import readInput

class ComparatorContext {
    var depth = 0
    val debug = false

    val prefix: String
        get () = "  ".repeat (depth * 2)

    fun print (s: String) {
        if (debug) println ("${prefix}$s")
        return
    }

    fun compare (a: Any, b: Any) {
        if (debug) println ("${prefix}COMPARE $a AND $b")
        depth++
    }

    fun pop (value: Int) {
        depth --
        if (debug) println ("${prefix}RETURN $value")
    }
}

private fun coerceList (any: Any): List<Any> {
    return when (any) {
        is Int -> listOf (any)
        is List<*> -> any as List<Any>
        else -> throw IllegalStateException ()
    }
}

/**
 * Compares the two data structures and returns true if they're in
 * the proper order.
 */
fun compare (left: List<Any>, right: List<Any>, ctx: ComparatorContext? = null): Int {
    ctx?.apply { compare (left, right) }

    for (i in 0 until left.size) {
        if (right.size == i) {
            ctx?.run { pop (1) }
            return 1
        }
        val lval = left[i]
        val rval = right[i]

        if (lval is Int && rval is Int) {
            ctx?.print ("$lval ? $rval")
            when {
                lval > rval -> {
                    ctx?.run { pop (1) }
                    return 1
                }
                rval > lval -> {
                    ctx?.run { pop (-1) }
                    return -1
                }
                else -> Unit
            }
        } else {
            val check = compare (coerceList (lval), coerceList (rval), ctx)
            if (check != 0) {
                ctx?.run { pop (check) }
                return check
            }
        }
    }

    if (left.size == right.size) {
        ctx?.run { pop (0) }
        return 0
    }
    ctx?.run { pop (-1) }
    return -1
}

fun main (args: Array<String>) {
    val packets = Packets.parse (readInput (13, true))
    packets.data.forEach {
        val (left, right) = it
        val compare = compare (left, right, ComparatorContext())
        println ("$left AND $right are ${if (compare (left, right) != -1) "NOT " else ""}in the right order")
        println ("\n\n")
    }
}

// EOF