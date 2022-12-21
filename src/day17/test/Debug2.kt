package day17.test

import day17.Chamber
import day17.JetDirection
import day17.Rocks

fun main (args: Array<String>) {
    JetDirection.values ().forEach { dir ->
        Rocks.rocks.forEach { rock ->
            println ("\n========================\n")
            val c = Chamber ()
            c.addRock (rock)
            c.dump ()

            while (c.fall ()) {
                c.jet (dir)
                c.dump ()
            }
            c.dump ()
        }
    }

    return
}