package tmp

fun main (args: Array<String>) {
    val seq = sequence {
        for (i in 0 .. 10) {
            yield (i)
        }
    }

    for (el in seq.iterator()) {
        println (el)
    }

    return
}

//class Test1 {
//
//    private fun sequence(): Sequence<GridItem> = sequence {
//        grid.forEachIndexed {y, row ->
//            row.forEachIndexed {x, item ->
//                yield(GridItem(item, Pos(x, y)))
//            }
//        }
//    }
//}