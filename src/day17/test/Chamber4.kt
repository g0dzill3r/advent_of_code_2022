package day17.test

import day17.Chamber
import day17.highestOctet

fun main (args: Array<String>) {
    val ia = IntArray(3) { 0 }
    ia[0] = 253495356
    ia[1] = 1028
    ia[2] = 0

    val c = Chamber ()
    c.data = ia
    c.dump ()

    for (i in 0 .. 2) {
        println ("$i - ${ia[i].highestOctet()}")
    }
    println (c.highest)
    return
}