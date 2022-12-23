package day18

fun <T, S> compose (i1: Iterable<T>, i2: Iterable<S>, func: (T, S) -> Unit) {
    for (e1 in i1) {
        for (e2 in i2) {
            func (e1, e2)
        }
    }
    return
}

fun <T, S, R> compose (ts: Iterable<T>, ss: Iterable<S>, rs: Iterable<R>, func: (T, S, R) -> Unit) {
    for (t in ts) {
        for (s in ss) {
            for (r in rs) {
                func (t, s, r)
            }
        }
    }
    return
}

fun main (args: Array<String>) {
    compose (0 .. 5, 0 .. 5) { x, y ->
        println ("($x, $y)")
    }
    compose (0 .. 5, 'a' .. 'e') { x, y ->
        println ("$x$y")
    }
    return
}