package day17.test

import day17.Chamber
import day17.Rock
import day17.Rocks

fun main (args: Array<String>) {
    testRock1 ()
    testRock2 ()
    testRock3 ()
    testRock4 ()
    testRock5 ()
    return
}

fun testRock5 () {
    val rock = Rocks.rocks[4]
    val seq = listOf (".......", "#######", "##..###", "##..###|##..###", "##..###|##..###|##..###")
    testRock (rock, seq)
}

fun testRock3 () {
    val rock = Rocks.rocks[2]
    val seq = listOf (".......", "#######", "##...##", "###.###|##...##", "##...##|##...##", "##...##|##...##|##...##", "##...##|##...##|##...##|##...##")
    testRock (rock, seq)
}

fun testRock4 () {
    val rock = Rocks.rocks[3]
    val seq = listOf (".......", "#######", "##.####", "##.####|##.####", "##.####|##.####|##.####", "##.####|##.####|##.####|##.####")
    testRock (rock, seq)
}

fun testRock1 () {
    val rock = Rocks.rocks[0]
    val seqs = listOf (".......", "#######", "##....#", "##....#|.#....#", "##....#|.#....#|##....#")
    testRock (rock, seqs)
}

fun testRock2 () {
    val rock = Rocks.rocks[1]
    val seqs = listOf (".......", "#######", "###.###", "###.###|##...##", "###.###|##...##|.#...#.")
    testRock (rock, seqs)
}

fun testRock (rock: Rock, seqs: List<String>) {
    seqs.forEach { seq ->
        println ("\n=======================\n")
        val c = Chamber ()
        seq.split ("|").forEachIndexed { i, str ->
            if (str.length % Chamber.WIDTH != 0) {
                throw IllegalArgumentException ("Invalid length: ${str.length}")
            }
            c.setRow (i, str)
        }
        c.addRock (rock)
        c.dump ()

        while (c.fall ()) {
            c.dump ()
        }
        c.dump ()
    }
    return
}


// EOF