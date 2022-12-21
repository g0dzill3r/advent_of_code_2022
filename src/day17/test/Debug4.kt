package day17.test

import day17.*

fun main (args: Array<String>) {
//    rock1 ()
//    rock2 ()
//    rock3 ()
    rock4 ()
    return
}

fun testRock (rock: Rock, lpos: Point, left: List<String>, rpos: Point, right: List<String>) {
    testRock (rock, lpos, JetDirection.LEFT, left)
    testRock (rock, rpos, JetDirection.RIGHT, right)
    return
}

fun testRock (rock: Rock, pos: Point, dir: JetDirection, seqs: List<String>) {
    seqs.forEach { seq ->
        println ("\n========= $dir ===========\n")
        val c = Chamber ()
        c.addRows ()
        seq.split ("|").forEachIndexed { i, str ->
            if (str.length % Chamber.WIDTH != 0) {
                throw IllegalArgumentException ("Invalid length: ${str.length}")
            }
            c.setRow (i, str)
        }
        c.addRock (rock, pos)
        c.dump ()

        while (c.jet (dir)) {
            c.dump ()
        }
    }
    return
}



private fun rock1 () {
    val rock = Rocks.rocks[0]
    val lpos = Point (3, 1)
    val left = listOf (
        ".......|.......|.......",
        "#......|#......|#......",
        "#......|.......|#......",
        "####...|.......|####...",
    )
    val rpos = Point (0, 1)
    val right = listOf (
        ".......|.......|.......",
        "......#|......#|......#",
        "......#|.......|......#",
        "...####|......#|...####",
    )
    testRock (rock, lpos, left, rpos, right)
}

private fun rock2 () {
    val rock = Rocks.rocks[1]
    val lpos = Point (4, 3)
    val left = listOf (
        ".......|.......|.......",
        "#......|.......|.......",
        "#......|#......|.......",
        "#......|#......|#......",
        "#......|##.....|#......|##.....",
    )
    val rpos = Point (0, 3)
    val right = listOf (
        ".......|.......|.......",
        "......#|.......|.......",
        "......#|......#|.......",
        "......#|......#|......#",
        "......#|.....##|......#|.....##",
    )
    testRock (rock, lpos, left, rpos, right)
}

private fun rock3 () {
    val rock = Rocks.rocks[2]
    val lpos = Point (4, 3)
    val left = listOf<String> (
        ".......|.......|.......",
        "#......|.......|.......",
        "#......|#......|.......",
        "###....|#......|###....",
        "####...|#......|####...",
        "#......|##.....|#......|##.....",
    )
    val rpos = Point (0, 3)
    val right = listOf<String> (
        ".......|.......|.......",
        "......#|.......|.......",
        "......#|......#|.......",
        "......#|.......|.......|.......|......#",
        "......#|......#|.......|.......|......#",
        "......#|.......|......#|.......|......#",
        "......#|.......|.......|......#|......#",
        "......#|.....##|......#|.....##",
    )
    testRock (rock, lpos, left, rpos, right)
}

private fun rock4 () {
    val rock = Rocks.rocks[3]
    val lpos = Point (4, 4)
    val left = listOf (
        ".......|.......|.......|.......|.......",
        "#......|.......|.......|.......|.......",
        "#......|.......|.......|.......|.......|#......",
        "#......|#......|.......|.......|.......|#......",
        "#......|.......|#......|.......|.......|#......",
        "#......|.......|.......|#......|.......|#......",
        "#......|.......|.......|.......|#......|#......",
    )
    val rpos = Point (0, 4)
    val right = listOf (
        ".......|.......|.......|.......|.......",
        "......#|.......|.......|.......|.......",
        "......#|.......|.......|.......|.......|......#",
        "......#|......#|.......|.......|.......|......#",
        "......#|.......|......#|.......|.......|......#",
        "......#|.......|.......|......#|.......|......#",
        "......#|.......|.......|.......|......#|......#",
    )
    testRock (rock, lpos, left, rpos, right)
}

// EOF