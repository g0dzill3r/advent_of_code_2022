package day22

fun main (args: Array<String>) {
    val example = false
    val board = loadBoard (example)

    // part1
    board.steps.forEach { step ->
        board.step (step)
    }
    println ("part1=${board.password}")

    // part2
    Board3D (board).let { it ->
        it.steps.forEach { step ->
            it.step (step)
        }
        println (it)
        println ("part2=${it.password}")
    }
    return
}