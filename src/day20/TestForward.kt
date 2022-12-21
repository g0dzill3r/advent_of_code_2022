package day20

data class TestCase (val index: Int, val list: List<Int>)

fun main (args: Array<String>) {

//    for (i in 0 .. 20) {
//        val coords = Coordinates (listOf (i, 100, 200, 300, 400, 500, 600, 700, 800, 900))
//        coords.dump (0)
//        coords.mix (0)
//        coords.dump (0)
//        println ()
//    }

//    for (i in 1 .. 20) {
//        val coords = Coordinates (listOf (100, 200, 300, 400, 500, i, 600, 700, 800, 900))
//        coords.dump (5)
//        coords.mix (5)
//        coords.dump (5)
//        println ()
//    }

    for (i in 0 .. 20) {
        val coords = Coordinates (listOf (100, 200, 300, 400, 500, -i, 600, 700, 800, 900, 1000).map { it.toLong ()})
        coords.dump (5)
        coords.mix (5)
        coords.dump (5)
        println ()
    }
    return
}