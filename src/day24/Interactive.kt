package day24

fun main (args: Array<String>) {
    val example = true
    var valley = loadValley(example)
    val blizzards = { p: Point ->
        val dirs = valley.getBlizzards (p)
        when (dirs.size) {
            0 -> '.'
            1 -> dirs[0].encoded
            in 1..9 ->  "${dirs.size}"[0]
            else -> 'B'
        }
    }

    println ("Valley interpreter, v0.1")
    while (true) {
        print ("> ")
        val input = readln().trim ()
        if (input.isEmpty()) {
            continue
        }
        val cmds = input.split (" ")
        val pos = valley.positions[0] as Point
        when (cmds[0]) {
            "d" -> valley.dump ()
            "t" -> valley.tick ()
            "td" -> {
                valley.tick()
                valley.dump()
            }
            "p" -> valley.possibleMoves(pos).forEach { println (it) }
            "pd" -> {
                val ps = valley.possibleMoves(pos)
                println (ps)
                println (valley.render {
                    if (ps.contains(it)) {
                        append ('P')
                    } else if (it == valley.startPoint || it == valley.endPoint) {
                        append ('.')
                    } else {
                        append (blizzards (it))
                    }
                })
            }
            "up" -> valley.positions[0] = pos.move (Direction.UP)
            "down" -> valley.positions[0] = pos.move (Direction.DOWN)
            "left" -> valley.positions[0] = pos.move (Direction.LEFT)
            "right" -> valley.positions[0] = pos.move (Direction.RIGHT)
            "dirs" -> Direction.values().forEach { println ("$it = !${it.invert}") }

            "l" -> valley.legalMoves (valley.possibleMoves (pos)).forEach { println (it) }
            "ld" -> {
                val ps = valley.possibleMoves(pos)
                val ls = valley.legalMoves (ps)
                println (valley.render {
                    if (ls.contains (it)) {
                        append ('L')
                    } else if (ps.contains(it)) {
                        append ('P')
                    } else if (it== valley.startPoint || it == valley.endPoint) {
                        append ('.')
                    } else {
                        append (blizzards (it))
                    }
                })
            }
            "alt" -> {
                val alternate = input.split (" ")
                val index = alternate[1].toInt ()
                valley = Valley.parse (ALTERNATES[index])
            }
            "b?" -> {
                for (i in valley.blizzards.indices) {
                    println ("${valley.toPoint (i)}: ${valley.blizzards[i]}")
                }
            }
            "example" -> valley = loadValley(true)
            "actual" -> valley = loadValley (false)
            "?" -> {
                valley.run {
                    println ("width=$width, height=$height")
                    println ("start=$start, end=$end")
                    println ("startPoint=$startPoint")
                    println ("endPoint=$endPoint")
                    println ("pos=$pos")
                }
            }
            "r" -> valley.round ()
            "rd" -> {
                valley.round ()
                valley.dump ()
            }
            "run1" -> {
                while (! valley.positions.contains (valley.endPoint)) {
                    valley.round ()
                }
                println (valley.time)
            }
            "b" -> {
                valley.blizzards.forEach { println (it) }
            }
            else -> println ("Unrecognized: $input")
        }

        // NOT REACHED
    }
}

private val ALTERNATES = listOf<String> (
    """
        #.##
        #>^#
        #v<#
        ##.#
    """.trimIndent(),
    """
        #.#####
        #.....#
        #>....#
        #.....#
        #...v.#
        #.....#
        #####.#
    """.trimIndent()
)


// EOF