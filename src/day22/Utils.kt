package day22


fun absorb (func: () -> Unit) {
    try {
        func ()
    } catch (t: Throwable) {
        // IGNORED
    }
}

val <T,S> Map<T,S>.inverted: Map<S, T>
    get ()  {
        val map = mutableMapOf<S, T> ()
        for ((key, value) in this) {
            map.put (value, key)
        }
        return map
    }

val String.isNumeric: Boolean
    get () {
        for (c in this) {
            if (! c.isDigit()) {
                return false
            }
        }
        return true
    }

class Indenter {
    var count = 0
    fun inc (): Int = count++
    fun dec (): Int = -- count

    fun p(str: String) = println ("$this$str")
    override fun toString (): String = "  ".repeat (count)
}

fun main (args: Array<String>) {
    return
}