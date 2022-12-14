package day14

fun <T, S> List<List<T>>.fold (init: S?, func: (acc: S?, x: Int, y: Int, t: T) -> S?): S? {
    var acc = init
    for (y in indices) {
        val row = this[y]
        for (x in row.indices) {
            acc = func (acc, x, y, row[x])
        }
    }
    return acc
}

fun <T> List<List<T>>.visit (func: (x: Int, y: Int, t: T) -> Unit): Unit {
    for (y in indices) {
        val row = this[y]
        for (x in row.indices) {
            func (x, y, row[x])
        }
    }
    return
}

// EOF