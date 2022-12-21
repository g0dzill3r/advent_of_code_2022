package day17.test

import day17.Chamber

fun main (args: Array<String>) {
    val c = Chamber ()
    c.dump ()


    for (y in 0 until 4) {
        for (x in 0 until 7 step 2) {
            val x0 = x + y % 2
            if (x0 < Chamber.WIDTH) {
                c.set(x0, y)
                c.dump()
            }
        }
    }

    c.addRows (2)
    c.dump ()

    for (y in 4 until 12) {
        c.set ((y - 4) % Chamber.WIDTH, y)
    }
    c.dump ()

    c.removeRows (2)
    c.dump ()

    return
}