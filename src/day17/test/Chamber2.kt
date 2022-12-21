package day17.test

import day17.Chamber
import day17.Rocks
import day17.loadJetSequence

fun main (args: Array<String>) {
    val c = Chamber ()

    val example = false
    val jets = loadJetSequence (example).iterator
    val rocks = Rocks.iterator

    val part1 = 2022
    val part2 = 1_000_000_000_000

    repeat (part1) {
        c.addRock (rocks.next ())
        while (true) {
            c.jet (jets.next ())
            if (! c.fall ()) {
                break
            }
        }
        // NOTE: We could be far more clever here, but this should work.
        if (c.data.size > 100) {
            c.removeRows (50)
        }
    }
    c.dump ()
    println ("part1=${c.highest + 1 + c.removed * 4}")


    return
}



// EOF