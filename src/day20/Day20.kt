package day20

fun main (args: Array<String>) {
    val example = false

    val doit = { coords: Coordinates, times: Int ->
        repeat (times) {
            coords.mix ()
        }
        val index = coords.findValue (0)
        val a = coords.data [(index + 1_000) % coords.data.size].value
        val b = coords.data [(index + 2_000) % coords.data.size].value
        val c = coords.data [(index + 3_000) % coords.data.size].value
        println ("[$index] = 0")
        println ("($a, $b, $c)")
        println ("part1=${a + b + c}")
    }

    // part1
    loadCoordinates(example).let {
        doit (it, 1)
    }

    // part2
    val key = 811589153L
    loadCoordinates(example).let {
        it.data.let{
            for (i in it.indices) {
                val old = it[i]
                val rec = Coordinate (old.value * key, old.index)
                it[i]= rec
            }
        }
        doit (it, 10)
    }

    return
}
// EOF
