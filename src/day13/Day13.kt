package day13

import readInput
import java.util.*

fun main (args: Array<String>) {
    val example = false
    val packets = loadPackets (example)

    // part1
    var total = 0
    packets.data.forEachIndexed { i, pair ->
        val (a, b) = pair
        if (compare (a, b, ComparatorContext ()) == -1) {
            total += i + 1
        }
    }
    println ("part1=$total")

    // part2
    val START = Packets.parsePacket("[[2]]")
    val END = Packets.parsePacket("[[6]]")
    val sorted = packets.sorted (listOf (START, END))

    sorted.forEach {
        println (it)
    }

    val startId = sorted.indexOf (START)
    val endId = sorted.indexOf (END)
    println ("part2=${(startId + 1) * (endId + 1)}")
    return
}

// EOF