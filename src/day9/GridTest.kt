package day9

fun intTest () {
    val grid = Grid<Int> (5, 5, { 0 })
    val xform = { it: Int ->
        if (it == 0) {
            "."
        } else {
            it.toString ()
        }
    }
    grid.dump (xform)

    grid.set (0, 0, 1)

    grid.addColumn (3)
    grid.dump (xform)

    grid.addRow (3)
    grid.dump (xform)
    return
}

fun main (args: Array<String>) {
    val start = Point (4, 4)
    val head = start.clone ()
    val tail = start.clone ()

    val dump = {
        val display = Grid<String> (9, 9) { "." }
//        display.clear ()
        display.set (start, "s")
        display.set (head, "H")
        display.set (tail, "T")
        display.dump ()
    }
    dump ()


    val steps = Step.parseSteps("""
    R 1 
    R 1
    U 1
    U 1
    L 1
    L 1
    L 1
    D 1
    D 1
    U 1
    """.trimIndent())

    steps.forEach { step ->
        step.count.repeat {
            head.move (step.direction)
            if (tail.follow (head)) {
                println ("MOVED!")
            }
            dump ()
        }
    }
    return
}

// EOF