package day17.test

import day17.Chamber
import day17.Rocks

fun main (args: Array<String>) {
    Rocks.rocks.forEach { rock ->
        println ("\n========================\n")
        val c = Chamber ()
        c.addRock (rock)
        c.dump ()

        while (c.fall ()) {
            c.dump ()
        }
        c.dump ()
    }

    return
}