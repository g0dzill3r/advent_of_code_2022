package day21

fun <T> List<T>.carcdr (separate: Int): Pair<List<T>, List<T>> {
    return Pair (subList (0, separate), subList (separate + 1, size))
}

