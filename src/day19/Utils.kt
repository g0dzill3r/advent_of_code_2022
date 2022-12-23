package day19

fun <T> MutableMap<T, Int>.add (t: T, count: Int) {
    val already = get (t)
    if (already != null) {
        put (t, already + count)
    } else {
        put (t, count)
    }
}

fun permutations (pairs: List<Robots>): List<List<Robots>> {
    return _permutations (pairs)
        .map {
            it.filter { it.count != 0 }
        }
        .filter { it.isNotEmpty () }
        .sortedBy { it.size }
}
private fun _permutations (pairs: List<Robots>): List<List<Robots>> {
    val res = mutableListOf<List<Robots>> ()
    if (pairs.isNotEmpty ()) {
        val pair = pairs[0]
        if (pairs.size == 1) {
            for (count in 0 .. pair.count) {
                res.add (listOf (Robots (pair.material, count)))
            }
        } else {
            val rest = _permutations (pairs.subList(1, pairs.size))
            for (count in 0 .. pair.count) {
                for (el in rest) {
                    val list = mutableListOf<Robots> (Robots (pair.material, count))
                    list.addAll (el)
                    res.add (list)
                }
            }
        }
    }

    return res
}

// EOF