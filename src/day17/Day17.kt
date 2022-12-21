package day17

fun main (args: Array<String>) {
    val example = true

    val jets = loadJetSequence (example).iterator
    val rocks = Rocks.iterator
    val c = Chamber ()

    val part1 = 2022
    val part2 = 1_000_000_000_000

    // part1


    // part2
    repeat (part2) {
        c.addRock (rocks.next ())
        while (true) {
            c.jet (jets.next ())
            if (! c.fall ()) {
                break
            }
            if (jets.atEnd) {
                val lastRow = c.highestRow
                if (lastRow == Chamber.FULL_ROW) {
                    val last = c.data[c.data.indexOfLast { it != 0 }]
                    println (last.toBinaryString ())
                }
            }
        }

        // NOTE: We could be far more clever here, but this should work.
        if (c.data.size > 100) {
            c.removeRows (50)
        }
    }
    println ("part1=${c.highest + 1 + c.removed * 4}")


    return
}

// EOF