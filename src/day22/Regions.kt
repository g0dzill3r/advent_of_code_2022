package day22

/**
 * Dump out the regions.
 */
fun main (args: Array<String>) {
    val example = true
    val board = Board3D (loadBoard(example))

    board.dumpRegions()
    return
}

// EOF