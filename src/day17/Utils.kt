package day17

/**
 * Wraps an iterator and transforms the results.
 */
class MappedIterator<T, S>  (iter: Iterator<T>, val xform: (T) -> S): Iterator<S> {
    private val wrapped = iter
    override fun hasNext(): Boolean = wrapped.hasNext ()
    override fun next(): S = xform (wrapped.next ())
}

fun Int.toBinaryString (): String {
    var s = Integer.toBinaryString (this)
    s = "0".repeat (32 - s.length) + s
    return String.format ("%s %s %s %s", s.substring (0, 8), s.substring (8, 16), s.substring (16, 24), s.substring (24, 32))
}

fun Int.highestOctet (): Int {
    for (i in 3 downTo 0) {
        if (this and (255 shl (8 * i)) != 0) {
            return i
        }
    }
    return -1
}

interface TrackableIterator<T> : Iterator<T> {
    val size: Int
    val current: Int
    val atStart: Boolean
        get () = current == 0
    val atEnd: Boolean
        get () = current == size - 1
}

inline fun repeat (times: Long, func: (Long) -> Unit) {
    for (l in 0 until times) {
        func (l)
    }
}

fun main (args: Array<String>) {
    val str = "hello, world!"
    val iter = MappedIterator (str.iterator ()) {
        it.toUpperCase()
    }
    while (iter.hasNext ()) {
        print (iter.next ())
    }
    println ()
    return
}

// EOF