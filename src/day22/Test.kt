package day22

fun main (args: Array<String>) {
    val example = true
//    val board = Board3D (loadBoard (example))
    val board = Board3D (loadSimple())
//    board.pos = Point (14, 9)
//    board.facing = Facing.DOWN

    while (true) {
        print ("> ")
        val cmd = readln ().trim ().toLowerCase()
        if (cmd.length == 0) {
            continue
        }
        if (cmd.isNumeric) {
            repeat (cmd.toInt ()) {
                board.forward ()
            }
            board.dump ()
        } else if (cmd == "l") {
            board.turn (Direction.LEFT)
            board.dump ()
        } else if (cmd == "r") {
            board.turn(Direction.RIGHT)
            board.dump()
        } else if (cmd == "b") {
            board.dump ()
        } else if (cmd == "m") {
            board.cubeMap.regions.forEach {
                println ("R${it.index} ${it.min}-${it.max}")
                Facing.values().forEach { facing ->
                    println ("  $facing - ${it.getAdjacency(facing)}")
                }
            }
        } else if (cmd == "rs") {
            board.dumpRegions()
        } else {
            println ("ERROR: Unrecognized command: $cmd")
        }
    }

    // NOT REACHED
}

// EOF1
