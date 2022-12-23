package day23

fun main (args: Array<String>) {

    val p = Point (7, 8)
    p.adjacent.forEach {
        println ("$it - ${Direction.fromPoint (it)}")
    }

    return
}